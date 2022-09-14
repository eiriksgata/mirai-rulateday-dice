package indi.eiriksgata.rulateday.utlis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import indi.eiriksgata.dice.utlis.VersionUtils;
import indi.eiriksgata.rulateday.RulatedayCore;
import indi.eiriksgata.rulateday.config.CustomDocumentHandler;
import indi.eiriksgata.rulateday.config.GlobalData;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import static indi.eiriksgata.dice.reply.CustomText.*;
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
            createCustomDoc();
            createDatabaseFile();
            createConfigFile();

            LoadDatabaseFile.loadCustomDocument();
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
        File file = new File("config/indi.eiriksgata.rulateday-dice/" + customTextConfigFileName);
        if (!file.exists()) {
            System.out.println("create custom text config file...");
            InputStream inputStream = getResourceAsStream(customTextConfigFileName);
            fileOut(file, inputStream);
        }
    }

    public static void createConfigFile() throws IOException {
        String configFileName = "config.json";
        String path = "config/indi.eiriksgata.rulateday-dice/" + configFileName;
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("create custom text config file...");
            InputStream inputStream = getResourceAsStream(configFileName);
            fileOut(file, inputStream);
        }
        loadConfigFile(path);
        checkMasterNumber();
    }

    public static void checkMasterNumber() {
        String number = GlobalData.configData.getString("master.QQ.number");
        RulatedayCore.INSTANCE.getLogger().info(GlobalData.configData.toJSONString());
        if (number.equals("")) {
            RulatedayCore.INSTANCE.getLogger().warning("-----------Rulateday-dice Waring--------------");
            RulatedayCore.INSTANCE.getLogger().warning("检测到当前还没有配置主人QQ，请在当前程序运行目录下的: /config/indi.eiriksgata.rulateday-dice/config.json 文件中的 'master.QQ.number' 进行设置");
            RulatedayCore.INSTANCE.getLogger().warning("设置完毕后，请重新启动该程序");
            RulatedayCore.INSTANCE.getLogger().warning("----------------------------------------------");
        }
    }

    public static void loadCustomDocument() throws IOException {
        createCustomDoc();
        String path = "data/indi.eiriksgata.rulateday-dice/custom-doc";
        RulatedayCore.INSTANCE.getLogger().info("-----------Rulateday-dice doc files loading-----------");
        //TODO: 扫描目录的json文档文件
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files != null) {
            int i = 0;
            for (File file : files) {
                i++;
                RulatedayCore.INSTANCE.getLogger().info(i + "." + file.getName());
                JSONObject document = JSON.parseObject(new String(
                        fileRead(new File(path + "/" + file.getName())), StandardCharsets.UTF_8
                ));
                String documentName = document.getString("mod");
                JSONObject helpDoc = document.getJSONObject("helpdoc");
                helpDoc.forEach((name, describe) -> {
                    CustomDocumentHandler.save(documentName, name, (String) describe);
                });
            }
        }
        RulatedayCore.INSTANCE.getLogger().info("-----------Rulateday-dice load end-----------");
    }


    public static void loadConfigFile(String filePath) {
        try {
            GlobalData.configData = JSON.parseObject(new String(
                    fileRead(new File(filePath)), StandardCharsets.UTF_8
            ));
            String loadFileVersion = GlobalData.configData.getString("file.version");
            InputStream inputStream = getResourceAsStream("config.json");
            JSONObject defaultJSONObject = JSON.parseObject(new String(
                    inputStreamRead(inputStream), StandardCharsets.UTF_8
            ));
            String defaultFileVersion = defaultJSONObject.getString("file.version");
            if (loadFileVersion == null) {
                JSONFileUtils.configFileMerge(filePath, defaultJSONObject);
            } else {
                int result = new VersionUtils().compareVersion(loadFileVersion, defaultFileVersion);
                if (result == -1) {
                    JSONFileUtils.configFileMerge(filePath, defaultJSONObject);
                    RulatedayCore.INSTANCE.getLogger().info("检测到新的配置文件项，正在合并！");
                }
            }
        } catch (IOException e) {
            try {
                InputStream inputStream = getResourceAsStream("config.json");
                GlobalData.configData = JSON.parseObject(new String(
                        inputStreamRead(inputStream), StandardCharsets.UTF_8
                ));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static void createImageFile() throws IOException {
        File mmImages = new File(path + "/mm-images");
        if (!mmImages.exists()) {
            mmImages.mkdirs();
        }
    }

    public static void createCustomDoc() throws IOException {
        File document = new File(path + "/custom-doc");
        if (!document.exists()) {
            document.mkdir();
        }
    }

    public static void createGalGame() throws IOException {
        File file = new File(path + "/galgame");
        if (!file.exists()) {
            file.mkdir();
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
