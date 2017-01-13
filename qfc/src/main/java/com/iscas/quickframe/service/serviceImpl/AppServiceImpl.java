package com.iscas.quickframe.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.iscas.quickframe.domain.p.App;
import com.iscas.quickframe.domain.p.AppDao;
import com.iscas.quickframe.domain.p.MenuDao;
import com.iscas.quickframe.domain.p.Organizational;
import com.iscas.quickframe.domain.p.OrganizationalDao;
import com.iscas.quickframe.domain.p.RequestDataDao;
import com.iscas.quickframe.domain.p.ResourceDao;
import com.iscas.quickframe.domain.p.RoleDao;
import com.iscas.quickframe.domain.p.UrlAddrDao;
import com.iscas.quickframe.domain.p.User;
import com.iscas.quickframe.domain.p.UserDao;
import com.iscas.quickframe.service.AppService;
import com.iscas.quickframe.util.EncodeDecodeUtil;

/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:应用系统的service
*/
@Service

public class AppServiceImpl extends BasePServiceImpl<App,AppDao> implements AppService{
	@Autowired
	private OrganizationalDao organizationalDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UrlAddrDao urlAddrDao;
	@Autowired
	private RequestDataDao requestDataDao;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private AppDao appDao;
//	@Autowired
//	private UserService userService;
	@Override
	public App newOneApp(String appName,String desc) {
		App app = new App();
		app.setName(appName);
		app.setDescription(desc);
		this.save(app);
		//为此应用创建超级管理员和管理员节点
		Organizational org1 = new Organizational();
		org1.setApp(app);
		org1.setName(appName+"管理员节点");
		//读取系统内置的超级管理员
		User superUser = userDao.findByUsername("admin");
		//生成每个系统对应的一个管理员
		User manageUser = new User();
		manageUser.setUsername(appName + "_manager");
		manageUser.setPassword(EncodeDecodeUtil.md5(appName + "_manager", null));
		ArrayList<User> userList = new ArrayList<User>();
		userList.add(superUser);
		userList.add(manageUser);
		org1.setUserList(userList);
		organizationalDao.save(org1);
		return app;
	}
	@Override
	public void removeOneApp(Integer id) {
		 App app = this.findOne(id);
		
//		 //删除此应用对应的所有url信息
//		 urlAddrDao.deleteByAppId(id);
//		 //删除此应用对应的所有requestData信息
//		 requestDataDao.deleteRequestRoleByAppId(id);
//		 requestDataDao.deleteByAppId(id);
//		 //删除此应用对应的所有权限资源
//		 resourceDao.deleteResourceRoleByAppId(id);
//		 resourceDao.deleteByAppId(id);
//		 //删除此应用对应的所有角色
//		 roleDao.deleteRoleOrgByAppId(id);
//		 roleDao.deleteByAppId(id);
		 
		//删除此应用对应的所有url信息
		 List<Integer> ids1 = urlAddrDao.findByAppId(id);
		 this.deleteByAppId(ids1, urlAddrDao);
		 //删除此应用对应的所有requestData信息
		 List<Integer> ids2 = requestDataDao.findByAppId(id);
		 this.deleteByAppId(ids2, requestDataDao);
		 //删除此应用对应的所有menu信息
		 List<Integer> ids5 = menuDao.findByAppId(id);
		 this.deleteByAppId(ids5, menuDao);
		
		 
		 //删除此应用对应的所有权限资源
		 List<Integer> ids3 = resourceDao.findByAppId(id);
		 this.deleteByAppId(ids3, resourceDao);
		 //删除此应用对应的所有角色
		 
		 List<Integer> ids4 = roleDao.findByAppId(id);
		 this.deleteByAppId(ids4, roleDao);
		 //删除系统自动生成的管理员用户
		 String manageName = app.getName() + "_manager";
		 User user = userDao.findByUsername(manageName);
		 userDao.delete(user);
		 this.delete(app);
		
		
		 
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void deleteByAppId(List<Integer> ids ,JpaRepository dao){
		if(ids != null){
			for (int i = 0; i < ids.size(); i++) {
				Object obj = dao.findOne(ids.get(i));
				dao.delete(obj);
			}
		}
	}
	@Override
	public App findByName(String name) {
		
		return appDao.findByName(name);
	}
}
