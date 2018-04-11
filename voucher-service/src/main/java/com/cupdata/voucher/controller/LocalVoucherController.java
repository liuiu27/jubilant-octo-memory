package com.cupdata.voucher.controller;

import com.cupdata.commons.api.voucher.ILocalVoucherController;
import com.cupdata.commons.constant.ModelConstants;
import com.cupdata.commons.constant.ResponseCodeMsg;
import com.cupdata.commons.exception.ErrorException;
import com.cupdata.commons.model.ElectronicVoucherCategory;
import com.cupdata.commons.model.ElectronicVoucherLib;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.product.ProductInfVo;
import com.cupdata.commons.vo.product.VoucherOrderVo;
import com.cupdata.commons.vo.voucher.*;
import com.cupdata.voucher.biz.VoucherCategoryBiz;
import com.cupdata.voucher.biz.VoucherLibBiz;
import com.cupdata.voucher.feign.OrderFeignClient;
import com.cupdata.voucher.feign.ProductFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

/**
 * @Author: DingCong
 * @Description: 从本地券码库根据券码类型id获取券码
 * @CreateDate: 2018/3/1 19:23
 */
@Slf4j
@RestController
public class LocalVoucherController implements ILocalVoucherController {

    @Autowired
    private ProductFeignClient productFeignClient ;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Resource
    private VoucherLibBiz voucherLibGetBiz ;

    @Resource
    private VoucherCategoryBiz voucherCategoryBiz ;

