package indi.eiriksgata.rulateday.service;

import indi.eiriksgata.dice.vo.MessageData;

public interface RandomPictureApiService {
    String yinhuaAPI(MessageData<?> data);

    String xiaoWaiAPI(MessageData<?> data);
}
