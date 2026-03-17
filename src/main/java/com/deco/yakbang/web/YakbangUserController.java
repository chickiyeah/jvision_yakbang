package com.deco.yakbang.web;

import java.util.HashMap;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@Tag(name = "YakbangUser", description = "약방 사용자 API (JWT 인증 적용)")
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

    @Operation(summary = "사용자 등록", description = "신규 사용자를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserVO userVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        int count = userService.checkDuplicateId(userVO.getUserId());

        if (count > 0) {
            resultMap.put("result", "fail");
            resultMap.put("reason", "이미 사용중인 아이디입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        } else {
            userService.insertUser(userVO);
            resultMap.put("result", "success");
            return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
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

    @Operation(summary = "로그인", description = "사용자 로그인을 처리하고 JWT 토큰을 발급합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공 (토큰 포함)", 
            content = @Content(schema = @Schema(example = "{\"result\": \"success\", \"accessToken\": \"eyJhbG...\", \"user\": {\"userId\": \"admin\"}}"))),
        @ApiResponse(responseCode = "401", description = "로그인 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserVO userVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        EgovMap loginUser = userService.actionLogin(userVO);
        
        if (loginUser != null && !loginUser.isEmpty()) {
            // JWT 토큰 생성 (userId와 name 기반)
            String userId = String.valueOf(loginUser.get("userId"));
            String userName = String.valueOf(loginUser.get("name"));
            String token = jwtTokenProvider.createToken(userId, userName);

            resultMap.put("result", "success");
            resultMap.put("accessToken", token); // 클라이언트는 이 토큰을 저장해야 함
            resultMap.put("user", loginUser);
            return ResponseEntity.ok(resultMap);
        } else {
            resultMap.put("result", "fail");
            resultMap.put("message", "Invalid ID or Password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultMap);
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

    @Operation(summary = "사용자 삭제", description = "토큰 인증이 필요한 API입니다.",
               security = @SecurityRequirement(name = "Bearer Authentication"))
    @DeleteMapping("/{userId}")
    public Map<String, Object> deleteUser(@PathVariable("userId") int userId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        userService.deleteUser(userId);
        resultMap.put("result", "success");
        return resultMap;
    }
}