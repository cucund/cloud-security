package com.cucund.security.premission.dao;

import java.io.Serializable;

import com.cucund.security.premission.model.PremissionList;

public interface PremissionListMapper extends Serializable {
    int deleteByPrimaryKey(Integer permissionListId);

    int insert(PremissionList record);

    int insertSelective(PremissionList record);

    PremissionList selectByPrimaryKey(Integer permissionListId);

    int updateByPrimaryKeySelective(PremissionList record);

    int updateByPrimaryKey(PremissionList record);
}