package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.pojo.Dnd5ESkillLib;
import indi.eiriksgata.rulateday.pojo.QueryDataBase;
import indi.eiriksgata.rulateday.pojo.RuleBook;
import indi.eiriksgata.rulateday.service.CrazyLibraryService;
import indi.eiriksgata.rulateday.service.Dnd5eLibService;
import indi.eiriksgata.rulateday.service.RuleService;
import indi.eiriksgata.rulateday.service.impl.CrazyLibraryImpl;
import indi.eiriksgata.rulateday.service.impl.Dnd5eLibServiceImpl;
import indi.eiriksgata.rulateday.service.impl.RuleServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.instruction
 * date: 2020/11/4
 **/

@InstructService
public class QueryController {

    @Resource
    private CrazyLibraryService crazyLibraryService = new CrazyLibraryImpl();

    @Resource
    private Dnd5eLibService dnd5eLibService = new Dnd5eLibServiceImpl();

    @Resource
    private RuleService ruleService = new RuleServiceImpl();

    //发疯状态确认
    @InstructReflex(value = {".ti", "。ti"})
    public String getCrazyState(MessageData data) {
        return crazyLibraryService.getRandomCrazyDescribe();
    }

    //发疯结束总结
    @InstructReflex(value = {".li", "。li"})
    public String getCrazyOverEvent(MessageData data) {
        return crazyLibraryService.getCrazyOverDescribe();
    }

    @InstructReflex(value = {".cr", "。cr", ".cr7"})
    public String queryCoc7Rule(MessageData data) {
        data.setMessage(data.getMessage().replaceAll(" ", ""));
        RuleBook result = ruleService.selectRule(data.getMessage());
        if (result == null) {
            return "找不到COC7规则书内容";
        }
        return result.getTitle() + "\n" + result.getContent();
    }

    @InstructReflex(value = {".dr", "。dr", ".d5er"})
    public String queryDnd5eRule(MessageData data) {
        //如果输入的数据是无关键字的
        if (data.getMessage().equals("")) {
            return "请输入关键字参数";
        }
        if (data.getMessage().equals(" ")) {
            return "请输入关键字参数";
        }

        List<QueryDataBase> result = dnd5eLibService.findName(data.getMessage());
        if (result.size() > 1) {
            StringBuilder text = new StringBuilder("查询结果存在多个，请完善查询关键字直到存在1个才回显示详细信息\n查询吻合关键字:");
            int count = 0;
            for (QueryDataBase temp : result) {
                if (count >= 20) {
                    text.append("最多显示20条数据。如果需要查询更多信息，请前往网站查询：https://eiriksgata.github.io/rulateday-dnd5e-wiki/#/");
                } else {
                    text.append("\n").append(temp.getName());
                }
                count++;
            }
            return text.toString();
        } else {
            if (result.size() == 0) {
                return "查询不到结果";
            }
            return result.get(0).getName() + "\n" + result.get(0).getDescribe().replaceAll("\n\n", "\n");
        }
    }


    @InstructReflex(value = {".help", "。help"})
    public String help(MessageData data) {
        return "插件名称:Rulateda v0.1.0 by Eiriksgata\n" +
                "反馈联系Github：https://github.com/Eiriksgata/mirai-rulateday-dice\n" +
                "所有指令：.help指令\n" +
                "DND5eWiki:https://eiriksgata.github.io/rulateday-dnd5e-wiki/#/";
    }

    @InstructReflex(value = {".help指令", "。help指令"}, priority = 3)
    public String helpInstruct(MessageData data) {
        return "骰子常用指令列表:\n" +
                ".st 属性设置\n" +
                ".ra|.rc 属性检测\n" +
                ".rb 奖励骰|.rp惩罚骰\n" +
                ".cr coc7规则书查询\n" +
                ".dr dnd5e信息查询\n" +
                ".ti 随机获取发疯情况\n" +
                ".li 发疯后总结\n" +
                ".sc 理智检测\n" +
                ".rh 暗骰\n" +
                ".set 设置默认骰\n" +
                ".coc 随机coc7角色属性\n" +
                ".dnd 随机dnd5e角色属性\n" +
                ".r 随机数生成\n" +
                ".rd 默认骰数值生成\n" +
                ".botoff | .boton 启用骰子开关\n";
    }


}
