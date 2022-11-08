package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.rulateday.config.GlobalData;
import indi.eiriksgata.rulateday.event.EventAdapter;
import indi.eiriksgata.rulateday.event.EventUtils;
import net.mamoe.mirai.event.events.GroupMessageEvent;

@InstructService
public class MessageRecordController {

    @InstructReflex(value = {"logon", "log-on"}, priority = 2)
    public String getAtkList(MessageData<?> data) {
        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                GlobalData.messageRecordMap.put(event.getGroup().getId() + "", true);
                event.getGroup().sendMessage("开记录");
            }
        });
        return null;
    }
}
