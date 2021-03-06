package com.cvicse.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public Map<String, String> assetsTypeAll() {
		return (Map<String, String>)yamlConfig.getYamlMap().get("assetsType");
	}

	@Override
	public Map<String,String> importTypeAll() {
//		return yamlConfig.getYamlPojo().getAssetsListImportType().get("assetsListImportType");
		return (Map<String,String>)yamlConfig.getYamlMap().get("assetsListImportType");
	}
	
	@Override
	public Map<String, String> importTypeByAssetsType(String assetsType) {
		Map<String,String> assetsListImportTypeAll = this.importTypeAll();
		if(null ==assetsType || assetsType.isEmpty() ) {
			return assetsListImportTypeAll;
		}
		Map<String, List<String>> assetsTypeRelImportType = (Map<String, List<String>>)yamlConfig.getYamlMap().get("assetsTypeRelImportType");
		List<String> assetsListImportTypeKey = assetsTypeRelImportType.get(assetsType);
		Map<String,String> assetsListImportTypeFilter = new HashMap<String,String>();
		for (int i = 0; i < assetsListImportTypeKey.size(); i++) {
			String key = assetsListImportTypeKey.get(i);
			if(assetsListImportTypeAll.containsKey(key)) {
				assetsListImportTypeFilter.put(key, assetsListImportTypeAll.get(key));
			}
		}
		
		return assetsListImportTypeFilter;
	}


//	@Override
//	public Map<String, String> importTypeByAssetsType(String assetsType) {
//		Map<String, String[]> assetsTypes = yamlConfig.getYamlPojo().getAssetsType().get("assetsType");
//		String[] assetsListImportTypeKey = assetsTypes.get(assetsType);
//		Map<String,String> assetsListImportTypeAll = this.importTypeAll();
//		Map<String,String> assetsListImportTypeFilter = new HashMap<String,String>();
//		for (int i = 0; i < assetsListImportTypeKey.length; i++) {
//			String key = assetsListImportTypeKey[i];
//			if(assetsListImportTypeAll.containsKey(key)) {
//				assetsListImportTypeFilter.put(key, assetsListImportTypeAll.get(key));
//			}
//		}
//		return assetsListImportTypeFilter;
//	}
	

}
