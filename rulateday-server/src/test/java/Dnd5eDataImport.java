
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import indi.eiriksgata.rulateday.mapper.Dnd5ePhbTestBaseMapper;
import indi.eiriksgata.rulateday.pojo.QueryDataBase;
import indi.eiriksgata.rulateday.utlis.FileUtil;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * author: create by Keith
 * version: v1.0
 * description: PACKAGE_NAME
 * date: 2020/11/26
 **/
@Slf4j
public class Dnd5eDataImport {


    @Test
    void importFeat() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String[] importFileName = {
                "query//5e_01_feat_by_pear.json"
        };
        //使用fast json 读取json文件
        for (String file : importFileName) {
            String path = Objects.requireNonNull(DBMybatisTest.class.getClassLoader().getResource(file)).getPath();
            String s = FileUtil.readJsonFile(path);

            Map<String, Object> result = new Gson().fromJson(s, new TypeToken<HashMap<String, Object>>() {
            }.getType());

            assert result != null;
            result.forEach((key, value) -> {
                QueryDataBase temp = new QueryDataBase();
                temp.setName(key);
                temp.setDescribe((String) value);
                mapper.insertFeat(temp);
            });

            MyBatisUtil.getSqlSession().commit();
        }
    }

    @Test
    void importClasses() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String[] importFileName = {
                "query//5e_01_phb_classes_by_pear_hze_xe.json"
        };
        //使用fast json 读取json文件
        for (String file : importFileName) {
            String path = DBMybatisTest.class.getClassLoader().getResource(file).getPath();
            String s = FileUtil.readJsonFile(path);
            JSONObject jsonObject = JSON.parseObject(s);
            jsonObject.forEach((key, value) -> {
                QueryDataBase temp = new QueryDataBase();
                temp.setName(key);
                temp.setDescribe((String) value);
                mapper.insertClasses(temp);

            });
            MyBatisUtil.getSqlSession().commit();
        }
    }

    @Test
    void importRaces() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String[] importFileName = {
                "query//5e_01_phb_races_by_pear.json"
        };
        //使用fast json 读取json文件
        for (String file : importFileName) {
            String path = DBMybatisTest.class.getClassLoader().getResource(file).getPath();
            String s = FileUtil.readJsonFile(path);
            JSONObject jsonObject = JSON.parseObject(s);
            jsonObject.forEach((key, value) -> {
                QueryDataBase temp = new QueryDataBase();
                temp.setName(key);
                temp.setDescribe((String) value);
                mapper.insertRaces(temp);

            });
            MyBatisUtil.getSqlSession().commit();
        }
    }

    @Test
    void importRule() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String[] importFileName = {
                "query//5e_02_phb_rule_by_pear.json"
        };
        //使用fast json 读取json文件
        for (String file : importFileName) {
            String path = DBMybatisTest.class.getClassLoader().getResource(file).getPath();
            String s = FileUtil.readJsonFile(path);
            JSONObject jsonObject = JSON.parseObject(s);
            jsonObject.forEach((key, value) -> {
                QueryDataBase temp = new QueryDataBase();
                temp.setName(key);
                temp.setDescribe((String) value);
                mapper.insertRule(temp);

            });
            MyBatisUtil.getSqlSession().commit();
        }
    }


    @Test
    void importTools() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String[] importFileName = {
                "query//5e_03_tools_phb_by_zxa.json"
        };
        //使用fast json 读取json文件
        for (String file : importFileName) {
            String path = DBMybatisTest.class.getClassLoader().getResource(file).getPath();
            String s = FileUtil.readJsonFile(path);
            JSONObject jsonObject = JSON.parseObject(s);
            jsonObject.forEach((key, value) -> {
                QueryDataBase temp = new QueryDataBase();
                temp.setName(key);
                temp.setDescribe((String) value);
                mapper.insertTools(temp);

            });
            MyBatisUtil.getSqlSession().commit();
        }
    }

    @Test
    void importSpellList() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String[] importFileName = {
                "query//5e_05_spell_list_by_pear.json"
        };
        //使用fast json 读取json文件
        for (String file : importFileName) {
            String path = DBMybatisTest.class.getClassLoader().getResource(file).getPath();
            String s = FileUtil.readJsonFile(path);
            JSONObject jsonObject = JSON.parseObject(s);
            jsonObject.forEach((key, value) -> {
                QueryDataBase temp = new QueryDataBase();
                temp.setName(key);
                temp.setDescribe((String) value);
                mapper.insertSpellList(temp);

            });
            MyBatisUtil.getSqlSession().commit();
        }
    }

    @Test
    void importMagicItemsDmg() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String[] importFileName = {
                "query//5e_06_magic_items_dmg_by_hze.json"
        };
        //使用fast json 读取json文件
        for (String file : importFileName) {
            String path = DBMybatisTest.class.getClassLoader().getResource(file).getPath();
            String s = FileUtil.readJsonFile(path);
            JSONObject jsonObject = JSON.parseObject(s);
            jsonObject.forEach((key, value) -> {
                QueryDataBase temp = new QueryDataBase();
                temp.setName(key);
                temp.setDescribe((String) value);
                mapper.insertMagicItemsDmg(temp);

            });
            MyBatisUtil.getSqlSession().commit();
        }
    }


    @Test
    void importRuleDmg() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String[] importFileName = {
                "query//5e_07_dmg_rule_by_pear.json"
        };
        //使用fast json 读取json文件
        for (String file : importFileName) {
            String path = DBMybatisTest.class.getClassLoader().getResource(file).getPath();
            String s = FileUtil.readJsonFile(path);
            JSONObject jsonObject = JSON.parseObject(s);
            jsonObject.forEach((key, value) -> {
                QueryDataBase temp = new QueryDataBase();
                temp.setName(key);
                temp.setDescribe((String) value);
                mapper.insertRuleDmg(temp);

            });
            MyBatisUtil.getSqlSession().commit();
        }
    }

    @Test
    void importMm() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String importFileName =
                "D:\\workspace\\mirai-rulateday-dice\\rulateday-server\\src\\main\\resources\\query\\5E_08_mm_by_hze_pear_query.json";
        //使用fast json 读取json文件
        String s = FileUtil.readJsonFile(importFileName);
        JSONObject jsonObject = JSON.parseObject(s);
        jsonObject.forEach((key, value) -> {
            QueryDataBase temp = new QueryDataBase();
            temp.setName(key);
            temp.setDescribe((String) value);
            try {
                mapper.insertMM(temp);
            } catch (PersistenceException e) {
                log.error("import data fail:{}", temp);
            }
        });
        MyBatisUtil.getSqlSession().commit();
    }

    @Test
    void importCreatureDmgMm() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String importFileName =
                "D:\\workspace\\mirai-rulateday-dice\\rulateday-server\\src\\main\\resources\\query\\5e_09_creature_phb_dmg_by_hze.json";
        //使用fast json 读取json文件
        String s = FileUtil.readJsonFile(importFileName);
        JSONObject jsonObject = JSON.parseObject(s);
        jsonObject.forEach((key, value) -> {
            QueryDataBase temp = new QueryDataBase();
            temp.setName(key);
            temp.setDescribe((String) value);
            try {
                mapper.insertMM(temp);
            } catch (PersistenceException e) {
                log.error("import data fail:{}", temp);
                e.printStackTrace();
            }
        });
        MyBatisUtil.getSqlSession().commit();
    }


    @Test
    void importBackgroundPhb() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String[] importFileName = {
                "query//5e_08_phb_background_by_pear.json"
        };
        //使用fast json 读取json文件
        for (String file : importFileName) {
            String path = DBMybatisTest.class.getClassLoader().getResource(file).getPath();
            String s = FileUtil.readJsonFile(path);
            JSONObject jsonObject = JSON.parseObject(s);
            jsonObject.forEach((key, value) -> {
                QueryDataBase temp = new QueryDataBase();
                temp.setName(key);
                temp.setDescribe((String) value);
                mapper.insertBackgroundPhb(temp);

            });
            MyBatisUtil.getSqlSession().commit();
        }
    }


    @Test
    void importCreaturePhbDmg() {
        Dnd5ePhbTestBaseMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbTestBaseMapper.class);
        String[] importFileName = {
                "query//5e_09_creature_phb_dmg_by_hze.json"
        };
        //使用fast json 读取json文件
        for (String file : importFileName) {
            String path = DBMybatisTest.class.getClassLoader().getResource(file).getPath();
            String s = FileUtil.readJsonFile(path);
            JSONObject jsonObject = JSON.parseObject(s);
            jsonObject.forEach((key, value) -> {
                QueryDataBase temp = new QueryDataBase();
                temp.setName(key);
                temp.setDescribe((String) value);
                mapper.insertCreaturePhbDmg(temp);

            });
            MyBatisUtil.getSqlSession().commit();
        }
    }


}
