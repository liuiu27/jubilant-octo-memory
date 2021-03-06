package com.cupdata.ihuyi.rest;

import com.cupdata.ihuyi.constant.IhuyiRechargeResCode;
import com.cupdata.ihuyi.feign.ConfigFeignClient;
import com.cupdata.ihuyi.feign.OrderFeignClient;
import com.cupdata.ihuyi.feign.OrgFeignClient;
import com.cupdata.ihuyi.feign.ProductFeignClient;
import com.cupdata.ihuyi.utils.IhuyiUtils;
import com.cupdata.ihuyi.vo.IhuyiVoucherRes;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.ihuyi.IhuyiVoucherController;
import com.cupdata.sip.common.api.order.request.CreateVoucherOrderVo;
import com.cupdata.sip.common.api.order.response.VoucherOrderVo;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.api.voucher.request.DisableVoucherReq;
import com.cupdata.sip.common.api.voucher.request.GetVoucherReq;
import com.cupdata.sip.common.api.voucher.request.WriteOffVoucherReq;
import com.cupdata.sip.common.api.voucher.response.DisableVoucherRes;
import com.cupdata.sip.common.api.voucher.response.GetVoucherRes;
import com.cupdata.sip.common.api.voucher.response.WriteOffVoucherRes;
import com.cupdata.sip.common.lang.constant.ModelConstants;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.constant.SysConfigParaNameEn;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.sip.common.lang.utils.DESUtil;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Auther: DingCong
 * @Description: 互亿电子券兑换相关业务
 * @@Date: Created in 19:11 2018/4/18
 */
@Slf4j
@RestController
public class IhuyiVoucherExchangeController implements IhuyiVoucherController{

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private ConfigFeignClient configFeignClient ;

    @Autowired
    private OrgFeignClient orgFeignClient;

    /**
     * 获取互亿电子券码Controller
     * @param org 机构编号
     * @param voucherReq 获取券码请求参数（实现方法中需要添加@RequestBody注解获取参数）
     * @return
     */
    @Override
    public BaseResponse<GetVoucherRes> getVoucher(String org, GetVoucherReq voucherReq) {

        log.info("调用互亿礼品券购买Controller...org:"+org+",ProductNo:"+voucherReq.getProductNo()+",OrgOrderNo:"+voucherReq.getOrgOrderNo());
        log.info("进入获取互亿礼品券controller...orderNo:" + voucherReq.getOrgOrderNo() +",org:" + org + ",mobileNo:" + voucherReq.getMobileNo()+",OrderDesc:" + voucherReq.getOrderDesc());
        //设置响应数据结果
        BaseResponse<GetVoucherRes> getVoucherRes = new BaseResponse<GetVoucherRes>();
        try {
            //获取该供应商产品,如果不存在此产品,直接返回错误状态码和信息
            log.info("根据供应商编号获取商户信息,productNo:"+voucherReq.getProductNo());
            BaseResponse<ProductInfoVo> productInfo = productFeignClient.findByProductNo(voucherReq.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())) {
                log.info("根据服务产品编号查询对应的服务产品失败,ProductNo:"+voucherReq.getProductNo());
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
            BaseResponse<VoucherOrderVo> voucherOrderRes = orderFeignClient.createVoucherOrder(createvoucherOrderVo);
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode()) || null == voucherOrderRes.getData()
                    || null == voucherOrderRes.getData().getOrderInfoVo()) {   //如果创建订单失败
                log.info("创建订单失败！");
                getVoucherRes.setResponseCode(ResponseCodeMsg.ORDER_CREATE_ERROR.getCode());
                getVoucherRes.setResponseMsg(ResponseCodeMsg.ORDER_CREATE_ERROR.getMsg());
                return getVoucherRes;
            }
            log.info("创建订单成功,订单编号:"+voucherOrderRes.getData().getOrderInfoVo().getOrderNo());

            //调用互亿电子券码接口获取礼品券码
            log.info("调用互亿工具类获取券码");
            IhuyiVoucherRes ihuyiVoucherRes = IhuyiUtils.ihuyiGiftCardBuy(voucherOrderRes.getData(),productInfo.getData().getSupplierParam(),voucherReq,configFeignClient);
            log.info("互亿获取电子券码结果状态码:"+ihuyiVoucherRes.getCode());

            //互亿获取券码是否成功
            if(ihuyiVoucherRes.getCode() != 1){
                log.info("互亿获取券码失败");
                getVoucherRes.setResponseCode(IhuyiRechargeResCode.FAIL_GRT_VOUCHER.getCode());
                getVoucherRes.setResponseMsg(IhuyiRechargeResCode.FAIL_GRT_VOUCHER.getMsg());
                return getVoucherRes;
            }

            //券码列表获取是否为空
            List<IhuyiVoucherRes.IhuyiGiftCardInfo> cardInfoList = ihuyiVoucherRes.getCards();
            if(CollectionUtils.isEmpty(cardInfoList) || StringUtils.isEmpty(cardInfoList.get(0).getNo()) || StringUtils.isEmpty(cardInfoList.get(0).getPwd())){
                log.info("互亿券码列表为空");
                getVoucherRes.setResponseCode(IhuyiRechargeResCode.EMPTY_VOUCHER_LIST.getCode());
                getVoucherRes.setResponseMsg(IhuyiRechargeResCode.EMPTY_VOUCHER_LIST.getMsg());
                return getVoucherRes;
            }

