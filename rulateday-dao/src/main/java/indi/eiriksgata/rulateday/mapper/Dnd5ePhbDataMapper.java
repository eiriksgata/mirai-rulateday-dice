package indi.eiriksgata.rulateday.mapper;

import indi.eiriksgata.rulateday.pojo.QueryDataBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.mapper
 * date: 2020/11/26
 **/

@Mapper
public interface Dnd5ePhbDataMapper {

    @Select("select * from dnd5e_skill_phb")
    List<QueryDataBase> selectAllSkillPhb();

    @Select("select * from dnd5e_skill_phb where name like #{name}")
    List<QueryDataBase> selectSkillPhb(@Param("name") String name);

    @Select("select * from dnd5e_skill_phb where id =#{id}")
    QueryDataBase selectSkillPhbById(@Param("id") long id);

    @Select("select * from dnd5e_armor_weapon_phb")
    List<QueryDataBase> selectAllArmorWeapon();

    @Select("select * from dnd5e_magic_items_dmg")
    List<QueryDataBase> selectAllMagicItemsDmg();

    @Select("select * from dnd5e_mm")
    List<QueryDataBase> selectAllMM();

    @Select("select * from dnd5e_mm where name like '怪物图鉴:%' ORDER BY RANDOM() LIMIT 1")
    QueryDataBase selectRandomMM();

    @Select("select * from dnd5e_spell_list_phb")
    List<QueryDataBase> selectAllSpellListPhb();

    @Select("select * from dnd5e_tools_phb ORDER BY RANDOM() LIMIT 1")
    QueryDataBase selectRandomTool();

    @Select("select * from dnd5e_tools_phb")
    List<QueryDataBase> selectAllToolsPhb();

    @Select("select * from dnd5e_armor_weapon_phb ORDER BY RANDOM() LIMIT 1")
    QueryDataBase selectRandomArmorWeapon();

    @Select("select * from dnd5e_armor_weapon_phb where name like #{name}")
    List<QueryDataBase> selectArmorWeapon(@Param("name") String name);

    @Select("select * from dnd5e_classes_phb where name like #{name}")
    List<QueryDataBase> selectClasses(@Param("name") String name);

    @Select("select * from dnd5e_feat_phb where name like #{name}")
    List<QueryDataBase> selectFeat(@Param("name") String name);

    @Select("select * from dnd5e_races_phb where name like #{name}")
    List<QueryDataBase> selectRaces(@Param("name") String name);

    @Select("select * from dnd5e_rule_phb where name like #{name}")
    List<QueryDataBase> selectRule(@Param("name") String name);

    @Select("select * from dnd5e_tools_phb where name like #{name}")
    List<QueryDataBase> selectTools(@Param("name") String name);

    @Select("select * from dnd5e_spell_list_phb where name like #{name}")
    List<QueryDataBase> selectSpellList(@Param("name") String name);

    @Select("select * from dnd5e_magic_items_dmg ORDER BY RANDOM() LIMIT 1")
    QueryDataBase selectRandomMagicItemsDmg();

    @Select("select * from dnd5e_magic_items_dmg where name like #{name}")
    List<QueryDataBase> selectMagicItemsDmg(@Param("name") String name);

    @Select("select * from dnd5e_rule_dmg where name like #{name}")
    List<QueryDataBase> selectRuleDmg(@Param("name") String name);

    @Select("select * from dnd5e_mm where name like #{name}")
    List<QueryDataBase> selectMM(@Param("name") String name);

    @Select("select * from dnd5e_background_phb where name like #{name}")
    List<QueryDataBase> selectBackgroundPhb(@Param("name") String name);

    @Select("select * from dnd5e_creature_phb_dmg where name like #{name}")
    List<QueryDataBase> selectCreaturePhbDmg(@Param("name") String name);


}
