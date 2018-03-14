package com.cupdata.content.biz;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.vo.content.ContentTransaction;
import com.cupdata.content.dao.ContentDao;
import org.springframework.stereotype.Service;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月9日 下午5:19:05
*/
@Service
public class ContentBiz  extends BaseBiz<ContentTransaction> {
	
	//@Autowired
	private ContentDao contentDao;
	
	@Override
	public BaseDao<ContentTransaction> getBaseDao() {
		return contentDao;
	}
	
}
