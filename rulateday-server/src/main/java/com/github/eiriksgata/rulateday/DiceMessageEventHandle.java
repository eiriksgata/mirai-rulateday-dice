package com.github.eiriksgata.rulateday;

import com.github.eiriksgata.rulateday.config.GlobalData;
import com.github.eiriksgata.rulateday.instruction.BotServiceControl;
import com.github.eiriksgata.rulateday.service.ApiReport;
import com.github.eiriksgata.rulateday.service.ChatRecordService;
import com.github.eiriksgata.rulateday.service.DiceConfigService;
import com.github.eiriksgata.rulateday.service.impl.ApiReportImpl;
import com.github.eiriksgata.rulateday.service.impl.ChatRecordServiceImpl;
import com.github.eiriksgata.rulateday.service.impl.UserConversationImpl;
import com.github.eiriksgata.rulateday.utlis.ExceptionUtils;
import com.github.eiriksgata.trpg.dice.exception.DiceInstructException;
import com.github.eiriksgata.trpg.dice.exception.ExceptionEnum;
import com.github.eiriksgata.trpg.dice.message.handle.InstructHandle;
import com.github.eiriksgata.trpg.dice.vo.MessageData;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.At;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DiceMessageEventHandle extends SimpleListenerHost {

    private static final InstructHandle instructHandle = new InstructHandle();
    private static final BotServiceControl botControl = new BotServiceControl();
    public static final ApiReport apiReport = new ApiReportImpl();

    public static final ChatRecordService chatRecordService = new ChatRecordServiceImpl();

    @EventHandler()
    public ListeningStatus onBotGroupRequest(BotInvitedJoinGroupRequestEvent event) {
        //检测是否是黑名单用户
        if (botControl.isBlacklist(-event.getInvitorId()) || botControl.isBlacklist(event.getGroupId())) {
            return ListeningStatus.LISTENING;
        }

        //收到邀请自动加入
        if (GlobalData.configData.getBooleanValue("auto.accept.group.request")) {
            event.accept();
        }
        return ListeningStatus.LISTENING;
    }

    @EventHandler()
    public ListeningStatus onFriendRequest(NewFriendRequestEvent event) {
        if (GlobalData.configData.getBooleanValue("auto.accept.friend.request") && !botControl.isBlacklist(-event.getFromId())) {
            event.accept();
        }
        return ListeningStatus.LISTENING;
    }


    @EventHandler()
    public ListeningStatus addFriendRequest(FriendAddEvent event) {
        return ListeningStatus.LISTENING;
    }

    @EventHandler()
    public ListeningStatus OnGroupTempMessageEvent(GroupTempMessageEvent event) {
        if (!DiceConfigService.diceConfigMapper.selectById().getPrivate_chat()) {
            event.getGroup().sendMessage("私聊功能已被禁止，请拉入群聊使用。或让骰主开启私聊功能。");
            return ListeningStatus.LISTENING;
        }

        if (botControl.isBlacklist(-event.getSender().getId())) {
            return ListeningStatus.LISTENING;
        }

        MessageData<GroupTempMessageEvent> messageData = new MessageData<>();
        messageData.setMessage(event.getMessage().contentToString());
        messageData.setQqID(event.getSender().getId());
        messageData.setEvent(event);

        //检测对话模式，具有最高优先级
        String conversationResult = UserConversationImpl.checkInputQuery(messageData);
        if (conversationResult != null) {
            event.getSender().sendMessage(conversationResult);
            return ListeningStatus.LISTENING;
        }
        List<String> result = personalMessageEventHandler(messageData);
        if (result != null) {
            result.forEach((text) -> event.getSender().sendMessage(text));
        }
        return ListeningStatus.LISTENING;
    }

    @EventHandler()
    public ListeningStatus onFriendMessage(FriendMessageEvent event) {

        if (!DiceConfigService.diceConfigMapper.selectById().getPrivate_chat()) {
            return ListeningStatus.LISTENING;
        }

        //判断群是否是黑名单，具体功能尚未实现
        if (botControl.isBlacklist(-event.getFriend().getId())) {
            return ListeningStatus.LISTENING;
        }


        MessageData<FriendMessageEvent> messageData = new MessageData<>();
        messageData.setMessage(event.getMessage().contentToString());
        messageData.setQqID(event.getSender().getId());
        messageData.setEvent(event);

        String conversationResult = UserConversationImpl.checkInputQuery(messageData);
        if (conversationResult != null) {
            event.getSender().sendMessage(conversationResult);
            return ListeningStatus.LISTENING;
        }
        List<String> result = personalMessageEventHandler(messageData);
        if (result != null) {
            result.forEach((text) -> event.getSender().sendMessage(text));
        }
        return ListeningStatus.LISTENING;
    }


    //EventHandler可以指定多个属性，包括处理方式、优先级、是否忽略已取消的事件
    //其默认值请见EventHandler注解类
    //因为默认处理的类型为Listener.ConcurrencyKind.CONCURRENT
    //需要考虑并发安全
    @EventHandler()
    public ListeningStatus onGroupMessage(GroupMessageEvent event) {
        groupMessageHandle(event);
        //保持监听
        return ListeningStatus.LISTENING;
    }

    @EventHandler()
    public ListeningStatus onGroupMessagePreSend(GroupMessagePostSendEvent groupMessagePostSendEvent) {
        //群记录处理
        chatRecordService.botSelfMessageRecord(groupMessagePostSendEvent);
        return ListeningStatus.LISTENING;
    }

    //处理在处理事件中发生的未捕获异常
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        apiReport.exceptionReport("在事件处理中发生异常", exception.toString(), -1L);
        throw new RuntimeException("在事件处理中发生异常", exception);
    }

    private static void groupMessageHandle(GroupMessageEvent event) {
        //判断群是否是黑名单，具体功能尚未实现
        if (botControl.isBlacklist(event.getGroup().getId())) {
            return;
        }

        //群消息的回复
        //回复群的筛选
        if (botControl.groupBotOff(event) || botControl.groupBotOn(event)) {
            return;
        }

        //群记录处理
        chatRecordService.groupRecordHandler(event);

        if (!botControl.isSpeakers(event)) {
            return;
        }

        MessageData<GroupMessageEvent> messageData = new MessageData<>();
        messageData.setMessage(event.getMessage().contentToString());
        messageData.setQqID(event.getSender().getId());
        messageData.setEvent(event);

        //检测对话模式，具有最高优先级
        String conversationResult = UserConversationImpl.checkInputQuery(messageData);
        if (conversationResult != null) {
            event.getGroup().sendMessage(conversationResult);
            return;
        }

        String prefix = botControl.isPrefixMatch(messageData.getMessage());
        if (prefix == null) {
            return;
        }
        messageData.setMessage(messageData.getMessage().substring(prefix.length()));

        String result;
        try {
            result = instructHandle.instructCheck(messageData);
        } catch (DiceInstructException e) {
            if (e.getErrCode().equals(ExceptionEnum.DICE_INSTRUCT_NOT_FOUND.getErrCode())) {
                return;
            }
            result = e.getErrMsg();
            apiReport.exceptionReport(event.getMessage().contentToString(), ExceptionUtils.getExceptionAllInfo(e), messageData.getQqID());
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            result = e.getMessage();
            apiReport.exceptionReport(event.getMessage().contentToString(), ExceptionUtils.getExceptionAllInfo(e), messageData.getQqID());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            result = e.getCause().toString();
            apiReport.exceptionReport(event.getMessage().contentToString(), ExceptionUtils.getExceptionAllInfo(e), messageData.getQqID());
        } catch (PersistenceException e) {
            e.printStackTrace();
            result = "数据库操作异常，可以尝试重启程序，如果还存在这种情况，可以联系开发人员进行反馈";
        } catch (Exception e) {
            e.printStackTrace();
            RulatedayCore.INSTANCE.getLogger().error(ExceptionUtil.unwrapThrowable(e));
            apiReport.exceptionReport(event.getMessage().contentToString(), ExceptionUtils.getExceptionAllInfo(e), messageData.getQqID());
            return;
        }
        if (result == null) return;
        String senderName = event.getSender().getNameCard();
        if (senderName.trim().equals("")) {
            senderName = event.getSender().getNick();
        }
        if (GlobalData.configData.getBooleanValue("reply.at.user")) {
            event.getGroup().sendMessage(new At(event.getSender().getId()).plus("\n" + result));
        } else {
            event.getGroup().sendMessage("[" + senderName + "]" + result);
        }
    }


    @SuppressWarnings("rawtypes")
    private List<String> personalMessageEventHandler(MessageData messageData) {
        String sourceMessage = messageData.getMessage();
        List<String> result = new ArrayList<>();

        String prefix = botControl.isPrefixMatch(messageData.getMessage());
        if (prefix == null) {
            return null;
        }
        messageData.setMessage(messageData.getMessage().substring(prefix.length()));

        try {
            String returnText = instructHandle.instructCheck(messageData);
            if (returnText == null) return null;
            //对于私聊的消息需要进行分割长度发送
            while (true) {
                if (returnText.length() > 200) {
                    result.add(returnText.substring(0, 200));
                    returnText = returnText.substring(200);
                } else {
                    result.add(returnText);
                    break;
                }
            }
        } catch (DiceInstructException e) {
            if (e.getErrCode().equals(ExceptionEnum.DICE_INSTRUCT_NOT_FOUND.getErrCode())) {
                return null;
            }
            result.add(e.getErrMsg());
            result.add("\n参数异常，请输入正确的参数范围，或联系QQ:2353686862");
            apiReport.exceptionReport(sourceMessage, ExceptionUtils.getExceptionAllInfo(e), messageData.getQqID());
        } catch (IllegalAccessException | InstantiationException e) {
            result.add(e.getMessage());
            result.add("\n实例化异常或非法访问，可联系QQ:2353686862");
            apiReport.exceptionReport(sourceMessage, ExceptionUtils.getExceptionAllInfo(e), messageData.getQqID());
        } catch (InvocationTargetException e) {
            result.add(e.getCause().toString());
            result.add("\n反射调用异常，可联系QQ:2353686862");
            apiReport.exceptionReport(sourceMessage, ExceptionUtils.getExceptionAllInfo(e), messageData.getQqID());
        } catch (Exception e) {
            e.printStackTrace();
            RulatedayCore.INSTANCE.getLogger().error(ExceptionUtil.unwrapThrowable(e));
            apiReport.exceptionReport(sourceMessage, ExceptionUtils.getExceptionAllInfo(e), messageData.getQqID());
            return null;
        }
        return result;
    }

}
