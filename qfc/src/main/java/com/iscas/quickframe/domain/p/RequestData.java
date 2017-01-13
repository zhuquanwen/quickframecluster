package com.iscas.quickframe.domain.p;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:请求数据表
*/
@Entity
@Table(name="t_request_data")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class RequestData implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;
	 @Column(length=30)
	 private String type; //请求数据类型（数据结果）
	 @Column(length=80)
	 private String description; //描述
	 
	
	 /*@ManyToOne
	 @JoinColumn(name = "url_id")
	 private UrlAddr urlAddr;*/
	 @ManyToMany(fetch=FetchType.EAGER)
	 @JoinTable(name = "t_request_data_role", joinColumns = { @JoinColumn(name = "request_data_id") }, inverseJoinColumns = {
	        @JoinColumn(name = "role_id") })
	 @JsonIgnore
	 private List<Role> roleList;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Role> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
	 
}
