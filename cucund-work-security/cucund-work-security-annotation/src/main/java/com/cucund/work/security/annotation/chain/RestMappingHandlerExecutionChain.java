package com.cucund.work.security.annotation.chain;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.cucund.security.common.utils.ListUtil;
import com.cucund.security.common.utils.StringUtils;
import com.cucund.work.security.annotation.bean.PermissionList;
import com.cucund.work.security.annotation.handler.TemplateHandler;
import com.cucund.work.security.annotation.handler.impl.GetMappingHandler;
import com.cucund.work.security.annotation.handler.impl.PostMappingHandler;
import com.cucund.work.security.annotation.handler.impl.RequestMappingHandler;

public class RestMappingHandlerExecutionChain {
	
	private RestMappingHandlerExecutionChain() {
		handlers = new ArrayList<TemplateHandler>();
        handlers.add(new GetMappingHandler());
        handlers.add(new PostMappingHandler());
        handlers.add(new RequestMappingHandler());
    }
    
    private List<TemplateHandler> handlers = null;
    
    private static RestMappingHandlerExecutionChain restMappingHandlerExecutionChain;
    
    public static Object metux = new Object();
    
    public static RestMappingHandlerExecutionChain newInstance() {
        if (restMappingHandlerExecutionChain == null) {
            synchronized (metux) {
                if (restMappingHandlerExecutionChain == null) {
                    restMappingHandlerExecutionChain = new RestMappingHandlerExecutionChain();
                }
            }
        }
        return restMappingHandlerExecutionChain;
    }
    
 public PermissionList execute(Method method) {
        
        if (ListUtil.isEmpty(handlers)) {
            return null;
        }
        
        for (TemplateHandler templateHandler : handlers) {
            PermissionList result = templateHandler.execute(method);
            
            if (result!=null) {
                return result;
            }
        }
        return null;
     }

}
