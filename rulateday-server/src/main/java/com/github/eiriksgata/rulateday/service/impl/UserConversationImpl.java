package com.github.eiriksgata.rulateday.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.github.eiriksgata.rulateday.mapper.UserConversationMapper;
import com.github.eiriksgata.rulateday.pojo.QueryDataBase;
import com.github.eiriksgata.rulateday.pojo.UserConversation;
import com.github.eiriksgata.trpg.dice.vo.MessageData;
import com.github.eiriksgata.rulateday.instruction.QueryController;
import com.github.eiriksgata.rulateday.service.UserConversationService;
import com.github.eiriksgata.rulateday.utlis.MyBatisUtil;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.github.eiriksgata.rulateday.service.impl
 * date: 2021/2/20
 **/
public class UserConversationImpl implements UserConversationService {

    private static final UserConversationMapper mapper = MyBatisUtil.getSqlSession().getMapper(UserConversationMapper.class);

    @Override
    public void saveConversation(Long qq, List<QueryDataBase> queryData) {
        UserConversation userConversation = new UserConversation();
        userConversation.setId(qq);
        userConversation.setData(JSONObject.toJSONString(queryData));
        userConversation.setTimestamp(System.currentTimeMillis());
        try {
            if (mapper.selectById(qq) == null) {
                mapper.insert(userConversation);
            } else {
                mapper.updateDataById(userConversation);
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
            try {
                mapper.createTable();
                mapper.insert(userConversation);
            } catch (PersistenceException ignored) {
            }
        }
        MyBatisUtil.getSqlSession().commit();
    }


    public static String checkInputQuery(MessageData<?> data) {
        //检测用户是否有对话模式记录
        UserConversation result = mapper.selectById(data.getQqID());
        if (result == null) {
            return null;
        }
        if (System.currentTimeMillis() - result.getTimestamp() > 1000 * 2 * 60) {
            mapper.deleteById(data.getQqID());
            MyBatisUtil.getSqlSession().commit();
            return null;
        }

        try {
            int number = Integer.parseInt(data.getMessage());
            List<QueryDataBase> queryData = JSONObject.parseObject(result.getData(), new TypeReference<List<QueryDataBase>>() {
            }.getType());

            if (number >= queryData.size() || number < 0) {
                return "输入的数字需要在0-" + queryData.size() + "范围内";
            }
            mapper.deleteById(data.getQqID());
            MyBatisUtil.getSqlSession().commit();
            if (queryData.get(number).getName().length() > 5) {
                if (queryData.get(number).getName().startsWith("怪物图鉴:")) {
                    QueryController.cachedThread.execute(() -> new Dnd5eLibServiceImpl().sendMMImage(data.getEvent(), queryData.get(number)));
                }
            }
            return queryData.get(number).getDescribe();
        } catch (Exception e) {
            mapper.deleteById(data.getQqID());
            MyBatisUtil.getSqlSession().commit();
            return "输入的数字不符合要求，已取消对话模式";
        }
    }

}
