/*
 * Copyright 2013 netfinworks.com, Inc. All rights reserved.
 */
package com.cucund.security.common.plugin;

import java.sql.Types;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.logging.Log;
import org.mybatis.generator.logging.LogFactory;

/**
 * <p></p>
 *
 * @author zhangjiewen
 * @version $Id: MoneyResolverImpl.java, v 0.1 13-12-12 上午11:10 zhangjiewen Exp $
 */
public class MoneyResolverImpl extends JavaTypeResolverDefaultImpl {
    private Log log = LogFactory.getLog(this.getClass());
    protected boolean forceMoney;
    protected String moneyColumnNamePattern;
    private Pattern pattern = null;
    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
        forceMoney = StringUtility
                .isTrue(properties
                        .getProperty(ExtendPropertyRegistry.TYPE_RESOLVER_FORCE_MONEY));
        moneyColumnNamePattern = properties
                .getProperty(ExtendPropertyRegistry.TYPE_RESOLVER_MONEY_COLUMN_NAME_PATTERN);
        if(moneyColumnNamePattern != null){
            pattern = Pattern.compile(moneyColumnNamePattern);
        }
        log.debug("MoneyResolver,forceMoney:"+forceMoney);
        log.debug("MoneyResolver,moneyColumnNamePattern:" + moneyColumnNamePattern);
    }
    //money精度15,2
    @Override
    public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType answer = super.calculateJavaType(introspectedColumn);
        //System.out.println("jdbcType:"+introspectedColumn.getJdbcType() );
        //System.out.println("scale:"+introspectedColumn.getScale() );
        //System.out.println("length:"+introspectedColumn.getLength() );
        if((Types.NUMERIC == introspectedColumn.getJdbcType()
            ||Types.DECIMAL == introspectedColumn.getJdbcType())&&
                introspectedColumn.getScale() >= 2
                && introspectedColumn.getLength() >= 15
                && forceMoney){
            if(pattern != null){
                Matcher matcher = pattern.matcher(introspectedColumn.getActualColumnName());
                if(matcher.find()){
//                    answer = new FullyQualifiedJavaType(Money.class
//                            .getName());
                }
            }else{
//                answer = new FullyQualifiedJavaType(Money.class
//                        .getName());
            }
        }
        log.debug("answer:" + answer);
        return answer;
    }
}
