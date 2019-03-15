package com.cvicse.service.impl;

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

}
