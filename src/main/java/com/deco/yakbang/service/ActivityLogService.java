package com.deco.yakbang.service;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import com.deco.yakbang.service.vo.ActivityLogVO;

public interface ActivityLogService {
	
	void insertActivityLog(ActivityLogVO vo) throws Exception;
	
}
