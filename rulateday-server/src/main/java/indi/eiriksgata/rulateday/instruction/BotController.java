package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.vo.MessageData;

@InstructService
public class BotController {


    @InstructReflex(value = {"blacklist-group-add"}, priority = 3)
    public String addBlacklistByGroup(MessageData<?> data) {


        return null;
    }


    @InstructReflex(value = {"blacklist-group-add"}, priority = 3)
    public String addBlacklistByFriend(MessageData<?> data) {


        return null;
    }


}
