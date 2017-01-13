package com.iscas.quickframe.domain.p;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:权限资源表
*/
@Entity
@Table(name="t_resource")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Resource implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;
	 
	 private String permissionname;
	 
	 @ManyToMany(cascade = {CascadeType.REFRESH})
	   	@JoinTable(name = "t_role_resource", joinColumns = { @JoinColumn(name = "resource_id") }, inverseJoinColumns = {
	   	            @JoinColumn(name = "role_id") })
	 @JsonIgnore
	 private List<Role> roleList;// 
	 
	
	 
	 @OneToMany(mappedBy = "resourcex", cascade={CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.REMOVE})
	 @JsonIgnore
	 private List<UrlAddr> urlAddrList;
	 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getPermissionname() {
		return permissionname;
	}
	public void setPermissionname(String permissionname) {
		this.permissionname = permissionname;
	}
	public List<UrlAddr> getUrlAddrList() {
		return urlAddrList;
	}
	public void setUrlAddrList(List<UrlAddr> urlAddrList) {
		this.urlAddrList = urlAddrList;
	}
	public List<Role> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
	 
}
