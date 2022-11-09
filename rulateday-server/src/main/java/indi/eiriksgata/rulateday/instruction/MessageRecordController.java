package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.DiceMessageEventHandle;
import indi.eiriksgata.rulateday.config.GlobalData;
import indi.eiriksgata.rulateday.dto.ChatRecordDTO;
import indi.eiriksgata.rulateday.dto.GroupRecordDTO;
import indi.eiriksgata.rulateday.event.EventAdapter;
import indi.eiriksgata.rulateday.event.EventUtils;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

@InstructService
public class MessageRecordController {

    @InstructReflex(value = {"logon", "log-on"}, priority = 2)
    public String openGroupRecord(MessageData<?> data) {
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                if (GlobalData.groupChatRecordEnableMap.get(event.getGroup().getId() + "") == null) {
                    GlobalData.groupChatRecordEnableMap.put(event.getGroup().getId() + "", System.currentTimeMillis());
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    GroupRecordDTO temp = new GroupRecordDTO();
                    temp.setGroupId(event.getGroup().getId());
                    temp.setGroupName(event.getGroup().getName());
                    temp.setCreatedAt(currentDate);
                    temp.setCreatedById(event.getSender().getId());

                    String senderName = event.getSender().getNameCard();
                    if (senderName.equals("")) {
                        senderName = event.getSender().getNick();
                    }

                    ChatRecordDTO chatRecordDTO = new ChatRecordDTO();
                    chatRecordDTO.setSenderName(senderName);
                    chatRecordDTO.setSenderId(event.getSender().getId());
                    chatRecordDTO.setDate(currentDate);
                    chatRecordDTO.setContent(event.getMessage().contentToString());

                    temp.getRecords().add(chatRecordDTO);

                    GlobalData.groupChatRecordDataMap.put(event.getGroup().getId() + "", temp);

                    event.getGroup().sendMessage(CustomText.getText("chat.record.open"));
                } else {
                    event.getGroup().sendMessage(CustomText.getText("chat.record.opened"));
                }
            }
        });
        return null;
    }

    @InstructReflex(value = {"logoff", "log-off"}, priority = 2)
    public String closeGroupRecord(MessageData<?> data) {
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                if (GlobalData.groupChatRecordEnableMap.get(event.getGroup().getId() + "") == null) {
                    event.getGroup().sendMessage(CustomText.getText("chat.record.closed"));
                    return;
                }
                DiceMessageEventHandle.chatRecordService.recordFileUpload(event);
                event.getGroup().sendMessage(CustomText.getText("chat.record.close"));
                GlobalData.groupChatRecordEnableMap.remove(event.getGroup().getId() + "");
                GlobalData.groupChatRecordDataMap.remove(event.getGroup().getId() + "");
            }
        });
        return null;
    }

}
