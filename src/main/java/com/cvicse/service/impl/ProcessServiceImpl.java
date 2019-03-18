package com.cvicse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cvicse.config.YamlConfig;
import com.cvicse.service.ProcessService;

@Service
public class ProcessServiceImpl implements ProcessService {

	/**
	 * yaml配置参数
	 */
	@Autowired 
	public YamlConfig yamlConfig;
	

	@Override
	public String importTypeAll() {
		return yamlConfig.getYamlMap().get("excelimporttypedict");
	}


}
