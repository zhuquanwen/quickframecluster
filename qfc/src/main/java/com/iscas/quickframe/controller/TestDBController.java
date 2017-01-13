package com.iscas.quickframe.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iscas.quickframe.domain.p.App;
import com.iscas.quickframe.domain.p.Organizational;
import com.iscas.quickframe.domain.p.Resource;
import com.iscas.quickframe.domain.p.Role;
import com.iscas.quickframe.domain.p.UrlAddr;
import com.iscas.quickframe.domain.p.User;
import com.iscas.quickframe.service.AppService;
import com.iscas.quickframe.service.OrganizationService;
import com.iscas.quickframe.service.ResourceService;
import com.iscas.quickframe.service.RoleService;
import com.iscas.quickframe.service.UrlAddrService;
import com.iscas.quickframe.service.UserService;

/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:测试一些权限增删改的级联关系
*/
@Controller
@RequestMapping("/testDB")
public class TestDBController {
	@Autowired
	private AppService appService;
	@Autowired
	private UrlAddrService urlAddrService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	  @RequestMapping(value="test1")
	  @ResponseBody
	  /**测试插入一个应用系统*/
	  public App test1(String appName,String desc){
		 App app = appService.newOneApp(appName, desc);
		 return app;
	  }
	  
	  @RequestMapping(value="test2")
	  @ResponseBody
	  /**测试删除一个应用系统*/
	  public String test2(Integer id){
		 appService.removeOneApp(id);
		 return "1111";
	  }
	  @RequestMapping(value="test3/{desc}")
	  @ResponseBody
	  /**测试创建一个URL*/
	  public UrlAddr test3(@PathVariable String desc){
		 UrlAddr urlAddr = new UrlAddr();
		
		 urlAddr.setUrl("/user/testUrl/**");
		 urlAddr.setDescription(desc);
		 Resource r = resourceService.findOne(7);
		 //可选直接设置资源进去
		 urlAddr.setResourcex(r);
		 
		 urlAddrService.save(urlAddr);
		 return urlAddr;
	  }
	  @RequestMapping(value="test4/{name}")
	  @ResponseBody
	  /**测试创建一个Resource*/
	  public Resource test4(@PathVariable String name){
		 Resource r = new Resource();
		 r.setPermissionname(name);
		 resourceService.save(r);
		 return r;
	  }
	  @RequestMapping(value="test5/{id}")
	  @ResponseBody
	  /**测试删除一个URL*/
	  public UrlAddr test5(@PathVariable Integer id){
		 UrlAddr urlAddr = urlAddrService.findOne(id);
		 urlAddrService.delete(urlAddr);
		 return urlAddr;
	  }
	  
	  @RequestMapping(value="test6/{id}")
	  @ResponseBody
	  /**测试修改一个URL的权限*/
	  public UrlAddr test6(@PathVariable Integer id){
		 UrlAddr urlAddr = urlAddrService.findOne(id);
		 Resource r = resourceService.findOne(6);
		 urlAddr.setResourcex(r);
		 urlAddrService.save(urlAddr);
		 return urlAddr;
	  }
	  @RequestMapping(value="test7/{id}")
	  @ResponseBody
	  /**测试删除一个Resource*/
	  public Resource test7(@PathVariable Integer id){
		Resource r = resourceService.findOne(id);
		resourceService.delete(r);
		return r;
				
	  }
	  
	  @RequestMapping(value="test8")
	  @ResponseBody
	  /**测试创建一个组织机构*/
	  public Organizational test8(){
		  Organizational pOrg = organizationService.findOne(1);
		  App app = appService.findOne(1);
		  Organizational org = new Organizational();
		  org.setParentOrg(pOrg);
		  org.setName("最高级节点1");
		  org.setApp(app);
		  organizationService.save(org);
		 
		  return org;
				
	  }
	  
	  @RequestMapping(value="test9/{id}")
	  @ResponseBody
	  /**测试删除一个组织机构*/
	  public Organizational test9(@PathVariable Integer id){
		  Organizational org = organizationService.findOne(id);
		  
		  organizationService.delete(org);
		 
		  return org;
				
	  }
	  
	  @RequestMapping(value="test10/{id}")
	  @ResponseBody
	  /**测试给一个组织机构分配角色*/
	  public Organizational test10(@PathVariable Integer id){
		  Organizational org = organizationService.findOne(id);
		  Role role = roleService.findOne(1);
		  List<Role> roles = org.getRoleList();
		  roles = new ArrayList<Role>(); 
		  roles.add(role);
		  org.setRoleList(roles);
		  organizationService.save(org);
		 
		  return org;
				
	  }
	  
	  @RequestMapping(value="test11/{id}")
	  @ResponseBody
	  /**测试给一个组织机构分配用户*/
	  public Organizational test11(@PathVariable Integer id){
		  Organizational org = organizationService.findOne(id);
		  User user =userService.findOne(1);
		  List<User> users = new ArrayList<User>();
		  users.add(user);
		  org.setUserList(users);
		  organizationService.save(org);
		 
		  return org;
				
	  }
	  
	  @RequestMapping(value="test12")
	  @ResponseBody
	  /**测试创建一个角色*/
	  public Role test12(){
		Role role = new Role();
		//创建一个角色的时候顺便给管理员组织机构节点给这个用户
		App app = appService.findOne(2);
		Organizational pOrg = organizationService.findByAppAndParentOrg(app, null);
		//Organizational org = organizationService.findOne(1);
		List<Organizational> orgs = new ArrayList<Organizational>();
		orgs.add(pOrg);
		role.setOrgList(orgs);
		role.setRolename("testRole2");
		roleService.save(role);
		
		
		
		return role;
				
	  }
	  
	  @RequestMapping(value="test13")
	  @ResponseBody
	  /**测试给一个角色 赋予 权限*/
	  public Role test13(){
		Role role = roleService.findOne(4);
		UrlAddr ua = urlAddrService.findOne(1);
		Resource r =  ua.getResourcex();
		List<Resource> rs = new ArrayList<Resource>();
		rs.add(r);
		role.setResourceList(rs);
		roleService.save(role);
		return role;
				
	  }
	  
	  @RequestMapping(value="test14/{id}")
	  @ResponseBody
	  /**测试删除一个角色*/
	  public Role test14(@PathVariable Integer id){
		Role role = roleService.findOne(id);
		
		
		
		roleService.delete(role);
		return role;		
	  }
	  @RequestMapping(value="test15/{id}")
	  @ResponseBody
	  /**测试查询一个角色*/
	  public Role test15(@PathVariable Integer id){
		Role role = roleService.findOne(id);
		return role;		
	  }
	  
	  @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/test16")
	  @ResponseBody
	  /**
	   * 测试返回一个List<Map>
	   * */
	  public List test16(){
		  String sql ="select * from t_app";
		  return appService.findToMap(sql);
	  }
	  @RequestMapping(value = "/test17")
	  @ResponseBody
	  /**
	   * 测试返回一个List<Map>
	   * */
	  public User test17(){
		 userService.findByUsername("tom");
		  return userService.findByUsernameAndAppName("tom", "app1");
	  }

}    

