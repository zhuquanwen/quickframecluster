package com.iscas.quickframe.domain.p;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
*@date:2016年12月13日
*@desc: 菜单实体
*/
@Entity
@Table(name = "t_menu")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Menu implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -567027535490320675L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;//id
	@Column
	private String name;//菜单名称
	
	@ManyToMany( cascade = {CascadeType.REFRESH} )
	@JoinTable(name = "t_menu_role", joinColumns = { @JoinColumn(name = "menu_id") }, inverseJoinColumns = {
	          @JoinColumn(name = "role_id") })
	@JsonIgnore
	private List<Role> roles;
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
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	
}
