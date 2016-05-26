package com.test.action;

import java.io.File;
import java.util.List;

import com.qdf.annotation.Action;
import com.qdf.core.QdfAction;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;

/**
 * 文件上传下载测试
 * @author xiezq
 *
 */
@Action(url = "/file")
public class FileAction implements QdfAction{

	@Override
	public void execute(IRequest request,IResponse response) {
		
		System.out.println("execute");
		
	}
	
	//文件上传
	public void upload(IRequest request, IResponse response) {
		List<String> uploadFiles = request.getUploadFiles("uplaod", 1024*1024*10);
		for (String path : uploadFiles) {
			System.out.println(path);
		}
		
		//获取参数必须在获取文件后面执行
		System.out.println("id:"+request.getParameter("id"));
		System.out.println("name:"+request.getParameter("name"));
		
		//通过该方法获取文件
		//File file = request.getUploadFile("uplaod/xxx.jpg");
		
	}

	//文件下载
	public void download(IRequest request,IResponse response) {
        
		// path是指欲下载的文件的路径。
		String path = "C:\\Users\\xiezq\\Pictures\\安联.jpg";
		response.download(path);
        
		//response.download(new File(path));
	}
}
