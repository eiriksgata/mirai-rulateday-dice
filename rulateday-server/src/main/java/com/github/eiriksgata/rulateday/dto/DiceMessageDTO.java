package com.github.eiriksgata.rulateday.dto;

import com.github.eiriksgata.trpg.dice.vo.Message;
import lombok.Data;

@Data
public class DiceMessageDTO extends Message {
    private String body;
    private Long id;

    private Object event;

}
