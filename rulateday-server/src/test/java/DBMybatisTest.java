import indi.eiriksgata.rulateday.mapper.UserTempDataMapper;
import indi.eiriksgata.rulateday.pojo.UserTempData;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: PACKAGE_NAME
 * @date:2020/10/20
 **/
public class DBMybatisTest {

    @Test
    void dbTest() {

        UserTempDataMapper userTempDataMapper = MyBatisUtil.getSqlSession().getMapper(UserTempDataMapper.class);
        UserTempData userTempData = userTempDataMapper.selectById(1L);
        System.out.println(userTempData.getAttribute());


    }
}
