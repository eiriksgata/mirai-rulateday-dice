package indi.eiriksgata.rulateday.utlis;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.utlis
 * date: 2021/5/21
 **/
public class CharacterUtils {

    public static String operationSymbolProcessing(String text) {
        text = text.replaceAll("[＋➕]", "+");
        text = text.replaceAll("[-➖—－]", "-");
        text = text.replaceAll("[×xX✖]", "*");
        text = text.replaceAll("[÷➗]", "/");
        return text;
    }


}
