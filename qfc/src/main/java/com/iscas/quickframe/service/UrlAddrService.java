package com.iscas.quickframe.service;

import java.util.List;

import com.iscas.quickframe.domain.p.UrlAddr;
import com.iscas.quickframe.domain.p.UrlAddrDao;

public interface UrlAddrService extends BaseService<UrlAddr, UrlAddrDao>{
	public List<UrlAddr> findByAppName(String name); 
}
