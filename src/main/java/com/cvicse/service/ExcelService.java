package com.cvicse.service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.cvicse.dao.exception.ReflectionException;
import com.cvicse.exception.DataAccessException;


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
	List<Map<String, Object>> loadExcel2Map(File file,StringBuilder errorInfo);
	
	void save(List<Map<String, Object>> excelDtaMap,StringBuilder errorInfo)  throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException ;
	
	void saveJDao(List<Map<String, Object>> excelDtaMap,StringBuilder errorInfo) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, DataAccessException, ReflectionException;
	
	
}