    /**
     * SIP根据券码类型id从本地库获取券码
     * @param voucherReq
     * @return
     */
    @Override
    public BaseResponse<GetVoucherRes> getLocalVoucher(@RequestParam(value = "org" , required = true) String org ,@RequestBody GetVoucherReq voucherReq) {
        log.info("LocalVoucherController getLocalVoucher is begin......org:"+org+",ProductNo"+voucherReq.getProductNo());
        //响应信息
        BaseResponse<GetVoucherRes> getVoucherRes = new BaseResponse<>();// 默认为成功
        try {
            //step1.获取产品信息,如果不存在此产品,直接返回错误状态码和信息
            log.info("根据产品编号获取产品信息");
            BaseResponse<ProductInfVo> productInfo = productFeignClient.findByProductNo(voucherReq.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())) {
                //设置状态码和错误信息,给予返回
                log.info("VoucherController getLocalVoucher 获取产品失败");
                getVoucherRes.setResponseCode(productInfo.getResponseCode());
                getVoucherRes.setResponseMsg(productInfo.getResponseMsg());
                return getVoucherRes;
            }

            //step2.根据产品编号查询商品类型(供应商参数)
            Long categoryId = Long.valueOf(productInfo.getData().getProduct().getSupplierParam());
            log.info("券码类别id:"+categoryId);

            //step3.根据券码类型获得券码状态是否有效
            ElectronicVoucherCategory electronicVoucherCategory = voucherCategoryBiz.checkVoucherValidStatusByCategoryId(categoryId);
            if (CommonUtils.isNullOrEmptyOfObj(electronicVoucherCategory)) {
                log.info("券码类型无效");
                getVoucherRes.setResponseMsg(ResponseCodeMsg.VOUCHER_TYPE_INVALID.getMsg());
                getVoucherRes.setResponseCode(ResponseCodeMsg.VOUCHER_TYPE_INVALID.getCode());
                return getVoucherRes;
            }

            //step4.判断券码列表是否存在有效券码,获取有效券码
            log.info("券码类型有效，开始获取券码");
            ElectronicVoucherLib electronicVoucherLib = voucherLibGetBiz.selectVoucherByCategoryId(categoryId);
            if (CommonUtils.isNullOrEmptyOfObj(electronicVoucherLib)) {
                log.info("券码列表没有可用券码");
                getVoucherRes.setResponseMsg(ResponseCodeMsg.NO_VOUCHER_AVALIABLE.getMsg());
                getVoucherRes.setResponseCode(ResponseCodeMsg.NO_VOUCHER_AVALIABLE.getCode());
                return getVoucherRes;
            }

            //step5.券码列表存在可用券码,从券码中获取券码号，并更新
            electronicVoucherLib.setOrgOrderNo(voucherReq.getOrgOrderNo());
            electronicVoucherLib.setOrgNo(org);
            electronicVoucherLib.setMobileNo(voucherReq.getMobileNo());
            voucherLibGetBiz.UpdateElectronicVoucherLib(electronicVoucherLib);

            //step6.封装券码信息,给予返回
            log.info("本地获取券码成功,券码号:"+electronicVoucherLib.getTicketNo());
            GetVoucherRes res = new GetVoucherRes();
            res.setVoucherLibId(electronicVoucherLib.getId());      //该条券码id
            res.setVoucherCode(electronicVoucherLib.getTicketNo()); //券码号
            res.setStartDate(electronicVoucherLib.getStartDate());  //开始时间
            res.setExpire(electronicVoucherLib.getEndDate());       //结束时间
            getVoucherRes.setData(res);
            return getVoucherRes;
        }catch (Exception e){
            log.error("LocalVoucherController getLocalVoucher error is " + e.getMessage());
            getVoucherRes.setResponseMsg(ResponseCodeMsg.FAIL_GAT_LOCAL_VOUCHER.getMsg());
            getVoucherRes.setResponseCode(ResponseCodeMsg.FAIL_GAT_LOCAL_VOUCHER.getCode());
            return getVoucherRes;
        }
    }

    /**
     * 其他机构通过SIP的券码库来获取券码:需要创建券码订单
     * @param org
     * @param voucherReq
     * @return
     */
    @Override
    public BaseResponse<GetVoucherRes> getVoucher(@RequestParam (value = "org" , required = true) String org, @RequestBody GetVoucherReq voucherReq) {
        log.info("LocalVoucherController getVoucher is begin......");
        try {
            //响应信息
            BaseResponse<GetVoucherRes> getVoucherRes = new BaseResponse();// 默认为成功

            //获取产品信息,如果不存在此产品,直接返回错误状态码和信息
            BaseResponse<ProductInfVo> productInfo = productFeignClient.findByProductNo(voucherReq.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())) {
                //设置状态码和错误信息,给予返回
                log.info("LocalVoucherController getVoucher 获取产品失败");
                getVoucherRes.setResponseCode(productInfo.getResponseCode());
                getVoucherRes.setResponseMsg(productInfo.getResponseMsg());
                return getVoucherRes;
            }

            //创建券码订单
            CreateVoucherOrderVo createvoucherOrderVo = new CreateVoucherOrderVo();
            createvoucherOrderVo.setOrderDesc(voucherReq.getOrderDesc());
            createvoucherOrderVo.setOrgNo(org);
            createvoucherOrderVo.setProductNo(voucherReq.getProductNo());
            createvoucherOrderVo.setOrgOrderNo(voucherReq.getOrgOrderNo());
            //调用创建订单服务
            log.info("LocalVoucherController getVoucher create order ,ProductNo:"+voucherReq.getProductNo()+",org"+org);
            BaseResponse<VoucherOrderVo> voucherOrderRes = orderFeignClient.createVoucherOrder(createvoucherOrderVo);
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode())
                    || null == voucherOrderRes.getData()
                    || null == voucherOrderRes.getData().getOrder()
                    || null == voucherOrderRes.getData().getVoucherOrder()) {   //如果创建订单失败
                //设置错误状态码和错误消息,给予返回
                log.info("LocalVoucherController getVoucher 创建订单失败！");
                getVoucherRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                getVoucherRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return getVoucherRes;
            }

            //根据产品编号查询商品类型(供应商参数)
            Long categoryId = Long.valueOf(productInfo.getData().getProduct().getSupplierParam());

            //根据券码类型获得券码状态是否有效
            ElectronicVoucherCategory electronicVoucherCategory = voucherCategoryBiz.checkVoucherValidStatusByCategoryId(categoryId);
            if (CommonUtils.isNullOrEmptyOfObj(electronicVoucherCategory)) {
                log.info("券码类型无效,categoryId:"+categoryId);
                getVoucherRes.setResponseMsg(ResponseCodeMsg.VOUCHER_TYPE_INVALID.getMsg());
                getVoucherRes.setResponseCode(ResponseCodeMsg.VOUCHER_TYPE_INVALID.getCode());
                return getVoucherRes;
            }

            //判断券码列表是否存在有效券码,获取有效券码
            ElectronicVoucherLib electronicVoucherLib = voucherLibGetBiz.selectVoucherByCategoryId(categoryId);
            if (CommonUtils.isNullOrEmptyOfObj(electronicVoucherLib)) {
                log.info("券码列表没有可用券码,categoryId:"+categoryId);

                //更新订单状态
                voucherOrderRes.getData().getOrder().setOrderFailDesc("券码列表没有可用券码");
                voucherOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_FAIL);

                //调用订单服务更新订单
                voucherOrderRes = orderFeignClient.updateVoucherOrder(voucherOrderRes.getData());
                if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode())
                        || null == voucherOrderRes.getData()
                        || null == voucherOrderRes.getData().getOrder()
                        || null == voucherOrderRes.getData().getVoucherOrder()) {
                    getVoucherRes.setResponseCode(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                    getVoucherRes.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                    return getVoucherRes;
                }

                //响应结果
                getVoucherRes.setResponseMsg(ResponseCodeMsg.NO_VOUCHER_AVALIABLE.getMsg());
                getVoucherRes.setResponseCode(ResponseCodeMsg.NO_VOUCHER_AVALIABLE.getCode());
                return getVoucherRes;
            }

            //券码列表存在可用券码,从券码中获取券码号，并更新
            electronicVoucherLib.setOrgOrderNo(voucherReq.getOrgOrderNo());
            electronicVoucherLib.setOrgNo(org);
            electronicVoucherLib.setMobileNo(voucherReq.getMobileNo());
            voucherLibGetBiz.UpdateElectronicVoucherLib(electronicVoucherLib);

            //获取券码成功，修改订单保存券码状态
            voucherOrderRes.getData().getOrder().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
            voucherOrderRes.getData().getVoucherOrder().setUseStatus(ModelConstants.VOUCHER_USE_STATUS_UNUSED);
            voucherOrderRes.getData().getVoucherOrder().setEffStatus(ModelConstants.VOUCHER_STATUS_EFF);
            voucherOrderRes.getData().getOrder().setSupplierOrderNo(voucherOrderRes.getData().getOrder().getOrderNo());
            voucherOrderRes.getData().getVoucherOrder().setVoucherCode(electronicVoucherLib.getTicketNo());  //券码号
            voucherOrderRes.getData().getVoucherOrder().setStartDate(DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMdd")); //起始时间
            voucherOrderRes.getData().getVoucherOrder().setEndDate(electronicVoucherLib.getEndDate());

            //调用订单服务更新订单
            voucherOrderRes = orderFeignClient.updateVoucherOrder(voucherOrderRes.getData());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode())
                    || null == voucherOrderRes.getData()
                    || null == voucherOrderRes.getData().getOrder()
                    || null == voucherOrderRes.getData().getVoucherOrder()) {
                getVoucherRes.setResponseCode(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                getVoucherRes.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                return getVoucherRes;
            }

            //响应结果
            log.info("机构获取sip券码结果为:券码号"+electronicVoucherLib.getTicketNo()+",有效期:"+electronicVoucherLib.getEndDate());
            GetVoucherRes res = new GetVoucherRes();
            res.setOrderNo(voucherOrderRes.getData().getOrder().getOrderNo());      //平台订单号
            res.setOrgOrderNo(voucherOrderRes.getData().getOrder().getOrgOrderNo());//机构订单编号
            res.setVoucherCode(electronicVoucherLib.getTicketNo()); //券码号
            res.setStartDate(electronicVoucherLib.getStartDate());  //开始时间
            res.setExpire(electronicVoucherLib.getEndDate());       //结束时间
            getVoucherRes.setData(res);
            return getVoucherRes;
        }catch (Exception e){
            log.error("error is " + e.getMessage());
            throw new ErrorException(ResponseCodeMsg.SYSTEM_ERROR.getCode(),ResponseCodeMsg.SYSTEM_ERROR.getMsg());
        }
    }

    @Override
    public BaseResponse<DisableVoucherRes> disableVoucher(@RequestParam(value = "org" , required = true) String org,@RequestBody DisableVoucherReq disableVoucherReq) {
        return null;
    }

    @Override
    public BaseResponse<WriteOffVoucherRes> writeOffVoucher(@RequestParam(value = "sup" , required = true) String sup,@RequestBody WriteOffVoucherReq writeOffVoucherReq) {
        return null;
    }
}