            //获取互亿api_key
            String apikey = null ;
            if(CommonUtils.isWindows()){
                apikey = "6j3ao593wMNQRz4Zo4ao";
            }else {
                //如果获取数据信息为空
                if ("".equals(configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIKEY").getParaValue())
                        || null == configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIKEY").getParaValue()) {
                    //设置错误码:获取api_id失败
                    getVoucherRes.setResponseMsg(IhuyiRechargeResCode.FAIL_TO_GET_API_ID.getMsg());
                    return getVoucherRes;
                }
                apikey = configFeignClient.getSysConfig(SysConfigParaNameEn.HUAJIFEN_BANK_CODE, "IHUYI_APIKEY").getParaValue();
            }
            log.info("互亿apikey:"+apikey);

            //获取券码
            String key = DESUtil.append(apikey, apikey.length(),24);
            String iv = DESUtil.substring(apikey, 0, 8);
            String decodeNo = DESUtil.decode(key, cardInfoList.get(0).getNo(), iv, DESUtil.DESEDE_ALGORITHM, DESUtil.DESEDE_CBC_PKCS5PADDING);
            String decodePwd = DESUtil.decode(key, cardInfoList.get(0).getPwd(), iv, DESUtil.DESEDE_ALGORITHM, DESUtil.DESEDE_CBC_PKCS5PADDING);
            String startDate = DateTimeUtil.getFormatDate(new Date(), "yyyyMMdd");
            voucherOrderRes.getData().setStartDate(startDate);
            SimpleDateFormat format =  new SimpleDateFormat( "yyyyMMdd" );
            String endDate = format.format(cardInfoList.get(0).getExpired()*1000L);
            voucherOrderRes.getData().getOrderInfoVo().setSupplierOrderNo(ihuyiVoucherRes.getTaskid());
            voucherOrderRes.getData().getOrderInfoVo().setIsNotify(ModelConstants.IS_NOTIFY_NO);
            voucherOrderRes.getData().setEndDate(endDate);
            voucherOrderRes.getData().setVoucherCode(decodeNo);
            voucherOrderRes.getData().setVoucherPassword(decodePwd);
            String other = cardInfoList.get(0).getOther();
            if (!StringUtils.isEmpty(other)) {
                String[] splitArr = other.split("\\$\\$");
                for (String s : splitArr) {
                    if (s.indexOf("url:") != -1) {
                        other = s.substring(4);
                        break;
                    }
                }
            }
            voucherOrderRes.getData().setQrCodeUrl(other);
            voucherOrderRes.getData().getOrderInfoVo().setOrderStatus(ModelConstants.ORDER_STATUS_SUCCESS);
            //更新订单信息
            log.info("互亿券码获取成功更新订单,订单编号:"+voucherOrderRes.getData().getOrderInfoVo().getOrderNo());
            voucherOrderRes = orderFeignClient.updateVoucherOrder(voucherOrderRes.getData());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(voucherOrderRes.getResponseCode()) || null == voucherOrderRes.getData()
                    || null == voucherOrderRes.getData().getOrderInfoVo()) {
                getVoucherRes.setResponseCode(ResponseCodeMsg.ORDER_UPDATE_ERROR.getCode());
                getVoucherRes.setResponseMsg(ResponseCodeMsg.ORDER_UPDATE_ERROR.getMsg());
                return getVoucherRes;
            }
            //响应参数:响应券码的券码号，卡密，二维码链接，有效期，平台订单号，机构订单唯一标识
            GetVoucherRes voucherRes = new GetVoucherRes();
            voucherRes.setVoucherCode(decodeNo);
            voucherRes.setVoucherPassword(decodePwd);
            voucherRes.setQrCodeUrl(other);
            voucherRes.setExpire(endDate);
            voucherRes.setOrderNo(voucherOrderRes.getData().getOrderInfoVo().getOrderNo());
            voucherRes.setOrgOrderNo(voucherReq.getOrgOrderNo());
            getVoucherRes.setData(voucherRes);
            log.info("响应结果:decodeNo:"+decodeNo);
            return getVoucherRes;
        }catch (Exception e){
            log.error("IhuyiVoucherExchangeController getVoucher error is",e);
            getVoucherRes.setResponseCode(IhuyiRechargeResCode.EXCEPTION_GET_VOUCHER.getCode());
            getVoucherRes.setResponseMsg(IhuyiRechargeResCode.EXCEPTION_GET_VOUCHER.getMsg());
            return getVoucherRes;
        }
    }




    @Override
    public BaseResponse<DisableVoucherRes> disableVoucher(String org, DisableVoucherReq disableVoucherReq) {
        return null;
    }

    @Override
    public BaseResponse<WriteOffVoucherRes> writeOffVoucher(String sup, WriteOffVoucherReq writeOffVoucherReq) {
        return null;
    }
}
