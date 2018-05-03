/*
 * Copyright 2013 netfinworks.com, Inc. All rights reserved.
 */
package com.cucund.security.common.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.logging.Log;
import org.mybatis.generator.logging.LogFactory;


/**
 * <p></p>
 *
 * @author zhangjiewen
 * @version $Id: DomainClassPlugin.java, v 0.1 13-12-12 下午3:36 zhangjiewen Exp $
 */
public class DomainClassPlugin extends PluginAdapter {
    private static final String ALL = "*";
    private static final String COMMA = "[,]";
    private static final String DOMAIN_SUFFIX = "Domain";
    private static final String CONVERTER_SUFFIX = "Converter";
    private Log log = LogFactory.getLog(this.getClass());
    protected List<String> tableNames = new ArrayList<String>();
    protected List<String> defaultExcludeColumns = new ArrayList<String>();
    protected String defaultPackage;

    private boolean allTables = false;
    private String rootClass = null;
    private String rootInterface = null;
    private List<DomainConfig> domainConfigs = new ArrayList<DomainConfig>();
    private boolean generateConverter = false;
    private String outputDir;
    private FtlTranslationService ftlTranslationService = new FtlTranslationService();
    private Set<String> imports = new HashSet<String>();
    @Override
    public boolean validate(List<String> warnings) {
        String domainTables = properties.getProperty(ExtendPropertyRegistry.PLUGIN_DOMAIN_TABLE_NAMES);
        if (!StringUtility.stringHasValue(domainTables)) {
            log.error("property tableNames can not be null.");
            return false;
        }
        if (ALL.equals(domainTables)) {
            allTables = true;
        } else {
            List<String> tableNameDefs = Arrays.asList(domainTables.toUpperCase().split(COMMA));
            for(String tableName:tableNameDefs){
                tableNames.add(tableName.trim());
            }
        }
        String defaultExcludeColumn = properties.getProperty(ExtendPropertyRegistry.PLUGIN_DOMAIN_DFLT_EXC_FIELDS);
        if(StringUtility.stringHasValue(defaultExcludeColumn)){
            defaultExcludeColumns = Arrays.asList(defaultExcludeColumn.split(COMMA));
        }
        String defaultPkg = properties.getProperty(ExtendPropertyRegistry.PLUGIN_DOMAIN_DEFAULT_PKG);
        if (!StringUtility.stringHasValue(defaultPkg)) {
            log.error("property defaultPackage can not be null.");
            return false;
        }
        String output = properties.getProperty(ExtendPropertyRegistry.PLUGIN_DOMAIN_OUTPUT_DIR);
        if(!StringUtility.stringHasValue(output)){
            log.error("property outputDir can not be null.");
            return false;
        }
        this.outputDir = output;
        defaultPackage = defaultPkg;
        String rootClass = properties.getProperty(ExtendPropertyRegistry.PLUGIN_DOMAIN_ROOT_CLASS);
        if(StringUtility.stringHasValue(rootClass)){
            this.rootClass = rootClass;
        }
        String rootInterface = properties.getProperty(ExtendPropertyRegistry.PLUGIN_DOMAIN_ROOT_INTERFACE);
        if(StringUtility.stringHasValue(rootInterface)){
            this.rootInterface = rootInterface;
        }

        String importConfig = properties.getProperty(ExtendPropertyRegistry.PLUGIN_DOMAIN_IMPORTS);
        if(StringUtility.stringHasValue(importConfig)){
            String[] importsArray = importConfig.split(COMMA);
            for(String importsStr : importsArray){
                this.imports.add(importsStr);
            }
        }
        String gconverter = properties.getProperty(ExtendPropertyRegistry.PLUGIN_DOMAIN_GENERATE_CONVERTER);
        generateConverter = StringUtility.isTrue(gconverter);
        try {
            ftlTranslationService.init();
        } catch (Exception e) {
            log.error("初始化模板失败." + e.getMessage());
        }
        return true;
    }
    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String remark = introspectedColumn.getRemarks();
        if(remark != null && remark.trim().length() > 0 && !"null".equals(remark)&&field.getJavaDocLines().isEmpty()){
            field.addJavaDocLine("/**");
            field.addJavaDocLine(" * "+remark.replaceAll("\r|\n|\r\n", ""));
            field.addJavaDocLine(" */");
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
    //&& !defaultExcludeColumns.contains(introspectedTable.getTableConfiguration().getTableName().toUpperCase())
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        try{
            if ((allTables || tableNames.contains(introspectedTable.getTableConfiguration().getTableName().toUpperCase()))
                    ) {
                DomainConfig domainConfig = generateDomainConfig(introspectedTable,topLevelClass);
                domainConfigs.add(domainConfig);
                //生成converter
                if(generateConverter){
                	
                }
                Map<String,Object> data = new HashMap<String, Object>();
                data.put("domainConfig",domainConfig);
                try {
                    ftlTranslationService.translate(data,"domain.ftl",
                            this.outputDir + "/" + domainConfig.getFilePath());
                    List<String> imports = domainConfig.getImports();
                    imports.remove("com.yqbsoft.laser.service.esb.core.transformer.BaseDomain");
                    if(imports.indexOf("java.util.Date") < 0) imports.add("java.util.Date");
                    domainConfig.setSuperClass(domainConfig.getDomainClass());
                    domainConfig.setDomainClass(domainConfig.getDomainClass().replace("Domain", "ReDomain"));
                    ftlTranslationService.translate(data,"redomain.ftl",
                            this.outputDir + "/" + domainConfig.getFilePath());
                    
                } catch (Exception e) {
                    log.error("处理模板失败." + e.getMessage());
                }
                List<DomainColumnConfig> domainColumnConfigs = domainConfig.getDomainColumnConfigs();
                for(DomainColumnConfig domainColumnConfig : domainColumnConfigs){
                    if(domainColumnConfig.getType()!=null){
                        if(domainColumnConfig.getType().equals("enum")){
                            File enumFilePath = new File(this.outputDir + "/" + domainColumnConfig.getFilePath());
                            if(!enumFilePath.exists()) {
                                Map<String, Object> columnData = new HashMap<String, Object>();
                                columnData.put("domainColumnConfig", domainColumnConfig);
                                try {
                                    ftlTranslationService.translate(columnData, "enum.ftl",
                                            this.outputDir + "/" + domainColumnConfig.getFilePath());
                                } catch (Exception e) {
                                    log.error("处理模板失败." + e.getMessage());
                                }
                            }
                        }
                    }
                }
            }else{
                log.debug(introspectedTable.getTableConfiguration().getTableName().toUpperCase()+"不需要生成domain对象");
        }
        } catch (Exception e) {
            log.error("处理Domain失败." + e.getMessage());
        }
        return true;
    }

