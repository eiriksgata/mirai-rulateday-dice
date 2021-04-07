package indi.eiriksgata.rulateday.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.mapper
 * date: 2021/4/7
 **/
@Mapper
public interface NamesCorpusMapper {

    @Insert("insert into cn_ancient_names_corpus (id,name) values (#{id},#{name})")
    void insertCNAncient(@Param("id") Long id, @Param("name") String name);

    @Insert("insert into english_cn_name_corpus (id,name) values (#{id},#{name})")
    void insertEnglishCN(@Param("id") Long id, @Param("name") String name);

    @Insert("insert into japanese_names_corpus (id,name) values (#{id},#{name})")
    void insertJapanese(@Param("id") Long id, @Param("name") String name);

    @Select("select name from cn_ancient_names_corpus ORDER BY RANDOM() LIMIT 1")
    String getCNAncientRandomName();

    @Select("select name from english_cn_name_corpus ORDER BY RANDOM() LIMIT 1")
    String getEnglishRandomName();

    @Select("select name from japanese_names_corpus ORDER BY RANDOM() LIMIT 1")
    String getJapaneseRandomName();


}
