package com.cvicse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 
* Title: YamlAddressConfig
* Description: 
* 自定义配置文件 
* Version:1.0.0  
 */
@Component
@PropertySource(value = "classpath:application.properties")
public class YamlPathConfig {  
  
    @Value("${import.yamlPath}")
    private String yamlPath;

	/**  
	 * 获取yamlPath  
	 * @return  yamlPath  
	 */
	public String getYamlPath() {
		return yamlPath;
	}
    
}  
