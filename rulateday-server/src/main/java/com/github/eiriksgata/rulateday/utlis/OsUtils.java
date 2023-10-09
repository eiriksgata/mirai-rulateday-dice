package com.github.eiriksgata.rulateday.utlis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: com.ajb.com.ajb.sdk.utils
 * @date:2020/7/29
 **/
public class OsUtils {

    public static Boolean isLinux() {
        String os = System.getProperty("os.name");
        System.out.println("os.name: " + os);
        return !os.toLowerCase().startsWith("win");
    }


    public static String getMACAddressByLinux() throws Exception {
        String[] cmd = {"ifconfig"};

        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        String str1 = sb.toString();
        String str2 = str1.split("ether")[1].trim();
        String result = str2.split("txqueuelen")[0].trim();
        br.close();
        return result;
    }

    public static String getIdentifierByLinux() throws Exception {
        String[] cmd = {"fdisk", "-l"};

        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        String str1 = sb.toString();
        String str2 = str1.split("identifier:")[1].trim();
        String result = str2.split("Device Boot")[0].trim();
        br.close();
        return result.replaceAll("-", "");
    }

    public static String getMACAddressByWindows() throws Exception {
        String result = "";
        Process process = Runtime.getRuntime().exec("ipconfig /all");
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));

        String line;
        int index = -1;
        while ((line = br.readLine()) != null) {
            index = line.toLowerCase().indexOf("物理地址");
            if (index >= 0) {// 找到了
                index = line.indexOf(":");
                if (index >= 0) {
                    result = line.substring(index + 1).trim();
                }
                break;
            }
        }
        br.close();
        return result;
    }

    public static String getIdentifierByWindows() throws Exception {
        String result = "";
        Process process = Runtime.getRuntime().exec("cmd /c dir C:");
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains("卷的序列号是 ")) {
                result = line.substring(line.indexOf("卷的序列号是 ") + "卷的序列号是 ".length());
                break;
            }
        }
        br.close();
        result = result.replaceAll("-", "");
        if (result.equals("")) {
            result = Integer.toHexString(Integer.parseInt(getHardDiskSN("C"))).toUpperCase();
        }
        return result.replaceAll("-", "");
    }

    public static String getHardDiskSN(String drive) {
        String result = "";
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);

            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    + "Set colDrives = objFSO.Drives\n"
                    + "Set objDrive = colDrives.item(\""
                    + drive
                    + "\")\n"
                    + "Wscript.Echo objDrive.SerialNumber"; // see note
            fw.write(vbs);
            fw.close();
            String path = file.getPath().replace("%20", " ");
            String[] cmd = {"cscript", "/nologo", path};
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

    public static void main(String[] a) throws Exception {
        //String result = getIdentifierByWindows();
        System.out.println(getHardDiskSN("C"));
        //System.out.println(result);
    }

}
