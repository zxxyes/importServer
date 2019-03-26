package com.cvicse.web;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cvicse.dao.exception.ReflectionException;
import com.cvicse.exception.DataAccessException;
import com.cvicse.service.ConnectService;
import com.cvicse.service.ExcelService;
import com.cvicse.service.ProcessService;

/**
 * 
* Title: FileController
* Description: springboot 接口测试
* 首先启动 Application 程序，然后在浏览器输入http://localhost:8080//hello 
* 即可查看信息
* 在类中添加  @RestController, 默认类中的方法都会以json的格式返回
* Version:1.0.0  
 */
@RestController
public class ImportController {
	
	@Autowired
    private ConnectService connectService;
	@Autowired
    private ProcessService processService;
	@Autowired
    private ExcelService excelService;
	
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public String importFile(@RequestParam(value = "fileSelect", required = true) MultipartFile fileSelect,HttpServletRequest request) {
    	Map<String,Object> reqMap = new HashMap<String,Object>();
    	connectService.getParameterMap(request, reqMap);
    	String projectId = ((String)reqMap.get("projectId"));
    	String assetsId = ((String)reqMap.get("assetsId"));
    	System.out.println("---------fileSelect----------:"+fileSelect);
    	StringBuilder errorInfo = new StringBuilder("");
    	if (fileSelect.getSize() > 0) {
            //获取保存上传文件的file文件夹绝对路径
            String path = request.getSession().getServletContext().getRealPath("");
            //获取上传文件名
            String fileName = fileSelect.getOriginalFilename();
            File file = new File(path, fileName);
            try {
            	fileSelect.transferTo(file);
            	List<Map<String, Object>> excelDataMap = excelService.loadExcel2Map(file,errorInfo);
            	if(null != errorInfo&& errorInfo.length()>0) {
            		return errorInfo.toString();
            	}
            	excelService.save(excelDataMap,errorInfo);
            	if(null != errorInfo&& errorInfo.length()>0) {
            		return errorInfo.toString();
            	}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (DataAccessException e) {
				e.printStackTrace();
			} 
            //保存上传之后的文件路径
            request.setAttribute("filePath", "file/"+fileName);
          }
        return "导入成功";
    }
    
    @RequestMapping(value = "/assetsTypes", method = RequestMethod.GET)
    public Map<String, String> assetsTypes(HttpServletRequest request) {
    	Map<String, String> importTypeAll = processService.assetsTypeAll();
        return importTypeAll;
    }
    
    @RequestMapping(value = "/imporotTypes", method = RequestMethod.GET)
    public Map<String, String> imporotTypes(HttpServletRequest request) {
    	Map<String,Object> reqMap = new HashMap<String,Object>();
    	connectService.request2Map(request, reqMap);
    	String assetsType = ((String[])reqMap.get("assetsType"))[0];
    	//String assetsType = request.getParameter("assetsType");
    	Map<String, String> importTypeAll = processService.importTypeByAssetsType(assetsType);
        return importTypeAll;
    }
    
    
    
    
}
