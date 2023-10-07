package com.github.eiriksgata.rulateday.websocket.client;

import com.github.eiriksgata.rulateday.config.GlobalData;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class WebSocketClientInit {

    public static WebSocketClient webSocketClient;

    public static Timer reconnectionTimer = new Timer();


    public static void run() {
        String url = GlobalData.configData.getJSONObject("ai-drawing").getString("url");
        if (url == null || url.equals("")) {
            return;
        }
        url += "/push-channel";

        String userId = GlobalData.configData.getJSONObject("ai-drawing").getString("userId");

        if (userId == null || GlobalData.machineCode == null || userId.equals("") || GlobalData.machineCode.equals("")) {
            System.out.println("Rulateday-dice: userId or machineCode get fail , ai drawing server run fail.");
            return;
        }


        Map<String, String> header = new HashMap<>();
        header.put("userId", userId);
        header.put("authorization", BCrypt.hashpw(GlobalData.machineCode, BCrypt.gensalt()));

        reconnectionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (webSocketClient.isOpen()) {
                    webSocketClient.sendPing();
                } else {
                    webSocketClient.reconnect();
                    System.out.println("ai drawing server reconnecting..");
                }
            }
        }, 10000, 30000);

        try {
            webSocketClient = new WebSocketClient(new URI(url), header) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("link server success!");
                }

                @Override
                public void onMessage(String text) {
                    MessageHandler.implement(text);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    System.out.println("onClose:" + s);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }


            };
            webSocketClient.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("websocket server request error ! client link fail.");
        }
    }


}
