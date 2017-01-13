package com.iscas.quickframe.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.iscas.quickframe.config.CasConfig;
import com.iscas.quickframe.config.MyShiroCasRealm;
import com.iscas.quickframe.domain.p.App;
import com.iscas.quickframe.domain.p.User;
import com.iscas.quickframe.domain.p.UserDao;
import com.iscas.quickframe.entity.ResponseEntity;
import com.iscas.quickframe.service.AppService;
/**
*@auhor:zhuquanwen
*@date:2016年12月1日
*@desc:测试shiro的控制器
*/
@Controller
public class ShiroController {
	/**登录页面formaction 绑定变量*/
	public static String FORM_ACTION = "FORM_ACTION";
	/**CAS登录地址绑定变量*/
	public static String CAS_LOGIN_URL = "CAS_LOGIN_URL";
	/**本地登录页面地址*/
	public static String LOCAL_LOGIN_URL = "LOCAL_LOGIN_URL";
	
	/**登录页面地址带的service参数*/
	public static String LOGIN_SERVICE_PARAM = "LOGIN_SERVICE_PARAM";
	
//	/**cookie存储的账号信息*/
//	public static String ACCOUNT_INTO_IN_COOKIE = "ACCOUNT_INTO_IN_COOKIE";
//	public static String SPE = "am23m4WEG5m346m7weE";
//	
//	/**绑定记忆的用户名*/
//	public static String RECORD_USERNAME = "RECORD_USERNAME";
//	/**绑定记忆的密码*/
//	public static String RECORD_PASSWORD = "RECORD_PASSWORD";
//	
//	/**绑定记忆账号的checkBox*/
//	public static String RECORD_ACCOUNT_CHECKBOX = "RECORD_ACCOUNT_CHECKBOX";
	@Resource
	private MyShiroCasRealm myShiroCasRealm;
	@Value("${casConfig.casPrefix}")
	private String casServerUrlPrefix ;
	@Value("${casConfig.shiroPrefix}")
	private String shiroServerUrlPrefix ;
	@Value("${appName}")
	private String appName ;
	@Autowired
	private UserDao userDao;
	@Autowired
	private AppService appService;
	@Autowired
	private CasConfig casConfig;
	
	@RequestMapping(value="/logout")  
	public String logout(HttpServletRequest request){ 
	        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
	   if(request.getSession(false) != null){
	    	SecurityUtils.getSubject().logout();
	   } 
	    	
	      String appAddr = "/";
	      App app = appService.findByName(appName);
	      if(app != null && app.getAppAddr() != null){
	    	  appAddr = app.getAppAddr();
	      }
	      //appAddr = "/user";
	      return "redirect:"+CasConfig.INSTANCE.getCasPrefix() + "/logout"
	      + "?service=" + CasConfig.INSTANCE.getShiroPrefix() + appAddr;
	      
	 } 
	    	
	   
	    @RequestMapping("/toLogin")
	    public ModelAndView toLogin(HttpServletRequest request){
	    	ModelAndView mav = new ModelAndView("login");
	    	String formAction = casConfig.getCasPrefix() + "/login" +
	    				"?service=" + casConfig.getShiroPrefix() + casConfig.getCasFilterUrlPattern();
	    	String casLoginURL = casConfig.getCasPrefix() + "/login";
	    	String localLoginURL = casConfig.getShiroPrefix() + casConfig.getLocalLoginURI();
	    	String loginServiceParam = casConfig.getShiroPrefix() + casConfig.getCasFilterUrlPattern();
	    	mav.addObject(FORM_ACTION, formAction);
	    	mav.addObject(CAS_LOGIN_URL, casLoginURL);
	    	mav.addObject(LOCAL_LOGIN_URL, localLoginURL);
	    	mav.addObject(LOGIN_SERVICE_PARAM, loginServiceParam);
	    	//读取记忆的账号信息
	    	//this.readRecordAccountInfo(request, mav);
	    	
	    	return mav;
	       
	    }
	    
	   
	    @RequestMapping("/user")
	    public String getUserList(Map<String, Object> model){
	        model.put("userList", ""); return "user"; }

	    @RequestMapping("/user/edit/{info}")
	    public ModelAndView getUserList(@PathVariable String info,HttpSession session){
	    	System.out.println(session.getId());
	    	ModelAndView mav = new ModelAndView("user_edit");
	    	mav.addObject("info", info);
	         return mav; 
	    }
	    @RequestMapping("/user/query")
	    @ResponseBody
	    public User getQueryList(){
	         return userDao.findByUsername("tom"); 
	    }
	    @RequestMapping(value="/login",method=RequestMethod.GET)
	    public String loginForm(Model model){
	        String loginUrl =  casServerUrlPrefix + "/login" + "?service=" + shiroServerUrlPrefix + "/shiro-cas";
	        return "redirect:" + loginUrl;
	    }
	    
	    @RequestMapping("/testAjax")
	    @ResponseBody
	    public String testAjax(){
	    	return "1111";
	    }
	    
	   
	   
	    
	    @RequestMapping(value = "/test500")
	    public String test(){
	    	@SuppressWarnings("unused")
			int a = 6/0;
	    	return "500";
	    }
	    
	    @RequestMapping("/403")
	    @ResponseBody
	    public ResponseEntity unauthorizedRole(){
	       return new ResponseEntity(HttpStatus.FORBIDDEN.value(), "没有权限"); 
	       
	    }
	    @RequestMapping("/500")
	    @ResponseBody
	    public ResponseEntity to500(){
	       return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误"); 
	       
	    }
	    
	    @RequestMapping("/502")
	    @ResponseBody
	    public ResponseEntity to502(){
	       return new ResponseEntity(HttpStatus.BAD_GATEWAY.value(), "无效网关(502 Bad Gateway)"); 
	       
	    }
	    @RequestMapping("/404")
	    @ResponseBody
	    public ResponseEntity to404(){
	       return new ResponseEntity(HttpStatus.NOT_FOUND.value(), "找不到请求"); 
	       
	    }
	   
}
