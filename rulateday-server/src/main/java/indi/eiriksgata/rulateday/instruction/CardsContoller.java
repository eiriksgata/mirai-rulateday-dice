package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.event.EventAdapter;
import indi.eiriksgata.rulateday.event.EventUtils;
import indi.eiriksgata.rulateday.mapper.CardsGroupDataMapper;
import indi.eiriksgata.rulateday.mapper.CardsTypeListMapper;
import indi.eiriksgata.rulateday.pojo.CardsGroupData;
import indi.eiriksgata.rulateday.pojo.CardsTypeList;
import indi.eiriksgata.rulateday.utlis.MyBatisUtil;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.instruction
 * date: 2021/4/19
 **/
@InstructService
public class CardsContoller {

    public static CardsGroupDataMapper cardsGroupDataMapper = MyBatisUtil.getSqlSession().getMapper(CardsGroupDataMapper.class);
    public static CardsTypeListMapper cardsTypeListMapper = MyBatisUtil.getSqlSession().getMapper(CardsTypeListMapper.class);

    @InstructReflex(value = {".cards"})
    public String cardsList(MessageData data) {
        List<CardsTypeList> lists = cardsTypeListMapper.selectAll();
        if (lists.size() == 0) {
            return "当前存储库没有卡组类型,可以通过指令.cardsAdd进行添加";
        }
        StringBuilder result = new StringBuilder();
        result.append("当前存储库下有以下卡组类型:");
        AtomicInteger count = new AtomicInteger();
        lists.forEach((cardsTypeList) -> {
            count.getAndIncrement();
            result.append("\n").append(count.get()).append(". ").append(cardsTypeList.getName());
        });
        return result.toString();
    }

    @InstructReflex(value = {".cardsAdd", ".cardsadd"}, priority = 3)
    public String cardsAdd(MessageData data) {
        String[] parsingData = data.getMessage().split(" ");
        if (parsingData.length < 2) {
            return "参数类型不正确，正确格式应该是[卡组名称 卡组数据]例如:麻将 白板,红中,发字,东风,南风";
        }
        CardsTypeList cardsTypeList = new CardsTypeList();
        cardsTypeList.setName(parsingData[0]);
        cardsTypeList.setContent(data.getMessage().substring(parsingData[0].length() + 1));
        try {
            cardsTypeListMapper.insert(cardsTypeList);
            MyBatisUtil.getSqlSession().commit();
        } catch (Exception e) {
            return "增加卡组类型失败，可能卡组中已存在该卡组名。";
        }
        return "增加成功";
    }

    @InstructReflex(value = {".cardsDel", ".cardsdel"}, priority = 3)
    public String cardsDel(MessageData data) {
        cardsTypeListMapper.deleteByName(data.getMessage());
        MyBatisUtil.getSqlSession().commit();
        return "已删除的卡组";
    }


    @InstructReflex(value = {".drawAdd", ".drawadd"}, priority = 3)
    public String drawAdd(MessageData data) {
        if (data.getMessage().equals("") || data.getMessage() == null) {
            return "请输入添加的牌组名称,可以使用.cards查看";
        }
        CardsTypeList cardsTypeList = cardsTypeListMapper.selectByName(data.getMessage());
        if (cardsTypeList == null) {
            return "查询不到卡组名称，请使用.cards查看所有牌组";
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
        return "已将卡组[" + cardsTypeList.getName() + "],添加至当前牌堆。";
    }

    @InstructReflex(value = {".drawList", ".drawlist"}, priority = 3)
    public String drawList(MessageData data) {
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
            return "当前所在群的牌堆中无数据，请使用指令.drawAdd进行添加";
        }
        StringBuilder result = new StringBuilder();
        result.append("卡池列表:[");
        list.forEach(cardsGroupData -> {
            result.append(cardsGroupData.getValue()).append(",");
        });
        result.delete(result.length() - 1, result.length());
        result.append("]");
        return result.toString();
    }


    @InstructReflex(value = {".drawHide", ".drawhide"}, priority = 3)
    public String drawHideOut(MessageData data) {
        final long[] groupId = new long[1];
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                groupId[0] = event.getGroup().getId();
                CardsGroupData result = cardsGroupDataMapper.randonGetCard(groupId[0]);
                event.getSender().sendMessage("在QQ群[" + event.getGroup().getId() + "]的抽出结果为：" + result.getValue());
            }

            @Override
            public void friend(FriendMessageEvent event) {
                groupId[0] = -event.getFriend().getId();
                CardsGroupData result = cardsGroupDataMapper.randonGetCard(groupId[0]);
                event.getSender().sendMessage("在QQ[" + event.getFriend().getId() + "]的抽出结果为：" + result.getValue());

            }

            @Override
            public void groupTemp(GroupTempMessageEvent event) {
                groupId[0] = event.getGroup().getId();
                CardsGroupData result = cardsGroupDataMapper.randonGetCard(groupId[0]);
                event.getSender().sendMessage("在QQ群[" + event.getGroup().getId() + "]的抽出结果为：" + result.getValue());
            }
        });
        CardsGroupData result = cardsGroupDataMapper.randonGetCard(groupId[0]);
        if (result == null) {
            return "当前牌堆没有任何数据";
        }
        cardsGroupDataMapper.deleteById(result.getId());
        MyBatisUtil.getSqlSession().commit();
        return "抽出结果已私发";
    }

    @InstructReflex(value = {".draw"})
    public String drawOut(MessageData data) {
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
        CardsGroupData result = cardsGroupDataMapper.randonGetCard(groupId[0]);
        if (result == null) {
            return "当前牌堆没有任何数据";
        }
        cardsGroupDataMapper.deleteById(result.getId());
        MyBatisUtil.getSqlSession().commit();
        return "抽取结果：" + result.getValue();
    }

    @InstructReflex(value = {".drawclear", ".drawClear"}, priority = 3)
    public String drawClear(MessageData data) {
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
        return "已清空当前牌堆所有数据";
    }


}
