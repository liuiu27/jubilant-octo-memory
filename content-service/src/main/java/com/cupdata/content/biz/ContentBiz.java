package com.cupdata.content.biz;

import com.cupdata.content.dto.ContentTransactionLogDTO;
import com.cupdata.content.exception.ContentException;
import com.cupdata.content.feign.ProductFeignClient;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.product.response.OrgProductRelVo;
import com.cupdata.sip.common.dao.entity.ServiceContentTransactionLog;
import com.cupdata.sip.common.dao.mapper.ServiceContentTransactionLogMapper;
import com.cupdata.sip.common.lang.BeanCopierUtils;
import com.cupdata.sip.common.lang.EntityUtils;
import com.cupdata.sip.common.lang.RSAHelper;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.exception.ErrorException;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.ParseException;
import java.util.Date;

/**
 * @author 作者: liwei
 * @createDate 创建时间：2018年3月9日 下午5:19:05
 */
@Slf4j
@Service
public class ContentBiz {

    @Resource
    private ProductFeignClient productFeignClient;

    @Autowired
    private ServiceContentTransactionLogMapper serviceContentTransactionLogMapper;

    /**
     * 查询产品号
     *
     * @param productNo
     * @return
     */
    //public ProductInfVo findByProductNo(String productNo) {
    //    log.info("ContentBiz  findByProductNo productNo is " + productNo);
    //    BaseResponse<ProductInf0Vo> productInfRes = productFeignClient
    //    if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfRes.getResponseCode())
    //            || null == productInfRes.getData()) {// 如果查询失败
    //        log.error("procduct-service  findByProductNo result is null......  productNo is" + productNo
    //                + " errorCode is " + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
    //        throw new ErrorException(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode(), ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
    //    }
    //    return productInfRes.getData();
    //}

    /**
     * 判断服务产品是否为内容 引入商品 是否与机构相关连
     *
     * @param org
     * @param productType
     * @param productNo
     */
    public void validatedProductNo(String org, String productType, String productNo) {
        log.info("ContentBiz  validatedParams org is " + org + "productType is" + productType + "productNo is" + productNo);
        if (!ModelConstants.PRODUCT_TYPE_CONTENT.equals(productType)) {
            log.error("Not a content product.....poductType is" + productType
                    + " errorCode is " + ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode());
            throw new ErrorException(ResponseCodeMsg.PRODUCT_NOT_EXIT.getCode(), ResponseCodeMsg.PRODUCT_NOT_EXIT.getMsg());
        }
        BaseResponse<OrgProductRelVo> orgProductRelRes = productFeignClient.findRel(org, productNo);
        if (!ResponseCodeMsg.SUCCESS.getCode().equals(orgProductRelRes.getResponseCode())
                || null == orgProductRelRes.getData()) {
            log.error("procduct-service findRel result is null...org is" + org + "productNo is "
                    + productNo + " errorCode is "
                    + ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode());
            throw new ErrorException(ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getCode(), ResponseCodeMsg.ORG_PRODUCT_REAL_NOT_EXIT.getMsg());
        }
    }


    /**
     * 根据流水号 及 交易类型  查询流水表
     *
     * @param sipTranNo
     * @param type
     * @return
     */
    public ContentTransactionLogDTO queryContentTransactionByTranNo(String sipTranNo, String type) {
        log.info("ContentBiz queryContentTransactionByTranNo tranNo is" + sipTranNo + "type" + type);
        ContentTransactionLogDTO transactionLogDTO =new ContentTransactionLogDTO();
        ServiceContentTransactionLog contentTransaction =new ServiceContentTransactionLog();
        contentTransaction.setTranNo(sipTranNo);
        contentTransaction.setTranType(type);
        ServiceContentTransactionLog serviceContentTransactionLog = serviceContentTransactionLogMapper.selectOne(contentTransaction);
        if (null == serviceContentTransactionLog){
            throw new ContentException();
        }
        BeanCopierUtils.copyProperties(serviceContentTransactionLog,transactionLogDTO);
        return transactionLogDTO;
    }

    /**
     * 验证时间戳是否超时
     *
     * @param timestamp
     */
    public void validatedtimestamp(String timestamp) {

        Date time = null;
        try {
            time = DateTimeUtil.getDateByString(timestamp.substring(0, 17),
                    "yyyyMMddHHmmssSSS");
        } catch (ParseException e) {
			log.info("timestamp ParseException ,info: "+timestamp);
        }
        // 时间戳超时
        if (DateTimeUtil.compareTime(DateTimeUtil.getCurrentTime(), time, -60 * 1000L, 3000 * 1000L)) {
            throw new ErrorException(ResponseCodeMsg.TIMESTAMP_TIMEOUT.getCode(), ResponseCodeMsg.TIMESTAMP_TIMEOUT.getMsg());
        }

    }

    /**
     * 插入或更新 登陆 流水记录
     * @param transactionLogDTO
     */
    @Transactional
    public void insertAndUpdateContentTransaction(ContentTransactionLogDTO transactionLogDTO) {

        if (null == transactionLogDTO) {
            log.info("transactionLogDTO 不能为空！！！");
            throw new RuntimeException("qweqwe");
        }
        ServiceContentTransactionLog contentTransaction = new ServiceContentTransactionLog();

        BeanCopierUtils.copyProperties(transactionLogDTO, contentTransaction);

        ServiceContentTransactionLog serviceContentTransactionLog = serviceContentTransactionLogMapper.selectOne(contentTransaction);

        if (null == serviceContentTransactionLog) {
            EntityUtils.setEntityInfo(contentTransaction, EntityUtils.cfields);
            serviceContentTransactionLogMapper.insert(contentTransaction);
        } else {
            contentTransaction.setId(serviceContentTransactionLog.getId());
            EntityUtils.setEntityInfo(contentTransaction, EntityUtils.ufields);
            serviceContentTransactionLogMapper.updateByPrimaryKeySelective(contentTransaction);
        }

    }

    public String createRequseUrl(String url,String parameter,String publicKey,String privateKey) throws Exception {

        PublicKey pemPublicKey = RSAHelper.getPemPublicKey(publicKey);
        PrivateKey pemPrivateKey = RSAHelper.getPemPrivateKey(privateKey);

        String encipher = RSAHelper.encipher(parameter, pemPublicKey, 0);
        String sign = RSAHelper.sign(parameter, pemPrivateKey);
        if (url.indexOf("?") == -1) {
            url = url + "?";
        } else {
            url = url + "&";
        }
        url = url + "data=" + encipher + "&sign=" + sign;
        return url;
    }
}
