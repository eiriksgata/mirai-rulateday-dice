import indi.eiriksgata.rulateday.utlis.HmacSHA256Util;
import indi.eiriksgata.rulateday.utlis.OsUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

    @Test
    void handlerTimer() {
        List<String> list = new ArrayList<>();
        list.add(".");
        list.add("。");
        String inputText = ".rd";
        long startA1 = System.nanoTime();
        for (String temp : list) {
            if (temp.length() > inputText.length()) {
                continue;
            }
            if (temp.equals(inputText.substring(0, temp.length()))) {
                System.out.println(temp);
                break;
            }
        }
        System.out.println(System.nanoTime() - startA1);


        String regex = "^[.。].*$";

        startA1 = System.nanoTime();
        if (inputText.matches(regex)){
            System.out.println(inputText);
        }
        System.out.println(System.nanoTime() - startA1);
    }

    @Test
    void listTest(){

    }

}

