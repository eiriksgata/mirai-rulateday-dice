package indi.eiriksgata.rulateday.utlis;

import indi.eiriksgata.rulateday.RulatedayCore;

import java.io.*;

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
        InputStream inputStream = getResourceAsStream("rulateday.db");
        String path = "data//rulateday";
        File file = new File(path + "//rulateday.db");
        File mkdir = new File(path);
        File mmImages = new File(path + "//mm-images");
        if (!mkdir.exists()) {
            mkdir.mkdirs();
            //RulatedayCore.INSTANCE.getLogger().info("Detecting no database file, creating..");
        }
        if (!mmImages.exists()){
            mmImages.mkdirs();
        }
        if (!file.exists()) {


            OutputStream output = new FileOutputStream(file);
            byte[] bytes = new byte[1];
            while (inputStream.read(bytes) != -1) {
                output.write(bytes);
            }
            output.flush();
            inputStream.close();
            output.close();
        }
    }





}
