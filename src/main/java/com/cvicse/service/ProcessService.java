package com.cvicse.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * 
* Title: ProcessServiceService
* Description: 
* 核心处理器接口 
* Version:1.0.0  
 */
public interface ProcessService {
	
	/**
	 * 转换request属性为MAP
	 * @param user
	 * @return
	 */
	void request2Map(HttpServletRequest request,Map<String,Object> reqMap);
	
	
}
