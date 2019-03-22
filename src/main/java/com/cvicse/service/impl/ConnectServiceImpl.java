package com.cvicse.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cvicse.service.ConnectService;

@Service
public class ConnectServiceImpl implements ConnectService {

	/**
	 * 请求参数MAP，加载request中所有请求参数
	 */
	public Map<String,Object> reqMap;

	@Override
	public void request2Map(HttpServletRequest request,Map<String,Object> reqMap) {
		reqMap.putAll(request.getParameterMap());
	}
	
	/** 
     * 从request中获得参数Map，并返回可读的Map 
     *  
     * @param request 
     * @return 
     */  
    public Map getParameterMap(HttpServletRequest request,Map reqMap) {  
        // 参数Map  
        Map properties = request.getParameterMap();  
        // 返回值Map  
        Iterator entries = properties.entrySet().iterator();  
        Map.Entry entry;  
        String name = "";  
        String value = "";  
        while (entries.hasNext()) {  
            entry = (Map.Entry) entries.next();  
            name = (String) entry.getKey();  
            Object valueObj = entry.getValue();  
            if(null == valueObj){  
                value = "";  
            }else if(valueObj instanceof String[]){  
                String[] values = (String[])valueObj;  
                for(int i=0;i<values.length;i++){  
                    value = values[i] + ",";  
                }  
                value = value.substring(0, value.length()-1);  
            }else{  
                value = valueObj.toString();  
            }  
            reqMap.put(name, value);  
        }  
        return reqMap;  
    }

}
