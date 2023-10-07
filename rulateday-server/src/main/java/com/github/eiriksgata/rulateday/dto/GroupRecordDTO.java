package com.github.eiriksgata.rulateday.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GroupRecordDTO {

    private String groupName;
    private long groupId;
    private String createdAt;

    private long createdById;

    private List<ChatRecordDTO> records = new ArrayList<>();

}
