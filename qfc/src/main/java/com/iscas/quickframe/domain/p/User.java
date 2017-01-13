package com.iscas.quickframe.domain.p;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
*@auhor:zhuquanwen
*@date:2016年12月6日
*@desc:用户表
*/

@Entity
@Table(name = "t_user")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class User implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 929214121075003594L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique=true,length=32)
    private String username;
   
    private String password;    
//    @ManyToMany(fetch=FetchType.EAGER)
//    @JoinTable(name = "t_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
//            @JoinColumn(name = "role_id") })
//    @JsonIgnore
//    private List<Role> roleList;// 一个用户具有多个角色

    @ManyToMany(fetch=FetchType.EAGER,cascade = {CascadeType.REFRESH})
    @JoinTable(name = "t_org_user", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "org_id") })
    @JsonIgnore
    private List<Organizational> orgList;// 一个用户可能在多个组织机构节点
    
    
    public User() {
        super();
    }

    public User(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    // 省略 get set 方法

//    @Transient
//    public Set<String> getRolesName() {
//        List<Role> roles = getRoleList();
//        Set<String> set = new HashSet<String>();
//        for (Role role : roles) {
//            set.add(role.getRolename());
//        }
//        return set;
//    }

    @Transient
    public Set<String> getRolesName() {
    	Set<String> set = new HashSet<String>();
    	List<Organizational> orgs = getOrgList(); 
    	if(orgs != null && orgs.size() > 0){
    		Organizational org = orgs.get(0);
        	List<Role> roles = org.getRoleList();
        	if(roles != null){
        		for (Role role : roles) {
            		set.add(role.getRolename());
            	}
        	}
        	
        	
    	}
    	return set;
    }
    @Transient
	public List<Role> getRoleList() {
    	List<Role> roles = new ArrayList<Role>();
    	List<Organizational> orgs = getOrgList(); 
    	if(orgs != null && orgs.size() > 0){
    		Organizational org = orgs.get(0);
        	roles = org.getRoleList();
    	}
    	return roles;
	}
    @Transient
   	public List<String> getUrls() {
    	List<String> urls = new ArrayList<String>();
       	List<Role> roles = this.getRoleList();
       	for (int i = 0; i < roles.size(); i++) {
			Role role = roles.get(i);
			List<Resource> rs = role.getResourceList();
			if(rs != null){
				for (int j = 0; j < rs.size(); j++) {
					Resource r = rs.get(j);
					List<UrlAddr> uas = r.getUrlAddrList();
					if(uas != null){
						for (int k = 0; k < uas.size(); k++) {
							urls.add(uas.get(k).getUrl());
						}
					}
				}
			}
		}
       	return urls;
   	}
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	public List<Role> getRoleList() {
//		return roleList;
//	}
//
//	public void setRoleList(List<Role> roleList) {
//		this.roleList = roleList;
//	}

	public List<Organizational> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<Organizational> orgList) {
		this.orgList = orgList;
	}
    
}