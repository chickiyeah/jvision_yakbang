package com.deco.yakbang.service.impl;

import java.util.Map;
import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import com.deco.yakbang.service.vo.UserVO;

@EgovMapper("userMapper")
public interface UserMapper {

    /** 1. 회원가입: Salt와 당뇨 관리 필드 포함 */
    void insertUser(UserVO userVO) throws Exception;

    /** 2. 아이디 중복 체크 */
    int checkDuplicateId(String userId) throws Exception;

    /** 3. 솔트 조회: 로그인 전 해싱을 위해 필요 */
    String getSaltByUserId(String userId) throws Exception;

    /** 4. 로그인 처리: 해싱된 PW와 ID 대조 */
    EgovMap actionLogin(UserVO userVO) throws Exception;

    /** 5. 리프레시 토큰 저장 및 갱신 (UPSERT) */
    void upsertRefreshToken(Map<String, Object> params) throws Exception;

    /** 6. 리프레시 토큰 조회: 토큰 갱신 시 검증용 */
    String getRefreshToken(String userId) throws Exception;

    /** 7. 프로필 업데이트: 신체 정보 및 당뇨 설정값 수정 */
    void updateUserProfile(UserVO userVO) throws Exception;

    /** 8. 회원 탈퇴 */
    void deleteUser(int id) throws Exception;
}