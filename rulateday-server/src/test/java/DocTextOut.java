import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.eiriksgata.trpg.dice.utlis.RegularExpressionUtils;
import com.github.eiriksgata.rulateday.config.CustomDocumentHandler;
import com.github.eiriksgata.rulateday.mapper.Dnd5ePhbDataMapper;
import com.github.eiriksgata.rulateday.pojo.QueryDataBase;
import com.github.eiriksgata.rulateday.utlis.MyBatisUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.eiriksgata.trpg.dice.reply.CustomText.fileRead;

/**
 * author: create by Keith
 * version: v1.0
 * description: PACKAGE_NAME
 * date: 2020/12/18
 **/
public class DocTextOut {

    @Test
    void ArmorWeapon() {
        Dnd5ePhbDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbDataMapper.class);
        List<QueryDataBase> result = mapper.selectAllArmorWeapon();
        for (QueryDataBase temp : result) {
            System.out.println("## " + temp.getName());
            System.out.println(temp.getDescribe().replaceAll("\n", "\n\n") + "\n\n");
        }
    }

    @Test
    void MagicItem() {
        Dnd5ePhbDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbDataMapper.class);
        List<QueryDataBase> result = mapper.selectAllMagicItemsDmg();
        for (QueryDataBase temp : result) {
            System.out.println("## " + temp.getName());
            System.out.println(temp.getDescribe().replaceAll("\n", "\n\n") + "\n\n");

        }
    }

    @Test
    void mm() {
        Dnd5ePhbDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbDataMapper.class);
        List<QueryDataBase> result = mapper.selectAllMM();
        for (QueryDataBase temp : result) {
            System.out.println("## " + temp.getName());

            if (temp.getName().length() > 5) {
                if (temp.getName().substring(0, 5).equals("怪物图鉴:")) {
                    String mmName = temp.getName().substring(5);
                    System.out.println("![" + mmName + "]" + "(../mm-image/" + mmName + ".png ':size=30%')\n\n");
                }
            }


            System.out.println(temp.getDescribe().replaceAll("\n", "\n\n") + "\n\n");

        }
    }

    @Test
    void selectAllToolsPhb() {
        Dnd5ePhbDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbDataMapper.class);
        List<QueryDataBase> result = mapper.selectAllToolsPhb();
        for (QueryDataBase temp : result) {
            System.out.println("## " + temp.getName());
            System.out.println(temp.getDescribe().replaceAll("\n", "\n\n") + "\n\n");
        }
    }

    @Test
    void SpellListPhb() {
        Dnd5ePhbDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbDataMapper.class);
        String nameList[] = {"吟游诗人", "牧师", "德鲁伊", "圣武士", "游侠", "术士", "邪术师", "法师"};
        List<QueryDataBase> result = new ArrayList<>();
        for (String occ : nameList) {
            for (int i = 0; i < 10; i++) {
                String findKey = "%" + occ + i + "%";
                //System.out.println(findKey);
                List<QueryDataBase> tempList = mapper.selectSpellList(findKey);
                if (tempList.size() != 0) {
                    result.add(tempList.get(0));
                }
            }
        }

        for (QueryDataBase temp : result) {
            System.out.println("## " + temp.getName());

            String[] skillNameList = temp.getDescribe().split("\n");
            for (String name : skillNameList) {
                String tempString = RegularExpressionUtils.getMatcher("\\（.+\\）", name);
                String stringBuilder;
                if (tempString != null) {
                    List<QueryDataBase> skillDesStr = mapper.selectSkillPhb("%" + name.substring(0, name.length() - tempString.length()) + "%");
                    if (skillDesStr.size() == 0) {
                        stringBuilder = "<details><summary>" + name.substring(0, name.length() - tempString.length()) +
                                "</summary>" + "NULL" + "</details>\n";
                    } else {
                        stringBuilder = "<details><summary>" + name.substring(0, name.length() - tempString.length()) +
                                "</summary>\n" + skillDesStr.get(0).getDescribe() + "\n</details>\n";
                    }
                } else {
                    List<QueryDataBase> skillDesStr = mapper.selectSkillPhb("%" + name + "%");
                    if (skillDesStr.size() == 0) {
                        stringBuilder = "<details><summary>" + name +
                                "</summary>" + "NULL" + "</details>\n";
                    } else {
                        stringBuilder = "<details><summary>" + name +
                                "</summary>\n" + skillDesStr.get(0).getDescribe() + "\n</details>\n";
                    }

                }
                System.out.println(stringBuilder);
            }


            //System.out.println(temp.getDescribe());
        }
    }

    @Test
    public void fileListShow() {
        String path = "data/com.github.eiriksgata.rulateday-dice/custom-doc";
        File directory = new File(path);
        File[] importFiles = directory.listFiles();
        assert importFiles != null;
        for (File file : importFiles) {
            System.out.println(file.getName());
        }
    }

    @Test
    public void mapFind() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");

        map.forEach((name, value) -> {
            System.out.println(name + " " + value);
        });
    }

    @Test
    public void loadAndFindCustomDocument() throws Exception {
        String path = "data/com.github.eiriksgata.rulateday-dice/custom-doc";
        System.out.println("-----------Rulateday-dice doc mod loading-----------");
        //TODO: 扫描目录的json文档文件
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files != null) {
            int i = 0;
            for (File file : files) {
                i++;
                System.out.println(i + "." + file.getName());
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
        System.out.println("-----------Rulateday-dice load end-----------");

        String searchName = "黄衣";

        CustomDocumentHandler.find(searchName);

    }

}
