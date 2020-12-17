package indi.eiriksgata.rulateday.mapper;

import indi.eiriksgata.rulateday.pojo.Dnd5ESkillLib;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.mapper
 * date: 2020/11/13
 **/
@Mapper
public interface Dnd5eSkillMapper {

    @Select("select * from dnd5e_skill_phb")
    List<Dnd5ESkillLib> selectAll();

    @Select("select * from dnd5e_skill_phb where name like #{name}")
    Dnd5ESkillLib selectByName(@Param("name") String name);

    @Select("select * from dnd5e_skill_phb where id =#{id}")
    Dnd5ESkillLib selectById(@Param("id") long id);

}
