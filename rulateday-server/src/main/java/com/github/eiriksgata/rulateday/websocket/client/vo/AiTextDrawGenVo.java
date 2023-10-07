package com.github.eiriksgata.rulateday.websocket.client.vo;

import lombok.Data;

@Data
public class AiTextDrawGenVo {

    private String prompt;
    private String negativePrompt;

    //图片规格:0 长  1 宽  2 正方型
    private int pictureShape = 0;

    private boolean translate = false;

    private int samplingSteps = 27;

    private Long groupId;
    private Long createdById;


    public void setSamplingSteps(int samplingSteps) {
        if (samplingSteps > 30) {
            this.samplingSteps = 30;
        }
        if (samplingSteps < 5) {
            this.samplingSteps = 5;
        }
    }
}
