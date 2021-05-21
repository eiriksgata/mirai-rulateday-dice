import indi.eiriksgata.rulateday.utlis.CharacterUtils;
import org.junit.jupiter.api.Test;

/**
 * author: create by Keith
 * version: v1.0
 * description: PACKAGE_NAME
 * date: 2021/5/21
 **/
public class CharacterUtilsTest {
    @Test
    void opTest() {
        String result = CharacterUtils.operationSymbolProcessing("3➕5");
        System.out.println(result);
    }
}
