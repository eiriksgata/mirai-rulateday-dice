package com.github.eiriksgata.rulateday.service;


import com.github.eiriksgata.rulateday.dto.DiceMessageDTO;

public interface RandomPictureApiService {
    String yinhuaAPI(DiceMessageDTO data);

    String urlEncodeAPI(DiceMessageDTO data, String url);
}
