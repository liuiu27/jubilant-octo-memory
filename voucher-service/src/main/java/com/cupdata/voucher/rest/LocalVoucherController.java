package com.cupdata.voucher.rest;

import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.product.response.ProductInfoVo;
import com.cupdata.sip.common.api.voucher.ILocalVoucherController;
import com.cupdata.sip.common.api.voucher.request.DisableVoucherReq;
import com.cupdata.sip.common.api.voucher.request.GetVoucherReq;
import com.cupdata.sip.common.api.voucher.request.WriteOffVoucherReq;
import com.cupdata.sip.common.api.voucher.response.*;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.voucher.biz.VoucherBiz;
import com.cupdata.voucher.feign.ProductFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: DingCong
 * @Description: 从本地券码库获取券码
 * @@Date: Created in 10:53 2018/4/13
 */
@Slf4j
@RestController
public class LocalVoucherController implements ILocalVoucherController {

    @Autowired
    private ProductFeignClient productFeignClient ;

    @Autowired
    private VoucherBiz voucherBiz;

    /**
     * 从本地券码库获取券码
     * @param org
     * @param voucherReq
     * @return
     */
    @Override
    public BaseResponse<GetVoucherRes> getLocalVoucher(@RequestParam("org") String org, @RequestBody GetVoucherReq voucherReq) {
        log.info("LocalVoucherController getLocalVoucher is begin......org:"+org+",ProductNo"+voucherReq.getProductNo());
        //响应信息
        BaseResponse<GetVoucherRes> getVoucherRes = new BaseResponse<>();// 默认为成功
        try {
            //step1.获取产品信息,如果不存在此产品,直接返回错误状态码和信息
            BaseResponse<ProductInfoVo> productInfo = productFeignClient.findByProductNo(voucherReq.getProductNo());
            if (!ResponseCodeMsg.SUCCESS.getCode().equals(productInfo.getResponseCode())) {
                //设置状态码和错误信息,给予返回
                log.info("VoucherController getLocalVoucher 获取产品失败");
                getVoucherRes.setResponseCode(productInfo.getResponseCode());
                getVoucherRes.setResponseMsg(productInfo.getResponseMsg());
                return getVoucherRes;
            }

            //step2.根据产品编号查询商品类型(供应商参数)
            Long categoryId = Long.valueOf(productInfo.getData().getSupplierParam());

            //step3.根据券码类型获得券码状态是否有效
            ElectronicVoucherCategory electronicVoucherCategory = voucherBiz.checkVoucherValidStatusByCategoryId(categoryId);
            if (CommonUtils.isNullOrEmptyOfObj(electronicVoucherCategory)) {
                log.info("券码类型无效");
                getVoucherRes.setResponseMsg(ResponseCodeMsg.VOUCHER_TYPE_INVALID.getMsg());
                getVoucherRes.setResponseCode(ResponseCodeMsg.VOUCHER_TYPE_INVALID.getCode());
                return getVoucherRes;
            }

            //step4.判断券码列表是否存在有效券码,获取有效券码
            ElectronicVoucherLib electronicVoucherLib = voucherBiz.selectVoucherByCategoryId(categoryId);
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
            voucherBiz.UpdateElectronicVoucherLib(electronicVoucherLib);

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

    @Override
    public BaseResponse<GetVoucherRes> getVoucher(String org, GetVoucherReq voucherReq) {
        return null;
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
