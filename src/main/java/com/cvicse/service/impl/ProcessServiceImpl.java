package com.cvicse.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.cvicse.config.YamlPathConfig;
import com.cvicse.service.ProcessService;

@Service
public class ProcessServiceImpl implements ProcessService {

	/**
	 * yaml配置参数
	 */
	@Autowired 
	public YamlPathConfig config;
	
	

	@Override
	public void request2Map(HttpServletRequest request, Map<String, Object> reqMap) {
		// TODO Auto-generated method stub
		
	}


}
