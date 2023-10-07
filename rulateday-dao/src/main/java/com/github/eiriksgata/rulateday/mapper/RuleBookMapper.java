package com.github.eiriksgata.rulateday.mapper;

import com.github.eiriksgata.rulateday.pojo.RuleBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.github.eiriksgata.rulateday.mapper
 * date: 2020/11/4
 **/

@Mapper
public interface RuleBookMapper {

    @Select("select * from coc7_rule_book where title like #{title}")
    List<RuleBook> selectByTitle(@Param("title") String title);


}
