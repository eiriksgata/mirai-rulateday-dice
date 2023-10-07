package com.github.eiriksgata.rulateday.vo;

import lombok.Data;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.anjubao.txld.vo
 * date: 2021/6/3
 **/

@Data
public class ResponseBaseVo<T> {
    private Long code;
    private String message;
    private T data;

}
