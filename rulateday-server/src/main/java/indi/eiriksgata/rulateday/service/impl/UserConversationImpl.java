package indi.eiriksgata.rulateday.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.exception.RulatedayException;
import indi.eiriksgata.rulateday.mapper.UserConversationMapper;
import indi.eiriksgata.rulateday.pojo.QueryDataBase;
import indi.eiriksgata.rulateday.pojo.UserConversation;
import indi.eiriksgata.rulateday.service.Dnd5eLibService;
import indi.eiriksgata.rulateday.service.UserConversationService;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;

import javax.annotation.Resource;
import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.service.impl
 * date: 2021/2/20
 **/
public class UserConversationImpl implements UserConversationService {

    private static UserConversationMapper mapper = MyBatisUtil.getSqlSession().getMapper(UserConversationMapper.class);

    @Override
    public void saveConversation(Long qq, List<QueryDataBase> queryData) {
        UserConversation userConversation = new UserConversation();
        userConversation.setId(qq);
        userConversation.setData(new Gson().toJson(queryData));
        userConversation.setTimestamp(System.currentTimeMillis());
        mapper.insert(userConversation);
        MyBatisUtil.getSqlSession().commit();
    }


    public static String checkInputQuery(MessageData data) {
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
            int number = Integer.valueOf(data.getMessage());
            List<QueryDataBase> queryData = new Gson().fromJson(result.getData(), new TypeToken<List<QueryDataBase>>() {
            }.getType());
            if (number >= queryData.size() || number < 0) {
                return "输入的数字需要在0-" + queryData.size() + "范围内";
            }
            mapper.deleteById(data.getQqID());
            MyBatisUtil.getSqlSession().commit();
            if (queryData.get(number).getName().length() > 5) {
                if (queryData.get(number).getName().substring(0, 5).equals("怪物图鉴:")) {
                    try {
                        new Dnd5eLibServiceImpl().sendMMImage(data.getEvent(), queryData.get(number));
                    } catch (RulatedayException e) {
                        return e.getErrMsg();
                    }
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
