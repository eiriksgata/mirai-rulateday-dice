package com.github.eiriksgata.rulateday.mapper;

import com.github.eiriksgata.rulateday.pojo.CardsTypeList;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
