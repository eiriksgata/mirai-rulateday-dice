package com.github.eiriksgata.rulateday.service.impl;

import com.github.eiriksgata.rulateday.mapper.RuleBookMapper;
import com.github.eiriksgata.rulateday.pojo.RuleBook;
import com.github.eiriksgata.trpg.dice.reply.CustomText;
import com.github.eiriksgata.rulateday.service.RuleService;
import com.github.eiriksgata.rulateday.utlis.MyBatisUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.github.eiriksgata.rulateday.service.impl
 * date: 2020/11/4
 **/
public class RuleServiceImpl implements RuleService {

    @Resource
    private static final RuleBookMapper ruleBookMapper = MyBatisUtil.getSqlSession().getMapper(RuleBookMapper.class);

    @Override
    public String selectRule(String title) {
        List<RuleBook> list = ruleBookMapper.selectByTitle("%" + title + "%");
        if (list.size() == 0) {
            return CustomText.getText("coc7.rule.not.found");
        }
        if (list.size() > 1) {
            return CustomText.getText("coc7.rule.multiple.query.result");
        }
        return list.get(0).getTitle() + "\n" + list.get(0).getContent();
    }

}
