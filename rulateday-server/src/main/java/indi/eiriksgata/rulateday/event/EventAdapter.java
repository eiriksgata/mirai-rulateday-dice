package indi.eiriksgata.rulateday.event;


import net.mamoe.mirai.event.events.*;

/**
 * author: create by Keith
 * version: v1.0
 * description: indi.eiriksgata.rulateday.event
 * date: 2021/3/15
 **/
public abstract class EventAdapter implements MessageEventHandler {

    @Override
    public void group(GroupMessageEvent event) {
    }

    @Override
    public void friend(FriendMessageEvent event) {

    }

    @Override
    public void groupTemp(GroupTempMessageEvent event) {

    }

    @Override
    public void otherClient(OtherClientEvent event) {

    }

    @Override
    public void stranger(StrangerMessageEvent strangerMessageEvent) {

    }


}
