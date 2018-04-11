
package com.cupdata.ihuyi.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.model.ServiceOrder;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.order.ServiceOrderList;
import com.cupdata.ihuyi.feign.CacheFeignClient;
import com.cupdata.ihuyi.feign.NotifyFeignClient;
import com.cupdata.ihuyi.feign.OrderFeignClient;
import com.cupdata.ihuyi.utils.IhuyiUtils;
import com.cupdata.ihuyi.vo.IhuyiOrderQueryRes;
import com.cupdata.ihuyi.vo.IhuyiVirtualOrderQueryRes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: DingCong
 * @Description: 互亿查询轮询任务(定时去查询互亿方充值订单)
 * @CreateDate: 2018/3/13 18:51
 */

@Slf4j
@Component
public class IhuyiSchedule {

    @Autowired
    private  CacheFeignClient cacheFeignClient ;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private NotifyFeignClient notifyFeignClient;

    /**
     * 互亿流量充值轮询（每5分钟去循环查询）
     * @throws Exception
     */
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void scheduleIhuyiTrafficRecharge() throws Exception {
        log.info("互亿流量充值订单轮询开始**********************************************");
        int timer = 0;
        try {
            //查询参数
            Character orderStatus = ModelConstants.ORDER_STATUS_HANDING;         //支付状态为处理中1
            String orderSubType = ModelConstants.ORDER_TYPE_RECHARGE_TRAFFIC;    //订单类型是流量充值
            String merchantFlag = ModelConstants.ORDER_MERCHANT_FLAG_IHUYI;      //订单是互亿的
            BaseResponse<ServiceOrderList> serviceOrderRes = orderFeignClient.getServiceOrderListByParam(orderStatus, orderSubType, merchantFlag);
            if (!serviceOrderRes.getResponseCode().equals("000000")) {
                log.info("互亿订单列表信息获取失败... Ihuyi Order list acquisition failure");
                return;
            }
            if (CommonUtils.isNullOrEmptyOfObj(serviceOrderRes.getData())) {
                log.info("互亿订单列表数据为空... Ihuyi Order list data is null");
                return;
            }
            //获取订单列表
            List<ServiceOrder> orderList = serviceOrderRes.getData().getServiceOrderList();
            for (ServiceOrder order : orderList) {
                timer += 1 ;
                //调用互亿订单接口去查询订单
                log.info("订单号:"+order.getOrderNo()+",商户标识:"+order.getSupplierFlag()+",订单子类型:"+order.getOrderSubType());

                //拿到订单去互亿查询该订单的充值状态
                IhuyiOrderQueryRes queryRes = IhuyiUtils.ihuyiRechargeQuery(order,cacheFeignClient);
                log.info("轮询互亿流量订购结果："+JSON.toJSONString(queryRes));

                if (1 == queryRes.getCode()) { //提交成功
                    //如果商户订单号为空，则更新商户订单号
                    if (StringUtils.isEmpty(order.getSupplierOrderNo())) {
                        order.setSupplierOrderNo(queryRes.getTaskid());
                        Integer  i= orderFeignClient.updateServiceOrder(order);
                        log.info("供应商订单号为空,更新供应商订单号,供应商订单号:"+queryRes.getTaskid());
                    }
                    if (2 == queryRes.getStatus()) { //充值成功
                        order.setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
                        Integer i = orderFeignClient.updateServiceOrder(order);
                        log.info("充值成功,更新主订单状态为充值成功,更新订单数量为:"+i );
                        //互亿流量充值成功,通知机构
                        log.info("通知机构充值成功");
                        notifyFeignClient.rechargeNotifyToOrg3Times(order.getOrderNo());
                    } else if (3 == queryRes.getStatus()) { //充值失败
                        String orderFailDesc = order.getOrderFailDesc();
                        if (StringUtils.isEmpty(orderFailDesc)) {
                            orderFailDesc = "互亿流量充值失败";
                        }
                        order.setOrderFailDesc(orderFailDesc);
                        Integer i = orderFeignClient.updateServiceOrder(order);
                        log.info("互亿流量充值失败,更新主订单失败描述,更新数量为:"+i );
                        //互亿流量充值失败,通知机构
                        log.info("通知机构充值失败");
                        notifyFeignClient.rechargeNotifyToOrg3Times(order.getOrderNo());
                    } else {
                        log.info("轮询互亿流量充值订单状态,结果为："+JSONObject.toJSONString(queryRes));
                    }
                }
            }
        } catch (Exception e) {
            log.info("互亿订单轮询出现异常...IhuyiOrderGetException" + e.getMessage());
            return;
        }
        log.info("互亿流量充值订单轮询结束,本次轮训"+timer+"笔订单*****************************");
    }