    private DomainConfig generateDomainConfig(IntrospectedTable introspectedTable,TopLevelClass topLevelClass){
        DomainConfig domainConfig = null;
        //查找context中table的配置
        TableConfiguration tableConfiguration = getTableConfig(introspectedTable.getFullyQualifiedTable().getIntrospectedTableName());
        String tableDomainPkg = getTableProperty(tableConfiguration, ExtendPropertyRegistry.PLUGIN_DOMAIN_PACKAGE, defaultPackage);
        String className = getTableProperty(tableConfiguration,ExtendPropertyRegistry.PLUGIN_DOMAIN_CLASS_NAME,topLevelClass.getType().getShortName() + DOMAIN_SUFFIX);
        String subPkg = getTableProperty(tableConfiguration, ExtendPropertyRegistry.PLUGIN_DOMAIN_SUBPACKAGE, null);
        String domainExcludeColumn = getTableProperty(tableConfiguration,ExtendPropertyRegistry.PLUGIN_DOMAIN_EXC_FIELDS,null);
        String superClass = getTableProperty(tableConfiguration,ExtendPropertyRegistry.PLUGIN_DOMAIN_SUPER_CLASS,rootClass);
        String superInterface = getTableProperty(tableConfiguration,ExtendPropertyRegistry.PLUGIN_DOMAIN_INTERFACE,rootInterface);
        String codeColumn = getTableProperty(tableConfiguration, ExtendPropertyRegistry.PLUGIN_DOMAIN_CODECOLUMN, null);
        domainConfig = new DomainConfig();
        domainConfig.setDomainClass(className);
        domainConfig.setDomainPkg(tableDomainPkg);
        domainConfig.setDomainSubPkg(subPkg);
        domainConfig.setDoClass(topLevelClass);
        domainConfig.setCodeColumn(codeColumn);
        List<String> excludeFields = generateExcludeDomainColumn(domainExcludeColumn);
        domainConfig.setExcludeDomainColumn(excludeFields);
        domainConfig.setInterfaceClass(superInterface);
        domainConfig.setSuperClass(superClass);
        List<String> importsSet = new ArrayList<String>();
        importsSet.addAll(this.imports);
        if("Serializable".equals(superInterface)&&!importsSet.contains("java.io.Serializable")){
        	importsSet.add("java.io.Serializable");
        }
        if("BaseDomain".equals(superClass)&&!superClass.contains("com.yqbsoft.laser.service.esb.core.transformer.BaseDomain")){
        	importsSet.add("com.yqbsoft.laser.service.esb.core.transformer.BaseDomain");
        }
        List<DomainColumnConfig> domainColumnConfigMap = new ArrayList<DomainColumnConfig>();
        for(Field field : topLevelClass.getFields()){
            if(excludeFields.contains(field.getName())){
                log.debug(field.getName() + "被忽略.");
                continue;
            }
            DomainColumnConfig domainColumnConfig = generateDomainColumnConfig(tableConfiguration,field,domainConfig);
            domainColumnConfigMap.add(domainColumnConfig);
            String typeClass = domainColumnConfig.getTypeClass();
            if(!typeClass.startsWith("java.lang.") && !importsSet.contains(typeClass)){
                importsSet.add(typeClass);
            }
        }
        domainConfig.setImports(importsSet);
        domainConfig.setDomainColumnConfigs(domainColumnConfigMap);
        String converterClass = getTableProperty(tableConfiguration,ExtendPropertyRegistry.PLUGIN_DOMAIN_CONVERTER,topLevelClass.getType().getShortName() + CONVERTER_SUFFIX);
        domainConfig.setConverter(converterClass);
        return domainConfig;
    }
      
