package com.deco.yakbang.web;

import java.util.HashMap;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.web.bind.annotation.*;

import com.deco.yakbang.service.DeviceService;
import com.deco.yakbang.service.vo.ClinicalLogVO;
import com.deco.yakbang.service.vo.DeviceVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

@Tag(name = "YakbangDevice", description = "약방 디바이스 API")
@RestController
@RequestMapping("/yakbang/api/device")
public class YakbangDeviceController {

    @Resource(name = "deviceService")
    private DeviceService deviceService;

    @Operation(summary = "디바이스 상세 조회", description = "사용자의 디바이스 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", 
            content = @Content(schema = @Schema(example = "{\"result\": \"success\", \"data\": {\"deviceId\": \"DEV-001\", \"battery\": 92, \"status\": \"ACTIVE\"}}")))
    })
    @GetMapping("/{userId}")
    public Map<String, Object> getUserDeviceDetail(
        @Parameter(description = "사용자 고유 ID", example = "1") @PathVariable("userId") int userId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        EgovMap deviceDetail = deviceService.selectUserDeviceDetail(userId);
        
        if (deviceDetail != null) {
            resultMap.put("result", "success");
            resultMap.put("data", deviceDetail);
        } else {
            resultMap.put("result", "fail");
            resultMap.put("message", "Device not found for this user");
        }
        return resultMap;
    }

    @Operation(summary = "디바이스 등록", description = "신규 디바이스를 등록합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "등록할 디바이스 정보",
        content = @Content(schema = @Schema(example = "{\"userId\": 1, \"deviceId\": \"DEV-999\", \"deviceType\": \"PEN\"}")))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "등록 성공", 
            content = @Content(schema = @Schema(example = "{\"result\": \"success\"}")))
    })
    @PostMapping("/register")
    public Map<String, Object> registerDevice(@RequestBody DeviceVO deviceVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        deviceService.insertDevice(deviceVO);
        resultMap.put("result", "success");
        return resultMap;
    }

    @Operation(summary = "디바이스 상태 업데이트", description = "디바이스의 상태 정보를 업데이트합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "업데이트할 디바이스 상태",
        content = @Content(schema = @Schema(example = "{\"deviceId\": \"DEV-001\", \"battery\": 45, \"status\": \"CHARGING\"}")))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "업데이트 성공", 
            content = @Content(schema = @Schema(example = "{\"result\": \"success\"}")))
    })
    @PutMapping("/status")
    public Map<String, Object> updateDeviceStatus(@RequestBody DeviceVO deviceVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        deviceService.updateDeviceStatus(deviceVO);
        resultMap.put("result", "success");
        return resultMap;
    }

    @Operation(summary = "임상 로그 등록", description = "디바이스의 임상 로그를 등록합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "등록할 임상 데이터",
        content = @Content(schema = @Schema(example = "{\"userId\": 1, \"glucose\": 145, \"insulinDose\": 4.2, \"stepCount\": 5200}")))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그 등록 성공", 
            content = @Content(schema = @Schema(example = "{\"result\": \"success\"}")))
    })
    @PostMapping("/log")
    public Map<String, Object> addClinicalLog(@RequestBody ClinicalLogVO logVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        deviceService.insertClinicalLog(logVO);
        resultMap.put("result", "success");
        return resultMap;
    }
}