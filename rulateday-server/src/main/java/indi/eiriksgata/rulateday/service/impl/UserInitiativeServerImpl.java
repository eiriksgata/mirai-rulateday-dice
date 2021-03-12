package indi.eiriksgata.rulateday.service.impl;

import indi.eiriksgata.dice.reply.CustomText;
import indi.eiriksgata.rulateday.mapper.UserInitiativeDataMapper;
import indi.eiriksgata.rulateday.pojo.UserInitiativeData;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.service.impl
 * date: 2021/3/11
 **/

public class UserInitiativeServerImpl {

    private static UserInitiativeDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(UserInitiativeDataMapper.class);

    public boolean diceLimit(String groupId) {
        List<UserInitiativeData> initiativeDataList = mapper.selectByGroupId(groupId);
        return initiativeDataList.size() < 20;
    }

    public void addInitiatveDice(String groupId, Long userId, String name, int value) {
        UserInitiativeData result = mapper.select(groupId, userId, name);
        UserInitiativeData userInitiativeData = new UserInitiativeData();
        userInitiativeData.setGroupId(groupId);
        userInitiativeData.setName(name);
        userInitiativeData.setUserId(userId);
        userInitiativeData.setValue(value);
        if (result == null) {
            mapper.insert(userInitiativeData);
            MyBatisUtil.getSqlSession().commit();
        } else {
            mapper.deleteById(result.getId());
            mapper.insert(userInitiativeData);
            MyBatisUtil.getSqlSession().commit();
        }
    }

    public void deleteDice(String groupId, Long userId, String name) {
        mapper.delete(groupId, userId, name);
        MyBatisUtil.getSqlSession().commit();
    }

    public void clearGroupDice(String groupId) {
        mapper.delteByGroupId(groupId);
        MyBatisUtil.getSqlSession().commit();
    }

    public String showInitiativeList(String groupId) {
        List<UserInitiativeData> initiativeDataList = mapper.selectByGroupId(groupId);
        if (initiativeDataList.size() == 0) {
            return CustomText.getText("dice.initiative.null");
        }
        if (initiativeDataList.size() == 1) {
            return CustomText.getText(
                    "dice.initiative.show",
                    initiativeDataList.get(0).getName() +
                            "[" + initiativeDataList.get(0).getValue() + "]");
        }
        initiativeSort(initiativeDataList);
        StringBuilder resultText = new StringBuilder();
        resultText.append(initiativeDataList.get(0).getName()).append("[").append(initiativeDataList.get(0).getValue()).append("]");
        for (int i = 1; i < initiativeDataList.size(); i++) {
            resultText.append(">")
                    .append(initiativeDataList.get(i).getName())
                    .append("[")
                    .append(initiativeDataList.get(i).getValue())
                    .append("]");
        }
        return CustomText.getText("dice.initiative.show", resultText.toString());
    }

    private void initiativeSort(List<UserInitiativeData> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; i < list.size(); i++) {
                if (list.get(i).getValue() > list.get(j).getValue()) {
                    UserInitiativeData temp = list.get(j);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
    }


}
