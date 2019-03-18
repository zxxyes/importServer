package com.cvicse.pojo;

import java.util.Map;

/**
 * 
* Title: yaml
* Description:yamlpojo类
* Version:1.0.0  
 */
public class YamlPojo {
	
	 private Map<String,String> assetsListImportType;
	 private Map<String,String[]> assetsType;
	 /** 编号 */
	 private int id;
	 /** 姓名 */
	 private String name;
	 
	 /** 年龄 */
	 private int age;
	 
	 public YamlPojo(){
	 }
	 /**
	  *  构造方法
	  * @param id  编号
	  * @param name  姓名
	  */
	public YamlPojo(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	/**  
	 * 获取编号  
	 * @return id 
	 */
	public int getId() {
		return id;
	}

	/**  
	 * 设置编号  
	 * @param id 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**  
	 * 获取姓名  
	 * @return name 
	 */
	public String getName() {
		return name;
	}

	/**  
	 * 设置姓名  
	 * @param name 
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**  
	 * 获取年龄  
	 * @return  age  
	 */
	public int getAge() {
		return age;
	}
	/**  
	 * 设置年龄  
	 * @param int age  
	 */
	public void setAge(int age) {
		this.age = age;
	}
	
	

}