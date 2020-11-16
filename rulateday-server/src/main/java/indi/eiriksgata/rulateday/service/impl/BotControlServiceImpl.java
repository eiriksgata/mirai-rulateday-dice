package indi.eiriksgata.rulateday.service.impl;

import indi.eiriksgata.rulateday.mapper.SpeakersGroupListMapper;
import indi.eiriksgata.rulateday.service.BotControlService;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.service.impl
 * date: 2020/11/13
 **/
public class BotControlServiceImpl implements BotControlService {

    private static SpeakersGroupListMapper speakersGroupListMapper = MyBatisUtil.getSqlSession().getMapper(SpeakersGroupListMapper.class);

    @Override
    public boolean groupIsEnable(long groupId) {
        Boolean isEnable = speakersGroupListMapper.selectByGroupId(groupId);
        return isEnable == null || isEnable;
    }

    @Override
    public void groupEnable(long groupId) {
        if (speakersGroupListMapper.selectByGroupId(groupId) == null) {
            speakersGroupListMapper.insert(groupId, true);
        } else {
            speakersGroupListMapper.updateIsEnableById(groupId, true);
        }
        MyBatisUtil.getSqlSession().commit();
    }

    @Override
    public void groupDisable(long groupId) {
        if (speakersGroupListMapper.selectByGroupId(groupId) == null) {
            speakersGroupListMapper.insert(groupId, false);
        } else {
            speakersGroupListMapper.updateIsEnableById(groupId, false);
        }
        MyBatisUtil.getSqlSession().commit();
    }

}