    /**
     * 互亿话费充值轮询（每5分钟去循环查询）
     * @throws Exception
     */
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void scheduleIhuyiPhoneRecharge() throws Exception {
        log.info("互亿话费充值订单轮询开始**********************************************");
        int timer = 0;
        try {
            //step1.查询参数:支付状态为处理中,订单子类型为话费充值,订单是互亿的
            Character orderStatus = ModelConstants.ORDER_STATUS_HANDING;         //支付状态为处理中1
            String orderSubType = ModelConstants.ORDER_TYPE_RECHARGE_PHONE;      //订单类型是话费充值
            String merchantFlag = ModelConstants.ORDER_MERCHANT_FLAG_IHUYI;      //订单是互亿的
            BaseResponse<ServiceOrderList> serviceOrderRes = orderFeignClient.getServiceOrderListByParam(orderStatus, orderSubType, merchantFlag);
            if (!serviceOrderRes.getResponseCode().equals("000000")) {
                log.info("互亿订单列表信息获取失败... Ihuyi Order list acquisition failure");
                return;
            }
            if (CommonUtils.isNullOrEmptyOfObj(serviceOrderRes.getData())) {
                log.info("互亿订单列表数据为空... Ihuyi Order list data is null");
                return;
            }

            //step2.获取订单列表
            List<ServiceOrder> orderList = serviceOrderRes.getData().getServiceOrderList();

            for (ServiceOrder order : orderList) {
                //调用互亿订单接口去查询订单
                timer += 1 ;
                log.info("订单号:"+order.getOrderNo()+",商户标识:"+order.getSupplierFlag()+",订单子类型:"+order.getOrderSubType());
                IhuyiOrderQueryRes queryRes = IhuyiUtils.ihuyiRechargeQuery(order,cacheFeignClient);
                log.info("轮询互亿话费订购结果："+JSON.toJSONString(queryRes));
                if (1 == queryRes.getCode()) { //提交成功
                    //如果商户订单号为空，则更新商户订单号
                    if (StringUtils.isEmpty(order.getSupplierOrderNo())) {
                        order.setSupplierOrderNo(queryRes.getTaskid());
                        Integer  i= orderFeignClient.updateServiceOrder(order);
                        log.info("供应商订单号为空,更新供应商订单号,供应商订单号:"+queryRes.getTaskid());
                    }
                    if (2 == queryRes.getStatus()) { //充值成功
                        order.setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
                        Integer i = orderFeignClient.updateServiceOrder(order);
                        log.info("互亿话费充值成功,通知机构并更新主订单状态,更新数量为:"+i );
                        notifyFeignClient.rechargeNotifyToOrg3Times(order.getOrderNo());
                    } else if (3 == queryRes.getStatus()) { //充值失败
                        String orderFailDesc = order.getOrderFailDesc();
                        if (StringUtils.isEmpty(orderFailDesc)) {
                            orderFailDesc = "互亿话费充值失败";
                        }
                        order.setOrderFailDesc(orderFailDesc);
                        Integer i = orderFeignClient.updateServiceOrder(order);
                        log.info("互亿话费充值失败,通知机构并更新主订单失败描述,更新数量为:"+i );
                        notifyFeignClient.rechargeNotifyToOrg3Times(order.getOrderNo());
                    } else {
                        log.info("轮询互亿话费充值订单状态，结果为："+JSONObject.toJSONString(queryRes));
                    }
                }
            }
        } catch (Exception e) {
            log.info("互亿订单轮询出现异常...IhuyiOrderGetException" + e.getMessage());
            return;
        }
        log.info("互亿话费充值订单轮询结束,本次轮询"+timer+"笔订单*****************************");
    }


