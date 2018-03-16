package com.cupdata.content.biz;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.BizException;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.content.dao.ServiceContentTransactionLogMapper;
import com.cupdata.content.domain.ServiceContentTransactionLog;
import com.cupdata.content.feign.OrderFeignClient;
import com.cupdata.content.feign.ProductFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 供应商业务处理
 * @author Tony
 * @date 2018/03/14
 */
@Slf4j
@Service
public class SupplierBiz {


    @Resource
    private ServiceContentTransactionLogMapper transactionLogMapper;

    @Resource
    private OrderFeignClient orderFeignClient;


    @Resource
    private ProductFeignClient productFeignClient;


    public ServiceContentTransactionLog checkTransaction(String tranNo){

        ServiceContentTransactionLog transactionLog = transactionLogMapper.selectByTranNo(tranNo);

        if (null == transactionLog){
            throw new BizException("","无效的流水号");
        }

        return  transactionLog;

    }

    public void alterTransactionLog(ServiceContentTransactionLog transactionLog){

        int flag = transactionLogMapper.updateByPrimaryKey(transactionLog);

    }


    public void createContentOrder(String tranNo) {

        BaseResponse response = orderFeignClient.createContentOrder();
        //创建失败
        if (!StringUtils.equals(response.getResponseCode(), ResponseCodeMsg.SUCCESS.getCode())){
            log.info(response.getResponseMsg());
            throw new BizException("","创建订单失败");
        }
    }


    public String getSourcePath(String tranNo){
        ServiceContentTransactionLog transactionLog = transactionLogMapper.selectByTranNo(tranNo);
        Map<String,Object> req = JSONObject.parseObject(transactionLog.getRequestInfo(),HashMap.class) ;
      // return product.getServiceApplicationPath();
        return req.get("payUrl").toString();
    }


    public String getSplicingParameters(){
        Map<String,String> parame =new HashMap<>();
        //TODO 2018/3/16 获取参数
        parame.put("timestamp","");
        parame.put("orderAmt","");
        parame.put("sipOrderNo","");
        parame.put("sipOrderTime","");
        parame.put("orderTitle","");
        parame.put("orderInfo","");
        parame.put("timeOut","");
        parame.put("payBackUrl","");
        parame.put("notifyUrl","");


        return signature(parame);
    }


    /**
     * 把map转换成链接
     * @param sortedParams
     * @return
     */
    public static String  signature(Map<String,String> sortedParams){
        StringBuffer content = new StringBuffer();
        List keys = new ArrayList(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = (String)keys.get(i);
            String value = sortedParams.get(key);
            content.append((index == 0 ? "" : "&") + key + "=" + value);
            index++;
        }
        return content.toString();
    }
}
