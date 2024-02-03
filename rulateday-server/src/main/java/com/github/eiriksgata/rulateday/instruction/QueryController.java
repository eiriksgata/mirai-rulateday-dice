package com.github.eiriksgata.rulateday.instruction;

import com.github.eiriksgata.rulateday.config.GlobalData;
import com.github.eiriksgata.rulateday.dto.DiceMessageDTO;
import com.github.eiriksgata.rulateday.init.LoadDatabaseFile;
import com.github.eiriksgata.rulateday.pojo.QueryDataBase;
import com.github.eiriksgata.rulateday.service.*;
import com.github.eiriksgata.rulateday.service.impl.*;
import com.github.eiriksgata.trpg.dice.injection.InstructReflex;
import com.github.eiriksgata.trpg.dice.injection.InstructService;
import com.github.eiriksgata.trpg.dice.reply.CustomText;

import com.github.eiriksgata.rulateday.config.CustomDocumentHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@InstructService
public class QueryController {

    public static ExecutorService cachedThread = Executors.newCachedThreadPool();

    @Resource
    private final CrazyLibraryService crazyLibraryService = new CrazyLibraryImpl();

    @Resource
    private final Dnd5eLibService dnd5eLibService = new Dnd5eLibServiceImpl();

    @Resource
    private final RuleService ruleService = new RuleServiceImpl();

    @Resource
    private final UserConversationService conversationService = new UserConversationImpl();

    @Resource
    public static final RandomPictureApiService randomPictureApiService = new RandomPictureApiImpl();


    //发疯状态确认
    @InstructReflex(value = {"ti"})
    public String getCrazyState(DiceMessageDTO data) {
        return crazyLibraryService.getRandomCrazyDescribe();
    }

    //发疯结束总结
    @InstructReflex(value = {"li"})
    public String getCrazyOverEvent(DiceMessageDTO data) {
        return crazyLibraryService.getCrazyOverDescribe();
    }

    @InstructReflex(value = {"cr", "cr7"})
    public String queryCoc7Rule(DiceMessageDTO data) {
        data.setBody(data.getBody().replaceAll(" ", ""));
        return ruleService.selectRule(data.getBody());
    }

    @InstructReflex(value = {"dr", "d5er"})
    public String queryDnd5eRule(DiceMessageDTO data) {
        //如果输入的数据是无关键字的
        if (data.getBody().equals("")) {
            return CustomText.getText("dr5e.rule.not.parameter");
        }
        if (data.getBody().equals(" ")) {
            return CustomText.getText("dr5e.rule.not.parameter");
        }

        //先进行模糊查询
        List<QueryDataBase> result = dnd5eLibService.findName("%" + data.getBody() + "%");
        List<QueryDataBase> saveData = new ArrayList<>();
        if (result.size() > 1) {
            StringBuilder text = new StringBuilder(CustomText.getText("dr5e.rule.lib.result.list.title"));
            int count = 0;
            for (QueryDataBase temp : result) {
                if (count >= 20) {
                    text.append(CustomText.getText("dr5e.rule.lib.result.list.tail"));
                    break;
                } else {
                    text.append("\n").append(count).append(". ").append(temp.getName());
                    saveData.add(temp);
                }
                count++;
            }
            //将记录暂时存入数据库
            conversationService.saveConversation(data.getId(), saveData);
            return text.toString();

        } else {
            if (result.size() == 0) {
                return CustomText.getText("dr5e.rule.lib.result.list.not.found");
            }
            if (result.get(0).getName().length() > 5) {
                if (result.get(0).getName().startsWith("怪物图鉴:")) {
                    cachedThread.execute(() -> dnd5eLibService.sendMMImage(data.getEvent(), result.get(0)));
                }
            }
            return result.get(0).getName() + "\n" + result.get(0).getDescribe().replaceAll("\n\n", "\n");
        }
    }

    @InstructReflex(value = {"help"})
    public String help(DiceMessageDTO data) {
        return CustomText.getText("instructions.help.result1");
    }

    @InstructReflex(value = {"help指令"}, priority = 3)
    public String helpInstruct(DiceMessageDTO data) {
        return CustomText.getText("instructions.all.result2");
    }

