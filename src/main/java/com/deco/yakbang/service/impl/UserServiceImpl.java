package com.deco.yakbang.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deco.yakbang.service.UserService;
import com.deco.yakbang.service.vo.UserVO;
import com.deco.yakbang.util.ResidentNumberUtil;
import com.deco.yakbang.util.Sha256Util;
import com.deco.yakbang.security.JwtTokenProvider;

import jakarta.annotation.Resource;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource(name = "userMapper")
    private UserMapper userMapper;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    /** 1. 솔트 조회: 안드로이드 로그인 전처리용 */
    @Override
    public String getSalt(String userId) throws Exception {
        return userMapper.getSaltByUserId(userId);
    }
    
    @Override
    public EgovMap getUserProfile(String userId) throws Exception {
        return userMapper.getUserProfile(userId);
    }

    @Override
    @Transactional
    public Map<String, Object> insertUser(UserVO userVO) throws Exception {
        // 1. 기존 가입 로직 (나이/성별 계산 및 해싱)
        String resNum = userVO.getResidentNumber();
        if (resNum != null && resNum.contains("-")) {
            userVO.setGender(ResidentNumberUtil.getGender(resNum));
            userVO.setAge(ResidentNumberUtil.getAge(resNum));
        }
        String salt = Sha256Util.getSalt();
        userVO.setSalt(salt);
        userVO.setPassword(Sha256Util.encrypt(userVO.getPassword(), salt));
        
        userMapper.insertUser(userVO);

        // 2. ⭐ 가입 성공 즉시 토큰 생성 (자동 로그인용)
        String at = jwtTokenProvider.createToken(userVO.getUserId(), userVO.getName());
        String rt = jwtTokenProvider.createRefreshToken(userVO.getUserId());

        // 3. RT DB 저장
        Map<String, Object> rtParam = new HashMap<>();
        rtParam.put("userId", userVO.getUserId());
        rtParam.put("tokenValue", rt);
        userMapper.upsertRefreshToken(rtParam);

        // 4. 안드로이드가 기다리는 데이터 조립
        Map<String, Object> result = new HashMap<>();
        result.put("result", "success");
        result.put("accessToken", at);
        result.put("refreshToken", rt);
        result.put("userId", userVO.getUserId());
        
        return result;
    }
    /** 3. 아이디 중복 체크 */
    @Override
    public int checkDuplicateId(String loginId) throws Exception {
        return userMapper.checkDuplicateId(loginId);
    }

    /** 4. 로그인 통합 처리: AT/RT 발급 및 RT DB 저장 */
    @Override
    @Transactional
    public Map<String, Object> processLogin(UserVO userVO) throws Exception {
        // 안드로이드 앱에서 이미 SHA256(PW+Salt)을 해서 보냈으므로 그대로 대조
        EgovMap loginUser = userMapper.actionLogin(userVO);
        
        if (loginUser != null && !loginUser.isEmpty()) {
            String userId = String.valueOf(loginUser.get("userId"));
            String userName = String.valueOf(loginUser.get("name"));

            // 1. Access Token (10시간) / Refresh Token (7일) 생성
            String accessToken = jwtTokenProvider.createToken(userId, userName);
            String refreshToken = jwtTokenProvider.createRefreshToken(userId);

            // 2. Refresh Token을 중앙 DB에 저장 (7대 피어 서버 공유)
            Map<String, Object> rtParam = new HashMap<>();
            rtParam.put("userId", userId);
            rtParam.put("tokenValue", refreshToken);
            userMapper.upsertRefreshToken(rtParam);

            // 3. 응답 객체 조립
            Map<String, Object> result = new HashMap<>();
            result.put("result", "success");
            result.put("accessToken", accessToken);
            result.put("refreshToken", refreshToken);
            result.put("user", loginUser);
            
            return result;
        }
        return null; // 로그인 실패 시 null 반환
    }

    /** 5. 토큰 갱신: 안드로이드 Authenticator 대응 로직 */
    @Override
    @Transactional
    public Map<String, Object> refreshAccessToken(String refreshToken) throws Exception {
        // 1. RT 자체의 유효성(만료 여부) 검증
        if (jwtTokenProvider.validateToken(refreshToken)) {
            String userId = jwtTokenProvider.getUserId(refreshToken);
            
            // 2. DB에 저장된 RT와 일치하는지 확인 (보안 강화)
            String savedRt = userMapper.getRefreshToken(userId);
            
            if (refreshToken.equals(savedRt)) {
                // 3. 새로운 Access Token 발급 (이름 정보가 필요할 경우 DB 재조회 가능)
                String newAccessToken = jwtTokenProvider.createToken(userId, "User"); 
                
                Map<String, Object> result = new HashMap<>();
                result.put("accessToken", newAccessToken);
                return result;
            }
        }
        return null; // 갱신 실패 (RT 만료 또는 불일치)
    }

    /** 6. 프로필 업데이트: 당뇨 관리 데이터 포함 */
    @Override
    @Transactional
    public void updateUserProfile(UserVO userVO) throws Exception {
        userMapper.updateUserProfile(userVO);
    }

    /** 7. 회원 탈퇴 */
    @Override
    @Transactional
    public void deleteUser(int id) throws Exception {
        userMapper.deleteUser(id);
    }

    /** 8. 기존 로그인 메서드 (호환성 유지용) */
    @Override
    public EgovMap actionLogin(UserVO userVO) throws Exception {
        return userMapper.actionLogin(userVO);
    }
}