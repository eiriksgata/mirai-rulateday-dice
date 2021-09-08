package indi.eiriksgata.rulateday.mapper;

import indi.eiriksgata.rulateday.pojo.QueryDataBase;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.mapper
 * date: 2020/11/26
 **/

/**
 * 该文件仅作为导入数据测试使用
 */
@Mapper
@Deprecated
public interface Dnd5ePhbTestBaseMapper {

    @Insert("insert into dnd5e_armor_weapon_phb (id,name,describe) values (#{id},#{name},#{describe})")
    void insertArmorWeapon(QueryDataBase dataBase);

    @Insert("insert into dnd5e_classes_phb (id,name,describe) values (#{id},#{name},#{describe})")
    void insertClasses(QueryDataBase dataBase);

    @Insert("insert into dnd5e_feat_phb (id,name,describe) values (#{id},#{name},#{describe})")
    void insertFeat(QueryDataBase dataBase);

    @Insert("insert into dnd5e_races_phb (id,name,describe) values (#{id},#{name},#{describe})")
    void insertRaces(QueryDataBase dataBase);

    @Insert("insert into dnd5e_rule_phb (id,name,describe) values (#{id},#{name},#{describe})")
    void insertRule(QueryDataBase dataBase);

    @Insert("insert into dnd5e_tools_phb (id,name,describe) values (#{id},#{name},#{describe})")
    void insertTools(QueryDataBase dataBase);

    @Insert("insert into dnd5e_spell_list_phb (id,name,describe) values (#{id},#{name},#{describe})")
    void insertSpellList(QueryDataBase dataBase);

    @Insert("insert into dnd5e_magic_items_dmg (id,name,describe) values (#{id},#{name},#{describe})")
    void insertMagicItemsDmg(QueryDataBase dataBase);

    @Insert("insert into dnd5e_rule_dmg (id,name,describe) values (#{id},#{name},#{describe})")
    void insertRuleDmg(QueryDataBase dataBase);

    @Insert("insert into dnd5e_mm (id,name,describe) values (#{id},#{name},#{describe})")
    void insertMM(QueryDataBase dataBase);

    @Insert("insert into dnd5e_background_phb (id,name,describe) values (#{id},#{name},#{describe})")
    void insertBackgroundPhb(QueryDataBase dataBase);

    @Insert("insert into dnd5e_creature_phb_dmg (id,name,describe) values (#{id},#{name},#{describe})")
    void insertCreaturePhbDmg(QueryDataBase dataBase);

    @Insert("insert into dnd5e_egtw (id,name,describe) values (#{id},#{name},#{describe})")
    void insertEgtw(QueryDataBase dataBase);

    @Insert("insert into dnd5e_base_module (id,name,describe) values (#{id},#{name},#{describe})")
    void insertBaseModule(QueryDataBase dataBase);

}
