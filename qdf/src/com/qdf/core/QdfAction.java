package com.qdf.core;

import com.qdf.servlet.IRequest;
import com.qdf.servlet.IResponse;
/**
 * Action的接口
 * 所有的Action必须实现该接口
 * @author xiezq
 *
 */
public interface QdfAction {

	void execute(IRequest request,IResponse response);
}
