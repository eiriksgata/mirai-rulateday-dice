package indi.eiriksgata.rulateday.mapper;

import org.apache.ibatis.annotations.*;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.mapper
 * date: 2020/11/13
 **/
@Mapper
public interface SpeakersGroupListMapper {

    @Select("select is_enable from speakers_group_list where id = #{id} ")
    Boolean selectByGroupId(@Param("id") long groupId);

    @Insert("insert into  speakers_group_list (id,is_enable) values (#{id},#{isEnable})")
    void insert(@Param("id") long id, @Param("isEnable") boolean isEnable);

    @Update("update speakers_group_list set is_enable=#{isEnable} where id=#{id}")
    void updateIsEnableById(@Param("id") long id, @Param("isEnable") boolean isEnable);

}
