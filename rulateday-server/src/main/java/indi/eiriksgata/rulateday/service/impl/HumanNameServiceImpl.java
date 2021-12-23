package indi.eiriksgata.rulateday.service.impl;

import indi.eiriksgata.rulateday.mapper.NamesCorpusMapper;
import indi.eiriksgata.rulateday.service.HumanNameService;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;

import java.security.SecureRandom;


/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.service.impl
 * date: 2021/4/7
 **/
public class HumanNameServiceImpl implements HumanNameService {


    @Override
    public String randomName(int number) {
        NamesCorpusMapper mapper = MyBatisUtil.getSqlSession().getMapper(NamesCorpusMapper.class);
        StringBuilder resultNames = new StringBuilder();
        resultNames.append("随机名字:");
        SecureRandom random = new SecureRandom();
        for (int count = 0; count < number; count++) {
            int randomValue = random.nextInt(3);
            switch (randomValue) {
                case 0:
                    resultNames.append(mapper.getCNAncientRandomName());
                    break;
                case 1:
                    resultNames.append(mapper.getEnglishRandomName());
                    break;
                case 2:
                    resultNames.append(mapper.getJapaneseRandomName());
                    break;
            }
            MyBatisUtil.getSqlSession().commit();
            if (count != number - 1) {
                resultNames.append("、");
            }
        }
        return resultNames.toString();
    }


}
