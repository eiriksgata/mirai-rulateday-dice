package com.github.eiriksgata.rulateday.dto;

import lombok.Data;

@Data
public class ChatRecordDTO {

    private long senderId;
    private String senderName;
    private String content;
    private String date;

}
