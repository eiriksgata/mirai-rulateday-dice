import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * author: create by Keith
 * version: v1.0
 * description: PACKAGE_NAME
 * date: 2020/12/24
 **/
public class NetURLTest {

    @Test
    void netTest() throws IOException {
        String mmNameFileName = "熔岩魔蝠" + ".png";
        try {
            mmNameFileName = URLEncoder.encode(mmNameFileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        URL url = new URL("https://eiriksgata.github.io/rulateday-dnd5e-wiki/mm-image/" + mmNameFileName);
        url.openConnection().connect();
        url.openConnection().connect();
    }
}
