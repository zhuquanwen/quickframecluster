package com.iscas.quickframe.domain.p;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:应用系统表
*/
@Entity
@Table(name="t_app",uniqueConstraints = {
	      @UniqueConstraint(columnNames = {"name"})
	})
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class App implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public App() {
		super();
	}
	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;//id
	 @Column(length=32)
	 private String name;//应用名称
	 @Column(length=80)
	 private String description;//应用说明
	 @Column(length=150)
	 private String appAddr;
	 
	 @OneToMany(mappedBy = "app",fetch=FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.REMOVE})  
	 @JsonIgnore
	 private List<Organizational> orgs;//当前应用系统拥有的组织结构
	 
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Organizational> getOrgs() {
		return orgs;
	}
	public void setOrgs(List<Organizational> orgs) {
		this.orgs = orgs;
	}
	public String getAppAddr() {
		return appAddr;
	}
	public void setAppAddr(String appAddr) {
		this.appAddr = appAddr;
	}
	 
}
