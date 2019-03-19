package com.cvicse.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cvicse.pojo.YamlPojo;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

/**
 * 
* Title: YamlConfig
* Description: 
* 自定义配置文件 
* Version:1.0.0  
 */
@Component
public class YamlConfig {  
  
    private YamlPojo yamlPojo;
    private Map yamlMap;
    @Autowired 
    private YamlPathConfig yamlPathConfig;

	/**  
	 * 获取yamlmap
	 * @return  yamlmap  
	 */
	public YamlPojo getYamlPojo() {
		if(null != yamlPojo){
			return yamlPojo;
		}
		try {
			String setConfig = yamlPathConfig.getYamlPath();
			if(null == setConfig || setConfig.isEmpty()) {
				setConfig = this.getClass().getResource("/excelImportPara.yaml").getPath();
			}
			YamlReader reader = new YamlReader(new FileReader(setConfig));
			Map map =(Map)reader.read();
			System.out.println(map);
			//yamlPojo = reader.read(YamlPojo.class);
			//System.out.println(yamlPojo.getAssetsListImportType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (YamlException e) {
			e.printStackTrace();
		}
		return yamlPojo;
	}
	
	public Map getYamlMap() {
		if(null != yamlMap){
			return yamlMap;
		}
		try {
			String setConfig = yamlPathConfig.getYamlPath();
			if(null == setConfig || setConfig.isEmpty()) {
				setConfig = this.getClass().getResource("/excelImportPara.yaml").getPath();
			}
			YamlReader reader = new YamlReader(new FileReader(setConfig));
			yamlMap =(Map)reader.read();
			System.out.println(yamlMap);
			//yamlPojo = reader.read(YamlPojo.class);
			//System.out.println(yamlPojo.getAssetsListImportType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (YamlException e) {
			e.printStackTrace();
		}
		return yamlMap;
	}
    
}  
