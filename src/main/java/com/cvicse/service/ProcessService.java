package com.cvicse.service;

import java.util.Map;


/**
 * 
* Title: ProcessServiceService
* Description: 
* 核心处理器接口 
* Version:1.0.0  
 */
public interface ProcessService {
	
	/**
	 * 获取导入类型
	 * @param user
	 * @return
	 */
	Map<String,String> importTypeAll();
	
	/**
	 * 获取导入类型
	 * @param user
	 * @return
	 */
	Map<String,String> importTypeByAssetsType(String assetsType);
	
	
}
