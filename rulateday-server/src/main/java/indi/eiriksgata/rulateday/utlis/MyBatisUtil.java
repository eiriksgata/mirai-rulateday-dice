package indi.eiriksgata.rulateday.utlis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class MyBatisUtil {

    private static SqlSessionFactory factory = createSFactory();
    private static SqlSession sqlSession = factory != null ? factory.openSession() : null;

    private static SqlSessionFactory createSFactory() {
        try {
            Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            return builder.build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SqlSession getSqlSession() {
        return sqlSession;
    }

}


