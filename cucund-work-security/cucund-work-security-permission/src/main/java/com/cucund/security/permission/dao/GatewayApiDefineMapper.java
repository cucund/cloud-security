package com.cucund.security.permission.dao;

import com.cucund.security.mybatis.BaseSupportDao;
import com.cucund.security.permission.model.GatewayApiDefine;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface GatewayApiDefineMapper extends BaseSupportDao{
    int deleteByPrimaryKey(String id);

    int insert(GatewayApiDefine record);

    int insertSelective(GatewayApiDefine record);

    List<GatewayApiDefine> query(Map<String, Object> parameters);

    int count(Map<String, Object> parameters);

    int updateStateByPrimaryKey(Map<String, Object> map);

    GatewayApiDefine getByCode(Map<String, Object> map);

    int delByCode(Map<String, Object> map);

    void insertBatch(List<GatewayApiDefine> list);

    GatewayApiDefine selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(GatewayApiDefine record);

    int updateByPrimaryKey(GatewayApiDefine record);
}