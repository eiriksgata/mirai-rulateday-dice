package indi.eiriksgata.rulateday.service.impl;

import indi.eiriksgata.rulateday.mapper.RuleBookMapper;
import indi.eiriksgata.rulateday.pojo.RuleBook;
import indi.eiriksgata.rulateday.service.RuleService;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;

import javax.annotation.Resource;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.service.impl
 * date: 2020/11/4
 **/
public class RuleServiceImpl implements RuleService {

    @Resource
    private static RuleBookMapper ruleBookMapper = MyBatisUtil.getSqlSession().getMapper(RuleBookMapper.class);

    @Override
    public RuleBook selectRule(String title) {
        return ruleBookMapper.selectByTitle("%" + title + "%");
    }

}
