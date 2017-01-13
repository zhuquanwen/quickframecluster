package com.iscas.quickframe.domain.p;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:组织机构表
*/
@Entity
@Table(name="t_organizational")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Organizational implements Serializable{
	 /**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	public Organizational() {
		super();
	}
	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;//id
	 @Column(length=50)
	 private String name;//组织机构名称
	 @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})  
	 @JoinColumn(name = "pid")
	 private Organizational parentOrg;//父节点Id
	 
	 @OneToMany(targetEntity = Organizational.class,  mappedBy = "parentOrg" ,cascade = {CascadeType.ALL})  
	 private List<Organizational> childOrgs;//当前拥有的子节点
	 
	 @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})  
	 @JoinColumn(name = "app_id")  
	 private App app;//当前节点所属的应用系统
	 
	 @ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE} )
	 @JoinTable(name = "t_org_role", joinColumns = { @JoinColumn(name = "org_id") }, inverseJoinColumns = {
	          @JoinColumn(name = "role_id") })
	 private List<Role> roleList;// 一个组织机构节点对应多个角色
	
	 @ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.REFRESH, CascadeType.PERSIST , CascadeType.MERGE})
	 @JoinTable(name = "t_org_user", joinColumns = { @JoinColumn(name = "org_id") }, inverseJoinColumns = {
	            @JoinColumn(name = "user_id") })
	 @JsonIgnore
	 private List<User> userList;// 一个组织结构节点对应着多个用户
	 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@SuppressWarnings("unchecked")
	public Organizational getParentOrg() {
		if(parentOrg != null){
			parentOrg.setChildOrgs(Collections.EMPTY_LIST);
			//parentOrg.setRoleList(null);
		}
		return parentOrg;
	}
	public void setParentOrg(Organizational parentOrg) {
		this.parentOrg = parentOrg;
	}
	public List<Organizational> getChildOrgs() {
		return childOrgs;
	}
	public void setChildOrgs(List<Organizational> childOrgs) {
		this.childOrgs = childOrgs;
	}
	public App getApp() {
		return app;
	}
	public void setApp(App app) {
		this.app = app;
	}
	public List<Role> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	 
}
