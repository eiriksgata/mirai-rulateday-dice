package com.github.eiriksgata.rulateday.service.impl;

import com.github.eiriksgata.rulateday.mapper.UserTempDataMapper;
import com.github.eiriksgata.rulateday.pojo.UserTempData;
import com.github.eiriksgata.trpg.dice.config.DiceConfig;
import com.github.eiriksgata.rulateday.service.UserTempDataService;
import com.github.eiriksgata.rulateday.utlis.MyBatisUtil;
import org.apache.ibatis.exceptions.PersistenceException;

public class UserTempDataServiceImpl implements UserTempDataService {

    private static final UserTempDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(UserTempDataMapper.class);

    @Override
    public void updateUserAttribute(Long id, String attribute) {
        UserTempData userTempData = null;
        try {
            userTempData = mapper.selectById(id);
        } catch (PersistenceException e) {
            mapper.createTable();
        }
        if (userTempData == null) {
            addUserTempData(id);
        }
        mapper.updateAttributeById(id, attribute);
        MyBatisUtil.getSqlSession().commit();
    }

    @Override
    public void updateUserDiceFace(Long id, int diceFace) {
        UserTempData userTempData = null;
        try {
            userTempData = mapper.selectById(id);
        } catch (PersistenceException e) {
            mapper.createTable();
        }
        if (userTempData == null) {
            addUserTempData(id);
        }
        mapper.updateDiceFaceById(id, diceFace);
        MyBatisUtil.getSqlSession().commit();
    }


    @Override
    public void addUserTempData(Long id) {
        UserTempData userTempData = new UserTempData();
        userTempData.setId(id);
        userTempData.setAttribute("");
        userTempData.setDice_face(Integer.valueOf(
                DiceConfig.diceSet.getString(
                        DiceConfig.diceSet.getString("dice.type") + ".face")
        ));
        mapper.insert(userTempData);
        MyBatisUtil.getSqlSession().commit();
    }

    @Override
    public Integer getUserDiceFace(Long id) {
        try {
            return mapper.selectById(id).getDice_face();
        } catch (NullPointerException ignored) {
            return null;
        } catch (PersistenceException e) {
            mapper.createTable();
            return null;
        }
    }

    @Override
    public String getUserAttribute(Long id) {
        try {
            return mapper.selectById(id).getAttribute();
        } catch (NullPointerException ignored) {
            return null;
        } catch (PersistenceException e) {
            mapper.createTable();
            return null;
        }
    }

}
