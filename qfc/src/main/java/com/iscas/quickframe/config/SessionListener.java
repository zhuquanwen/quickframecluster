package com.iscas.quickframe.config;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


/**
*@auhor:zhuquanwen
*@date:2017年1月4日
*@desc:
*/

public class SessionListener implements HttpSessionListener{


	public void sessionCreated(HttpSessionEvent arg0) {
		ServletContext context = arg0.getSession().getServletContext();
		
		if (context.getAttribute("count")==null) {
			context.setAttribute("count", 1);
		}else {
			int count = (Integer) context.getAttribute("count");
			synchronized (SessionListener.class) {
				context.setAttribute("count", count+1);
			}
			
		}
	}


	public void sessionDestroyed(HttpSessionEvent arg0) {
		ServletContext context = arg0.getSession().getServletContext();
		if (context.getAttribute("count")==null) {
			context.setAttribute("count", 0);
		}else {
			int count = (Integer) context.getAttribute("count");
			if (count<1) {
			count = 1;
			}
			synchronized (SessionListener.class) {
				context.setAttribute("count", count-1);
			}
			
			}
			
		}
}
