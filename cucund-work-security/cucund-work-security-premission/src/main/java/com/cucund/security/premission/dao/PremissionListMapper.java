package com.cucund.security.premission.dao;

import com.cucund.security.premission.model.PremissionList;
import java.util.List;
import java.util.Map;

public interface PremissionListMapper {
    int deleteByPrimaryKey(Integer permissionListId);

    int insert(PremissionList record);

    int insertSelective(PremissionList record);

    List<PremissionList> query(Map<String, Object> parameters);

    int count(Map<String, Object> parameters);

    int updateStateByPrimaryKey(Map<String, Object> map);

    PremissionList getByCode(Map<String, Object> map);

    int delByCode(Map<String, Object> map);

    void insertBatch(List<PremissionList> list);

    PremissionList selectByPrimaryKey(Integer permissionListId);

    int updateByPrimaryKeySelective(PremissionList record);

    int updateByPrimaryKey(PremissionList record);
}