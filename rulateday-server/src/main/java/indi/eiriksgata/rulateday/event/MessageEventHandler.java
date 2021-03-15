package indi.eiriksgata.rulateday.event;

import net.mamoe.mirai.event.events.*;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.instruction
 * date: 2021/3/12
 **/
public interface MessageEventHandler {

    void group(GroupMessageEvent event);

    void friend(FriendMessageEvent event);

    void groupTemp(GroupTempMessageEvent event);

    void otherClient(OtherClientEvent event);

    void stranger(StrangerMessageEvent strangerMessageEvent);


}
