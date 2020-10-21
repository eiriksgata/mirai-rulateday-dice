package indi.eiriksgata.rulateday.mapper;

import indi.eiriksgata.rulateday.pojo.UserTempData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.rulateday.dao
 * @date:2020/10/20
 **/

@Mapper
public interface UserTempDataMapper {

    @Select("select * from user_temp_data where id=#{id}")
    UserTempData selectById(@Param("id") Long id);


}
