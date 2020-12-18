import indi.eiriksgata.rulateday.mapper.Dnd5ePhbDataMapper;
import indi.eiriksgata.rulateday.pojo.QueryDataBase;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: PACKAGE_NAME
 * date: 2020/12/18
 **/
public class DocTextOut {

    @Test
    void ArmorWeapon() {
        Dnd5ePhbDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbDataMapper.class);
        List<QueryDataBase> result = mapper.selectAllArmorWeapon();
        for (QueryDataBase temp : result) {
            System.out.println("## " + temp.getName());
            System.out.println(temp.getDescribe());
        }
    }

    @Test
    void MagicItem() {
        Dnd5ePhbDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbDataMapper.class);
        List<QueryDataBase> result = mapper.selectAllMagicItemsDmg();
        for (QueryDataBase temp : result) {
            System.out.println("## " + temp.getName());
            System.out.println(temp.getDescribe());
        }
    }
}
