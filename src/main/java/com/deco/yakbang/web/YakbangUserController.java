package com.deco.yakbang.web;

import java.util.HashMap;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deco.yakbang.service.UserService;
import com.deco.yakbang.service.vo.UserVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

@Tag(name = "YakbangUser", description = "약방 사용자 API")
@RestController
@RequestMapping("/yakbang/api/user")
public class YakbangUserController {

    @Resource(name = "userService")
    private UserService userService;

    @Operation(summary = "사용자 등록", description = "신규 사용자를 등록합니다.")
    @PostMapping("/register")
    public Map<String, Object> registerUser(@RequestBody UserVO userVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        userService.insertUser(userVO);
        resultMap.put("result", "success");
        return resultMap;
    }

    @Operation(summary = "아이디 중복 체크", description = "사용자 아이디 중복 여부를 확인합니다.")
    @GetMapping("/check-id")
    public Map<String, Object> checkDuplicateId(@RequestParam("loginId") String loginId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        int count = userService.checkDuplicateId(loginId);
        resultMap.put("count", count);
        resultMap.put("isDuplicate", count > 0);
        return resultMap;
    }

    @Operation(summary = "로그인", description = "사용자 로그인을 처리합니다.")
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody UserVO userVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        EgovMap loginUser = userService.actionLogin(userVO);
        
        if (loginUser != null && !loginUser.isEmpty()) {
            resultMap.put("result", "success");
            resultMap.put("user", loginUser);
        } else {
            resultMap.put("result", "fail");
            resultMap.put("message", "Invalid ID or Password");
        }
        return resultMap;
    }

    @Operation(summary = "프로필 수정", description = "사용자 프로필 정보를 수정합니다.")
    @PutMapping("/profile")
    public Map<String, Object> updateUserProfile(@RequestBody UserVO userVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        userService.updateUserProfile(userVO);
        resultMap.put("result", "success");
        return resultMap;
    }

    @Operation(summary = "사용자 삭제", description = "사용자 계정을 삭제합니다.")
    @DeleteMapping("/{userId}")
    public Map<String, Object> deleteUser(@PathVariable("userId") int userId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        userService.deleteUser(userId);
        resultMap.put("result", "success");
        return resultMap;
    }
}