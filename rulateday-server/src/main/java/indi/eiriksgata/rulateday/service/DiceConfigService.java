package indi.eiriksgata.rulateday.service;

import indi.eiriksgata.rulateday.mapper.DiceConfigMapper;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;

public class DiceConfigService {

    public static final DiceConfigMapper diceConfigMapper = MyBatisUtil.getSqlSession().getMapper(DiceConfigMapper.class);


}
