package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.vo.MessageData;

@InstructService
public class BlacklistController {

    @InstructReflex(value = {"blacklist-friend-add"}, priority = 3)
    public String addBlacklistByFriend(MessageData<?> data) {


        return null;
    }

    @InstructReflex(value = {"blacklist-group-add"}, priority = 3)
    public String addBlacklistByGroup(MessageData<?> data) {


        return null;
    }

    @InstructReflex(value = {"blacklist-friend-del"}, priority = 3)
    public String deleteBlacklistByFriend(MessageData<?> data) {


        return null;
    }

    @InstructReflex(value = {"blacklist-group-del"}, priority = 3)
    public String deleteBlacklistByGroup(MessageData<?> data) {


        return null;
    }


}
