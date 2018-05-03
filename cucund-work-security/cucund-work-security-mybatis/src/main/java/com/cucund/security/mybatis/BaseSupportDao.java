package com.cucund.security.mybatis;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;

public interface BaseSupportDao {
	Date selectSysDate();
	int getMaxCode();
}
