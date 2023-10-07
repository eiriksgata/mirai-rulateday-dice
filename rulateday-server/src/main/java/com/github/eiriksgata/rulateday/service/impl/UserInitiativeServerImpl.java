package com.github.eiriksgata.rulateday.service.impl;

import com.github.eiriksgata.rulateday.mapper.UserInitiativeDataMapper;
import com.github.eiriksgata.rulateday.pojo.UserInitiativeData;
import com.github.eiriksgata.trpg.dice.reply.CustomText;
import com.github.eiriksgata.rulateday.utlis.MyBatisUtil;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.github.eiriksgata.rulateday.service.impl
 * date: 2021/3/11
 **/

public class UserInitiativeServerImpl {

    private static final UserInitiativeDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(UserInitiativeDataMapper.class);

    public boolean diceLimit(String groupId) {
        try {
            List<UserInitiativeData> initiativeDataList = mapper.selectByGroupId(groupId);
            return initiativeDataList.size() > 30;
        } catch (PersistenceException e) {
            try {
                mapper.createTable();
            } catch (PersistenceException ignored) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void addInitiativeDice(String groupId, Long userId, String name, int value) {
        UserInitiativeData result = mapper.selectByGroupIdAndUserIdAndName(groupId, userId, name);
        UserInitiativeData userInitiativeData = new UserInitiativeData();
        userInitiativeData.setGroupId(groupId);
        userInitiativeData.setName(name);
        userInitiativeData.setUserId(userId);
        userInitiativeData.setValue(value);
        try {
            if (result != null) {
                mapper.deleteById(result.getId());
            }
            mapper.insert(userInitiativeData);
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        MyBatisUtil.getSqlSession().commit();
    }

    public void deleteDice(String groupId, Long userId, String name) {
        mapper.delete(groupId, userId, name);
        MyBatisUtil.getSqlSession().commit();
    }

    public void clearGroupDice(String groupId) {
        mapper.deleteByGroupId(groupId);
        MyBatisUtil.getSqlSession().commit();
    }

    public String showInitiativeList(String groupId) {
        List<UserInitiativeData> initiativeDataList = mapper.selectByGroupId(groupId);
        if (initiativeDataList.size() == 0) {
            return CustomText.getText("initiative.null");
        }
        if (initiativeDataList.size() == 1) {
            return CustomText.getText(
                    "initiative.show",
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
        return CustomText.getText("initiative.show", resultText.toString());
    }

    private void initiativeSort(List<UserInitiativeData> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(i).getValue() > list.get(j).getValue()) {
                    UserInitiativeData temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
    }


}