    /**
     * 互亿虚拟充值轮询（每5分钟去循环查询）
     * @throws Exception
     */
    //@Scheduled(cron = "0 0/5 * * * ?")
    public void scheduleIhuyiVirtualRecharge() throws Exception {
        log.info("互亿虚拟充值订单轮询开始**********************************************");
        int timer = 0;
        // 1.查询订单类型为:RECHARGE_*（多种虚拟产品）,订单状态:1（处理中）支付状态2（支付成功）,商户标识为:IHUYI(互亿)的合作商户订单号
        try {
            //查询参数
            Character orderStatus = ModelConstants.ORDER_STATUS_HANDING;          //支付状态为处理中1
            String merchantFlag = ModelConstants.ORDER_MERCHANT_FLAG_IHUYI;       //订单是互亿的
            List<String> orderTypes = new ArrayList<String>();                         //类型为虚拟充值的
            orderTypes.add(ModelConstants.ORDER_TYPE_RECHARGE_GAME);
            orderTypes.add(ModelConstants.ORDER_TYPE_RECHARGE_VIDEO);
            orderTypes.add(ModelConstants.ORDER_TYPE_RECHARGE_BOOK);
            orderTypes.add(ModelConstants.ORDER_TYPE_RECHARGE_MUSIC);
            orderTypes.add(ModelConstants.ORDER_TYPE_RECHARGE_SOCIAL);
            String join = JSON.toJSONString(orderTypes);             //虚拟充值类型
            List<ServiceOrder> orderList = orderFeignClient.selectMainOrderList(orderStatus,merchantFlag,join);
            if (CommonUtils.isNullOrEmptyOfObj(orderList)) {
                log.info("互亿订单列表数据为空... Ihuyi Order list data is null");
                return;
            }
            //获取订单列表
            for (ServiceOrder order : orderList) {
                timer += 1;
                //调用互亿订单接口去查询订单
                log.info("订单号:"+order.getOrderNo()+",商户标识:"+order.getSupplierFlag()+",订单子类型:"+order.getOrderSubType());
                IhuyiOrderQueryRes ihuyiOrderQueryRes = new IhuyiOrderQueryRes();
                IhuyiVirtualOrderQueryRes queryRes = IhuyiUtils.virtualGoodsRechargeQuery(order,cacheFeignClient);
                log.info("轮询互亿话费订购结果："+JSON.toJSONString(queryRes));
                if (1 == queryRes.getCode()) { //提交成功
                    //如果商户订单号为空，则更新商户订单号
                    if (StringUtils.isEmpty(order.getSupplierOrderNo())) {
                        order.setSupplierOrderNo(queryRes.getTaskid());
                        Integer  i= orderFeignClient.updateServiceOrder(order);
                        log.info("供应商订单号为空,更新供应商订单号,供应商订单号:"+queryRes.getTaskid());
                    }
                    if (2 == queryRes.getStatus()) { //充值成功
                        order.setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
                        Integer i = orderFeignClient.updateServiceOrder(order);
                        log.info("充值成功,更新主订单状态,更新数量为:"+i );
                        //充值成功,通知商户
                        notifyFeignClient.rechargeNotifyToOrg3Times(order.getOrderNo());

                    } else if (3 == queryRes.getStatus()) { //充值失败
                        String orderFailDesc = order.getOrderFailDesc();
                        if (StringUtils.isEmpty(orderFailDesc)) {
                            orderFailDesc = "互亿话费充值失败";
                        }
                        order.setOrderFailDesc(orderFailDesc);
                        Integer i = orderFeignClient.updateServiceOrder(order);
                        log.info("充值失败,更新主订单失败描述,更新数量为:"+i );
                        //充值失败,通知商户
                        notifyFeignClient.rechargeNotifyToOrg3Times(order.getOrderNo());
                    } else {
                        log.info("轮询互亿话费充值订单状态，结果为："+JSONObject.toJSONString(queryRes));
                    }
                }
            }
        } catch (Exception e) {
            log.info("互亿订单轮询出现异常...IhuyiOrderGetException" + e.getMessage());
            return;
        }
        log.info("互亿虚拟充值订单轮询结束,本次轮询"+timer+"笔订单*****************************");
    }

}



