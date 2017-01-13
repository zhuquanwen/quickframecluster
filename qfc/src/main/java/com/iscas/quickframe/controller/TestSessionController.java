package com.iscas.quickframe.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iscas.quickframe.helper.SessionHelper;

/**
*@auhor:zhuquanwen
*@date:2017年1月3日
*@desc:
*/
@RestController
public class TestSessionController {
	@Autowired
	private SessionHelper sessionHelper;
	@RequestMapping(value="/testSessionCount")
	public Long testSessionCount(HttpServletRequest request){
		
		Long count = sessionHelper.getActiveSession(request);
		return count;
	}
}
