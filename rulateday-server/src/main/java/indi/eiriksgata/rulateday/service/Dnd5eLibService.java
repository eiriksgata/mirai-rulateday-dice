package indi.eiriksgata.rulateday.service;

import indi.eiriksgata.rulateday.pojo.Dnd5ESkillLib;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.service
 * date: 2020/11/13
 **/
public interface Dnd5eLibService {
    List<Dnd5ESkillLib> findAllSkill();

    Dnd5ESkillLib findName(String name);

    Dnd5ESkillLib findById(long id);
}
