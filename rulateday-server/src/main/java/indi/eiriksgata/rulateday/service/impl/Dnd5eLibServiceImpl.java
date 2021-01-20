package indi.eiriksgata.rulateday.service.impl;

import indi.eiriksgata.rulateday.mapper.Dnd5ePhbDataMapper;
import indi.eiriksgata.rulateday.pojo.QueryDataBase;
import indi.eiriksgata.rulateday.service.Dnd5eLibService;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.service.impl
 * date: 2020/11/13
 **/
public class Dnd5eLibServiceImpl implements Dnd5eLibService {

    @Override
    public List<QueryDataBase> findName(String name) {
        List<QueryDataBase> result = new ArrayList<>();
        Dnd5ePhbDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbDataMapper.class);
        result.addAll(mapper.selectSkillPhb("%" + name + "%"));
        result.addAll(mapper.selectArmorWeapon("%" + name + "%"));
        result.addAll(mapper.selectClasses("%" + name + "%"));
        result.addAll(mapper.selectFeat("%" + name + "%"));
        result.addAll(mapper.selectRaces("%" + name + "%"));
        result.addAll(mapper.selectRule("%" + name + "%"));
        result.addAll(mapper.selectTools("%" + name + "%"));
        result.addAll(mapper.selectSpellList("%" + name + "%"));
        result.addAll(mapper.selectMagicItemsDmg("%" + name + "%"));
        result.addAll(mapper.selectRuleDmg("%" + name + "%"));
        result.addAll(mapper.selectMM("%" + name + "%"));
        result.addAll(mapper.selectBackgroundPhb("%" + name + "%"));
        return result;
    }

    @Override
    public QueryDataBase findById(long id) {
        Dnd5ePhbDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbDataMapper.class);
        return mapper.selectSkillPhbById(id);
    }


    @Override
    public QueryDataBase getRandomMMData() {
        Dnd5ePhbDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbDataMapper.class);
        MyBatisUtil.getSqlSession().clearCache();
        return mapper.selectRandomMM();
    }


}
