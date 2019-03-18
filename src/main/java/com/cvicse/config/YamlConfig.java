package com.cvicse.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import org.springframework.stereotype.Component;

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
  
    private Map yamlMap;

	/**  
	 * 获取yamlmap
	 * @return  yamlmap  
	 */
	public Map getYamlMap() {
		if(null != yamlMap){
			return yamlMap;
		}
		try {
			YamlReader reader = new YamlReader(new FileReader("contact.yml"));
			Object object = reader.read();
			System.out.println(object);
			this.yamlMap = (Map)object;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (YamlException e) {
			e.printStackTrace();
		}
		return yamlMap;
	}
    
}  
