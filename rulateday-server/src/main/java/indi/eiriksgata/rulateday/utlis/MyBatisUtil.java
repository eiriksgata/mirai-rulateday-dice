package indi.eiriksgata.rulateday.utlis;

import indi.eiriksgata.rulateday.RulatedayCore;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class MyBatisUtil {

    private static SqlSessionFactory factory = null;
    private static SqlSession sqlSession = null;

    private static SqlSessionFactory createSFactory() {
        try {
            Properties properties = Resources.getResourceAsProperties("db.properties");

            Reader reader = Resources.getResourceAsReader("mybatis-config.xml");

            ResourceBundle dbPropertiesConfig = ResourceBundle.getBundle("db");
            String dbFileName = dbPropertiesConfig.getString("db.file.name");
            String dbFileVersion = dbPropertiesConfig.getString("db.file.version");
            String dbCreateName = dbFileName + dbFileVersion + ".db";

            RulatedayCore.INSTANCE.getLogger().info(System.getProperty("os.name"));
            String path = "jdbc:sqlite:" + RulatedayCore.INSTANCE.getDataFolderPath() + "/" + dbCreateName;

            if (System.getProperty("os.name").endsWith("Linux")) {
                path = "jdbc:sqlite:" + "/" + RulatedayCore.INSTANCE.getDataFolderPath().toString().substring(1) + "/" + dbCreateName;
            }

            properties.setProperty("url", path);

            RulatedayCore.INSTANCE.getLogger().info("db-config:" + properties);

            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            return builder.build(reader, properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SqlSession getSqlSession() {
        if (sqlSession == null) {
            factory = createSFactory();
            if (factory == null) {
                RulatedayCore.INSTANCE.getLogger().error("Unable to connect to database file!");
            } else {
                sqlSession = factory.openSession();
            }
        }
        return sqlSession;
    }

}


