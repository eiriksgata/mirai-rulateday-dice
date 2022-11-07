package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
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
            return "本插件还未设置骰主QQ号，请在本程序目录下的 config/indi.eiriksgata.rulateday-dice/config.json 文件中的 'master.QQ.number' 中进行设置，设置后才可以使用本功能";
        }
        if (data.getQqID() == Long.parseLong(number)) {
            long groupId;
            try {
                groupId = Long.parseLong(data.getMessage().trim());
                BotServiceControl.botControl.groupBlacklistEnable(groupId);
            } catch (Exception e) {
                return "输入的群号不对。";
            }
            return "群组[" + groupId + "]已添加至黑名单。";
        } else {
            return "你不是骰主，无法使用黑名单功能";
        }
    }


    @InstructReflex(value = {"blacklist-group-del"}, priority = 3)
    public String deleteBlacklistByGroup(MessageData<?> data) {
        //验证骰主
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return "本插件还未设置骰主QQ号，请在本程序目录下的 config/indi.eiriksgata.rulateday-dice/config.json 文件中的 'master.QQ.number' 中进行设置，设置后才可以使用本功能";
        }
        if (data.getQqID() == Long.parseLong(number)) {
            long groupId;
            try {
                groupId = Long.parseLong(data.getMessage().trim());
                BotServiceControl.botControl.groupBlacklistDisable(groupId);
            } catch (Exception e) {
                return "输入的群号不对。";
            }
            return "群组[" + groupId + "]已从黑名单中移除。";
        } else {
            return "你不是骰主，无法使用黑名单功能";
        }
    }

    @InstructReflex(value = {"blacklist-friend-add"}, priority = 3)
    public String addBlacklistByFriend(MessageData<?> data) {
        //验证骰主
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return "本插件还未设置骰主QQ号，请在本程序目录下的 config/indi.eiriksgata.rulateday-dice/config.json 文件中的 'master.QQ.number' 中进行设置，设置后才可以使用本功能";
        }
        if (data.getQqID() == Long.parseLong(number)) {
            if (number.equals(data.getMessage().trim())) {
                return "不可以将骰主拉入黑名单";
            }
            long friendId;
            try {
                friendId = Long.parseLong(data.getMessage().trim());
                BotServiceControl.botControl.groupBlacklistEnable(-friendId);
            } catch (Exception e) {
                return "输入的号码不对。";
            }
            return "用户[" + friendId + "]已添加至黑名单。";
        } else {
            return "你不是骰主，无法使用黑名单功能";
        }
    }

    @InstructReflex(value = {"blacklist-friend-del"}, priority = 3)
    public String deleteBlacklistByFriend(MessageData<?> data) {
        //验证骰主
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return "本插件还未设置骰主QQ号，请在本程序目录下的 config/indi.eiriksgata.rulateday-dice/config.json 文件中的 'master.QQ.number' 中进行设置，设置后才可以使用本功能";
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
                return "输入的号码不对。";
            }
            return "用户[" + friendId + "]已从黑名单中移除。";
        } else {
            return "你不是骰主，无法使用黑名单功能";
        }
    }


    @InstructReflex(value = {"group-list-get"}, priority = 3)
    public String getGroupList(MessageData<?> data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return "本插件还未设置骰主QQ号，请在本程序目录下的 config/indi.eiriksgata.rulateday-dice/config.json 文件中的 'master.QQ.number' 中进行设置，设置后才可以使用本功能";
        }
        if (data.getQqID() == Long.parseLong(number)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("当前加入的群聊:\n");
            for (Group group : Bot.getInstances().get(0).getGroups()) {
                stringBuilder.append("[").append(group.getName()).append("]").append(" ").append(group.getId()).append("\n");
            }
            return stringBuilder.toString();
        } else {
            return "你不是骰主，无法使用该功能";
        }
    }

    @InstructReflex(value = {"friend-list-get"}, priority = 3)
    public String getFriendList(MessageData<?> data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return "本插件还未设置骰主QQ号，请在本程序目录下的 config/indi.eiriksgata.rulateday-dice/config.json 文件中的 'master.QQ.number' 中进行设置，设置后才可以使用本功能";
        }
        if (data.getQqID() == Long.parseLong(number)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("当前拥有的好友:\n");
            for (Friend friend : Bot.getInstances().get(0).getFriends()) {
                stringBuilder.append("[").append(friend.getNick()).append("]").append(" ").append(friend.getId()).append("\n");
            }
            return stringBuilder.toString();
        } else {
            return "你不是骰主，无法使用该功能";
        }
    }

    @InstructReflex(value = {"friend-list-del"}, priority = 3)
    public String deleteFriend(MessageData<?> data) {
        String number = GlobalData.configData.getString("master.QQ.number");
        if (number.equals("")) {
            return "本插件还未设置骰主QQ号，请在本程序目录下的 config/indi.eiriksgata.rulateday-dice/config.json 文件中的 'master.QQ.number' 中进行设置，设置后才可以使用本功能";
        }
        if (data.getQqID() == Long.parseLong(number)) {
            try {
                long deleteFriendId = Long.parseLong(data.getMessage());
                Friend friend = Bot.getInstances().get(0).getFriend(deleteFriendId);
                if (friend == null) {
                    return "尚未添加该QQ为好友";
                }
                friend.delete();
                return "已删除该好友";
            } catch (Exception e) {
                return "删除好友失败。";
            }
        } else {
            return "你不是骰主，无法使用该功能";
        }
    }


}
