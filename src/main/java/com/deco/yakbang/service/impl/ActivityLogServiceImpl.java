package com.deco.yakbang.service.impl;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Service;

import com.deco.yakbang.service.ActivityLogService;
import com.deco.yakbang.service.vo.ActivityLogVO;

import jakarta.annotation.Resource;

@Service("appService")
public class ActivityLogServiceImpl extends EgovAbstractServiceImpl implements ActivityLogService {
	
	@Resource(name="appMapper")
	private ActivityLogMapper mapper;

	@Override
	public void insertActivityLog(ActivityLogVO vo) throws Exception {
		mapper.insertActivityLog(vo);
	}
	
}
