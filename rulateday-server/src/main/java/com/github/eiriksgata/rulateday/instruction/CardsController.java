package com.github.eiriksgata.rulateday.instruction;

import com.github.eiriksgata.rulateday.event.EventUtils;
import com.github.eiriksgata.rulateday.mapper.CardsGroupDataMapper;
import com.github.eiriksgata.rulateday.pojo.CardsGroupData;
import com.github.eiriksgata.rulateday.pojo.CardsTypeList;
import com.github.eiriksgata.trpg.dice.injection.InstructReflex;
import com.github.eiriksgata.trpg.dice.injection.InstructService;
import com.github.eiriksgata.trpg.dice.reply.CustomText;
import com.github.eiriksgata.trpg.dice.vo.MessageData;
import com.github.eiriksgata.rulateday.event.EventAdapter;
import com.github.eiriksgata.rulateday.mapper.CardsTypeListMapper;
import com.github.eiriksgata.rulateday.utlis.MyBatisUtil;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.github.eiriksgata.rulateday.instruction
 * date: 2021/4/19
 **/
@InstructService
public class CardsController {

    public static CardsGroupDataMapper cardsGroupDataMapper = MyBatisUtil.getSqlSession().getMapper(CardsGroupDataMapper.class);
    public static CardsTypeListMapper cardsTypeListMapper = MyBatisUtil.getSqlSession().getMapper(CardsTypeListMapper.class);

    @InstructReflex(value = {"cards"})
    public String cardsList(MessageData<?> data) {
        List<CardsTypeList> lists = cardsTypeListMapper.selectAll();
        if (lists.size() == 0) {
            return CustomText.getText("cards.type.not.found");
        }
        StringBuilder result = new StringBuilder();
        result.append(CustomText.getText("cards.type.list.title"));
        AtomicInteger count = new AtomicInteger();
        lists.forEach((cardsTypeList) -> {
            count.getAndIncrement();
            result.append("\n").append(count.get()).append(". ").append(cardsTypeList.getName());
        });
        return result.toString();
    }

    @InstructReflex(value = {"cardsAdd", "cardsadd"}, priority = 3)
    public String cardsAdd(MessageData<?> data) {
        String[] parsingData = data.getMessage().split(" ");
        if (parsingData.length < 2) {
            return CustomText.getText("cards.add.parameter.format.error");
        }
        CardsTypeList cardsTypeList = new CardsTypeList();
        cardsTypeList.setName(parsingData[0]);
        cardsTypeList.setContent(data.getMessage().substring(parsingData[0].length() + 1));
        try {
            cardsTypeListMapper.insert(cardsTypeList);
            MyBatisUtil.getSqlSession().commit();
        } catch (Exception e) {
            return CustomText.getText("cards.add.error");
        }
        return CustomText.getText("cards.add.success");
    }

    @InstructReflex(value = {"cardsDel", "cardsdel"}, priority = 3)
    public String cardsDel(MessageData<?> data) {
        cardsTypeListMapper.deleteByName(data.getMessage());
        MyBatisUtil.getSqlSession().commit();
        return CustomText.getText("cards.delete.success");
    }


    @InstructReflex(value = {"drawAdd", "drawadd"}, priority = 3)
    public String drawAdd(MessageData<?> data) {
        if (data.getMessage().equals("") || data.getMessage() == null) {
            return CustomText.getText("cards.draw.not.parameter");
        }
        CardsTypeList cardsTypeList = cardsTypeListMapper.selectByName(data.getMessage());
        if (cardsTypeList == null) {
            return CustomText.getText("cards.draw.not.found");
        }
        String[] addDataArr = cardsTypeList.getContent().split(",");
        final Long[] groupId = new Long[1];
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                groupId[0] = event.getGroup().getId();
            }

            @Override
            public void friend(FriendMessageEvent event) {
                groupId[0] = -event.getFriend().getId();
            }

