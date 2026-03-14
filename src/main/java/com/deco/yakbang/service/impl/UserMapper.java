package com.deco.yakbang.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import com.deco.yakbang.service.vo.UserVO;

@EgovMapper("userMapper")
public interface UserMapper {

    void insertUser(UserVO userVO) throws Exception;

    int checkDuplicateId(String loginId) throws Exception;

    EgovMap actionLogin(UserVO userVO) throws Exception;

    void updateUserProfile(UserVO userVO) throws Exception;

    void deleteUser(int userId) throws Exception;
}