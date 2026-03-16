package com.deco.yakbang.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deco.yakbang.service.ActivityLogService;
import com.deco.yakbang.service.vo.ActivityLogVO;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "걸음 수 데이터 등록", description = "앱에서 측정된 사용자의 일일 걸음 수를 등록합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "걸음 수 데이터",
        content = @Content(schema = @Schema(example = "{\"userId\": 1, \"stepCount\": 5420, \"walkDate\": \"2026-03-16\"}")))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "등록 성공", 
            content = @Content(schema = @Schema(example = "{\"result\": \"success\"}")))
    })
    @PostMapping("/walk")
    public Map<String, Object> addWalkData(@RequestBody ActivityLogVO appVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        
        appService.insertActivityLog(appVO);//
        
        resultMap.put("result", "success");
        return resultMap;
    }
}