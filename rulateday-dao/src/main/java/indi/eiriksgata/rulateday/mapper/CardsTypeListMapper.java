package indi.eiriksgata.rulateday.mapper;

import indi.eiriksgata.rulateday.pojo.CardsGroupData;
import indi.eiriksgata.rulateday.pojo.CardsTypeList;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.mapper
 * date: 2021/4/19
 **/
@Mapper
public interface CardsTypeListMapper {

    @Insert("insert into cards_type_list (id,name,content) values (#{id},#{name},#{content})")
    void insert(CardsTypeList cardsTypeList);

    @Delete("delete from cards_type_list where id = #{id}")
    void deleteById(@Param("id") Long id);

    @Delete("delete from cards_type_list where name = #{name}")
    void deleteByName(@Param("name") String name);

    @Select("select * from cards_type_list where id =#{id}")
    CardsTypeList selectById(@Param("id") Long id);

    @Select("select * from cards_type_list where name=#{name}")
    CardsTypeList selectByName(@Param("name") String name);

    @Select("select * from cards_type_list")
    List<CardsTypeList> selectAll();

}
