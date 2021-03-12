package indi.eiriksgata.rulateday.mapper;

import indi.eiriksgata.rulateday.pojo.UserConversation;
import indi.eiriksgata.rulateday.pojo.UserInitiativeData;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.mapper
 * date: 2021/3/11
 **/

@Mapper
public interface UserInitiativeDataMapper {


    @Insert("insert into user_initiative_data (id,name,group_id,user_id,value) values (#{id},#{name},#{groupId},#{userId},value)")
    void insert(UserInitiativeData userInitiativeData);

    @Delete("delete from user_initiative_data where group_id=#{groupId} and user_id=#{userId} and name=#{name}")
    void delete(@Param("groupId") String groupId, @Param("userId") Long userId, @Param("name") String name);

    @Delete("delete from user_initiative_data where id=#{id}")
    void deleteById(@Param("id") Long id);

    @Delete("delete from user_initiative_data where group_id=#{groupId}")
    void delteByGroupId(@Param("groupId") String groupId);

    @Select("select * from user_initiative where group_id=#{groupId}")
    List<UserInitiativeData> selectByGroupId(@Param("groupId") String groupId);

    @Select("select * from user_initiative where group_id=#{groupId} and user_id =#{userId} and name=#{name}")
    UserInitiativeData select(@Param("groupId") String groupId, @Param("userId") Long userId, @Param("name") String name);

    @Update("create table user_initiative_data\n" +
            "(\n" +
            "  id       integer,\n" +
            "  name     varchar not null,\n" +
            "  group_id varchar not null,\n" +
            "  user_id  bigint  not null,\n" +
            "  value    int\n" +
            ");\n" +
            "\n" +
            "create unique index user_initiative_data_id_uindex\n" +
            "  on user_initiative_data (id);\n")
    void createTable();

}
