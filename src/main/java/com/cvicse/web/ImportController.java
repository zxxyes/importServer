package com.cvicse.web;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cvicse.service.ConnectService;
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
	
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public String importFile(@RequestParam(value = "fileSelect", required = true) MultipartFile fileSelect,HttpServletRequest request) {
    	System.out.println("---------fileSelect----------:"+fileSelect);
    	if (fileSelect.getSize() > 0) {
            //获取保存上传文件的file文件夹绝对路径
            String path = request.getSession().getServletContext().getRealPath("");
            //获取上传文件名
            String fileName = fileSelect.getOriginalFilename();
            File file = new File(path, fileName);
            try {
            	fileSelect.transferTo(file);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
            //保存上传之后的文件路径
            request.setAttribute("filePath", "file/"+fileName);
            return "upload";
          }
        return "error";
    }
    
    @RequestMapping(value = "/chooseType", method = RequestMethod.GET)
    public Map<String, String> chooseType(HttpServletRequest request) {
    	Map<String, String> importTypeAll = processService.importTypeAll();
        return importTypeAll;
    }
    
    
}
