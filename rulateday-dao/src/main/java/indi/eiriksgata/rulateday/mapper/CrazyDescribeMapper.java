package indi.eiriksgata.rulateday.mapper;

import indi.eiriksgata.rulateday.pojo.CrazyDescribe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.mapper
 * date: 2020/11/4
 **/
@Mapper
public interface CrazyDescribeMapper {

    @Select("select * from crazy_describe")
    List<CrazyDescribe> selectAll();

}
