package indi.eiriksgata.rulateday.mapper;

import indi.eiriksgata.rulateday.pojo.DiceConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DiceConfigMapper {

    @Select("select * from dice_config where id = 1")
    DiceConfigEntity selectById();

    @Update("update dice_config set private_chat=#{privateChat} where id= 1")
    void updateByPrivateChat(@Param("privateChat") Boolean privateChat);

    @Update("update dice_config set beta_version =#{betaVersion} where id = 1")
    void updateByBetaVersion(@Param("betaVersion") Boolean betaVersion);


}