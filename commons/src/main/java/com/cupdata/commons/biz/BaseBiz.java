package com.cupdata.commons.biz;


import com.cupdata.commons.constant.PageConstant;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.model.BaseModel;
import com.cupdata.commons.page.Page;
import com.cupdata.commons.page.Result;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: BaseServiceImpl 
* @Description: 基础service实现类
* @author LinYong 
* @date 2017年3月20日 下午9:11:02 
* 
* @param <T>
 */
public abstract class BaseBiz<T extends BaseModel> {
	/**
	 * 获取mapper的抽象方法。
	 * 继承该类的子类必须实现该方法
	 * @return
	 */
	public abstract BaseDao<T> getBaseDao();

	@Transactional(rollbackFor=Exception.class)
	public T insert(T t) {
		getBaseDao().insert(t);
		return t;
	}

	@Transactional(rollbackFor=Exception.class)
	public Integer delete(Serializable id) {
		return getBaseDao().delete(id);
	}

	@Transactional(rollbackFor=Exception.class)
	public Integer deleteBatch(List<T> list) {
		return getBaseDao().deleteBatch(list);
	}

	@Transactional(rollbackFor=Exception.class)
	public Integer update(T t) {
		return getBaseDao().update(t);
	}

	@Transactional(propagation= Propagation.NOT_SUPPORTED,readOnly=true)
	public T select(Serializable id) {
		return getBaseDao().select(id);
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public T selectSingle(Map<String, Object> paramMap) {
		return getBaseDao().selectSingle(paramMap);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public List selectAll(Map<String, Object> paramMap) {
		return getBaseDao().selectAll(paramMap);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public Result selectPage(Map<String, Object> paramMap) {
		if(null == paramMap){
			paramMap = new HashMap<String, Object>();
		}
		
		convertParamMap(paramMap);

		Integer currentPage = (Integer) paramMap.get(PageConstant.CURRENT_PAGE);
		if(null == currentPage){
			currentPage = 1;
		}
		Integer pageSize = (Integer) paramMap.get(PageConstant.PAGE_SIZE);
		if(null == pageSize){
			pageSize = PageConstant.DEFAULT_PAGE_SIZE;
		}

		//分页查询返回结果
		Result result = new Result();
		
		//分页page信息
		Page page = new Page();
		Integer rowCounts = getBaseDao().getRows(paramMap);//总记录数
		page.setPage(currentPage, rowCounts, PageConstant.DEFAULT_PAGE_SIZE);//设置page
		result.setPage(page);

		paramMap.put(PageConstant.OFFSET, (currentPage - 1) * pageSize);//
		paramMap.put(PageConstant.PAGE_SIZE, pageSize);//每页记录大小
		
		result.setContent(getBaseDao().selectPage(paramMap));
		return result;
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public Integer getRows(Map<String, Object> paramMap) {
		return getBaseDao().getRows(paramMap);
	}
	
	/**
	 * 转换查询参数
	 * @param paramMap
	 */
	private void convertParamMap(Map<String, Object> paramMap){
		for(Iterator<Map.Entry<String, Object>> it = paramMap.entrySet().iterator();it.hasNext();){
			Map.Entry<String, Object> entry = it.next();
			Object value = entry.getValue();
			if(value instanceof String && "".equals(((String)value).trim())){
				it.remove();
			}
		}		
		
		Object _offset = paramMap.get(PageConstant.OFFSET);
		Object _currentPage = paramMap.get(PageConstant.CURRENT_PAGE);
		Object _pageSize = paramMap.get(PageConstant.PAGE_SIZE);
		
		Integer offset = PageConstant.DEFAULT_OFFSET;
		Integer currentPage = PageConstant.DEFAULT_CURRENT_PAGE;
		Integer pageSize = PageConstant.DEFAULT_PAGE_SIZE;
		
		if(_currentPage instanceof Integer){
			currentPage = (Integer) _currentPage;
		}else if(_currentPage instanceof String && ((String)_currentPage).matches("[0-9]*")){
			currentPage = Integer.parseInt((String) _currentPage);
		}
		paramMap.put(PageConstant.CURRENT_PAGE, currentPage);
		
		if(_pageSize instanceof Integer){
			pageSize = (Integer) _pageSize;
		}else if(_pageSize instanceof String && ((String)_pageSize).matches("[0-9]*")){
			pageSize = Integer.parseInt((String) _pageSize);
		}
		paramMap.put(PageConstant.PAGE_SIZE, pageSize);
	}
}