    private DomainColumnConfig generateDomainColumnConfig(TableConfiguration tableConfiguration,Field field,DomainConfig domainConfig){
        DomainColumnConfig domainColumnConfig = new DomainColumnConfig();
        domainColumnConfig.setDoFeild(field);
        String fieldName = field.getName();
        domainColumnConfig.setName(fieldName);
        String typeClass = null;
        //查找XXX.type属性，如果没有，使用默认，如果有根据属性值生成column对应的属性
        String type = getTableProperty(tableConfiguration,fieldName
                +"."+ExtendPropertyRegistry.PLUGIN_DOMAIN_COLUMN_TYPE,null);
        if(type != null){
            if(type.startsWith("enum:")){
                String enumName = type.substring("enum:".length());
                if(enumName.trim().length() == 0){
                    String enumPkg = domainConfig.getDomainPkg();
                    if(domainConfig.getDomainSubPkg() != null){
                        enumPkg = enumPkg + "." + domainConfig.getDomainSubPkg();
                    }
                    String enumClassName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                    enumName = enumPkg + "."+enumClassName + "Kind";
                }
                domainColumnConfig.setTypeClass(enumName);
                domainColumnConfig.setType("enum");
                String simpleTypeClass = enumName;
                String pkg = "";
                if (enumName.indexOf(".") != -1) {
                    simpleTypeClass = enumName.substring(enumName.lastIndexOf(".") + 1);
                    pkg = enumName.substring(0,enumName.lastIndexOf("."));
                }
                domainColumnConfig.setPkg(pkg);
                domainColumnConfig.setSimpleTypeClass(simpleTypeClass);
                typeClass = simpleTypeClass;
                //查找XXX.enumDef属性，如果有根据属性值生成enum的枚举,
                //逗号分隔作为列分隔符，第一列为属性名称，后面每一列是属性的值,分号作为枚举的分隔
                //XXX:code:msg,XXX:code1:msg1,
                String enumDef = getTableProperty(tableConfiguration,field.getName()
                        +"."+ExtendPropertyRegistry.PLUGIN_DOMAIN_COLUMN_ENUMDEF,null);
                domainColumnConfig.setEnumDef(generateEnumDef(enumDef));
            }else if(type.startsWith("ref:")){
                String enumName = type.substring(type.indexOf("ref:"));
                domainColumnConfig.setTypeClass(enumName);
                domainColumnConfig.setType("ref");
                String simpleTypeClass = enumName;
                if (simpleTypeClass.indexOf(".") != -1) {
                    simpleTypeClass = simpleTypeClass.substring(simpleTypeClass.indexOf(".") + 1);
                }
                domainColumnConfig.setSimpleTypeClass(simpleTypeClass);
                typeClass = simpleTypeClass;
            }
        }else{
            domainColumnConfig.setType(null);
            typeClass = field.getType().getFullyQualifiedName();
            domainColumnConfig.setTypeClass(typeClass);
            String simpleTypeClass = field.getType().getShortName();
            domainColumnConfig.setSimpleTypeClass(simpleTypeClass);
            domainColumnConfig.setJavaDocLines(field.getJavaDocLines());
            domainColumnConfig.setGetterName(getGetterMethodName(field.getName(),typeClass));
            domainColumnConfig.setSetterName(getSetterMethodName(field.getName()));
        }
        domainColumnConfig.setGetterName(getGetterMethodName(field.getName(),typeClass));
        domainColumnConfig.setSetterName(getSetterMethodName(field.getName()));
        return domainColumnConfig;
    }

