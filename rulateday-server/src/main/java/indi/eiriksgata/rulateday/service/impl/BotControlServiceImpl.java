package indi.eiriksgata.rulateday.service.impl;

import indi.eiriksgata.rulateday.mapper.SpeakersGroupListMapper;
import indi.eiriksgata.rulateday.service.BotControlService;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;
import org.apache.ibatis.exceptions.PersistenceException;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.service.impl
 * date: 2020/11/13
 **/
public class BotControlServiceImpl implements BotControlService {

    private static final SpeakersGroupListMapper speakersGroupListMapper = MyBatisUtil.getSqlSession().getMapper(SpeakersGroupListMapper.class);

    @Override
    public boolean groupIsEnable(long groupId) {
        try {
            Boolean isEnable = speakersGroupListMapper.selectByGroupId(groupId);
            return isEnable == null || isEnable;
        } catch (PersistenceException e) {
            speakersGroupListMapper.createTable();
            return true;
        }
    }

    @Override
    public void groupEnable(long groupId) {
        try {
            if (speakersGroupListMapper.selectByGroupId(groupId) == null) {
                speakersGroupListMapper.insert(groupId, true);
            } else {
                speakersGroupListMapper.updateIsEnableById(groupId, true);
            }
            MyBatisUtil.getSqlSession().commit();
        } catch (PersistenceException e) {
            speakersGroupListMapper.createTable();
        }
    }

    @Override
    public void groupDisable(long groupId) {
        try {
            if (speakersGroupListMapper.selectByGroupId(groupId) == null) {
                speakersGroupListMapper.insert(groupId, false);
            } else {
                speakersGroupListMapper.updateIsEnableById(groupId, false);
            }
            MyBatisUtil.getSqlSession().commit();
        } catch (PersistenceException e) {
            speakersGroupListMapper.createTable();
        }
    }

}
