package indi.eiriksgata.rulateday.utlis;

import indi.eiriksgata.rulateday.RulatedayCore;

import java.io.*;
import java.util.ResourceBundle;

import static org.apache.ibatis.io.Resources.getResourceAsStream;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.utlis
 * date: 2020/11/24
 **/
public class LoadDatabaseFile {

    public static void init() {
        try {
            createDatabaseFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createDatabaseFile() throws IOException {
        //读取db配置文件
        ResourceBundle dbPropertiesConfig = ResourceBundle.getBundle("db");

        String dbFileName = dbPropertiesConfig.getString("db.file.name");
        String dbFileVersion = dbPropertiesConfig.getString("db.file.version");
        String dbCreateName = dbFileName + dbFileVersion + ".db";
        InputStream inputStream = getResourceAsStream(dbFileName + ".db");
        String path = RulatedayCore.INSTANCE.getDataFolderPath().toString();

        File file = new File(path + "/" + dbCreateName);
        File mkdir = new File(path);
        File mmImages = new File(path + "/mm-images");
        if (!mkdir.exists()) {
             mkdir.mkdirs();
        }
        if (!mmImages.exists()) {
            mmImages.mkdirs();
        }
        if (!file.exists()) {

//      RulatedayCore.INSTANCE.getLogger().info("Detecting no database file, creating..");
            OutputStream output = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            while (true) {
                int readLength = inputStream.read(bytes);
                if (readLength == -1) {
                    break;
                }
                byte[] outData = new byte[readLength];
                System.arraycopy(bytes, 0, outData, 0, readLength);
                output.write(outData);
            }
            output.flush();
            inputStream.close();
            output.close();
        }
    }


}
