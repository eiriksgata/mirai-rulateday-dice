package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.pojo.RuleBook;
import indi.eiriksgata.rulateday.service.CrazyLibraryService;
import indi.eiriksgata.rulateday.service.RuleService;
import indi.eiriksgata.rulateday.service.impl.CrazyLibraryImpl;
import indi.eiriksgata.rulateday.service.impl.RuleServiceImpl;

import javax.annotation.Resource;

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

    @InstructReflex(value = {".rule", "。rule"})
    public String queryRule(MessageData data) {
        data.setMessage(data.getMessage().replaceAll(" ", ""));
        RuleBook result = ruleService.selectRule(data.getMessage());
        if (result == null) {
            return "找不到规则书内容";
        }
        return result.getTitle() + "\n" + result.getContent();
    }



}
