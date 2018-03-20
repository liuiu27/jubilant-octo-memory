package com.cupdata.content.biz;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cupdata.commons.biz.BaseBiz;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.dao.BaseDao;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.content.ContentTransaction;
import com.cupdata.commons.vo.product.OrgProductRelVo;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.content.dao.ContentDao;
import com.cupdata.content.feign.ProductFeignClient;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月9日 下午5:19:05
*/
@Slf4j
@Service
public class ContentBiz extends BaseBiz<ContentTransaction> {
	
	@Resource
	private ProductFeignClient productFeignClient;
	
	@Autowired
	private ContentDao contentDao;
	
	@Override
	public BaseDao<ContentTransaction> getBaseDao() {
		return contentDao;
	}
	
	/**
	 * 查询产品号
	 * @param productNo
	 * @return
	 */
	public ProductInfVo findByProductNo(String productNo){
		log.info("ContentBiz  findByProductNo productNo is " + productNo);
	    BaseResponse<ProductInfVo> productInfRes = productFeignClient.findByProductNo(productNo);
		if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
				|| null == productInfRes.getData()) {// 如果查询失败
			log.error("procduct-service  findByProductNo result is null......  productNo is" + productNo
					+ " errorCode is " + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
			throw new ErrorException(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode(),ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
		}
		return productInfRes.getData();
	}
	
	/**
	 * 判断服务产品是否为内容 引入商品 是否与机构相关连
	 * @param org
	 * @param productType
	 * @param productNo
	 */
	public void validatedProductNo(String org,String productType,String productNo) {
		log.info("ContentBiz  validatedParams org is " + org + "productType is" + productType + "productNo is" + productNo);
		if (!ModelConstants.PRODUCT_TYPE_CONTENT.equals(productType)) {
			log.error("Not a content product.....poductType is" + productType
					+ " errorCode is " + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
			throw new ErrorException(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode(),ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
		}
		BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(org,productNo);
		if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode())
				|| null == orgProductRelRes.getData()) {
			log.error("procduct-service findRel result is null...org is" + org + "productNo is "
					+ productNo + " errorCode is "
					+ ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
			throw new ErrorException(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode(),ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
		}
	}
	
	/**
	 * 生成新的流水号并保存
	 * @param contentJumpReq
	 * @param productInfRes
	 * @return
	 */
	public void insertContentTransaction(String tranNo,String org,String requestInfo,ProductInfVo productInfRes) {
		//保存数据库
		ContentTransaction contentTransaction =  new  ContentTransaction();
		contentTransaction.setTranNo(tranNo);
		contentTransaction.setTranType(ModelConstants.CONTENT_TYPE_NOT_LOGGED);
		contentTransaction.setOrgNo(org);
		contentTransaction.setRequestInfo(requestInfo);
		if(null != productInfRes) {
			contentTransaction.setProductNo(productInfRes.getProduct().getProductNo());
			contentTransaction.setSupNo(productInfRes.getProduct().getSupplierNo());
		}
		
		
		contentDao.insert(contentTransaction);
	}
	
	/**
	 * 修改流水表
	 * @param contentTransaction
	 * @param productNo
	 * @param type
	 * @param org
	 * @param sup
	 * @param requestInfo
	 */
	public void updateContentTransaction(ContentTransaction contentTransaction,
			String productNo,
			String type,
			String org,
			String sup,
			String requestInfo) {
		contentTransaction.setProductNo(productNo);
		contentTransaction.setTranType(type);
		contentTransaction.setOrgNo(org);
		contentTransaction.setSupNo(sup);
		contentTransaction.setRequestInfo(requestInfo);
		contentDao.update(contentTransaction);
	}
	
	
	/**
	 * 根据流水号 及 交易类型  查询流水表
	 * @param tranNo
	 * @param type
	 * @return
	 */
	public ContentTransaction queryContentTransactionByTranNo(String tranNo,String type) {
		log.info("ContentBiz queryContentTransactionByTranNo tranNo is" + tranNo + "type" + type);
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("TRAN_NO", tranNo);
		if(StringUtils.isBlank(type)) {
			paramMap.put("TRAN_TYPE", type);
		}
		ContentTransaction contentTransaction = contentDao.selectSingle(paramMap);
		return contentTransaction;
	}
	
	/**
	 * 验证时间戳是否超时
	 * @param timestamp
	 */
	public void validatedtimestamp(String timestamp) {
		try {
			Date time = DateTimeUtil.getDateByString(timestamp.substring(0, 17),
					"yyyyMMddHHmmssSSS");
			// 时间戳超时
			if (DateTimeUtil.compareTime(DateTimeUtil.getCurrentTime(), time, -60 * 1000L, 3000 * 1000L)) {
				throw new ErrorException(ResponseCodeMsg.TIMESTAMP_TIMEOUT.getCode(),ResponseCodeMsg.TIMESTAMP_TIMEOUT.getMsg());
			}
		} catch (ParseException e) {
			log.error("error msg is " + e.getMessage());
		}
	}
	
	
}
