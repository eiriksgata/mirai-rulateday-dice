package com.github.eiriksgata.rulateday.mapper;

import com.github.eiriksgata.rulateday.pojo.CrazyOverDescribe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CrazyOverDescribeMapper {

    @Select("select * from coc7_crazy_over_describe")
    List<CrazyOverDescribe> selectAll();

}
