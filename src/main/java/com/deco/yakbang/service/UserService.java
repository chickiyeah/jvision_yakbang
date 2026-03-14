package com.deco.yakbang.service;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import com.deco.yakbang.service.vo.UserVO;

public interface UserService {

    /** 회원가입 */
    void insertUser(UserVO userVO) throws Exception;

    /** 아이디 중복 체크 */
    int checkDuplicateId(String loginId) throws Exception;

    /** 로그인 처리 */
    EgovMap actionLogin(UserVO userVO) throws Exception;

    /** 프로필 업데이트 */
    void updateUserProfile(UserVO userVO) throws Exception;
    
    /** 회원 탈퇴 */
    void deleteUser(int userId) throws Exception;
}