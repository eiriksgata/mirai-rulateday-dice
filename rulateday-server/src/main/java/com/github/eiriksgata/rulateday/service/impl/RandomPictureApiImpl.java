package com.github.eiriksgata.rulateday.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.eiriksgata.rulateday.event.EventUtils;
import com.github.eiriksgata.trpg.dice.reply.CustomText;
import com.github.eiriksgata.trpg.dice.vo.MessageData;
import com.github.eiriksgata.rulateday.event.EventAdapter;
import com.github.eiriksgata.rulateday.service.RandomPictureApiService;
import com.github.eiriksgata.rulateday.utlis.RestUtil;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class RandomPictureApiImpl implements RandomPictureApiService {

    @Override
    public String yinhuaAPI(MessageData<?> data) {
        String pictureAddress;
        try {
            String result = RestUtil.get("https://www.dmoe.cc/random.php?return=json");
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (Objects.equals(jsonObject.getString("code"), "200")) {
                pictureAddress = jsonObject.getString("imgurl");
            } else {
                return CustomText.getText("api.request.error");
            }
        } catch (Exception e) {
            return CustomText.getText("api.request.error");
        }

        URL pictureUrl;
        InputStream inputStream;
        try {
            pictureUrl = new URL(pictureAddress);
            HttpURLConnection conn = (HttpURLConnection) pictureUrl.openConnection();
            // 设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            // 防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            // 得到输入流
            inputStream = conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return CustomText.getText("kkp.error");
        }

        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                try {
                    event.getGroup().sendMessage(
                            event.getGroup().uploadImage(ExternalResource.create(inputStream)));
                } catch (IOException ignored) {
                }
            }

            @Override
            public void friend(FriendMessageEvent event) {
                try {
                    event.getSender().sendMessage(
                            event.getSender().uploadImage(ExternalResource.create(inputStream))
                    );
                } catch (IOException ignored) {
                }
            }

            @Override
            public void groupTemp(GroupTempMessageEvent event) {
                try {
                    event.getSender().sendMessage(
                            event.getSender().uploadImage(ExternalResource.create(inputStream))
                    );
                } catch (IOException ignored) {
                }
            }
        });

        return null;
    }


    @Override
    public String urlEncodeAPI(MessageData<?> data, String url) {
        URL pictureUrl;
        InputStream inputStream;
        try {
            pictureUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) pictureUrl.openConnection();
            // 设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            // 防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            // 得到输入流
            inputStream = conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return CustomText.getText("kkp.error");
        }

        EventUtils.eventCallback(data.getEvent(), new EventAdapter() {
            @Override
            public void group(GroupMessageEvent event) {
                try {
                    event.getGroup().sendMessage(
                            event.getGroup().uploadImage(ExternalResource.create(inputStream)));
                } catch (IOException ignored) {
                }
            }

            @Override
            public void friend(FriendMessageEvent event) {
                try {
                    event.getSender().sendMessage(
                            event.getSender().uploadImage(ExternalResource.create(inputStream))
                    );
                } catch (IOException ignored) {
                }
            }

            @Override
            public void groupTemp(GroupTempMessageEvent event) {
                try {
                    event.getSender().sendMessage(
                            event.getSender().uploadImage(ExternalResource.create(inputStream))
                    );
                } catch (IOException ignored) {
                }
            }
        });
        return null;
    }


}
