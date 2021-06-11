import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import indi.eiriksgata.rulateday.utlis.HexConvertUtil;
import indi.eiriksgata.rulateday.vo.ResponseBaseVo;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static indi.eiriksgata.rulateday.utlis.FileUtil.readInputStream;

/**
 * author: create by Keith
 * version: v1.0
 * description: PACKAGE_NAME
 * date: 2021/6/8
 **/
public class Netimage2File {

    @Test
    void run() {

        File file = new File("tempFile" + System.currentTimeMillis());
        try {
            URL url = new URL("https://danbooru.donmai.us/data/original/2e/f9/__ethan_winters_resident_evil_and_1_more_drawn_by_lesle_kieu__2ef905d0def35e3127e8e2d2f60bcd8e.jpg");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            // 防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            // 得到输入流
            InputStream inputStream = conn.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(readInputStream(inputStream));
            fos.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    void getByteArray() {
        String url = "http://localhost:8089/picture/random";
        String resultJson = HttpRequest.get(url).body();
        System.out.println(resultJson);
        ResponseBaseVo<String> response = new Gson().fromJson(
                resultJson, new TypeToken<ResponseBaseVo<String>>() {
                }.getType());

        byte[] pictureData = HexConvertUtil.hexStringToByteArray(response.getData());
        System.out.println(HexConvertUtil.bytesToHex(pictureData));

    }
}
