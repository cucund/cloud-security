package com.cucund.security.premission.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import com.cucund.security.premission.model.PremissionList;

@Repository
public interface PremissionListMapper extends Serializable {
    int deleteByPrimaryKey(Integer permissionListId);

    int insert(PremissionList record);

    int insertSelective(PremissionList record);

    PremissionList selectByPrimaryKey(Integer permissionListId);

    int updateByPrimaryKeySelective(PremissionList record);

    int updateByPrimaryKey(PremissionList record);
}