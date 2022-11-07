package indi.eiriksgata.rulateday.service;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.service
 * date: 2020/11/13
 **/
public interface BotControlService {
    boolean groupIsEnable(long groupId);

    boolean groupIsBlacklist(long groupId);

    void groupEnable(long groupId);

    void groupDisable(long groupId);

    void groupBlacklistEnable(long groupId);

    void groupBlacklistDisable(long groupId);
}
