package indi.eiriksgata.rulateday.trpggame;

import indi.eiriksgata.dice.utlis.RegularExpressionUtils;

public class RoleDataHandler {

    public static String update(String source, String inputString) {
        String[] attributeList = inputString.split(",");
        //TODO: 实现属性
        for (String attribute : attributeList) {
            String symbolAndValue = RegularExpressionUtils.getMatcher("[\\+\\-\\=][0-9]+", attribute);
            String attributeName = attribute.substring(0, attribute.length() - symbolAndValue.length());
            String attributeValue = symbolAndValue.substring(1);
            switch (symbolAndValue.substring(0, 1)) {
                case "+":
                    source = attributeAdd(source, attributeName, attributeValue);
                    continue;
                case "=":
                    source = attributeSet(source, attributeName, attributeValue);
                    continue;
                case "-":
                    source = attributeReduce(source, attributeName, attributeValue);
            }
        }
        return source;
    }

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
