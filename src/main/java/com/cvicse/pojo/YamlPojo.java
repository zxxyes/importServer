package com.cvicse.pojo;

import java.util.Map;

/**
 * 
* Title: yaml
* Description:yamlpojo类
* Version:1.0.0  
 */
public class YamlPojo {
	
	 private Map<String,Map<String,String>> assetsListImportType;
	 private Map<String,Map<String,String[]>> assetsType;
	 private Map<String,Map<String,String[]>> projectType;
	 
	 
	 public YamlPojo(){
	 }


	public Map<String, Map<String, String>> getAssetsListImportType() {
		return assetsListImportType;
	}


	public void setAssetsListImportType(Map<String, Map<String, String>> assetsListImportType) {
		this.assetsListImportType = assetsListImportType;
	}


	public Map<String, Map<String, String[]>> getAssetsType() {
		return assetsType;
	}


	public void setAssetsType(Map<String, Map<String, String[]>> assetsType) {
		this.assetsType = assetsType;
	}


	public Map<String, Map<String, String[]>> getProjectType() {
		return projectType;
	}


	public void setProjectType(Map<String, Map<String, String[]>> projectType) {
		this.projectType = projectType;
	}
	 
	 

}
