/*
 * Copyright 2013 netfinworks.com, Inc. All rights reserved.
 */
package com.cucund.security.common.plugin;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

/**
 * <p></p>
 *
 * @author zhangjiewen
 * @version $Id: DaoClassRenamePlugin.java, v 0.1 13-11-27 上午10:57 zhangjiewen Exp $
 */
public class MapperPackageRenamePlugin extends PluginAdapter {
    private String searchString;
    private String replaceString;
    @Override
    public boolean validate(List<String> warnings) {
        searchString = properties.getProperty("searchString"); //$NON-NLS-1$
        replaceString = properties.getProperty("replaceString"); //$NON-NLS-1$

        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
//        System.out.println("introspectedTable:"+introspectedTable.getDAOImplementationType());
//        System.out.println("introspectedTable:"+introspectedTable.getMyBatis3JavaMapperType());
        String oldType = introspectedTable.getMyBatis3JavaMapperType();
        System.out.println("oldType:"+oldType);
        oldType = oldType.replaceAll(searchString,replaceString);
        System.out.println("oldType:" + oldType);
        introspectedTable.setMyBatis3JavaMapperType(oldType);
    }

    public static void  main(String... args){
        String oldType = "TransactionLogDOMapper";
                System.out.println("oldType:"+oldType);
        Pattern pattern = Pattern.compile("DOMapper$");
        Matcher matcher = pattern.matcher(oldType);
        oldType = matcher.replaceAll("DAO");
        System.out.println("oldType:"+oldType);
    }
}
