package com.deco.yakbang.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deco.yakbang.service.ActivityLogService;
import com.deco.yakbang.service.vo.ActivityLogVO;
import com.deco.yakbang.security.JwtTokenProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

@Tag(name = "YakbangApp", description = "약방 앱 데이터 수집 API")
@RestController
@RequestMapping("/yakbang/api/app")
public class YakbangActivityLogController {

    @Resource(name = "appService")
    private ActivityLogService appService;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Operation(
        summary = "걸음 수 데이터 등록", 
        description = "인증된 사용자의 일일 걸음 수를 등록합니다. 토큰에서 사용자 정보를 자동으로 추출합니다.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "등록 성공", 
            content = @Content(schema = @Schema(example = "{\"result\": \"success\"}"))),
        @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 오류)")
    })
    @PostMapping("/walk")
    public Map<String, Object> addWalkData(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody ActivityLogVO appVO) throws Exception {
        
        Map<String, Object> resultMap = new HashMap<>();

        // 1. 토큰 유효성 검사 (Bearer 제외 후 파싱)
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            
            if (jwtTokenProvider.validateToken(jwtToken)) {
                // 2. 토큰에서 userId 추출 (보안상 클라이언트 전달값보다 토큰값이 우선)
                String userIdFromToken = jwtTokenProvider.getUserId(jwtToken);
                
                // VO에 토큰에서 뽑은 ID 세팅 (String/int 타입에 맞춰 변환 필요)
                // 만약 ActivityLogVO의 userId가 int라면: Integer.parseInt(userIdFromToken)
                appVO.setUserId(Integer.parseInt(userIdFromToken)); 
                
                appService.insertActivityLog(appVO);
                resultMap.put("result", "success");
            } else {
                resultMap.put("result", "fail");
                resultMap.put("message", "유효하지 않거나 만료된 토큰입니다.");
            }
        } else {
            resultMap.put("result", "fail");
            resultMap.put("message", "인증 헤더가 누락되었습니다.");
        }

        return resultMap;
    }
}