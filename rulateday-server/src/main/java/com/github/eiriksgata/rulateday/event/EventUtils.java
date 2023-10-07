package com.github.eiriksgata.rulateday.event;

import net.mamoe.mirai.event.events.*;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.github.eiriksgata.rulateday.event
 * date: 2021/3/12
 **/

public class EventUtils {

    public static void eventCallback(Object event, EventAdapter eventHandler) {

        if (event.getClass() == GroupMessageEvent.class) {
            eventHandler.group((GroupMessageEvent) event);
            return;
        }
        if (event.getClass() == FriendMessageEvent.class) {
            eventHandler.friend((FriendMessageEvent) event);
            return;
        }
        if (event.getClass() == StrangerMessageEvent.class) {
            eventHandler.stranger((StrangerMessageEvent) event);
            return;
        }
        if (event.getClass() == GroupTempMessageEvent.class) {
            eventHandler.groupTemp((GroupTempMessageEvent) event);
            return;
        }

        if (event.getClass() == OtherClientEvent.class) {
            eventHandler.otherClient((OtherClientEvent) event);
        }



    }

}
