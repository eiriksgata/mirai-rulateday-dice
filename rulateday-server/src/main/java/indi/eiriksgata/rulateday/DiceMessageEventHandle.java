package indi.eiriksgata.rulateday;

import indi.eiriksgata.dice.exception.DiceInstructException;
import indi.eiriksgata.dice.exception.ExceptionEnum;
import indi.eiriksgata.dice.message.handle.InstructHandle;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.instruction.BotServiceControl;
import indi.eiriksgata.rulateday.service.impl.UserConversationImpl;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.At;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class DiceMessageEventHandle extends SimpleListenerHost {

    private static final InstructHandle instructHandle = new InstructHandle();
    private static final BotServiceControl botControl = new BotServiceControl();

    @EventHandler()
    public ListeningStatus onBotGroupRequest(BotInvitedJoinGroupRequestEvent event) {
        //收到邀请自动加入
        event.accept();
        return ListeningStatus.LISTENING;
    }


    @EventHandler()
    public ListeningStatus onFriendRequest(NewFriendRequestEvent event) {
        event.accept();
        return ListeningStatus.LISTENING;
    }

    @EventHandler()
    public ListeningStatus OnGroupTempMessageEvent(GroupTempMessageEvent event) {
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
        String result;
        try {
            result = instructHandle.instructCheck(messageData);
            //对于私聊的消息需要进行分割长度发送
            while (true) {
                if (result.length() > 200) {
                    event.getSender().sendMessage(result.substring(0, 200));
                    result = result.substring(200);
                } else {
                    event.getSender().sendMessage(result);
                    break;
                }
            }
        } catch (DiceInstructException e) {
            if (e.getErrCode().equals(ExceptionEnum.DICE_INSTRUCT_NOT_FOUND.getErrCode())) {
                return ListeningStatus.LISTENING;
            }
            event.getSender().sendMessage(e.getErrMsg());
        } catch (IllegalAccessException | InstantiationException e) {
            event.getSender().sendMessage(e.getMessage());
        } catch (InvocationTargetException e) {
            event.getSender().sendMessage(e.getCause().toString());
        }
        return ListeningStatus.LISTENING;
    }


    @EventHandler()
    public ListeningStatus onFriendMessage(FriendMessageEvent event) {
        MessageData<FriendMessageEvent> messageData = new MessageData<>();
        messageData.setMessage(event.getMessage().contentToString());
        messageData.setQqID(event.getSender().getId());
        messageData.setEvent(event);
        //检测对话模式，具有最高优先级
        String conversationResult = UserConversationImpl.checkInputQuery(messageData);
        if (conversationResult != null) {
            event.getSender().sendMessage(conversationResult);
            return ListeningStatus.LISTENING;
        }
        String result;
        try {
            result = instructHandle.instructCheck(messageData);
            //对于私聊的消息需要进行分割长度发送
            while (true) {
                if (result.length() > 200) {
                    event.getFriend().sendMessage(result.substring(0, 200));
                    result = result.substring(200);
                } else {
                    event.getFriend().sendMessage(result);
                    break;
                }
            }
        } catch (DiceInstructException e) {
            if (e.getErrCode().equals(ExceptionEnum.DICE_INSTRUCT_NOT_FOUND.getErrCode())) {
                return ListeningStatus.LISTENING;
            }
            event.getFriend().sendMessage(e.getErrMsg());
        } catch (IllegalAccessException | InstantiationException e) {
            event.getFriend().sendMessage(e.getMessage());
        } catch (InvocationTargetException e) {
            event.getFriend().sendMessage(e.getCause().toString());
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

    //处理在处理事件中发生的未捕获异常
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理中发生异常", exception);
    }

    private static void groupMessageHandle(GroupMessageEvent event) {
        //群消息的回复
        //回复群的筛选
        if (botControl.groupBotOff(event) || botControl.groupBotOn(event)) {
            return;
        }
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
        String result;
        try {
            result = instructHandle.instructCheck(messageData);
        } catch (DiceInstructException e) {
            if (e.getErrCode().equals(ExceptionEnum.DICE_INSTRUCT_NOT_FOUND.getErrCode())) {
                return;
            }
            result = e.getErrMsg();
        } catch (IllegalAccessException | InstantiationException e) {
            //e.printStackTrace();
            result = e.getMessage();
        } catch (InvocationTargetException e) {
            result = e.getCause().toString();
        }
        event.getGroup().sendMessage(new At(event.getSender().getId()).plus("\n" + result));
    }

}
