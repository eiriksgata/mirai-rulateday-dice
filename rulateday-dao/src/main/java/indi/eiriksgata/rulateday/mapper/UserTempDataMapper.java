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

    @Update("create table user_temp_data\n" +
            "(\n" +
            "  id        bigint not null\n" +
            "    primary key,\n" +
            "  attribute varchar,\n" +
            "  dice_face int default 100 not null\n" +
            ");\n" +
            "\n" +
            "create unique index user_temp_data_id_uindex\n" +
            "  on user_temp_data (id);")
    void createTable();

}
