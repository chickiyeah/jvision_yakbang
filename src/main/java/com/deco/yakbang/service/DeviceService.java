package com.deco.yakbang.service;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import com.deco.yakbang.service.vo.ClinicalLogVO;
import com.deco.yakbang.service.vo.DeviceVO;

public interface DeviceService {

    /** 사용자 디바이스 상세 정보 조회 */
    EgovMap selectUserDeviceDetail(int userId) throws Exception;

    /** 디바이스 상태 업데이트 */
    void updateDeviceStatus(DeviceVO deviceVO) throws Exception;

    /** 디바이스 등록 */
    void insertDevice(DeviceVO deviceVO) throws Exception;

    /** 임상 로그 등록 */
    void insertClinicalLog(ClinicalLogVO logVO) throws Exception;
}