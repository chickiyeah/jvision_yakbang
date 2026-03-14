package com.deco.yakbang.web;

import java.util.HashMap;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deco.yakbang.service.DeviceService;
import com.deco.yakbang.service.vo.ClinicalLogVO;
import com.deco.yakbang.service.vo.DeviceVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

@Tag(name = "YakbangDevice", description = "약방 디바이스 API")
@RestController
@RequestMapping("/yakbang/api/device")
public class YakbangDeviceController {

    @Resource(name = "deviceService")
    private DeviceService deviceService;

    @Operation(summary = "디바이스 상세 조회", description = "사용자의 디바이스 상세 정보를 조회합니다.")
    @GetMapping("/{userId}")
    public Map<String, Object> getUserDeviceDetail(@PathVariable("userId") int userId) throws Exception {
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
    @PostMapping("/register")
    public Map<String, Object> registerDevice(@RequestBody DeviceVO deviceVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        deviceService.insertDevice(deviceVO);
        resultMap.put("result", "success");
        return resultMap;
    }

    @Operation(summary = "디바이스 상태 업데이트", description = "디바이스의 상태 정보를 업데이트합니다.")
    @PutMapping("/status")
    public Map<String, Object> updateDeviceStatus(@RequestBody DeviceVO deviceVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        deviceService.updateDeviceStatus(deviceVO);
        resultMap.put("result", "success");
        return resultMap;
    }

    @Operation(summary = "임상 로그 등록", description = "디바이스의 임상 로그를 등록합니다.")
    @PostMapping("/log")
    public Map<String, Object> addClinicalLog(@RequestBody ClinicalLogVO logVO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        deviceService.insertClinicalLog(logVO);
        resultMap.put("result", "success");
        return resultMap;
    }
}