    public EnumDef generateEnumDef(String enumDefStr){
        if(enumDefStr == null || enumDefStr.trim().length() == 0){
            return null;
        }
        EnumDef enumDef = new EnumDef();
        String[] columns = enumDefStr.split("[,]");
        if(columns.length < 2){
            return null;
        }
        enumDef.setAttributes(Arrays.asList(columns[0].split("[:]")));
        List<List<String>> attributeValues = new ArrayList<List<String>>();
        for(int i = 1;i < columns.length;i++){
            String column = columns[i];
            attributeValues.add(Arrays.asList(column.split("[:]")));
        }
        enumDef.setAttributeValues(attributeValues);
        return enumDef;
    }

    public String getGetterMethodName(String property,
                                             String type) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }

        if (type.equals("boolean")) {
            sb.insert(0, "is"); //$NON-NLS-1$
        } else {
            sb.insert(0, "get"); //$NON-NLS-1$
        }

        return sb.toString();
    }

    public String getSetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }

        sb.insert(0, "set"); //$NON-NLS-1$

        return sb.toString();
    }
    private List<String> generateExcludeDomainColumn(String domainExcludeColumn){
        List<String> excludeColumnList = new ArrayList<String>();
        if(StringUtility.stringHasValue(domainExcludeColumn)){
            excludeColumnList.addAll(Arrays.asList(domainExcludeColumn.split(COMMA)));
        }
        excludeColumnList.addAll(defaultExcludeColumns);
        List<String> ret = new ArrayList<String>();
        for(String c : excludeColumnList){
            if(!ret.contains(c)){
                ret.add(c);
            }
        }
        return ret;
    }

//    @Override
//    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
//        List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();
//        for(DomainConfig domainConfig : domainConfigs){
//            GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile();
//        }
//        return answer;
//    }

    private String getTableProperty(TableConfiguration tableConfiguration,String name,String defaultValue){
        String value = tableConfiguration.getProperty(name);
        if(!StringUtility.stringHasValue(value)){
            return defaultValue;
        }
        return value;
    }

