package com.cucund.security.common.plugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cucund.security.common.annotation.ColumnName;


public class ControlCode {
	public static String javaOutputDir     ="src\\main\\java\\com\\cucund\\security\\html\\{pagename}\\controller\\";
	public static String javaBeanOutputDir ="src\\main\\java\\com\\cucund\\security\\html\\{pagename}\\bean\\";
	public static String htmlOutputDir ="src\\main\\resources\\META-INF\\html\\{context}\\";
	private static FtlTranslationService ftlTranslationService = new FtlTranslationService();
	public static final Map<String,String> colMap=new HashMap<String,String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1549926805156219813L;

	{
		put("gmtCreate", "gmtCreate");
		put("serialVersionUID", "serialVersionUID");
		put("gmtModified", "gmtModified");
		put("memo", "memo");
		put("dataState", "dataState");
		
	}
	};
	public static void createControl(ControlConfig controlConfig) {
		if(!check(controlConfig))return;
		try {
			ftlTranslationService.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,Object> data = new HashMap<String, Object>();
        data.put("controlConfig",controlConfig);
        javaOutputDir=controlConfig.getProjectPath()+javaOutputDir.replace("{pagename}", controlConfig.getPagename());
        htmlOutputDir=controlConfig.getProjectPath()+htmlOutputDir.replace("{context}", controlConfig.getContext());
        javaBeanOutputDir=controlConfig.getProjectPath()+javaBeanOutputDir.replace("{pagename}", controlConfig.getPagename());
        try {
        	//htmlbean
        	ftlTranslationService.translate(data,"htmlInbean.ftl",
        			javaBeanOutputDir+ "/"+controlConfig.getUpContext()+"Bean.java");
        	ftlTranslationService.translate(data,"htmlRebean.ftl",
        			javaBeanOutputDir+  "/"+controlConfig.getUpContext()+"ReBean.java");
        	//control
        	ftlTranslationService.translate(data,"control.ftl",
        			javaOutputDir+ "/"+controlConfig.getUpContext()+"Con.java");
        	//html
        	ftlTranslationService.translate(data,"html-add.ftl",
        			htmlOutputDir+ "/add.ftl");//新增
        	ftlTranslationService.translate(data,"html-list.ftl",
        			htmlOutputDir+ "/list.ftl");//管理
        	ftlTranslationService.translate(data,"html-edit.ftl",
        			htmlOutputDir+ "/edit.ftl");//修改
        	ftlTranslationService.translate(data,"html-view.ftl",
        			htmlOutputDir+ "/view.ftl");//修改
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	public static boolean checkObjectNull(Object obj){
		if(null==obj){
			return true;
		}
		if("".equals(String.valueOf(obj))){
			return true;
		}
		return false;
	}
	public static boolean check(ControlConfig controlConfig){
		if(null==controlConfig)return false;
		boolean flag=true;
		if(checkObjectNull(controlConfig.getIdName())){
			System.out.println("IdName为空");
			flag=false;
		}
		if(checkObjectNull(controlConfig.getModel())){
			System.out.println("Model为空");
			flag=false;
		}
		if(checkObjectNull(controlConfig.getName())){
			System.out.println("Name为空");
			flag=false;
		}
		if(checkObjectNull(controlConfig.getNameNick())){
			System.out.println("NameNick为空");
			flag=false;
		}
		if(checkObjectNull(controlConfig.getModelName())){
			System.out.println("modelName为空");
			flag=false;
		}
		if(flag){
			String s=controlConfig.getNameNick();
			s=(s.substring(0,1)).toLowerCase()+s.substring(1);
			System.out.println(controlConfig.getNameNick()+"====>"+s);
			controlConfig.setLnameNick(s);
			s=controlConfig.getModelName();
			s=(s.substring(0,1)).toLowerCase()+s.substring(1);
			System.out.println(controlConfig.getModelName()+"====>"+s);
			controlConfig.setLmodelName(s);
			try{
			Class<?> clazz =Class.forName("com.yqbsoft.laser.service."+controlConfig.getServicePagename()+".domain."+controlConfig.getModelName()+controlConfig.getDomainName());
			createColumn(controlConfig,clazz);
			clazz =Class.forName("com.yqbsoft.laser.service."+controlConfig.getServicePagename()+".domain."+controlConfig.getModelName()+controlConfig.getReDomainName());
			createColumn(controlConfig,clazz);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return flag;
	}
	private static void createColumn(ControlConfig controlConfig,Class<?> clazz) {
		try {
			Field[] fieldList=clazz.getDeclaredFields();
			if(null!=fieldList){
				List<ColumnBean> infieldList = controlConfig.getInfieldList();
				if(null==infieldList){
					infieldList=new ArrayList<ColumnBean>();
					controlConfig.setInfieldList(infieldList);
				}
				List<ColumnBean> refieldList = controlConfig.getRefieldList();
				if(null==refieldList){
					refieldList=new ArrayList<ColumnBean>();
					controlConfig.setRefieldList(refieldList);
				}
				List<String> columnList=controlConfig.getColumnList();
				if(null==columnList){
					columnList=new ArrayList<String>();
					controlConfig.setColumnList(columnList);
				}
				Map<String,String> columnNameMap=controlConfig.getColumnNameMap();
				if(null==columnNameMap){
					columnNameMap=new HashMap<String, String>();
					controlConfig.setColumnNameMap(columnNameMap);
				}
				ColumnBean columnBean=null;
				for (Field field : fieldList) {
					columnBean=new ColumnBean();
					columnBean.setDoc("");
					if(!colMap.containsKey(field.getName())&&!controlConfig.getIdName().equals(field.getName())){
						columnList.add(field.getName());
						ColumnName colname=field.getAnnotation(ColumnName.class);
						if(null!=colname&&null!=field.getName()){
							columnNameMap.put(field.getName(), colname.value().equals("")?field.getName():colname.value());
							columnBean.setDoc(columnNameMap.get(field.getName()));
						}
					}
					if(!"serialVersionUID".equals(field.getName())){
						columnBean.setName(field.getName());
						columnBean.setSimpleTypeClass(field.getType().getSimpleName());
						columnBean.setGetterName("get"+toUpperCaseFirstOne(columnBean.getName()));
						columnBean.setSetterName("set"+toUpperCaseFirstOne(columnBean.getName()));
						if(colMap.containsKey(field.getName())){
							refieldList.add(columnBean);
						}else{
							infieldList.add(columnBean);
						}
					}
				}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//首字母转大写
    public static String toUpperCaseFirstOne(String s)
    {
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
	
}
