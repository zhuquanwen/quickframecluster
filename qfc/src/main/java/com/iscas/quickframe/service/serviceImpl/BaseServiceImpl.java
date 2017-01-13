package com.iscas.quickframe.service.serviceImpl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iscas.quickframe.service.BaseService;

/**
 * 基础框架数据库操作的基类service
 * */
@SuppressWarnings("rawtypes")
@Transactional(value="transactionManagerSecond",isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED)
public class BaseServiceImpl<R extends Object,T extends JpaRepository> implements BaseService<R,T>{
	@Autowired
	private T t;
	@Autowired
	@Qualifier(value="entityManagerSecond")
	private EntityManager entityManager;
	
	/** 
	 * @Title: findAll 
	 * @Description: 查询全部
	 * @return List<R>
	 * @see com.iscas.service.BaseService#findAll() 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<R> findAll() {
		
		return t.findAll();
	}

	
	/** 
	 * @Title: findAll 
	 * @Description: 分页查询全部
	 * @param p 分页对象
	 * @return Page<R>
	 * @see com.iscas.service.BaseService#findAll(org.springframework.data.domain.Pageable) 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page<R> findAll(Pageable p) {
		return t.findAll(p);
	}

	
	/** 
	 * @Title: save 
	 * @Description: 保存对象
	 * @param r 要保存的对象
	 * @return 
	 * @see com.iscas.service.BaseService#save(java.lang.Object) 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object save(R r) {
		return t.save(r);
	}

	/** 
	 * @Title: findOne 
	 * @Description: 按照主键查询
	 * @param s 主键
	 * @return 
	 * @see com.iscas.service.BaseService#findOne(java.io.Serializable) 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public R findOne(Serializable s) {
		
		return (R) t.findOne(s);
	}
	
	/** 
	 * @Title: delete 
	 * @Description: 在数据库删除对象
	 * @param r 要删除的对象
	 * @see com.iscas.service.BaseService#delete(java.lang.Object) 
	 */
	@SuppressWarnings("unchecked")
	public void delete(R r){
		t.delete(r);
		
	}

	/** 
	 * @Title: delete 
	 * @Description: 删除对象集合
	 * @param rs 要删除的对象集合
	 * @see com.iscas.service.BaseService#delete(java.util.List) 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void delete(List<R> rs) {
		t.delete(rs);
		  
	}

	/** 
	 * @Title: count 
	 * @Description: 总条目
	 * @return long 总条目
	 * @throws 
	 */
	public long count(){
		return t.count();
		
	}
	@SuppressWarnings("unchecked")
	public long count(Example exa){
		return t.count(exa);
	}
	@SuppressWarnings("unchecked")
	public void delete(Iterable<R> rs){
		t.delete(rs);
	}
	
	@SuppressWarnings("unchecked")
	public void delete(Serializable r){
		t.delete(r);
	}
	
	public void deleteAll(){
		t.deleteAll();
	}
	public void deleteAllInBatch(){
		t.deleteAllInBatch();
	}
	@SuppressWarnings("unchecked")
	public boolean exists(Example exa){
		return t.exists(exa);
	}
	@SuppressWarnings("unchecked")
	public boolean exists(Serializable r){
		return t.exists(r);
	}
	@SuppressWarnings("unchecked")
	public List<R> findAll(Example ex){
		return t.findAll(ex);
	}
	@SuppressWarnings("unchecked")
	public List<R> findAll(Iterable ids){
		return t.findAll(ids);
	}
	@SuppressWarnings("unchecked")
	public List<R> findAll(Sort sort){
		return t.findAll(sort);
	}
	
	@SuppressWarnings("unchecked")
	public Page<R> findAll(Example exa, Pageable p){
		return t.findAll(exa, p);
	}
	@SuppressWarnings("unchecked")
	public List<R> findAll(Example exa, Sort sort){
		return t.findAll(exa, sort);
	}
	@SuppressWarnings("unchecked")
	public R findOne(Example exa){
		return (R)t.findOne(exa);
	}
	
	public void flush(){
		t.flush();
	}
	@SuppressWarnings("unchecked")
	public R getOne(Serializable r){
		return (R) t.getOne(r);
	}
	@SuppressWarnings("unchecked")
	public List<R> save(Iterable<R> rs){
		return t.save(rs);
	}
	@SuppressWarnings("unchecked")
	public R saveAndFlush(R r){
		return (R)t.saveAndFlush(r);
		
	}


	@Override
	public List findToMap(String nativeSql) {
		  Query query = entityManager.createNativeQuery(nativeSql);  
		  query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);  
		  List rows = query.getResultList();  
		  return rows;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void deleteInBatch(Iterable<R> entities) {
		t.deleteInBatch(entities);
		
	}


	
	

	

}
