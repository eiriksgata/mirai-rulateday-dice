package com.github.eiriksgata.rulateday.mapper;

import com.github.eiriksgata.rulateday.pojo.UserConversation;
import org.apache.ibatis.annotations.*;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.github.eiriksgata.rulateday.mapper
 * date: 2021/2/20
 **/

@Mapper
public interface UserConversationMapper {

    @Insert("insert into user_conversation (id,data,timestamp) values (#{id},#{data},#{timestamp})")
    void insert(UserConversation userConversation);

    @Select("select * from user_conversation where id=#{id}")
    UserConversation selectById(@Param("id") Long id);

    @Update("update user_conversation set data=#{data}, timestamp=#{timestamp} where id=#{id}")
    void updateDataById(UserConversation userConversation);

    @Delete("delete from user_conversation where id=#{id}")
    void deleteById(@Param("id") Long id);

    @Update("create table user_conversation\n" +
            "(\n" +
            "  id        bigint\n" +
            "    primary key,\n" +
            "  data      varchar,\n" +
            "  timestamp bigint\n" +
            ");\n" +
            "\n" +
            "create unique index user_conversation_id_uindex\n" +
            "  on user_conversation (id);\n")
    void createTable();

}
