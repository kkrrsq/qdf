package com.test.action;


import com.qdf.annotation.Action;
import com.qdf.annotation.Skip;
import com.qdf.core.QdfAction;
import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;
import com.test.model.User;
import com.test.service.UserService;
import com.test.service.impl.UserServiceImpl;

/**
 * ORM测试
 * @author xiezq
 *
 */
@Skip
@Action(url = "/user")
public class UserAction implements QdfAction {

	private UserService us = new UserServiceImpl();

	@Override
	public void execute(IRequest request, IResponse response) {
		System.out.println("user execute");
	}
	
	public void add(IRequest request,IResponse response) {
		int row = us.add(request.getBean(User.class));
		response.setDataByJsonCMD(row);
	}
	
	public void delete(IRequest request,IResponse response) {
		int row = us.delete(request.getBean(User.class));
		response.setDataByJsonCMD(row);
	}
	
	public void update(IRequest request,IResponse response) {
		int row = us.update(request.getBean(User.class));
		response.setDataByJsonCMD(row);
	}
	
	public void find(IRequest request,IResponse response) {
		String id = request.getParameter("id");
		User user = us.getUserById(id);
		response.setDataByJsonCMD(user);
	}
	
	public void list(IRequest request,IResponse response) {
		response.setDataByJsonCMD(us.list());
	}
	
	public void page(IRequest request,IResponse response) {
		response.setDataByJsonCMD(us.page(1,5));
	}
	
	
	public void redirect(IRequest request,IResponse responses) {
		responses.redirect("http://www.baidu.com");
	}
	
	public void forward(IRequest request,IResponse responses) {
		responses.forward("list");
	}
	
	
}
