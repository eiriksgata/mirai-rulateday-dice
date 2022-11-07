package indi.eiriksgata.rulateday.service.impl;

import indi.eiriksgata.rulateday.mapper.SpeakersGroupListMapper;
import indi.eiriksgata.rulateday.service.BotControlService;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.Objects;

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
    public boolean groupIsBlacklist(long groupId) {
        try {
            Boolean isBlacklist = speakersGroupListMapper.selectBlacklistByGroupId(groupId);
            return Objects.requireNonNullElse(isBlacklist, false);
        } catch (PersistenceException e) {
            return false;
        }
    }

    @Override
    public void groupEnable(long groupId) {
        try {
            if (speakersGroupListMapper.selectByGroupId(groupId) == null) {
                speakersGroupListMapper.insert(groupId, true, false);
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
                speakersGroupListMapper.insert(groupId, false, false);
            } else {
                speakersGroupListMapper.updateIsEnableById(groupId, false);
            }
            MyBatisUtil.getSqlSession().commit();
        } catch (PersistenceException e) {
            speakersGroupListMapper.createTable();
        }
    }


    @Override
    public void groupBlacklistEnable(long groupId) {
        try {
            if (speakersGroupListMapper.selectByGroupId(groupId) == null) {
                speakersGroupListMapper.insert(groupId, false, false);
            } else {
                speakersGroupListMapper.updateIsBlacklistById(groupId, true);
            }
            MyBatisUtil.getSqlSession().commit();
        } catch (PersistenceException e) {
            speakersGroupListMapper.createTable();
        }
    }


    @Override
    public void groupBlacklistDisable(long groupId) {
        try {
            if (speakersGroupListMapper.selectByGroupId(groupId) == null) {
                speakersGroupListMapper.insert(groupId, true, false);
            } else {
                speakersGroupListMapper.updateIsBlacklistById(groupId, false);
            }
            MyBatisUtil.getSqlSession().commit();
        } catch (PersistenceException e) {
            speakersGroupListMapper.createTable();
        }
    }

}
