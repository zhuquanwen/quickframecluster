package com.iscas.quickframe.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iscas.quickframe.domain.p.UrlAddr;
import com.iscas.quickframe.domain.p.UrlAddrDao;
import com.iscas.quickframe.service.UrlAddrService;
@Service
public class UrlAddrServiceImpl extends BasePServiceImpl<UrlAddr, UrlAddrDao>
									implements UrlAddrService{

	@Autowired
	private UrlAddrDao urlAddrDao;
	@Override
	public List<UrlAddr> findByAppName(String name) {
		List<UrlAddr> uas = new ArrayList<UrlAddr>();
		List<Integer> ids = urlAddrDao.findIdByAppName(name);
		if(ids != null){
			for (Integer id : ids) {
				uas.add(this.findOne(id));
			}
		}
		return uas;
	}

}
