package indi.eiriksgata.rulateday.utlis;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: com.ajb.com.ajb.sdk.utils
 * @date:2020/8/25
 **/
@Slf4j
public class ImageToBase64 {

    /**
     * 本地图片转换Base64的方法
     *
     * @param imgPath
     */
    public static String ImageToBase64(String imgPath) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = Files.newInputStream(Paths.get(imgPath));
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码

        // 返回Base64编码过的字节数组字符串
        return new String(Base64.encode(Objects.requireNonNull(data))).replaceAll("\n", "");
    }

    /**
     * Base64 转图片
     */
    public static boolean base64ToImage(String base64Str, String savePath) {
        if (base64Str.equals("")) {
            return false;
        }
        try {
            byte[] bytes = Base64.decode2(base64Str);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            File file = new File(savePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream out = Files.newOutputStream(file.toPath());
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}