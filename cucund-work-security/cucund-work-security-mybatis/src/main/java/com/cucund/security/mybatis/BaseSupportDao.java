package com.cucund.security.mybatis;

import java.util.Date;


public interface BaseSupportDao {
	Date selectSysDate();
	int getMaxCode();
}
