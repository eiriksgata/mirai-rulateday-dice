package indi.eiriksgata.rulateday.init;

import com.alibaba.fastjson2.TypeReference;
import indi.eiriksgata.rulateday.config.GlobalData;

import java.util.List;

/**
 * 优化策列 虽然会提高耦合度，但是考虑到这个数据复用率极高，因此需要做静态处理
 */
public class CacheReuseData {

    public static List<String> prefixMatchList;

    public static String prefixMatchListRegex;

    public static void init() {
        prefixMatchListHandler();

        prefixMatchListRegex();
    }


    private static void prefixMatchListHandler() {
        prefixMatchList = GlobalData.configData.getObject("instructions.prefix.list", new TypeReference<List<String>>() {
        }.getType());
    }


    private static void prefixMatchListRegex() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (String prefix : prefixMatchList) {
            stringBuilder.append(prefix);
        }
        stringBuilder.append("]{1}");
        prefixMatchListRegex = stringBuilder.toString();
    }
}
