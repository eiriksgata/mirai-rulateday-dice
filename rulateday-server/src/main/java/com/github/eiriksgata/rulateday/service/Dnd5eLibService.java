package com.github.eiriksgata.rulateday.service;
import com.github.eiriksgata.rulateday.pojo.QueryDataBase;

import java.util.List;

/**
 * author: create by Keith
 * version: v1.0
 * description: com.github.eiriksgata.rulateday.service
 * date: 2020/11/13
 **/
public interface Dnd5eLibService {

    List<QueryDataBase> findName(String name);

    QueryDataBase findById(long id);

    QueryDataBase getRandomMMData();

    void sendMMImage(Object event, QueryDataBase result) ;
}
