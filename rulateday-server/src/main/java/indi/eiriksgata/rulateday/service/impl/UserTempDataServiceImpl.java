package indi.eiriksgata.rulateday.service.impl;

import indi.eiriksgata.dice.config.DiceConfig;
import indi.eiriksgata.rulateday.mapper.UserTempDataMapper;
import indi.eiriksgata.rulateday.pojo.UserTempData;
import indi.eiriksgata.rulateday.service.UserTempDataService;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.rulateday.service.impl
 * @date:2020/10/22
 **/
public class UserTempDataServiceImpl implements UserTempDataService {


    @Override
    public void updateUserAttribute(Long id, String attribute) {
        UserTempDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(UserTempDataMapper.class);
        if (mapper.selectById(id) == null) {
            UserTempData userTempData = new UserTempData();
            userTempData.setId(id);
            userTempData.setAttribute(attribute);
            userTempData.setDiceFace(Long.valueOf(
                    DiceConfig.diceSet.getString(
                            DiceConfig.diceSet.getString("dice.type") + ".face")
            ));
            mapper.insert(userTempData);
        } else {
            mapper.updateAttributeById(id, attribute);
        }
    }


}
