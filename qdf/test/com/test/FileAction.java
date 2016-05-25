package com.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.print.attribute.ResolutionSyntax;

import com.qdf.annotation.Action;
import com.qdf.core.QdfAction;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;

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
		System.out.println(request.getParameter("id"));
		
	}

	//文件下载
	public void download(IRequest request,IResponse response) {
		try {
            // path是指欲下载的文件的路径。
			String path = "C:\\Users\\xiezq\\Pictures\\4f6178310a55b31965a0315d44a98226cefc1.jpg";
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 取得文件的后缀名。
            String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 设置response的Header
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
            response.setHeader("Content-Length", "" + file.length());
            response.setDataInputStream("application/octet-stream", fis);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
}
