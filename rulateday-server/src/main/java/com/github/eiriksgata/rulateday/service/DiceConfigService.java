package com.github.eiriksgata.rulateday.service;

import com.github.eiriksgata.rulateday.mapper.DiceConfigMapper;
import com.github.eiriksgata.rulateday.utlis.MyBatisUtil;

public class DiceConfigService {

    public static final DiceConfigMapper diceConfigMapper = MyBatisUtil.getSqlSession().getMapper(DiceConfigMapper.class);


}
