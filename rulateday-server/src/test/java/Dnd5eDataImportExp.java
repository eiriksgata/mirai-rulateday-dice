import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.eiriksgata.rulateday.mapper.Dnd5ePhbTestBaseMapper;
import com.github.eiriksgata.rulateday.pojo.QueryDataBase;
import com.github.eiriksgata.rulateday.utlis.FileUtil;
import com.github.eiriksgata.rulateday.utlis.MyBatisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.junit.jupiter.api.Test;

@Slf4j
public class Dnd5eDataImportExp {

    @Test
    void importClassesExp() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String importFileName =
                "D:\\workspace\\mirai-rulateday-dice\\rulateday-server\\src\\main\\resources\\query\\5e_10_exp_classes_by_hh_pear.json";
        //使用fast json 读取json文件
        String s = FileUtil.readJsonFile(importFileName);
        JSONObject jsonObject = JSON.parseObject(s);
        jsonObject.forEach((key, value) -> {
            QueryDataBase temp = new QueryDataBase();
            temp.setName(key);
            temp.setDescribe((String) value);
            try {
                mapper.insertClasses(temp);
            } catch (PersistenceException e) {
                log.error("import data fail:{}", temp);
                e.printStackTrace();
            }
        });
        MyBatisUtil.getSqlSession().commit();
    }

    @Test
    void importMagicItemsExp() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String importFileName =
                "D:\\workspace\\mirai-rulateday-dice\\rulateday-server\\src\\main\\resources\\query\\5e_10_exp_magic_items_by_hh.json";
        //使用fast json 读取json文件
        String s = FileUtil.readJsonFile(importFileName);
        JSONObject jsonObject = JSON.parseObject(s);
        jsonObject.forEach((key, value) -> {
            QueryDataBase temp = new QueryDataBase();
            temp.setName(key);
            temp.setDescribe((String) value);
            try {
                mapper.insertMagicItemsDmg(temp);
            } catch (PersistenceException e) {
                log.error("import data fail:{}", temp);
                e.printStackTrace();
            }
        });
        MyBatisUtil.getSqlSession().commit();
    }

    @Test
    void importRacesExp() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String importFileName =
                "D:\\workspace\\mirai-rulateday-dice\\rulateday-server\\src\\main\\resources\\query\\5e_10_exp_races_by_pear.json";
        //使用fast json 读取json文件
        String s = FileUtil.readJsonFile(importFileName);
        JSONObject jsonObject = JSON.parseObject(s);
        jsonObject.forEach((key, value) -> {
            QueryDataBase temp = new QueryDataBase();
            temp.setName(key);
            temp.setDescribe((String) value);
            try {
                mapper.insertRaces(temp);
            } catch (PersistenceException e) {
                log.error("import data fail:{}", temp);
                e.printStackTrace();
            }
        });
        MyBatisUtil.getSqlSession().commit();
    }

    @Test
    void importToolsXge() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String importFileName =
                "D:\\workspace\\mirai-rulateday-dice\\rulateday-server\\src\\main\\resources\\query\\5e_10_xge_dm_tools_by_pear.json";
        //使用fast json 读取json文件
        String s = FileUtil.readJsonFile(importFileName);
        JSONObject jsonObject = JSON.parseObject(s);
        jsonObject.forEach((key, value) -> {
            QueryDataBase temp = new QueryDataBase();
            temp.setName(key);
            temp.setDescribe((String) value);
            try {
                mapper.insertTools(temp);
            } catch (PersistenceException e) {
                log.error("import data fail:{}", temp);
                e.printStackTrace();
            }
        });
        MyBatisUtil.getSqlSession().commit();
    }

    @Test
    void importEgtw() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String importFileName =
                "D:\\workspace\\mirai-rulateday-dice\\rulateday-server\\src\\main\\resources\\query\\5e_19_egtw_by_pear_3_29.json";
        //使用fast json 读取json文件
        String s = FileUtil.readJsonFile(importFileName);
        JSONObject jsonObject = JSON.parseObject(s);
        jsonObject.forEach((key, value) -> {
            QueryDataBase temp = new QueryDataBase();
            temp.setName(key);
            temp.setDescribe((String) value);
            try {
                mapper.insertEgtw(temp);
            } catch (PersistenceException e) {
                log.error("import data fail:{}", temp);
                e.printStackTrace();
            }
        });
        MyBatisUtil.getSqlSession().commit();
    }

    @Test
    void importBaseModule() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String importFileName =
                "D:\\workspace\\mirai-rulateday-dice\\rulateday-server\\src\\main\\resources\\query\\5e_30_module_items_by_hh.json";
        //使用fast json 读取json文件
        String s = FileUtil.readJsonFile(importFileName);
        JSONObject jsonObject = JSON.parseObject(s);
        jsonObject.forEach((key, value) -> {
            QueryDataBase temp = new QueryDataBase();
            temp.setName(key);
            temp.setDescribe((String) value);
            try {
                mapper.insertBaseModule(temp);
            } catch (PersistenceException e) {
                log.error("import data fail:{}", temp);
                e.printStackTrace();
            }
        });
        MyBatisUtil.getSqlSession().commit();
    }


}
