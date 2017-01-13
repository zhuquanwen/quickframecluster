package com.iscas.quickframe.domain.p;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:URL定义表
*/
@Entity
@Table(name="t_url_addr")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class UrlAddr implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length=80)
	private String url;
	@Column(length=50)
	private String description;
	@ManyToOne(  cascade={CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name = "resource_id")
	private Resource resourcex;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Resource getResourcex() {
		return resourcex;
	}
	public void setResourcex(Resource resourcex) {
		this.resourcex = resourcex;
	}
	
}
