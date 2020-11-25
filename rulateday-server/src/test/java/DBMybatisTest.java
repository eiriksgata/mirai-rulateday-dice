
import indi.eiriksgata.rulateday.mapper.CrazyDescribeMapper;
import indi.eiriksgata.rulateday.mapper.CrazyOverDescribeMapper;
import indi.eiriksgata.rulateday.pojo.CrazyDescribe;
import indi.eiriksgata.rulateday.pojo.CrazyOverDescribe;
import indi.eiriksgata.rulateday.service.impl.Dnd5eLibServiceImpl;
import indi.eiriksgata.rulateday.utlis.LoadDatabaseFile;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * @author: create by Keith
 * @version: v1.0
 * @description: PACKAGE_NAME
 * @date:2020/10/20
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
        new Dnd5eLibServiceImpl().findAllSkill();

    }

    @Test
    void file() throws IOException {

        LoadDatabaseFile.createDatabaseFile();


    }
}
