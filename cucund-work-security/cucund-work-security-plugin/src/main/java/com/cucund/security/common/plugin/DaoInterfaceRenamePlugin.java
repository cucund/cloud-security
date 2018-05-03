/*
 * Copyright 2013 netfinworks.com, Inc. All rights reserved.
 */
package com.cucund.security.common.plugin;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

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
public class DaoInterfaceRenamePlugin extends PluginAdapter {
    private String searchString;
    private String replaceString;
    private Pattern pattern;
    @Override
    public boolean validate(List<String> warnings) {
        searchString = properties.getProperty("searchString"); //$NON-NLS-1$
        replaceString = properties.getProperty("replaceString"); //$NON-NLS-1$

        boolean valid = stringHasValue(searchString)
                && stringHasValue(replaceString);

        if (valid) {
            pattern = Pattern.compile(searchString);
        } else {
            if (!stringHasValue(searchString)) {
                warnings.add(getString("ValidationError.18", //$NON-NLS-1$
                        "RenameExampleClassPlugin", //$NON-NLS-1$
                        "searchString")); //$NON-NLS-1$
            }
            if (!stringHasValue(replaceString)) {
                warnings.add(getString("ValidationError.18", //$NON-NLS-1$
                        "RenameExampleClassPlugin", //$NON-NLS-1$
                        "replaceString")); //$NON-NLS-1$
            }
        }

        return valid;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
//        System.out.println("introspectedTable:"+introspectedTable.getDAOImplementationType());
//        System.out.println("introspectedTable:"+introspectedTable.getMyBatis3JavaMapperType());
        String oldType = introspectedTable.getMyBatis3JavaMapperType();
        System.out.println("oldType:"+oldType);
        Matcher matcher = pattern.matcher(oldType);
        oldType = matcher.replaceAll(replaceString);
        System.out.println("oldType:"+oldType);
        //introspectedTable.setMyBatis3FallbackSqlMapNamespace(oldType);
        introspectedTable.setMyBatis3JavaMapperType(oldType);
    }

    public static void  main(String... args){
        String oldType = "TransactionLogDOMapper";
        System.out.println("oldType:"+oldType);
        Pattern pattern = Pattern.compile("DOMapper$");
        Matcher matcher = pattern.matcher(oldType);
        System.out.println(matcher.find());
        oldType = matcher.replaceAll("DAO");
        System.out.println(matcher.find());
        System.out.println("oldType:"+oldType);

    }
}
