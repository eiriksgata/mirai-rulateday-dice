package indi.eiriksgata.rulateday.vo;

import lombok.Data;

@Data
public class AiTextDrawVo {

    private String prompt;
    private String negativePrompt;

    private int pictureShape;
    private int samplingSteps;

}