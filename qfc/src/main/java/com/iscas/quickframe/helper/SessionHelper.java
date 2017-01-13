package com.iscas.quickframe.helper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

/**
*@auhor:zhuquanwen
*@date:2017年1月3日
*@desc:
*/
@Component
public class SessionHelper {
	
	
	
	/** 
	 * @Title: 获取当前在线人数
	 * @Description: 获取当前在线人数
	 * @param request
	 * @return Long
	 * @throws 
	 */
	public Long getActiveSession(HttpServletRequest request){
		ServletContext context = request.getServletContext();
		Long count = 0L;
		if (context.getAttribute("count")==null) {
			count = 0L;
		}else {
			count = ((Integer) context.getAttribute("count")).longValue();
		}
		return count;
	}
}
