package indi.eiriksgata.rulateday.service;

import indi.eiriksgata.rulateday.pojo.QueryDataBase;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.service
 * date: 2021/2/20
 **/
public interface UserConversationService {
    void saveConversation(Long qq, List<QueryDataBase> queryData);
}
