package indi.eiriksgata.rulateday.galgame;

import lombok.Data;

@Data
public class DetectionEntity {

    private boolean result;
    private String diceText;
    private int randomValue;
    private int checkValue;

}
