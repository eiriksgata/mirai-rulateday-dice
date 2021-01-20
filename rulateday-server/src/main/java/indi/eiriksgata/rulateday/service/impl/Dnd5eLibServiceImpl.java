package indi.eiriksgata.rulateday.service.impl;

import indi.eiriksgata.rulateday.RulatedayCore;
import indi.eiriksgata.rulateday.exception.ExceptionEnum;
import indi.eiriksgata.rulateday.exception.RulatedayException;
import indi.eiriksgata.rulateday.mapper.Dnd5ePhbDataMapper;
import indi.eiriksgata.rulateday.pojo.QueryDataBase;
import indi.eiriksgata.rulateday.service.Dnd5eLibService;
import indi.eiriksgata.rulateday.utlis.FileUtil;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.service.impl
 * date: 2020/11/13
 **/
public class Dnd5eLibServiceImpl implements Dnd5eLibService {

    private String imagesUrl = ResourceBundle.getBundle("resources").getString("resources.mm.images.url");
    private String localPath = ResourceBundle.getBundle("resources").getString("resources.mm.images.path");


    @Override
    public List<QueryDataBase> findName(String name) {
        List<QueryDataBase> result = new ArrayList<>();
        Dnd5ePhbDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbDataMapper.class);
        result.addAll(mapper.selectSkillPhb("%" + name + "%"));
        result.addAll(mapper.selectArmorWeapon("%" + name + "%"));
        result.addAll(mapper.selectClasses("%" + name + "%"));
        result.addAll(mapper.selectFeat("%" + name + "%"));
        result.addAll(mapper.selectRaces("%" + name + "%"));
        result.addAll(mapper.selectRule("%" + name + "%"));
        result.addAll(mapper.selectTools("%" + name + "%"));
        result.addAll(mapper.selectSpellList("%" + name + "%"));
        result.addAll(mapper.selectMagicItemsDmg("%" + name + "%"));
        result.addAll(mapper.selectRuleDmg("%" + name + "%"));
        result.addAll(mapper.selectMM("%" + name + "%"));
        result.addAll(mapper.selectBackgroundPhb("%" + name + "%"));
        return result;
    }

    @Override
    public QueryDataBase findById(long id) {
        Dnd5ePhbDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbDataMapper.class);
        return mapper.selectSkillPhbById(id);
    }


    @Override
    public QueryDataBase getRandomMMData() {
        Dnd5ePhbDataMapper mapper = MyBatisUtil.getSqlSession().getMapper(Dnd5ePhbDataMapper.class);
        MyBatisUtil.getSqlSession().clearCache();
        return mapper.selectRandomMM();
    }


    @Override
    public void sendMMImage(Object event, QueryDataBase result) throws RulatedayException {
        String mmNameFileName = result.getName().substring(5) + ".png";
        String mmName = result.getName().substring(5) + ".png";
        try {
            mmNameFileName = URLEncoder.encode(mmNameFileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RulatedayException(ExceptionEnum.DND5E_MM_NAME_DECODE_ERR);
        }
        String url = imagesUrl + mmNameFileName;
        File imageFile = new File(localPath + mmName);

        if (!imageFile.exists()) {
            try {
                FileUtil.downLoadFromUrl(url, localPath + mmName);
            } catch (Exception e) {
                RulatedayCore.INSTANCE.getLogger().info("下载" + result.getName().substring(5) + "图片失败，服务器可能没有该资源");
            }
        }
        if (event.getClass() == GroupMessageEvent.class) {
            if (imageFile.exists()) {
                ((GroupMessageEvent) event).getGroup()
                        .sendMessage(((GroupMessageEvent) event)
                                .getGroup().uploadImage(ExternalResource.create(imageFile)));

            }
        }
        if (event.getClass() == FriendMessageEvent.class) {
            if (imageFile.exists()) {
                ((FriendMessageEvent) event).getFriend()
                        .sendMessage(
                                ((FriendMessageEvent) event)
                                        .getFriend().uploadImage(ExternalResource.create(imageFile)));
            }
        }
    }


}
