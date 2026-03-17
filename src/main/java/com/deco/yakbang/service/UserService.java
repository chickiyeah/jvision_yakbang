package com.deco.yakbang.service;

import java.util.Map;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import com.deco.yakbang.service.vo.UserVO;

public interface UserService {

    /** * 회원가입 
     * 내부적으로 랜덤 솔트를 생성하고 비밀번호를 해싱하여 저장합니다.
     * @return 
     */
    Map<String, Object> insertUser(UserVO userVO) throws Exception;

    /** * 아이디 중복 체크 
     */
    int checkDuplicateId(String loginId) throws Exception;

    /** * 솔트 조회 
     * 로그인 전 사용자의 고유 솔트값을 가져옵니다.
     */
    String getSalt(String userId) throws Exception;

    /** * 로그인 처리 (통합)
     * ID/PW 대조 후 AccessToken과 RefreshToken을 생성하여 반환합니다.
     */
    Map<String, Object> processLogin(UserVO userVO) throws Exception;

    /** * 토큰 갱신 
     * 유효한 RefreshToken인지 확인 후 새로운 AccessToken을 발급합니다.
     */
    Map<String, Object> refreshAccessToken(String refreshToken) throws Exception;

    /** * 프로필 업데이트 
     * 신체 정보(키, 몸무게) 및 당뇨 설정값(목표 혈당 등)을 수정합니다.
     */
    void updateUserProfile(UserVO userVO) throws Exception;
    
    /** * 회원 탈퇴 
     */
    void deleteUser(int userId) throws Exception;

    /** * 기존 로그인 메서드 (호환성 유지용) 
     */
    EgovMap actionLogin(UserVO userVO) throws Exception;

	EgovMap getUserProfile(String userId) throws Exception;
}