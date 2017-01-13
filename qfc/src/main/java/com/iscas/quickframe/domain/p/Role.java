package com.iscas.quickframe.domain.p;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:角色表
*/
@Entity
@Table(name = "t_role")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Role implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String rolename;
//    @OneToMany(mappedBy = "role", fetch=FetchType.EAGER,cascade = {CascadeType.REFRESH,CascadeType.REMOVE,CascadeType.MERGE,CascadeType.PERSIST})
//    private List<Permission> permissionList;// 一个角色对应多个权限
//    @ManyToMany
//    @JoinTable(name = "t_user_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
//            @JoinColumn(name = "user_id") })
//    private List<User> userList;// 一个角色对应多个用户

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
   	@JoinTable(name = "t_role_resource", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
   	            @JoinColumn(name = "resource_id") })
   	private List<Resource> resourceList;// 
    
    @ManyToMany(cascade = {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
	@JoinTable(name = "t_request_data_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
	            @JoinColumn(name = "request_data_id") })
	private List<RequestData> requestDataList;// 
             
    @ManyToMany(cascade = {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
	@JoinTable(name = "t_org_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
	          @JoinColumn(name = "org_id") })
    @JsonIgnore
	private List<Organizational> orgList;// 一个角色对应多 个组织结构节点

    @ManyToMany(cascade = {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.PERSIST})
   	@JoinTable(name = "t_menu_role", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
   	          @JoinColumn(name = "menu_id") })
    private List<Menu> menus;
    
//    @Transient
//    public List<String> getPermissionsName() {
//        List<String> list = new ArrayList<String>();
//        List<Permission> perlist = getPermissionList();
//        if(perlist != null){
//        	for (Permission per : perlist) {
////              list.add(per.getPermissionname());
//          	list.add(per.getResource().getPermissionname());
//          }
//          
//        }
//        return list;
//    }
    @Transient
    public List<String> getPermissionsName() {
        List<String> list = new ArrayList<String>();
        List<Resource> perlist = this.getResourceList();
        if(perlist != null){
        	for (Resource per : perlist) {
//              list.add(per.getPermissionname());
          	list.add(per.getPermissionname());
          }
          
        }
        return list;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

//	public List<Permission> getPermissionList() {
//		return permissionList;
//	}
//
//	public void setPermissionList(List<Permission> permissionList) {
//		this.permissionList = permissionList;
//	}

//	public List<User> getUserList() {
//		return userList;
//	}
//
//	public void setUserList(List<User> userList) {
//		this.userList = userList;
//	}

	public List<RequestData> getRequestDataList() {
		return requestDataList;
	}

	public void setRequestDataList(List<RequestData> requestDataList) {
		this.requestDataList = requestDataList;
	}

	public List<Organizational> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<Organizational> orgList) {
		this.orgList = orgList;
	}

	public List<Resource> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<Resource> resourceList) {
		this.resourceList = resourceList;
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
    
}