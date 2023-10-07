package com.github.eiriksgata.rulateday.service;

public interface UserTempDataService {

    void updateUserAttribute(Long id, String attribute);

    void updateUserDiceFace(Long id, int diceFace);

    void addUserTempData(Long id);

    Integer getUserDiceFace(Long id);

    String getUserAttribute(Long id);

}
