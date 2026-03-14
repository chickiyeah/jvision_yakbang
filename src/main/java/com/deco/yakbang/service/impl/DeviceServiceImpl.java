package com.deco.yakbang.service.impl;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Service;

import com.deco.yakbang.service.DeviceService;
import com.deco.yakbang.service.vo.ClinicalLogVO;
import com.deco.yakbang.service.vo.DeviceVO;
import org.springframework.context.annotation.Bean;

import jakarta.annotation.Resource;

@Service("deviceService")
public class DeviceServiceImpl extends EgovAbstractServiceImpl implements DeviceService {
	
    @Resource(name = "deviceMapper")
    private DeviceMapper deviceMapper;

    @Override
    public EgovMap selectUserDeviceDetail(int userId) throws Exception {
        return deviceMapper.selectUserDeviceDetail(userId);
    }

    @Override
    public void updateDeviceStatus(DeviceVO deviceVO) throws Exception {
        deviceMapper.updateDeviceStatus(deviceVO);
    }

    @Override
    public void insertDevice(DeviceVO deviceVO) throws Exception {
        deviceMapper.insertDevice(deviceVO);
    }

    @Override
    public void insertClinicalLog(ClinicalLogVO logVO) throws Exception {
        deviceMapper.insertClinicalLog(logVO);
    }
}