    @InstructReflex(value = {"rmm"})
    public String rollMM(DiceMessageDTO data) {
        QueryDataBase result = dnd5eLibService.getRandomMMData();
        cachedThread.execute(() -> dnd5eLibService.sendMMImage(data.getEvent(), result));
        return result.getName() + "\n" + result.getDescribe().replaceAll("\n\n", "\n");
    }

    @InstructReflex(value = {"kkp"})
    public String randomPicture(DiceMessageDTO data) {
        switch (GlobalData.randomPictureApiType) {
            case 1:
                return randomPictureApiService.urlEncodeAPI(data, "https://api.ixiaowai.cn/api/api.php");
            case 2:
                return randomPictureApiService.yinhuaAPI(data);
            case 3:
                return randomPictureApiService.urlEncodeAPI(data, "https://api.ixiaowai.cn/mcapi/mcapi.php");
            case 4:
                return randomPictureApiService.urlEncodeAPI(data, "https://api.paugram.com/wallpaper");
            case 5:
                return randomPictureApiService.urlEncodeAPI(data, "https://api.yimian.xyz/img");
            case 6:
                return randomPictureApiService.urlEncodeAPI(data, "https://img.paulzzh.tech/touhou/random");
        }
        return null;
    }

    @InstructReflex(value = {"rmi"}, priority = 3)
    public String rollMagicItem(DiceMessageDTO data) {
        return "null";
    }

    @InstructReflex(value = {"rt"}, priority = 3)
    public String rollTool(DiceMessageDTO data) {
        return "null";
    }

    @InstructReflex(value = {"drw"}, priority = 4)
    public String rollWeapon(DiceMessageDTO data) {
        return "null";
    }

    @InstructReflex(value = {"modlist"})
    public String queryModList(DiceMessageDTO data) {
        return "null";
    }

    @InstructReflex(value = {"modon"})
    public String modOpen(DiceMessageDTO data) {

        return "null";
    }

    @InstructReflex(value = {"modoff"})
    public String modClose(DiceMessageDTO data) {
        return "null";
    }

    @InstructReflex(value = {"reload"}, priority = 3)
    public String fileReload(DiceMessageDTO data) {
        try {
            LoadDatabaseFile.loadCustomDocument();
        } catch (IOException e) {
            e.printStackTrace();
            return CustomText.getText("system.config.reload.fail");
        }
        return CustomText.getText("system.config.reload.success");
    }

    @InstructReflex(value = {"q", "Q"}, priority = 3)
    public String queryModelCustom(DiceMessageDTO data) {
        List<QueryDataBase> result = CustomDocumentHandler.find(data.getBody());
        if (result == null) {
            return CustomText.getText("query.doc.lib.result.list.not.found");
        }
        List<QueryDataBase> saveData = new ArrayList<>();
        if (result.size() > 1) {
            StringBuilder text = new StringBuilder(CustomText.getText("query.doc.lib.result.list.title"));
            int count = 0;
            for (QueryDataBase temp : result) {
                if (count >= 20) {
                    text.append(CustomText.getText("query.doc.lib.result.list.tail"));
                    break;
                } else {
                    text.append("\n").append(count).append(". ").append(temp.getName());
                    saveData.add(temp);
                }
                count++;
            }
            //将记录暂时存入数据库
            conversationService.saveConversation(data.getId(), saveData);
            return text.toString();
        } else {
            if (result.size() == 0) {
                return CustomText.getText("query.doc.lib.result.list.not.found");
            }
            return result.get(0).getName() + "\n" + result.get(0).getDescribe();
        }
    }

    @InstructReflex(value = {"tr-en"}, priority = 3)
    public String translateToEnglish(DiceMessageDTO data) {
        if (data.getBody() == null || data.getBody().equals("")) {
            return CustomText.getText("translate.cn-to-en.not.found.content");
        }
        if (data.getBody().length() > 200) {
            return CustomText.getText("translate.cn-to-en.text.length.error");
        }
        return OtherApiService.translateToEnglishByYouDu(data.getBody());
    }
}
