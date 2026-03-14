package com.deco.yakbang.service.impl;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Service;

import com.deco.yakbang.service.UserService;
import com.deco.yakbang.service.vo.UserVO;

import jakarta.annotation.Resource;

@Service("userService")
public class UserServiceImpl extends EgovAbstractServiceImpl implements UserService {

    @Resource(name = "userMapper")
    private UserMapper userMapper;

    @Override
    public void insertUser(UserVO userVO) throws Exception {
        userMapper.insertUser(userVO);
    }

    @Override
    public int checkDuplicateId(String loginId) throws Exception {
        return userMapper.checkDuplicateId(loginId);
    }

    @Override
    public EgovMap actionLogin(UserVO userVO) throws Exception {
        return userMapper.actionLogin(userVO);
    }

    @Override
    public void updateUserProfile(UserVO userVO) throws Exception {
        userMapper.updateUserProfile(userVO);
    }

    @Override
    public void deleteUser(int userId) throws Exception {
        userMapper.deleteUser(userId);
    }
}