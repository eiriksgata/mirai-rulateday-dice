package indi.eiriksgata.rulateday.mapper;

import indi.eiriksgata.rulateday.pojo.UserConversation;
import indi.eiriksgata.rulateday.pojo.UserTempData;
import org.apache.ibatis.annotations.*;

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

    @Insert("insert into user_temp_data (id,attribute,dice_face) values (#{id},#{attribute},#{dice_face})")
    void insert(UserTempData userTempData);

    @Update("update user_temp_data set attribute = #{attribute} where id =#{id}")
    void updateAttributeById(@Param("id") Long id, @Param("attribute") String attribute);

    @Update("update user_temp_data set dice_face = #{diceFace} where id=#{id}")
    void updateDiceFaceById(@Param("id") Long id, @Param("diceFace") int diceFace);

}
