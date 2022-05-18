package indi.eiriksgata.rulateday.config;

import indi.eiriksgata.rulateday.pojo.QueryDataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomDocumentHandler {

    public static void save(String name, String key, String value) {
        if (GlobalData.documentContext == null) {
            GlobalData.documentContext = new HashMap<>();
        }
        if (GlobalData.documentContext.get(name) == null) {
            Map<String, String> map = new HashMap<>();
            map.put(key, value);
            GlobalData.documentContext.put(name, map);
        } else {
            GlobalData.documentContext.get(name).put(key, value);
        }
    }


    public static List<QueryDataBase> find(String key) {
        if (GlobalData.documentContext == null) {
            return null;
        }
        AtomicInteger index = new AtomicInteger();
        List<QueryDataBase> result = new ArrayList<>();
        GlobalData.documentContext.forEach((docName, dataMap) -> {
            dataMap.forEach((name, describe) -> {
                index.getAndIncrement();
                if (name.contains(key)) {
                    QueryDataBase temp = new QueryDataBase();
                    temp.setId(index.longValue());
                    temp.setName(name);
                    temp.setDescribe(describe);
                    result.add(temp);
                }
            });
        });
        return result;
    }


}
