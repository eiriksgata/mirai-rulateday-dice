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

    



}
