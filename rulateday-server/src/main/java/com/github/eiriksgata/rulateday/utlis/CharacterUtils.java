package com.github.eiriksgata.rulateday.utlis;

import org.jetbrains.annotations.NotNull;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.github.eiriksgata.rulateday.utlis
 * date: 2021/5/21
 **/
public class CharacterUtils {

    public static @NotNull String operationSymbolProcessing(String text) {
        text = text.replaceAll("[＋➕]", "+");
        text = text.replaceAll("[-➖—－]", "-");
        text = text.replaceAll("[×xX✖]", "*");
        text = text.replaceAll("[÷➗]", "/");
        return text;
    }


}
