package com.iscas.quickframe.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;




@SuppressWarnings("rawtypes")
public interface BaseService<R extends Object,T extends JpaRepository> {
	public List<R> findAll();
	public Page<R> findAll(Pageable p);
	
	public Object save(R r);
	
	public R findOne(Serializable s);
	
	public void delete(R r);
	
	//。。。。。。
	void delete(List<R> rs);
	
	public long count();
	
	public long count(Example exa);
	
	public void delete(Iterable<R> rs);
		
	
	public void delete(Serializable r);
	
	
	public void deleteAll();
	public void deleteAllInBatch();
	public boolean exists(Example exa);
	public boolean exists(Serializable r);
	public List<R> findAll(Example ex);
	public List<R> findAll(Iterable ids);
	public List<R> findAll(Sort sort);
	
	public Page<R> findAll(Example exa, Pageable p);
	public List<R> findAll(Example exa, Sort sort);
	public R findOne(Example exa);
	
	public void flush();
	public R getOne(Serializable r);
	public List<R> save(Iterable<R> rs);
	public R saveAndFlush(R r);
	
	/** 
	 * @Title: findToMap 查询结果集为List<Map>
	 * @Description: 按照SQL语句查询，结果集放入List<Map>，Map key为数据库字段大写
	 * @param  nativeSql 本地sql方法
	 * @return List
	 * @throws 
	 */
	public List findToMap(String nativeSql);
	public void deleteInBatch(Iterable<R> entities);

}
