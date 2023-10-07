package com.github.eiriksgata.rulateday.service;

import com.github.eiriksgata.trpg.dice.vo.MessageData;

public interface RandomPictureApiService {
    String yinhuaAPI(MessageData<?> data);

    String urlEncodeAPI(MessageData<?> data, String url);
}
