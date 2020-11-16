package indi.eiriksgata.rulateday.service.impl;

import indi.eiriksgata.rulateday.mapper.Dnd5eSkillLibMapper;
import indi.eiriksgata.rulateday.pojo.Dnd5ESkillLib;
import indi.eiriksgata.rulateday.service.Dnd5eLibService;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.service.impl
 * date: 2020/11/13
 **/
public class Dnd5eLibServiceImpl implements Dnd5eLibService {


    @Override
    public List<Dnd5ESkillLib> findAllSkill() {
        Dnd5eSkillLibMapper skillMapper = MyBatisUtil.getSqlSession().getMapper(Dnd5eSkillLibMapper.class);
        return skillMapper.selectAll();
    }


    @Override
    public Dnd5ESkillLib findName(String name) {
        Dnd5eSkillLibMapper skillMapper = MyBatisUtil.getSqlSession().getMapper(Dnd5eSkillLibMapper.class);
        return skillMapper.selectByName("%" + name + "%");
    }

    @Override
    public Dnd5ESkillLib findById(long id) {
        Dnd5eSkillLibMapper skillMapper = MyBatisUtil.getSqlSession().getMapper(Dnd5eSkillLibMapper.class);
        return skillMapper.selectById(id);
    }


}
