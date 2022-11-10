package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.config.GlobalData;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;

@InstructService
public class BotController {


    @InstructReflex(value = {"blacklist-group-add"}, priority = 3)
    public String addBlacklistByGroup(MessageData<?> data) {
        //验证骰主
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getQqID() == Long.parseLong(number)) {
            long groupId;
            try {
                groupId = Long.parseLong(data.getMessage().trim());
                BotServiceControl.botControl.groupBlacklistEnable(groupId);
            } catch (Exception e) {
                return CustomText.getText("blacklist.group.id.error");
            }
            return CustomText.getText("blacklist.group.add.success", groupId);
        } else {
            return CustomText.getText("blacklist.no.permission");
        }
    }


    @InstructReflex(value = {"blacklist-group-del"}, priority = 3)
    public String deleteBlacklistByGroup(MessageData<?> data) {
        //验证骰主
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getQqID() == Long.parseLong(number)) {
            long groupId;
            try {
                groupId = Long.parseLong(data.getMessage().trim());
                BotServiceControl.botControl.groupBlacklistDisable(groupId);
            } catch (Exception e) {
                return CustomText.getText("blacklist.group.id.error");
            }
            return CustomText.getText("blacklist.group.delete.success");
        } else {
            return CustomText.getText("blacklist.no.permission");
        }
    }

    @InstructReflex(value = {"blacklist-friend-add"}, priority = 3)
    public String addBlacklistByFriend(MessageData<?> data) {
        //验证骰主
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getQqID() == Long.parseLong(number)) {
            if (number.equals(data.getMessage().trim())) {
                return CustomText.getText("blacklist.friend.cannot.master");
            }
            long friendId;
            try {
                friendId = Long.parseLong(data.getMessage().trim());
                BotServiceControl.botControl.groupBlacklistEnable(-friendId);
            } catch (Exception e) {
                return CustomText.getText("blacklist.friend.id.error");
            }
            return CustomText.getText("blacklist.friend.add.success", friendId);
        } else {
            return CustomText.getText("blacklist.no.permission");
        }
    }

    @InstructReflex(value = {"blacklist-friend-del"}, priority = 3)
    public String deleteBlacklistByFriend(MessageData<?> data) {
        //验证骰主
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getQqID() == Long.parseLong(number)) {
            if (number.equals(data.getMessage().trim())) {
                return null;
            }
            long friendId;
            try {
                friendId = Long.parseLong(data.getMessage().trim());
                BotServiceControl.botControl.groupBlacklistDisable(-friendId);
            } catch (Exception e) {
                return CustomText.getText("blacklist.friend.id.error");
            }
            return CustomText.getText("blacklist.friend.add.success", friendId);
        } else {
            return CustomText.getText("blacklist.no.permission");
        }
    }


    @InstructReflex(value = {"group-list-get"}, priority = 3)
    public String getGroupList(MessageData<?> data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getQqID() == Long.parseLong(number)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(CustomText.getText("bot.group.list.title")).append("\n");
            for (Group group : Bot.getInstances().get(0).getGroups()) {
                stringBuilder.append("[").append(group.getName()).append("]").append(" ").append(group.getId()).append("\n");
            }
            return stringBuilder.toString();
        } else {
            return CustomText.getText("blacklist.no.permission");
        }
    }

    @InstructReflex(value = {"friend-list-get"}, priority = 3)
    public String getFriendList(MessageData<?> data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getQqID() == Long.parseLong(number)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(CustomText.getText("bot.friend.list.title")).append("\n");
            for (Friend friend : Bot.getInstances().get(0).getFriends()) {
                stringBuilder.append("[").append(friend.getNick()).append("]").append(" ").append(friend.getId()).append("\n");
            }
            return stringBuilder.toString();
        } else {
            return CustomText.getText("blacklist.no.permission");
        }
    }

    @InstructReflex(value = {"friend-list-del"}, priority = 3)
    public String deleteFriend(MessageData<?> data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return CustomText.getText("dice.master.number.no.set");
        }
        if (data.getQqID() == Long.parseLong(number)) {
            try {
                long deleteFriendId = Long.parseLong(data.getMessage());
                Friend friend = Bot.getInstances().get(0).getFriend(deleteFriendId);
                if (friend == null) {
                    return CustomText.getText("friend.delete.no.found");
                }
                friend.delete();
                return CustomText.getText("friend.delete.success");
            } catch (Exception e) {
                e.printStackTrace();
                return CustomText.getText("friend.delete.fail");
            }
        } else {
            return CustomText.getText("blacklist.no.permission");
        }
    }


}
