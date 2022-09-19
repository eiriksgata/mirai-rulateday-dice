import indi.eiriksgata.rulateday.utlis.MyBatisUtil;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.security.SecureRandom;

/**
 * author: create by Keith
 * version: v1.0
 * description: PACKAGE_NAME
 * date: 2021/4/7
 **/
public class NameTextToDb {

    @Test
    void run() throws IOException {
        File file = new File("D:\\workspace\\mirai-rulateday-dice\\LoadFileTest.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        int readLength;
        int countLength = 0;
        byte[] bufferContent = new byte[1048576];
        while (true) {
            readLength = fileInputStream.read(bufferContent, countLength, 1024);
            if (readLength == -1) {
                break;
            }
            countLength += readLength;
        }
        byte[] result = new byte[countLength];
        System.arraycopy(bufferContent, 0, result, 0, countLength);
        System.out.println(
                new String(result)
        );

    }


}
