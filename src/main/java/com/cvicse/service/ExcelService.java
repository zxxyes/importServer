package com.cvicse.service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


/**
 * 
* Title: ExcelService
* Description: 
* 核心处理器接口 
* Version:1.0.0  
 */
public interface ExcelService {
	
	/**
	 *文件转为list map
	 * @return
	 */
	List<Map<String, Object>> loadExcel2Map(File file,String errorInfo);
	
	void save(List<Map<String, Object>> excelDtaMap,String errorInfo)  throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException ;
	
	
}
