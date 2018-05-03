package com.cucund.security.common.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerCode {
	public static String outputDir ="src\\main\\java";
	private static FtlTranslationService ftlTranslationService = new FtlTranslationService();

	public static void createService(ServerConfig serverConfig) {
		if(!check(serverConfig))return;
		try {
			ftlTranslationService.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,Object> data = new HashMap<String, Object>();
        data.put("serverConfig",serverConfig);
        String filePath=serverConfig.getServerPkg().replace(".", "/");
        try {
        	ftlTranslationService.translate(data,"serverinterface.ftl",
        			outputDir + "/" + filePath+ "/service"+ "/"+serverConfig.getInterfaceStr()+".java");
        	ftlTranslationService.translate(data,"server.ftl",
                    outputDir + "/" + filePath+ "/service/impl"+ "/"+serverConfig.getImplementsStr()+".java" );
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
	public static boolean check(ServerConfig serverConfig){
		if(null==serverConfig)return false;
		boolean flag=true;
		if(checkObjectNull(serverConfig.getInterfaceStr())){
			System.out.println("InterfaceStr为空");
			flag=false;
		}
		if(checkObjectNull(serverConfig.getImplementsStr())){
			System.out.println("implementsStr为空");
			flag=false;
		}
		if(checkObjectNull(serverConfig.getServerPkg())){
			System.out.println("serverPkg为空");
			flag=false;
		}
		if(checkObjectNull(serverConfig.getName())){
			System.out.println("name为空");
			flag=false;
		}
		if(checkObjectNull(serverConfig.getNick())){
			System.out.println("nick为空");
			flag=false;
		}
		if(checkObjectNull(serverConfig.getModel())){
			System.out.println("model为空");
			flag=false;
		}
		if(checkObjectNull(serverConfig.getGetsysdateDao())){
			System.out.println("getsysdateDao为空");
			flag=false;
		}
		List<ServerProConfig> proList=serverConfig.getProList();
		if(null==proList||proList.isEmpty()){
			System.out.println("proList为空");
			flag=false;
		}
		boolean listflag=true;
		for (ServerProConfig serverProConfig : proList) {
			if(checkObjectNull(serverProConfig.getDao())){
				System.out.println("proList:"+"Dao为空");
				listflag=false;
			}
			if(checkObjectNull(serverProConfig.getDaoName())){
				System.out.println("proList:"+"DaoName为空");
				listflag=false;
			}
			if(checkObjectNull(serverProConfig.getIdName())){
				System.out.println("proList:"+"IdName为空");
				listflag=false;
			}
			
			if(checkObjectNull(serverProConfig.getName())){
				System.out.println("proList:"+"Name为空");
				listflag=false;
			}
			if(checkObjectNull(serverProConfig.getNick())){
				System.out.println("proList:"+"Nick为空");
				listflag=false;
			}
			if(listflag){
				String s=serverProConfig.getDao();
				s=(s.substring(0,1)).toLowerCase()+s.substring(1);
				System.out.println(serverProConfig.getDao()+"====>"+s);
				serverProConfig.setLdao(s);
				s=serverProConfig.getName();
				s=(s.substring(0,1)).toLowerCase()+s.substring(1);
				System.out.println(serverProConfig.getName()+"====>"+s);
				serverProConfig.setLname(s);
				s=serverProConfig.getIdName();
				s="get"+(s.substring(0,1)).toUpperCase()+s.substring(1);
				System.out.println(serverProConfig.getIdName()+"====>"+s);
				serverProConfig.setGetidName(s);
				List<String> ilist=serverConfig.getImportsilist();
				if(null==ilist){
					ilist=new ArrayList<String>();
					serverConfig.setImportsilist(ilist);
				}
				List<String> list=serverConfig.getImportslist();
				if(null==list){
					list=new ArrayList<String>();
					serverConfig.setImportslist(list);
				}
				ilist.add(serverConfig.getServerPkg()+".domain."+serverProConfig.getName()+serverProConfig.getDomainName());
				ilist.add(serverConfig.getServerPkg()+".model."+serverProConfig.getName());
				list.add(serverConfig.getServerPkg()+".dao."+serverProConfig.getDao());
				list.add(serverConfig.getServerPkg()+".domain."+serverProConfig.getName()+serverProConfig.getDomainName());
				list.add(serverConfig.getServerPkg()+".model."+serverProConfig.getName());
			}
		}
		if(!listflag)flag=false;
		if(flag){
			String s=serverConfig.getInterfaceStr();
			s=(s.substring(0,1)).toLowerCase()+s.substring(1);
			System.out.println(serverConfig.getInterfaceStr()+"====>"+s);
			serverConfig.setLinterfaceStr(s);
			//处理默认imPL
			List<String> list=serverConfig.getImportslist();
			if(null==list){
				list=new ArrayList<String>();
				serverConfig.setImportslist(list);
			}
			list.add("com.yqbsoft.laser.service.esb.core.transformer.PageTools");
			list.add("com.yqbsoft.laser.service.esb.core.support.BaseServiceImpl");
			list.add("com.yqbsoft.laser.service.tool.util.BeanUtils");
			list.add("com.yqbsoft.laser.service.tool.util.StringUtils");
			list.add(serverConfig.getServerPkg()+"."+serverConfig.getConstants());
			list.add(serverConfig.getServerPkg()+".service."+serverConfig.getInterfaceStr());
			list.add("java.util.Date");
			list.add("java.util.HashMap");
			list.add("java.util.List");
			list.add("java.util.Map");
			list.add("com.yqbsoft.laser.service.esb.core.transformer.QueryResult");
			list.add("com.yqbsoft.laser.service.esb.core.ApiException");
			List<String> ilist=serverConfig.getImportsilist();
			if(null==ilist){
				ilist=new ArrayList<String>();
				serverConfig.setImportsilist(ilist);
			}
			ilist.add("com.yqbsoft.laser.service.esb.annotation.ApiMethod");
			ilist.add("com.yqbsoft.laser.service.esb.annotation.ApiService");
			ilist.add("com.yqbsoft.laser.service.esb.core.ApiException");
			ilist.add("com.yqbsoft.laser.service.esb.core.support.BaseService");
			ilist.add("com.yqbsoft.laser.service.esb.core.transformer.QueryResult");
			ilist.add("java.util.Map");
		}
		return flag;
	}
	public static ServerConfig createConfig(){
		ServerConfig serverConfig=new ServerConfig();
		
		return serverConfig;
	}
	
}
