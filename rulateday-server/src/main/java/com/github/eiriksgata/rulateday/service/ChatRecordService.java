package com.github.eiriksgata.rulateday.service;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupMessagePostSendEvent;

public interface ChatRecordService {
    void groupRecordHandler(GroupMessageEvent groupMessageEvent);

    void botSelfMessageRecord(GroupMessagePostSendEvent groupMessagePostSendEvent);

    void recordFileUpload(GroupMessageEvent groupMessageEvent);
}
