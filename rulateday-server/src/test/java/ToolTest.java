import indi.eiriksgata.rulateday.utlis.HmacSHA256Util;
import indi.eiriksgata.rulateday.utlis.OsUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;


public class ToolTest {
    @Test
    void deviceCode() throws Exception {
        String result = "";
        if (OsUtils.isLinux()) {
            result = OsUtils.getIdentifierByLinux();
        } else {
            result = OsUtils.getIdentifierByWindows();
        }
        result = HmacSHA256Util.hmacSHA256("rulateday-dice", result);
        System.out.println(result);
    }

    @Test
    void files() throws IOException {
        File file = new File("D:\\workspace\\mirai-rulateday-dice\\rulateday-server\\src\\main\\resources\\custom-text.json");

        System.out.println(file.getPath());
        System.out.println(file.getCanonicalPath());
        System.out.println(file.getAbsoluteFile());
        System.out.println(file.getParent());

    }
}
