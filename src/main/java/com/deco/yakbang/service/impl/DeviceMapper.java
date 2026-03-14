package com.deco.yakbang.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import com.deco.yakbang.service.vo.ClinicalLogVO;
import com.deco.yakbang.service.vo.DeviceVO;

@EgovMapper("deviceMapper")
public interface DeviceMapper {

    EgovMap selectUserDeviceDetail(int userId) throws Exception;

    void updateDeviceStatus(DeviceVO deviceVO) throws Exception;

    void insertDevice(DeviceVO deviceVO) throws Exception;

    void insertClinicalLog(ClinicalLogVO logVO) throws Exception;
}