            @Override
            public void groupTemp(GroupTempMessageEvent event) {
                groupId[0] = event.getGroup().getId();
            }
        });
        for (String anAddDataArr : addDataArr) {
            CardsGroupData cardsGroupData = new CardsGroupData();
            cardsGroupData.setTypeId(cardsTypeList.getId());
            cardsGroupData.setGroupId(groupId[0]);
            cardsGroupData.setValue(anAddDataArr);
            cardsGroupDataMapper.insert(cardsGroupData);
        }
        MyBatisUtil.getSqlSession().commit();
        return CustomText.getText("cards.draw.add.success", cardsTypeList.getName());
    }

    @InstructReflex(value = {"drawList", "drawlist"}, priority = 3)
    public String drawList(MessageData<?> data) {
        final Long[] groupId = new Long[1];
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                groupId[0] = event.getGroup().getId();
            }

            @Override
            public void friend(FriendMessageEvent event) {
                groupId[0] = -event.getFriend().getId();
            }

            @Override
            public void groupTemp(GroupTempMessageEvent event) {
                groupId[0] = event.getGroup().getId();
            }
        });
        List<CardsGroupData> list = cardsGroupDataMapper.getGroupCardsList(groupId[0]);
        if (list.size() <= 0) {
            return CustomText.getText("cards.draw.not.data");
        }
        StringBuilder result = new StringBuilder();

        result.append(CustomText.getText("cards.draw.list")).append("[");
        list.forEach(cardsGroupData -> result.append(cardsGroupData.getValue()).append(","));
        result.delete(result.length() - 1, result.length());
        result.append("]");
        return result.toString();
    }


    @InstructReflex(value = {"drawHide", "drawhide"}, priority = 3)
    public String drawHideOut(MessageData<?> data) {
        final long[] groupId = new long[1];
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                groupId[0] = event.getGroup().getId();
                CardsGroupData result = cardsGroupDataMapper.randomGetCard(groupId[0]);
                event.getSender().sendMessage(
                        CustomText.getText("cards.draw.hide.group.result",
                                event.getGroup().getId(), result.getValue())
                );
            }

            @Override
            public void friend(FriendMessageEvent event) {
                groupId[0] = -event.getFriend().getId();
                CardsGroupData result = cardsGroupDataMapper.randomGetCard(groupId[0]);
                event.getSender().sendMessage(
                        CustomText.getText("cards.draw.hide.friend.result",
                                event.getFriend().getId(), result.getValue())
                );

            }

            @Override
            public void groupTemp(GroupTempMessageEvent event) {
                groupId[0] = event.getGroup().getId();
                CardsGroupData result = cardsGroupDataMapper.randomGetCard(groupId[0]);
                event.getSender().sendMessage(
                        CustomText.getText("cards.draw.hide.group.result",
                                event.getGroup().getId(), result.getValue())
                );
            }
        });
        CardsGroupData result = cardsGroupDataMapper.randomGetCard(groupId[0]);
        if (result == null) {
            return CustomText.getText("cards.draw.not.data");
        }
        cardsGroupDataMapper.deleteById(result.getId());
        MyBatisUtil.getSqlSession().commit();
        return CustomText.getText("cards.draw.hide.success");
    }

    @InstructReflex(value = {"draw"}, priority = 2)
    public String drawOut(MessageData<?> data) {
        final long[] groupId = new long[1];
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                groupId[0] = event.getGroup().getId();
            }

            @Override
            public void friend(FriendMessageEvent event) {
                groupId[0] = -event.getFriend().getId();
            }

            @Override
            public void groupTemp(GroupTempMessageEvent event) {
                groupId[0] = event.getGroup().getId();
            }
        });
        CardsGroupData result = cardsGroupDataMapper.randomGetCard(groupId[0]);
        if (result == null) {
            return CustomText.getText("cards.draw.not.data");
        }
        cardsGroupDataMapper.deleteById(result.getId());
        MyBatisUtil.getSqlSession().commit();
        return CustomText.getText("cards.draw.success", result.getValue());
    }

    @InstructReflex(value = {"drawclear", "drawClear"}, priority = 3)
    public String drawClear(MessageData<?> data) {
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                cardsGroupDataMapper.clearByGroupId(event.getGroup().getId());
                MyBatisUtil.getSqlSession().commit();
            }

            @Override
            public void friend(FriendMessageEvent event) {
                cardsGroupDataMapper.clearByGroupId(-event.getFriend().getId());
                MyBatisUtil.getSqlSession().commit();
            }

            @Override
            public void groupTemp(GroupTempMessageEvent event) {
                cardsGroupDataMapper.clearByGroupId(event.getGroup().getId());
                MyBatisUtil.getSqlSession().commit();
            }
        });
        return CustomText.getText("cards.draw.clear");
    }


}
