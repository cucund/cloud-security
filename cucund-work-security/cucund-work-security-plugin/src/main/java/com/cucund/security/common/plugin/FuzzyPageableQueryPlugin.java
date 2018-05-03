/*
 * Copyright 2013 netfinworks.com, Inc. All rights reserved.
 */
package com.cucund.security.common.plugin;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.logging.Log;
import org.mybatis.generator.logging.LogFactory;

import com.cucund.security.common.utils.StringUtils;

/**
 * <p></p>
 *
 * @author zhangjiewen
 * @version $Id: FuzzyPageableQueryPlugin.java, v 0.1 13-12-13 上午10:06 zhangjiewen Exp $
 */
public class FuzzyPageableQueryPlugin extends PluginAdapter {
    private Log logger = LogFactory.getLog(this.getClass());
    private static final String fuzzyColumnSql = " AND (%1$s like concat('%%',#{%2$s},'%%')) ";
    private static final String fuzzyStartDateColumnSql = " AND (%1$s &gt;= #{%2$s}) ";
    private static final String fuzzyendDateColumnSql = " AND (%1$s &lt;= #{%2$s}) ";
    private static final String normalColumnSql = "AND (%1$s = #{%2$s})";
    private static final String testSqlVarchar = "%1$s != null and %1$s.trim().length() != 0";
    private static final String testSql = "%1$s != null";
    private static final String fuzzyQueryId = "%1$s_query_fuzzy_condition";
    private static final String normalQueryId = "%1$s_query_condition";
    private FullyQualifiedJavaType implparameterType = FullyQualifiedJavaType.getNewMapInstance();
    private FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType("java.util.Map<String,Object>");
    private FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
    protected String paginationStartId = "PAGINATION.MYSQL_paginationStart";
    protected String paginationEndId = "PAGINATION.MYSQL_paginationEnd";
    protected String paginationSysDateId = "PAGINATION.MYSQL_selectSysDate";
    protected boolean isFuzzy = false;
    protected boolean needOrderby = false;
    protected String orderbyColumns = "GMT_CREATE desc";
    private List<XmlElement> xmlElementsToAdd;
    @Override
    public boolean validate(List<String> warnings) {
        if (StringUtility.stringHasValue(properties.getProperty(ExtendPropertyRegistry.PLUGIN_QUERY_PAGEABLE_START))) {
            paginationStartId = properties.getProperty(ExtendPropertyRegistry.PLUGIN_QUERY_PAGEABLE_START);
        }
        if (StringUtility.stringHasValue(properties.getProperty(ExtendPropertyRegistry.PLUGIN_QUERY_PAGEABLE_END))) {
            paginationEndId = properties.getProperty(ExtendPropertyRegistry.PLUGIN_QUERY_PAGEABLE_END);
        }
        if (StringUtility.stringHasValue(properties.getProperty(ExtendPropertyRegistry.PLUGIN_QUERY_FUZZY))) {
            isFuzzy = StringUtility.isTrue(properties.getProperty(ExtendPropertyRegistry.PLUGIN_QUERY_FUZZY));
        }
        if (StringUtility.stringHasValue(properties.getProperty(ExtendPropertyRegistry.PLUGIN_QUERY_ORDERBY_COLUMNS))) {
            needOrderby = true;
        }
        if (StringUtility.stringHasValue(properties.getProperty(ExtendPropertyRegistry.PLUGIN_QUERY_ORDERBY_COLUMNS))) {
            orderbyColumns = properties.getProperty(ExtendPropertyRegistry.PLUGIN_QUERY_ORDERBY_COLUMNS);
        }
        if (StringUtility.stringHasValue(properties.getProperty(ExtendPropertyRegistry.PLUGIN_SELECT_SYS_DATE))) {
        	paginationSysDateId = properties.getProperty(ExtendPropertyRegistry.PLUGIN_SELECT_SYS_DATE);
        }
        
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document,
                                           IntrospectedTable introspectedTable) {
        List<XmlElement> elements = this.xmlElementsToAdd;
        logger.debug("elements:"+elements);
        if (elements != null) {
            for (XmlElement element : elements) {
                document.getRootElement().addElement(element);
            }
        }

        return true;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            copyAndAddMethod(method, interfaze,introspectedTable);
        }
        return true;
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element,
                                                            IntrospectedTable introspectedTable) {
        if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
            try{
                this.xmlElementsToAdd = copyAndSaveElement(introspectedTable);
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
        return true;
    }

    private void copyAndAddMethod(Method method, Interface interfaze,IntrospectedTable introspectedTable) {
        Method newMethod = new Method("query");
        newMethod.addParameter(new Parameter(parameterType, "parameters"));
        interfaze.addImportedType(implparameterType);
        newMethod.setReturnType(new FullyQualifiedJavaType("java.util.List<"+introspectedTable.getFullyQualifiedTable().getDomainObjectName()+">"));
        interfaze.addImportedType(returnType);
        interfaze.addMethod(newMethod);
        Method countMethod = new Method("count");
        countMethod.addParameter(new Parameter(parameterType,"parameters"));
        countMethod.setReturnType(new FullyQualifiedJavaType("int"));
        interfaze.addMethod(countMethod);
        
//        Method getdataMethod = new Method("selectSysDate");
//        getdataMethod.setReturnType(FullyQualifiedJavaType.getDateInstance());
//        interfaze.addMethod(getdataMethod);
//        interfaze.addImportedType(FullyQualifiedJavaType.getDateInstance());
        Method sdataMethod = new Method("updateStateByPrimaryKey");
        sdataMethod.addParameter(new Parameter(parameterType,"map"));
        sdataMethod.setReturnType(new FullyQualifiedJavaType("int"));
        interfaze.addMethod(sdataMethod);
        if(!"".equals(introspectedTable.getTableConfigurationProperty(ExtendPropertyRegistry.PLUGIN_DOMAIN_CODECOLUMN))){
	        Method getMethod = new Method("getByCode");
	        getMethod.addParameter(new Parameter(parameterType,"map"));
	        getMethod.setReturnType(new FullyQualifiedJavaType(introspectedTable.getFullyQualifiedTable().getDomainObjectName()));
	        interfaze.addMethod(getMethod);
	        Method delMethod = new Method("delByCode");
	        delMethod.addParameter(new Parameter(parameterType,"map"));
	        delMethod.setReturnType(new FullyQualifiedJavaType("int"));
	        interfaze.addMethod(delMethod);
        }
        //batch insert
        Method insertBachMethod = new Method("insertBatch");
        insertBachMethod.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.List<"+introspectedTable.getFullyQualifiedTable().getDomainObjectName()+">"),"list"));
        interfaze.addMethod(insertBachMethod);
        
//        Set<FullyQualifiedJavaType> importedTypes=new HashSet<FullyQualifiedJavaType>();
//        importedTypes.add(new FullyQualifiedJavaType("com.yqbsoft.laser.service.mybatis.BaseSupportDao"));
//        interfaze.addImportedTypes(importedTypes);
//        if(!interfaze.getSuperInterfaceTypes().isEmpty()){
//        	interfaze.getSuperInterfaceTypes().clear();
//        	interfaze.getSuperInterfaceTypes().add(new FullyQualifiedJavaType("com.yqbsoft.laser.service.mybatis.BaseSupportDao"));
//        }
    }

    private XmlElement generateTrim() {
        XmlElement ret = new XmlElement("trim");
        ret.addAttribute(new Attribute("prefix", "WHERE"));
        ret.addAttribute(new Attribute("prefixOverrides", "AND |OR "));
        return ret;
    }

    private XmlElement generateCondition(IntrospectedTable introspectedTable, boolean fuzzy) {
        XmlElement queryCondition = new XmlElement("sql");
        if (fuzzy) {
            queryCondition.addAttribute(new Attribute("id", String.format(fuzzyQueryId,
                    introspectedTable.getFullyQualifiedTable().getIntrospectedTableName())));
        } else {
            queryCondition.addAttribute(new Attribute("id", String.format(normalQueryId,
                    introspectedTable.getFullyQualifiedTable().getIntrospectedTableName())));
        }
        XmlElement trimElement = generateTrim();
        //遍历varchar字段
        List<IntrospectedColumn> columns = introspectedTable.getBaseColumns();
        for (IntrospectedColumn column : columns) {
        	if(column.getActualColumnName().equals("GMT_CREATE")){
        		XmlElement columnElement = new XmlElement("if");
        		columnElement.addAttribute(new Attribute("test", String.format(testSqlVarchar, "startDate")));
        			columnElement.addElement(new TextElement(String.format(fuzzyStartDateColumnSql, column.getActualColumnName(),
        					"startDate")));
        		 trimElement.addElement(columnElement);
        		columnElement = new XmlElement("if");
         		columnElement.addAttribute(new Attribute("test", String.format(testSqlVarchar, "endDate")));
         			 columnElement.addElement(new TextElement(String.format(fuzzyendDateColumnSql, column.getActualColumnName(), 
         					 "endDate")));
         		 trimElement.addElement(columnElement);
        	}else{
        		XmlElement columnElement = new XmlElement("if");
	            if (column.getJdbcType() == Types.VARCHAR) {
	//                System.out.println(column.getActualColumnName() + " jdbc type is "+column.getJdbcType()+",generate testSqlVarchar.");
	                columnElement.addAttribute(new Attribute("test", String.format(testSqlVarchar, column.getJavaProperty())));
	            }else{
	//                System.out.println(column.getActualColumnName() + " jdbc type is "+column.getJdbcType());
	                columnElement.addAttribute(new Attribute("test", String.format(testSql, column.getJavaProperty())));
	            }
	//            System.out.println(column.getActualColumnName() + " columnElement:"+columnElement.getFormattedContent(0));
	            if (column.getJdbcType() == Types.VARCHAR && fuzzy) {
	            	
	                columnElement.addElement(new TextElement(String.format(fuzzyColumnSql, column.getActualColumnName(),
	                        column.getJavaProperty())));
	            } else {
	                columnElement.addElement(new TextElement(String.format(normalColumnSql, column.getActualColumnName(), column.getJavaProperty())));
	            }
	//            System.out.println(column.getActualColumnName() + " columnElement:"+columnElement.getFormattedContent(0));
	            trimElement.addElement(columnElement);
        	}
        }
        queryCondition.addElement(trimElement);
        return queryCondition;
    }

    private List<XmlElement> copyAndSaveElement(IntrospectedTable introspectedTable) {
        List<XmlElement> xmlElementsToAdd = new ArrayList<XmlElement>();
        //模糊查询条件
        XmlElement fuzzyCondition = null;
        if (isFuzzy) {
            fuzzyCondition = generateCondition(introspectedTable, true);
            xmlElementsToAdd.add(fuzzyCondition);
        }
        //普通查询条件
        XmlElement normalCondition = generateCondition(introspectedTable, false);
        xmlElementsToAdd.add(normalCondition);
        //select语句
        XmlElement selectElement = generateSelectQuery(introspectedTable);
        xmlElementsToAdd.add(selectElement);
        //batchinsert语句
        XmlElement batchinsertElement = generateBatchinsert(introspectedTable);
        xmlElementsToAdd.add(batchinsertElement);
        //count语句
        xmlElementsToAdd.add(generateSelectCount(introspectedTable));
        //获取系统时间
        xmlElementsToAdd.add(generateSelectSysDate(introspectedTable));
        //通用状态更新
        xmlElementsToAdd.add(generateUpdateState(introspectedTable));
        
        String codeColumn = introspectedTable.getTableConfigurationProperty(ExtendPropertyRegistry.PLUGIN_DOMAIN_CODECOLUMN);
        if(StringUtils.isNotBlank(codeColumn)){
        	xmlElementsToAdd.add(generateSelectByCode(introspectedTable));
        	xmlElementsToAdd.add(generateDelByCode(introspectedTable));
        	
        }
        return xmlElementsToAdd;
    }

    
    private XmlElement generateSelectByCode(IntrospectedTable introspectedTable){
        XmlElement selectElement = new XmlElement("select");
        selectElement.addAttribute(new Attribute("id", "getByCode"));
        if(introspectedTable.getBLOBColumns() != null && !introspectedTable.getBLOBColumns().isEmpty()){
        	selectElement.addAttribute(new Attribute("resultMap", "ResultMapWithBLOBs"));
        }else{
        	selectElement.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        }
        //select
        TextElement selectText = new TextElement("select ");
        selectElement.addElement(selectText);
        //columnList
        XmlElement columnListElement = new XmlElement("include");
        columnListElement.addAttribute(new Attribute("refid","Base_Column_List"));
        selectElement.addElement(columnListElement);
        if(introspectedTable.getBLOBColumns() != null && !introspectedTable.getBLOBColumns().isEmpty()){
        	selectElement.addElement(new TextElement(","));
        	 XmlElement bcolumnListElement = new XmlElement("include");
        	 bcolumnListElement.addAttribute(new Attribute("refid","Blob_Column_List"));
             selectElement.addElement(bcolumnListElement);
        }
        //form table
        TextElement formText = new TextElement(" from " + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName() + " ");
        selectElement.addElement(formText);
        //where
        TextElement whereText = new TextElement(" where  "+introspectedTable.getTableConfigurationProperty(ExtendPropertyRegistry.PLUGIN_DOMAIN_CODECOLUMN)+" = #{"+getColumn(introspectedTable.getTableConfigurationProperty(ExtendPropertyRegistry.PLUGIN_DOMAIN_CODECOLUMN))+",jdbcType=VARCHAR} ");
        selectElement.addElement(whereText);
        
        XmlElement settestFuzzy = new XmlElement("if");
        settestFuzzy.addAttribute(new Attribute("test","tenantCode != null and tenantCode.trim().length() != 0"));
        TextElement setifText = new TextElement(" and TENANT_CODE = #{tenantCode,jdbcType=VARCHAR} " );
        settestFuzzy.addElement(setifText);
        selectElement.addElement(settestFuzzy);
        
        return selectElement;
    }
    public String getColumn(String tableName){
    	String[] tabs=tableName.split("_");
    	String codeColumn="";
    	for (int i = 0; i < tabs.length; i++) {
			tabs[i]=toUpperCaseFirstOne(tabs[i].toLowerCase());
			codeColumn+=tabs[i];
		}
    	return toLowerCaseFirstOne(codeColumn);
    }
  //首字母转大写
    public String toUpperCaseFirstOne(String s)
    {
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
  //首字母转小写
    public String toLowerCaseFirstOne(String s)
    {
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
    private XmlElement generateDelByCode(IntrospectedTable introspectedTable){
        XmlElement selectElement = new XmlElement("delete");
        selectElement.addAttribute(new Attribute("id", "delByCode"));
        selectElement.addAttribute(new Attribute("parameterType", "Map"));
        //form table
        TextElement formText = new TextElement(" delete from " + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName() + " ");
        selectElement.addElement(formText);
        //where
        TextElement whereText = new TextElement(" where  "+introspectedTable.getTableConfigurationProperty(ExtendPropertyRegistry.PLUGIN_DOMAIN_CODECOLUMN)+" = #{"+getColumn(introspectedTable.getTableConfigurationProperty(ExtendPropertyRegistry.PLUGIN_DOMAIN_CODECOLUMN))+",jdbcType=VARCHAR} ");
        selectElement.addElement(whereText);
        
        XmlElement settestFuzzy = new XmlElement("if");
        settestFuzzy.addAttribute(new Attribute("test","tenantCode != null and tenantCode.trim().length() != 0"));
        TextElement setifText = new TextElement(" and TENANT_CODE = #{tenantCode,jdbcType=VARCHAR} " );
        settestFuzzy.addElement(setifText);
        selectElement.addElement(settestFuzzy);
        
        return selectElement;
    }
    
    private XmlElement generateBatchinsert(IntrospectedTable introspectedTable){
    	  XmlElement insertElement = new XmlElement("insert");
    	  insertElement.addAttribute(new Attribute("id", "insertBatch"));
    	  insertElement.addAttribute(new Attribute("useGeneratedKeys", "true"));
    	  insertElement.addAttribute(new Attribute("parameterType", "java.util.List"));
    	  TextElement intoText = new TextElement("insert into "+introspectedTable.getFullyQualifiedTable().getIntrospectedTableName()+" ( ");
    	  insertElement.addElement(intoText);
    	  //columnList
    	  String acls="";
    	  for(IntrospectedColumn introspectedColumn :introspectedTable.getAllColumns()){
        	  if(!"".equals(acls))acls+=",";
        	  acls+=introspectedColumn.getActualColumnName();
          }
          TextElement valuesText = new TextElement(acls+" ) values ");
    	  insertElement.addElement(valuesText);
    	  //values
          XmlElement valuesOrder = new XmlElement("foreach");
          valuesOrder.addAttribute(new Attribute("collection","list"));
          valuesOrder.addAttribute(new Attribute("item","item"));
          valuesOrder.addAttribute(new Attribute("index","index"));
          valuesOrder.addAttribute(new Attribute("separator",","));
          String vls=" ( ";
          String cls="";
          for(IntrospectedColumn introspectedColumn :introspectedTable.getAllColumns()){
        	  if(!"".equals(cls))cls+=",";
        	  cls+="#{item."+introspectedColumn.getJavaProperty()+",jdbcType="+introspectedColumn.getJdbcTypeName()+"}";
          }
          vls+=cls+" ) ";
          valuesOrder.addElement(new TextElement(vls));
          insertElement.addElement(valuesOrder);
          return insertElement;
    }
    private XmlElement generateSelectQuery(IntrospectedTable introspectedTable){
        XmlElement selectElement = new XmlElement("select");
        selectElement.addAttribute(new Attribute("id", "query"));
        selectElement.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        //分页开始
        XmlElement paginationStart = new XmlElement("include");
        paginationStart.addAttribute(new Attribute("refid", paginationStartId));
        selectElement.addElement(paginationStart);
        //select
        TextElement selectText = new TextElement("select ");
        selectElement.addElement(selectText);
        //columnList
        XmlElement columnListElement = new XmlElement("include");
        columnListElement.addAttribute(new Attribute("refid","Base_Column_List"));
        selectElement.addElement(columnListElement);
        //form table
        TextElement formText = new TextElement(" from " + introspectedTable.getFullyQualifiedTable().getIntrospectedTableName() + " ");
        selectElement.addElement(formText);
        //normal condition
        XmlElement normalInc = new XmlElement("include");
        normalInc.addAttribute(new Attribute("refid",String.format(normalQueryId,
                introspectedTable.getFullyQualifiedTable().getIntrospectedTableName())));
        //fuzzy condition
        if(isFuzzy){
            //test fuzzy
            selectElement.addElement(generateTestFuzzy(introspectedTable));
            //test !fuzzy
            selectElement.addElement(generateTestNotFuzzy(normalInc));
        }else{
            selectElement.addElement(normalInc);
        }
        //order by
        XmlElement testOrder = new XmlElement("if");
        testOrder.addAttribute(new Attribute("test","order and orderStr == null"));
        testOrder.addElement(new TextElement("order by " + orderbyColumns));
        selectElement.addElement(testOrder);
        XmlElement testOrderStr = new XmlElement("if");
        testOrderStr.addAttribute(new Attribute("test","order and orderStr != null and orderStr.trim().length() != 0"));
        testOrderStr.addElement(new TextElement("order by ${orderStr}" ));
        selectElement.addElement(testOrderStr);
        //分页结束
        XmlElement paginationEnd = new XmlElement("include");
        paginationEnd.addAttribute(new Attribute("refid", paginationEndId));
        selectElement.addElement(paginationEnd);
        return selectElement;
    }

    private XmlElement generateSelectCount(IntrospectedTable introspectedTable){
        XmlElement countElement = new XmlElement("select");
        countElement.addAttribute(new Attribute("id", "count"));
        countElement.addAttribute(new Attribute("resultType", "int"));
        TextElement selectText = new TextElement("select count(*) from " +
                introspectedTable.getFullyQualifiedTable().getIntrospectedTableName() + " ");
        countElement.addElement(selectText);
        //normal condition
        XmlElement normalInc = new XmlElement("include");
        normalInc.addAttribute(new Attribute("refid",String.format(normalQueryId,
                introspectedTable.getFullyQualifiedTable().getIntrospectedTableName())));
        //fuzzy condition
        if(isFuzzy){
            //test fuzzy
            countElement.addElement(generateTestFuzzy(introspectedTable));
            //test !fuzzy
            countElement.addElement(generateTestNotFuzzy(normalInc));
        }else{
            countElement.addElement(normalInc);
        }
        return countElement;
    }
    private XmlElement generateSelectSysDate(IntrospectedTable introspectedTable){
        XmlElement countElement = new XmlElement("select");
        countElement.addAttribute(new Attribute("id", "selectSysDate"));
        countElement.addAttribute(new Attribute("resultType", "java.util.Date"));
        //normal condition
        XmlElement normalInc = new XmlElement("include");
        normalInc.addAttribute(new Attribute("refid",paginationSysDateId));
        countElement.addElement(normalInc);
        return countElement;
    }
    private XmlElement generateUpdateState(IntrospectedTable introspectedTable){
        XmlElement countElement = new XmlElement("update");
        countElement.addAttribute(new Attribute("id", "updateStateByPrimaryKey"));
        countElement.addAttribute(new Attribute("parameterType", "Map"));
        TextElement updateText = new TextElement("update " +
                introspectedTable.getFullyQualifiedTable().getIntrospectedTableName() + " ");
        countElement.addElement(updateText);
        TextElement setText = new TextElement(" set DATA_STATE = #{dataState,jdbcType=INTEGER},GMT_MODIFIED=SYSDATE() " );
        countElement.addElement(setText);
        XmlElement settestFuzzy = new XmlElement("if");
        settestFuzzy.addAttribute(new Attribute("test","memo != null"));
        TextElement setifText = new TextElement(" , MEMO = #{memo,jdbcType=VARCHAR} " );
        settestFuzzy.addElement(setifText);
        countElement.addElement(settestFuzzy);
        String whereSql="where ";
        for(IntrospectedColumn introspectedColumn:introspectedTable.getPrimaryKeyColumns()){
        	whereSql+=" "+introspectedColumn.getActualColumnName()+" = #{"+introspectedColumn.getJavaProperty()+",jdbcType="+introspectedColumn.getJdbcTypeName()+"}";
        }
        TextElement whereText = new TextElement(whereSql);
        countElement.addElement(whereText);
        XmlElement testFuzzy = new XmlElement("if");
        testFuzzy.addAttribute(new Attribute("test","oldDataState != null"));
        TextElement whereandText = new TextElement(" and DATA_STATE = #{oldDataState,jdbcType=INTEGER} " );
        testFuzzy.addElement(whereandText);
        countElement.addElement(testFuzzy);
        return countElement;
    }
    private XmlElement generateTestFuzzy(IntrospectedTable introspectedTable){
        XmlElement testFuzzy = new XmlElement("if");
        testFuzzy.addAttribute(new Attribute("test","fuzzy"));
        XmlElement fuzzyInc = new XmlElement("include");
        fuzzyInc.addAttribute(new Attribute("refid",String.format(fuzzyQueryId,
                introspectedTable.getFullyQualifiedTable().getIntrospectedTableName())));
        testFuzzy.addElement(fuzzyInc);
        return testFuzzy;
    }

    private XmlElement generateTestNotFuzzy(XmlElement normalInc){
        XmlElement testNotFuzzy = new XmlElement("if");
        testNotFuzzy.addAttribute(new Attribute("test","!fuzzy"));
        testNotFuzzy.addElement(normalInc);
        return testNotFuzzy;
    }

    public static void main(String... args){
        String tpl = " AND (%1$s like '%%' || #{%2$s} || '%%') ";
        System.out.println(String.format(tpl,"a","b"));
    }
}
