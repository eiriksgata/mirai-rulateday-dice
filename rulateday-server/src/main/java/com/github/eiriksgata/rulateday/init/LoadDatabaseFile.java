package com.github.eiriksgata.rulateday.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.eiriksgata.rulateday.config.GlobalData;
import com.github.eiriksgata.trpg.dice.utlis.VersionUtils;
import com.github.eiriksgata.rulateday.RulatedayCore;
import com.github.eiriksgata.rulateday.config.CustomDocumentHandler;
import com.github.eiriksgata.rulateday.utlis.JSONFileUtils;
import com.github.eiriksgata.rulateday.websocket.client.WebSocketClientInit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import static com.github.eiriksgata.trpg.dice.reply.CustomText.*;
import static org.apache.ibatis.io.Resources.getResourceAsStream;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.github.eiriksgata.rulateday.utlis
 * date: 2020/11/24
 **/
public class LoadDatabaseFile {

    private static String path;

    public static void init() {
        try {
            //检查是否在mirai-console执行。如果是则采用控制台路径。
            path = RulatedayCore.INSTANCE.getDataFolderPath().toString();
        } catch (ExceptionInInitializerError ignored) {
            //如果不是则采用当前项目路径。
            path = "";
        }
        try {
            //初始化载入数据顺序，考虑到一些方法或者数据需要重复构建，因此应当优化一下结构。

            //检查创建项目文件夹
            createProjectFile();

            //检查并创建自定义文本文件
            createCustomTextConfigFile();

            //创建图片文件夹
            createImageFile();

            //创建自定义文档文件夹
            createCustomDoc();

            //创建数据库文件
            createDatabaseFile();

            //创建并载入配置文件
            createAndLoadConfigFile();

            //载入自定义文档数据
            LoadDatabaseFile.loadCustomDocument();

            //建立ws通讯
            new Thread(WebSocketClientInit::run).start();

            //通用缓存数据
            CacheReuseData.init();

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
        File file = new File("config/com.github.eiriksgata.rulateday-dice/" + customTextConfigFileName);
        if (!file.exists()) {
            System.out.println("create custom text config file...");
            InputStream inputStream = getResourceAsStream(customTextConfigFileName);
            fileOut(file, inputStream);
        }
    }

    public static void createAndLoadConfigFile() throws IOException {
        String configFileName = "config.json";
        String path = "config/com.github.eiriksgata.rulateday-dice/" + configFileName;
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
        if (number.equals("")) {
            RulatedayCore.INSTANCE.getLogger().warning("-----------Rulateday-dice Waring--------------");
            RulatedayCore.INSTANCE.getLogger().warning("检测到当前还没有配置主人QQ，请在当前程序运行目录下的: /config/com.github.eiriksgata.rulateday-dice/config.json 文件中的 'master.QQ.number' 进行设置");
            RulatedayCore.INSTANCE.getLogger().warning("设置完毕后，请重新启动该程序");
            RulatedayCore.INSTANCE.getLogger().warning("----------------------------------------------");
        }
    }

    public static void loadCustomDocument() throws IOException {
        createCustomDoc();
        String path = "data/com.github.eiriksgata.rulateday-dice/custom-doc";
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
