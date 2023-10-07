package com.github.eiriksgata.rulateday.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.eiriksgata.rulateday.config.GlobalData;
import com.github.eiriksgata.trpg.dice.reply.CustomText;
import com.github.eiriksgata.rulateday.dto.ChatRecordDTO;
import com.github.eiriksgata.rulateday.service.ChatRecordService;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupMessagePostSendEvent;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class ChatRecordServiceImpl implements ChatRecordService {

    @Override
    public void groupRecordHandler(GroupMessageEvent groupMessageEvent) {
        Long startTime = GlobalData.groupChatRecordEnableMap.get(groupMessageEvent.getGroup().getId() + "");
        if (startTime != null) {
            if (System.currentTimeMillis() - startTime > 1000 * 60 * 60 * 5) {
                groupMessageEvent.getGroup().sendMessage(CustomText.getText("chat.record.cache.timeout"));
                GlobalData.groupChatRecordEnableMap.remove(groupMessageEvent.getGroup().getId() + "");
                recordFileUpload(groupMessageEvent);
            } else {
                ChatRecordDTO chatRecordDTO = new ChatRecordDTO();
                chatRecordDTO.setSenderId(groupMessageEvent.getSender().getId());
                String senderName = groupMessageEvent.getSender().getNameCard();
                if (senderName.equals("")) {
                    senderName = groupMessageEvent.getSender().getNick();
                }

                chatRecordDTO.setSenderName(senderName);
                chatRecordDTO.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                chatRecordDTO.setContent(groupMessageEvent.getMessage().contentToString());
                GlobalData.groupChatRecordDataMap.get(groupMessageEvent.getGroup().getId() + "").getRecords().add(chatRecordDTO);
            }
        }
    }

    @Override
    public void botSelfMessageRecord(GroupMessagePostSendEvent groupMessagePostSendEvent) {
        long groupId = groupMessagePostSendEvent.component1().getId();
        Long startTime = GlobalData.groupChatRecordEnableMap.get(groupId + "");
        if (startTime != null) {
            ChatRecordDTO chatRecordDTO = new ChatRecordDTO();
            long botId = Bot.getInstances().get(0).getId();
            chatRecordDTO.setSenderId(botId);

            NormalMember normalMember = Objects.requireNonNull(Objects.requireNonNull(Bot.getInstances().get(0).getGroup(groupId)).get(botId));

            String senderName = normalMember.getNameCard();
            if (senderName.equals("")) {
                senderName = normalMember.getNick();
            }

            chatRecordDTO.setSenderName(senderName);
            chatRecordDTO.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            chatRecordDTO.setContent(groupMessagePostSendEvent.getMessage().contentToString());
            GlobalData.groupChatRecordDataMap.get(groupId + "").getRecords().add(chatRecordDTO);
        }
    }

    public File recordsFileCreate(String text) throws IOException {
        String fileName = "group-record-" + System.currentTimeMillis() + ".json";
        String path = "data/com.github.eiriksgata.rulateday-dice/" + fileName;
        File file = new File(path);
        file.createNewFile();
        CustomText.fileOut(file, text);
        return file;
    }

    @Override
    public void recordFileUpload(GroupMessageEvent groupMessageEvent) {
        try {
            File file = recordsFileCreate(JSONObject.toJSONString(GlobalData.groupChatRecordDataMap.get(groupMessageEvent.getGroup().getId() + "")));
            ExternalResource resource = ExternalResource.create(file);
            groupMessageEvent.getGroup().getFiles().uploadNewFile("/" + file.getName(), resource);
            resource.close();
            file.delete();
            groupMessageEvent.getGroup().sendMessage(
                    CustomText.getText("record.file.upload.success.result")
            );
        } catch (IOException e) {
            groupMessageEvent.getGroup().sendMessage(
                    CustomText.getText("record.file.upload.fail.result")
            );
        }
    }

}
