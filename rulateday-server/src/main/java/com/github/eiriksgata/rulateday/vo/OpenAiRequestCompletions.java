package com.github.eiriksgata.rulateday.vo;

import lombok.Data;

@Data
public class OpenAiRequestCompletions {

    private String model = "text-davinci-003";
    private String prompt = "";
    private Double temperature = 0.6;

    private Integer max_tokens = 150;
    private Integer top_p = 1;
    private Integer frequency_penalty = 1;
    private Integer presence_penalty = 1;


}
