package com.cucund.security.permission.dao;

import com.cucund.security.mybatis.BaseSupportDao;
import com.cucund.security.permission.model.PermissionList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PermissionListMapper extends BaseSupportDao {
    int deleteByPrimaryKey(Integer permissionListId);

    int insert(PermissionList record);

    int insertSelective(PermissionList record);

    List<PermissionList> query(Map<String, Object> parameters);

    int count(Map<String, Object> parameters);

    int updateStateByPrimaryKey(Map<String, Object> map);

    PermissionList getByCode(Map<String, Object> map);

    int delByCode(Map<String, Object> map);

    void insertBatch(List<PermissionList> list);

    PermissionList selectByPrimaryKey(Integer permissionListId);

    int updateByPrimaryKeySelective(PermissionList record);

    int updateByPrimaryKey(PermissionList record);
}