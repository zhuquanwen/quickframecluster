package com.iscas.quickframe.config;


import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.iscas.quickframe.domain.p.Role;
import com.iscas.quickframe.domain.p.User;
import com.iscas.quickframe.domain.p.UserDao;
/**
*@auhor:zhuquanwen
*@date:2016年12月2日
*@desc:shiro整合 cas的权限认证
*/
@SuppressWarnings("deprecation")
public class MyShiroCasRealm extends CasRealm{

    private static final Logger logger = LoggerFactory.getLogger(MyShiroCasRealm.class);

    @Autowired
    private UserDao userDao; 
    @Autowired
    private CasConfig casConfig;
    @Value("${appName}")
    private String appName;
   
    public MyShiroCasRealm() {  
    	//定义一个realm名字
        setName("ShiroCasRealm"); 
        
    }  
    
    @PostConstruct
    public void initProperty(){
//      setDefaultRoles("ROLE_USER");
        setCasServerUrlPrefix(casConfig.getCasPrefix());
        // 客户端回调地址
        setCasService(casConfig.getShiroPrefix() + casConfig.getCasFilterUrlPattern());
    }

    /**
     * 权限认证，为当前登录的Subject授予角色和权限 
     * @see 经测试：本例中该方法的调用时机为需授权资源被访问时 
     * @see 经测试：并且每次访问需授权资源时都会执行该方法中的逻辑，这表明本例中默认并未启用AuthorizationCache 
     * @see 经测试：如果连续访问同一个URL（比如刷新），该方法不会被重复调用，Shiro有一个时间间隔（也就是cache时间，在ehcache-shiro.xml中配置），超过这个时间间隔再刷新页面，该方法会被执行
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("##################执行Shiro权限认证##################");
        //获取当前登录输入的用户名，等价于(String) principalCollection.fromRealm(getName()).iterator().next();
        String loginName = (String)super.getAvailablePrincipal(principalCollection); 
        //到数据库查是否有此对象
        //User user=userDao.findByUsername(loginName);// 实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        User user = userDao.findByUsernameAndAppName(appName,loginName);
        if(user!=null){
            //权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
            //用户的角色集合
            info.setRoles(user.getRolesName());
            //用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的四行可以不要
            List<Role> roleList=user.getRoleList();
            for (Role role : roleList) {
                info.addStringPermissions(role.getPermissionsName());
            }
            
            return info;
        }
        // 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
        return null;
    }
   
}