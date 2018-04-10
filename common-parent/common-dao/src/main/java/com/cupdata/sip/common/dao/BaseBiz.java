package com.cupdata.sip.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Tony
 * @date 2018/03/15
 */
public abstract class BaseBiz<M extends Mapper<T>, T> {

    @Autowired
    protected M mapper;

    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

    public T selectOne(T entity) {
        return mapper.selectOne(entity);
    }


    public T selectById(Object id) {
        return mapper.selectByPrimaryKey(id);
    }


    public List<T> selectList(T entity) {
        return mapper.select(entity);
    }


    public List<T> selectListAll() {
        return mapper.selectAll();
    }


    public Long selectCount(T entity) {
        return new Long(mapper.selectCount(entity));
    }


    public void insert(T entity) {
        //EntityUtils.setCreatAndUpdatInfo(entity);
        mapper.insert(entity);
    }


    public void insertSelective(T entity) {
        //EntityUtils.setCreatAndUpdatInfo(entity);
        mapper.insertSelective(entity);
    }


    public void delete(T entity) {
        mapper.delete(entity);
    }


    public void deleteById(Object id) {
        mapper.deleteByPrimaryKey(id);
    }


    public void updateById(T entity) {
        //  EntityUtils.setUpdatedInfo(entity);
        mapper.updateByPrimaryKey(entity);
    }


    public void updateSelectiveById(T entity) {
        //  EntityUtils.setUpdatedInfo(entity);
        mapper.updateByPrimaryKeySelective(entity);

    }

    public List<T> selectByExample(Object example) {
        return mapper.selectByExample(example);
    }

    public int selectCountByExample(Object example) {
        return mapper.selectCountByExample(example);
    }

    //public TableResultResponse<T> selectByQuery(LinkedHashMap<String, Object> query) {
    //    Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    //    Example example = new Example(clazz);
    //    if (query.entrySet().size() > 0) {
    //        Example.Criteria criteria = example.createCriteria();
    //        for (Map.Entry<String, Object> entry : query.entrySet()) {
    //            criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
    //        }
    //    }
    //    Page<Object> result = PageHelper.startPage(Integer.parseInt(query.get("page").toString()), Integer.parseInt(query.get("limit").toString()));
    //    List<T> list = mapper.selectByExample(example);
    //    return new TableResultResponse<T>(result.getTotal(), list);
    //}

}
