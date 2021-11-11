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

    private static String path;

    public static void init() {
        try {
            path = RulatedayCore.INSTANCE.getDataFolderPath().toString();
        } catch (ExceptionInInitializerError ignored) {
            path = "";
        }
        try {
            createProjectFile();
            createCustomTextConfigFile();
            createImageFile();
            createDatabaseFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createProjectFile() throws IOException {
        File mkdir = new File(path);
        if (!mkdir.exists()) {
            mkdir.mkdirs();
        }
    }

    public static void createCustomTextConfigFile() throws IOException {
        String customTextConfigFileName = "custom-text.json";
        File file = new File( "config/indi.eiriksgata.rulateday-dice/" + customTextConfigFileName);
        InputStream inputStream = getResourceAsStream(customTextConfigFileName);
        if (!file.exists()) {
            System.out.println("create custom text config file...");
            fileOut(file, inputStream);
        }
    }

    public static void createImageFile() throws IOException {
        File mmImages = new File(path + "/mm-images");
        if (!mmImages.exists()) {
            mmImages.mkdirs();
        }
    }


    public static void createDatabaseFile() throws IOException {
        //读取db配置文件
        ResourceBundle dbPropertiesConfig = ResourceBundle.getBundle("db");
        String dbFileName = dbPropertiesConfig.getString("db.file.name");
        String dbFileVersion = dbPropertiesConfig.getString("db.file.version");
        String dbCreateName = dbFileName + dbFileVersion + ".db";
        InputStream inputStream = getResourceAsStream(dbFileName + ".db");
        File file = new File(path + "/" + dbCreateName);
        if (!file.exists()) {
            //RulatedayCore.INSTANCE.getLogger().info("Detecting no database file, creating..");
            fileOut(file, inputStream);
        }
    }

    public static void fileOut(File file, InputStream inputStream) throws IOException {
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
