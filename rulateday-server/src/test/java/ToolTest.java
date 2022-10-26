import indi.eiriksgata.rulateday.utlis.HmacSHA256Util;
import indi.eiriksgata.rulateday.utlis.OsUtils;
import org.junit.jupiter.api.Test;


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
}
