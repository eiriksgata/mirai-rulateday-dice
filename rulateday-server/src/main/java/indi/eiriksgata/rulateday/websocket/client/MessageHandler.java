package indi.eiriksgata.rulateday.websocket.client;

import com.alibaba.fastjson.JSONObject;
import indi.eiriksgata.rulateday.utlis.Base64;
import indi.eiriksgata.rulateday.websocket.client.vo.SendGroupMessageVo;
import indi.eiriksgata.rulateday.websocket.client.vo.WsDataBean;
import com.alibaba.fastjson.TypeReference;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.utils.ExternalResource;

import static indi.eiriksgata.rulateday.websocket.client.EventType.*;

public class MessageHandler {

    public static void implement(String text) {
        JSONObject jsonObject = JSONObject.parseObject(text);
        String eventType = jsonObject.getString("eventType");
        switch (eventType) {
            case SEND_GROUP_MESSAGE:
                sendGroupMessage(JSONObject.parseObject(text, new TypeReference<
                        WsDataBean<SendGroupMessageVo>>() {
                }.getType()));
                break;
            default:
                break;
        }
    }

    public static void sendGroupMessage(WsDataBean<SendGroupMessageVo> wsDataBean) {
        String currentTimestamp = System.currentTimeMillis() + "";
//      ImageToBase64.base64ToImage(wsDataBean.getData().getPictureBase64(), "temp" + currentTimestamp + ".png");
//      File file = new File("temp" + currentTimestamp + ".png");
        Group group = Bot.getInstances().get(0).getGroup(
                wsDataBean.getData().getGroupId()
        );
        if (wsDataBean.getData().getText() != null && !wsDataBean.getData().getText().equals("")) {
            assert group != null;
            group.sendMessage(
                    new At(wsDataBean.getData().getSenderId()).plus(wsDataBean.getData().getText())
            );
        }
        if (wsDataBean.getData().getPictureBase64() != null && !wsDataBean.getData().getPictureBase64().equals("")) {
            assert group != null;
            group.sendMessage(group.uploadImage(ExternalResource.create(
                    Base64.decode2(wsDataBean.getData().getPictureBase64())
            )));
        }
    }

}
