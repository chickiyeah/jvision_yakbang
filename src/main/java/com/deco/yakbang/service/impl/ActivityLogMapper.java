package com.deco.yakbang.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.EgovMapper;

import com.deco.yakbang.service.vo.ActivityLogVO;

@EgovMapper("appMapper")
public interface ActivityLogMapper {
	void insertActivityLog(ActivityLogVO vo) throws Exception;
}
