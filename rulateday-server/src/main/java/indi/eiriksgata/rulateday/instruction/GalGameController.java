package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.vo.MessageData;

import java.util.HashMap;
import java.util.Map;

@InstructService
public class GalGameController {

    public static Map<String, Boolean> galGamePlayerList = new HashMap<>();

    @InstructReflex(value = {".galgame"}, priority = 3)
    public String galGameOn(MessageData<?> data) {


        return "";
    }


}
