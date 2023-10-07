
import com.github.eiriksgata.rulateday.mapper.CrazyOverDescribeMapper;
import com.github.eiriksgata.rulateday.mapper.SpeakersGroupListMapper;
import com.github.eiriksgata.rulateday.pojo.CrazyOverDescribe;
import com.github.eiriksgata.rulateday.service.impl.UserInitiativeServerImpl;
import com.github.eiriksgata.rulateday.init.LoadDatabaseFile;
import com.github.eiriksgata.rulateday.utlis.MyBatisUtil;
import org.apache.ibatis.io.Resources;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;


/**
 * author: create by Keith
 * version: v1.0
 * description: PACKAGE_NAME
 * date:2020/10/20
 **/
public class DBMybatisTest {

    @Test
    void dbTest() {

        CrazyOverDescribeMapper mapper = MyBatisUtil.getSqlSession().getMapper(CrazyOverDescribeMapper.class);
        List<CrazyOverDescribe> list = mapper.selectAll();

        list.forEach((temp) -> {
            System.out.println(temp.getDescribe());
        });

    }


    @Test
    void dnd5eSkill() {
        SpeakersGroupListMapper mapper = MyBatisUtil.getSqlSession().getMapper(SpeakersGroupListMapper.class);
        mapper.createTable();
        MyBatisUtil.getSqlSession().commit();

    }

    @Test
    void create() {


    }

    @Test
    void file() throws IOException {
        File file = new File("");
        //输出程序路径
        try {
            System.out.println("canonical:" + file.getCanonicalPath());
            System.out.println("absolute:" + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        LoadDatabaseFile.createDatabaseFile();


    }

    @Test
    void testInitiativeListShow() {
        System.out.println(
                new UserInitiativeServerImpl().showInitiativeList("1000")

        );

    }


    @Test
    void testConfigFile() throws IOException {
        Properties properties = new Properties();
        ResourceBundle dbPropertiesConfig = ResourceBundle.getBundle("db");
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");

        String dbFileName = dbPropertiesConfig.getString("db.file.name");
        String dbFileVersion = dbPropertiesConfig.getString("db.file.version");
        String dbCreateName = dbFileName + dbFileVersion + ".db";
       // String path = "jdbc:sqlite:" + RulatedayCore.INSTANCE.getDataFolderPath() + "\\" + dbCreateName;
        String path = "jdbc:sqlite:" + "test" + "\\" + dbCreateName;


        properties.load(inputStream);
        properties.setProperty("url", path);

        System.out.println(properties);


        System.out.println(properties.get("driver"));

    }


}
