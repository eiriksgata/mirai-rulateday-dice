package indi.eiriksgata.rulateday.galgame.utils;

import indi.eiriksgata.dice.utlis.RegularExpressionUtils;
import indi.eiriksgata.rulateday.galgame.DetectionEntity;
import org.apache.commons.lang3.RandomUtils;

public class DetectionUtil {

    public static DetectionEntity attribute(String source, String checkName) {
        if (source == null || checkName == null) {
            return null;
        }
        String result = RegularExpressionUtils.getMatcher(checkName + "[0-9]+", source);
        DetectionEntity detectionEntity = new DetectionEntity();
        int randomValue = RandomUtils.nextInt(1, 101);
        detectionEntity.setResult(false);
        if (result == null) {
            detectionEntity.setDiceText("[" + checkName + "0]检定！D100=" + randomValue + " 失败！");
            return detectionEntity;
        }
        int checkValue = Integer.parseInt(result.substring(checkName.length()));
        detectionEntity.setCheckValue(checkValue);
        detectionEntity.setRandomValue(randomValue);
        if (randomValue <= checkValue ) {
            detectionEntity.setResult(true);
            detectionEntity.setDiceText("[" + result + "]检定！D100=" + randomValue + " 成功！");
        }else {
            detectionEntity.setDiceText("[" + result + "]检定！D100=" + randomValue + " 失败！");
        }
        return detectionEntity;
    }


    public static DetectionEntity consumables(String source, String checkData) {
        if (source == null || checkData == null) {
            return null;
        }
        DetectionEntity detectionEntity = new DetectionEntity();
        detectionEntity.setResult(false);
        String symbolValue = RegularExpressionUtils.getMatcher("[><]+[0-9]+", checkData);
        if (symbolValue == null) {
            System.out.println("consumables 错误的参数形式");
            return detectionEntity;
        }
        String checkName = checkData.substring(0, symbolValue.length());

        //检测消耗品
        String holdString = RegularExpressionUtils.getMatcher(checkName + "[0-9]+", source);
        int holdValue = Integer.parseInt(holdString.substring(checkName.length()));
        int checkValue = Integer.parseInt(symbolValue.substring(1));
        switch (symbolValue.substring(0, 1)) {
            case ">":
                detectionEntity.setResult(holdValue > checkValue);
                break;
            case "<":
                detectionEntity.setResult(holdValue < checkValue);
                break;
        }
        return detectionEntity;
    }


}

