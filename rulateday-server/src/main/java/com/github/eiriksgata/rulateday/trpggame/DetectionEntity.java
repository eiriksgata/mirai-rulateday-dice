package com.github.eiriksgata.rulateday.trpggame;

import lombok.Data;

@Data
public class DetectionEntity {

    private boolean result;
    private String diceText;
    private int randomValue;
    private int checkValue;

}
