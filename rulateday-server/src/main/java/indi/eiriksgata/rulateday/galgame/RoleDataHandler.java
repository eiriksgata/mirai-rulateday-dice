package indi.eiriksgata.rulateday.galgame;

import indi.eiriksgata.dice.utlis.RegularExpressionUtils;

public class RoleDataHandler {


    public static String attributeAdd(String source, String attributeName, String value) {
        String result = RegularExpressionUtils.getMatcher(attributeName + "[0-9]+", source);
        if (result == null) {
            source += attributeName + value;
        } else {
            int sourceValue = Integer.parseInt(result.substring(attributeName.length()));
            int inputValue = Integer.parseInt(value);
            int resultValue = sourceValue + inputValue;
            source = source.replaceFirst(result, attributeName + resultValue);
        }
        return source;
    }

    public static String attributeSet(String source, String attributeName, String value) {
        String result = RegularExpressionUtils.getMatcher(attributeName + "[0-9]+", source);
        if (result == null) {
            source += attributeName + value;
        } else {
            source = source.replaceFirst(result, attributeName + value);
        }
        return source;
    }

    public static String attributeReduce(String source, String attributeName, String value) {
        String result = RegularExpressionUtils.getMatcher(attributeName + "[0-9]+", source);
        if (result == null) {
            source += attributeName + 0;
        } else {
            int sourceValue = Integer.parseInt(result.substring(attributeName.length()));
            int inputValue = Integer.parseInt(value);
            int resultValue = sourceValue - inputValue;
            if (resultValue < 0) {
                source = source.replaceFirst(result, attributeName + 0);
            } else {
                source = source.replaceFirst(result, attributeName + resultValue);
            }
        }
        return source;
    }

}
