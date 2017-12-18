package com.cupdata.commons.dao;

import com.cupdata.commons.model.BaseModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName: BaseMapper
 * @Description: myBatis基础mapper
 * @author LinYong
 * @date 2017年3月20日 下午2:05:05
 *
 * @param <T>
 */
public interface BaseDao<T extends BaseModel> {
    /**
     * 增加
     * @param t
     */
    public Long insert(T t);

    /**
     * 删除
     * @param id
     * @return
     */
    public Integer delete(Serializable id);

    /**
     * 批量删除
     * @param list
     * @return
     */
    public Integer deleteBatch(List<T> list);

    /**
     * 更新
     * @param t
     */
    public Integer update(T t);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public T select(Serializable id);

    /**
     * 查询结果为单个对象
     * @param paramMap
     * @return
     */
    public T selectSingle(Map<String, Object> paramMap);

    /**
     * 不分页条件查询
     * @param paramMap
     * @return
     */
    public List selectAll(Map<String, Object> paramMap);

    /**
     * 分页条件查询
     * @param paramMap
     * @return
     */
    public List selectPage(Map<String, Object> paramMap);

    /**
     * 获取记录数量
     * @param parameter
     * @return
     */
    public Integer getRows(Map<String, Object> parameter);
}
