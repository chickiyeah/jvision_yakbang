package com.deco.yakbang.web;

import java.util.HashMap;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.deco.yakbang.service.UserService;
import com.deco.yakbang.service.vo.UserVO;
import com.deco.yakbang.security.JwtTokenProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

@Tag(name = "YakbangUser", description = "약방 사용자 API (JWT & Salt 보안 적용)")
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
@RestController
@RequestMapping("/yakbang/api/user")
public class YakbangUserController {

    @Resource(name = "userService")
    private UserService userService;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "솔트 조회", description = "로그인 전 비밀번호 해싱을 위한 유저별 솔트값을 조회합니다.")
    @GetMapping("/salt/{userId}")
    public ResponseEntity<Map<String, String>> getUserSalt(
            @Parameter(description = "조회할 유저 아이디", example = "tester01") @PathVariable("userId") String userId) throws Exception {
        
        String salt = userService.getSalt(userId);
        if (salt != null) {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("salt", salt);
            return ResponseEntity.ok(resultMap);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "사용자 등록", description = "신규 사용자를 등록합니다. 서버에서 솔트를 생성하여 저장합니다.")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserVO userVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        int count = userService.checkDuplicateId(userVO.getUserId());

        if (count > 0) {
            resultMap.put("result", "fail");
            resultMap.put("reason", "이미 사용중인 아이디입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        } else {
            // Service 내부에서 Salt 생성 및 해싱 처리
        	Map<String, Object> result = userService.insertUser(userVO);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
    }

    @Operation(summary = "아이디 중복 체크", description = "사용자 아이디 중복 여부를 확인합니다.")
    @GetMapping("/check-id.do")
    public Map<String, Object> checkDuplicateId(@RequestParam("loginId") String loginId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        int count = userService.checkDuplicateId(loginId);
        resultMap.put("count", count);
        resultMap.put("isDuplicate", count > 0);
        return resultMap;
    }

    @Operation(summary = "로그인", description = "ID와 해싱된 PW로 로그인을 처리하고 AT/RT를 발급합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공", 
            content = @Content(schema = @Schema(example = "{\"result\": \"success\", \"accessToken\": \"at_value\", \"refreshToken\": \"rt_value\", \"user\": {...}}"))),
        @ApiResponse(responseCode = "401", description = "로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserVO userVO) throws Exception {
        // Service에서 AT/RT 발급 및 RT DB 저장 로직 수행
        Map<String, Object> loginResult = userService.processLogin(userVO);
        
        if (loginResult != null) {
            return ResponseEntity.ok(loginResult);
        } else {
            Map<String, Object> failMap = new HashMap<>();
            failMap.put("result", "fail");
            failMap.put("message", "Invalid ID or Password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(failMap);
        }
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 이용하여 새로운 액세스 토큰을 발급합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) throws Exception {
        String refreshToken = request.get("refreshToken");
        
        // 토큰 유효성 및 DB 대조 (Service 로직 호출)
        Map<String, Object> refreshResult = userService.refreshAccessToken(refreshToken);
        
        if (refreshResult != null) {
            return ResponseEntity.ok(refreshResult);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
        }
    }
    
    @Operation(summary = "내 프로필 조회", security = @SecurityRequirement(name = "Bearer Authentication"))
    @GetMapping("/profile")
    public ResponseEntity<EgovMap> getMyProfile() throws Exception {
        // 1. SecurityContext에서 현재 로그인한 유저 ID 추출
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // 2. DB 조회
        EgovMap profile = userService.getUserProfile(userId);
        
        if (profile != null) {
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "프로필 수정", description = "토큰 인증이 필요한 API입니다.", 
               security = @SecurityRequirement(name = "Bearer Authentication"))
    @PutMapping("/profile")
    public Map<String, Object> updateUserProfile(@RequestBody UserVO userVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        userService.updateUserProfile(userVO);
        resultMap.put("result", "success");
        return resultMap;
    }

    @Operation(summary = "사용자 삭제", description = "사용자 계정을 삭제합니다.",
               security = @SecurityRequirement(name = "Bearer Authentication"))
    @DeleteMapping("/{userId}")
    public Map<String, Object> deleteUser(
        @Parameter(description = "삭제할 사용자 고유 ID", example = "1") @PathVariable("userId") int userId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        userService.deleteUser(userId);
        resultMap.put("result", "success");
        return resultMap;
    }
}