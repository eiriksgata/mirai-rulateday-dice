package com.github.eiriksgata.rulateday.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class DiceResponse {
    private List<String> body;

    private HashMap<String, List<String>> expandBody = null;

    public static DiceResponse body(String text) {
        DiceResponse diceResponse = new DiceResponse();
        List<String> list = new ArrayList<>();
        list.add(text);
        diceResponse.setBody(list);
        return diceResponse;
    }


}
