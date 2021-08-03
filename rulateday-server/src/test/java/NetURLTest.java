import indi.eiriksgata.rulateday.RulatedayCore;
import indi.eiriksgata.rulateday.utlis.FileUtil;
import net.mamoe.mirai.console.plugin.PluginManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;

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

    @Test
    void fileSave() {

       PluginManager.INSTANCE.loadPlugin(RulatedayCore.INSTANCE);
        PluginManager.INSTANCE.enablePlugin(RulatedayCore.INSTANCE);

        String imagesUrl = ResourceBundle.getBundle("resources").getString("resources.mm.images.url");
        String localPath = RulatedayCore.INSTANCE.getDataFolderPath() + ResourceBundle.getBundle("resources").getString("resources.mm.images.path");
        String mmNameFileName = "食尸鬼.png";
        String url = imagesUrl;
        try {
            url += URLEncoder.encode(mmNameFileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        File imageFile = new File(localPath + mmNameFileName);
        if (!imageFile.exists()) {
            try {
                FileUtil.downLoadFromUrl(url, localPath + mmNameFileName);
            } catch (Exception e) {
                RulatedayCore.INSTANCE.getLogger().info("下载图片失败，服务器可能没有该资源");
            }

            String abc = "1";
        }

    }


}