//    private GeneratedJavaFile generateDomain(IntrospectedTable introspectedTable) {
//        //查找context中table的配置
//        //<property name="domainClass" value="pkg.name" />
//
//        String pkg = null;
//        String name = null;
//        //查找context中table的配置
//        TableConfiguration tableConfiguration = getTableConfig(introspectedTable.getFullyQualifiedTable().getIntrospectedTableName());
//        //查找domainClass属性，如果没有，跳过
//        TopLevelClass clazz = new TopLevelClass(pkg + "." + name);
//        GeneratedJavaFile gjf = new GeneratedJavaFile(clazz,
//                context.getJavaModelGeneratorConfiguration()
//                        .getTargetProject(),
//                context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
//                context.getJavaFormatter());
//        List<IntrospectedColumn> columns = introspectedTable.getBaseColumns();
//        for (IntrospectedColumn column : columns) {
//            generateDomainColumn(introspectedTable,column);
//        }
//        return gjf;
//    }
//
//    private void generateDomainColumn(IntrospectedTable introspectedTable,IntrospectedColumn column){
//        //<property name="domainColumn.aaaa" value="type:{class,enum},name,pkg" />
//        //<property name="excludeDomainColumn" value="id,gmt_create,gmt_modified" />
//        //查找excludeDomainColumn属性，如果存在，那么判断是否需要排除，如果排除返回
//        //查找domainColumn.aaaa属性，如果没有，使用默认，如果有根据属性值生成column对应的属性
//        //查找
//    }

    private TableConfiguration getTableConfig(String tableName){
        Context context = this.getContext();
        List<TableConfiguration> tableConfigurations = context.getTableConfigurations();
        for(TableConfiguration tableConfiguration : tableConfigurations){
            if(tableName.equalsIgnoreCase(tableConfiguration.getTableName())){
                return tableConfiguration;
            }
        }
        return null;
    }

    public static class DomainConfig{
    	private String codeColumn;//业务唯一字段名
        private String domainPkg;
        private String domainSubPkg;
        private String domainClass;
        private String converter;
        private List<String> excludeDomainColumn;
        private List<DomainColumnConfig> domainColumnConfigs;
        private TopLevelClass doClass;
        private String interfaceClass;
        private String superClass;
        private List<String> imports;

        public String getCodeColumn() {
			return codeColumn;
		}

		public void setCodeColumn(String codeColumn) {
			this.codeColumn = codeColumn;
		}

		public List<String> getImports() {
            return imports;
        }

        public void setImports(List<String> imports) {
            this.imports = imports;
        }

        public String getDomainPkg() {
            return domainPkg;
        }

        public void setDomainPkg(String domainPkg) {
            this.domainPkg = domainPkg;
        }

        public String getInterfaceClass() {
            return interfaceClass;
        }

        public void setInterfaceClass(String interfaceClass) {
            this.interfaceClass = interfaceClass;
        }

        public String getSuperClass() {
            return superClass;
        }

        public void setSuperClass(String superClass) {
            this.superClass = superClass;
        }

        public TopLevelClass getDoClass() {
            return doClass;
        }

        public void setDoClass(TopLevelClass doClass) {
            this.doClass = doClass;
        }

        public String getDomainSubPkg() {
            return domainSubPkg;
        }

        public void setDomainSubPkg(String domainSubPkg) {
            this.domainSubPkg = domainSubPkg;
        }

        public String getDomainClass() {
            return domainClass;
        }

        public void setDomainClass(String domainClass) {
            this.domainClass = domainClass;
        }

        public String getConverter() {
            return converter;
        }

        public void setConverter(String converter) {
            this.converter = converter;
        }

        public List<String> getExcludeDomainColumn() {
            return excludeDomainColumn;
        }

        public void setExcludeDomainColumn(List<String> excludeDomainColumn) {
            this.excludeDomainColumn = excludeDomainColumn;
        }

        public List<DomainColumnConfig> getDomainColumnConfigs() {
            return domainColumnConfigs;
        }

        public void setDomainColumnConfigs(List<DomainColumnConfig> domainColumnConfigs) {
            this.domainColumnConfigs = domainColumnConfigs;
        }
        public String getFilePath(){
            StringBuilder s = new StringBuilder();
            s.append(domainPkg.replaceAll("[.]","/"));
            if(domainSubPkg != null && domainSubPkg.trim().length() > 0){
                s.append("/");
                s.append(domainSubPkg);
            }
            s.append("/").append(domainClass).append(".java");
            return s.toString();
        }
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

    public static class DomainColumnConfig{
        private String name;
        private String typeClass;
        private String type;//enum,refClass
        private Field doFeild;
        private String simpleTypeClass;
        private String getterName;
        private String setterName;
        private String pkg;
        private EnumDef enumDef;
        private List<String> imports;
        private List<String> javaDocLines;
        public List<String> getJavaDocLines() {
			return javaDocLines;
		}

		public void setJavaDocLines(List<String> javaDocLines) {
			this.javaDocLines = javaDocLines;
		}

		public List<String> getImports() {
            return imports;
        }

        public void setImports(List<String> imports) {
            this.imports = imports;
        }

        public EnumDef getEnumDef() {
            return enumDef;
        }

        public void setEnumDef(EnumDef enumDef) {
            this.enumDef = enumDef;
        }

        public String getPkg() {
            return pkg;
        }

        public void setPkg(String pkg) {
            this.pkg = pkg;
        }

        public String getGetterName() {
            return getterName;
        }

        public void setGetterName(String getterName) {
            this.getterName = getterName;
        }

        public String getSetterName() {
            return setterName;
        }

        public void setSetterName(String setterName) {
            this.setterName = setterName;
        }

        public String getSimpleTypeClass() {
            return simpleTypeClass;
        }

        public void setSimpleTypeClass(String simpleTypeClass) {
            this.simpleTypeClass = simpleTypeClass;
        }

        public Field getDoFeild() {
            return doFeild;
        }

        public void setDoFeild(Field doFeild) {
            this.doFeild = doFeild;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTypeClass() {
            return typeClass;
        }

        public void setTypeClass(String typeClass) {
            this.typeClass = typeClass;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
        public String getFilePath(){
            StringBuilder s = new StringBuilder();
            s.append(pkg.replaceAll("[.]","/"));
            s.append("/").append(simpleTypeClass).append(".java");
            return s.toString();
        }
    }
    public static class EnumDef{
        private List<String> attributes;
        private List<List<String>> attributeValues;

        public List<String> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<String> attributes) {
            this.attributes = attributes;
        }

        public List<List<String>> getAttributeValues() {
            return attributeValues;
        }

        public void setAttributeValues(List<List<String>> attributeValues) {
            this.attributeValues = attributeValues;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }
}
