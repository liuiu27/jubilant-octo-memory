package com.cupdata.iyooc.utils;

import com.alibaba.fastjson.JSON;
import com.cupdata.iyooc.feign.ConfigFeignClient;
import com.cupdata.iyooc.vo.BestPayingReq;
import com.cupdata.iyooc.vo.BestPayingRes;
import com.cupdata.iyooc.vo.Footer;
import com.cupdata.iyooc.vo.Header;
import com.cupdata.sip.common.lang.constant.SysConfigParaNameEn;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.sip.common.lang.utils.HttpUtil;
import com.cupdata.sip.common.lang.utils.MD5Util;
import org.apache.commons.lang.StringUtils;

/**
 * @Auther: DingCong
 * @Description: 优积付工具类
 * @@Date: Created in 13:48 2018/4/24
 */
public class ParkingPayUtils {


    /**
     * 优积付接口
     * @param url
     * @param bestPayReq
     * @return
     */
    public BestPayingRes bestPayReqRes(String url , BestPayingReq bestPayReq ) {
        BestPayingRes bestPayRes = new BestPayingRes(); // 优积付 接口返回参数
        String jsonString = JSON.toJSONString(bestPayReq);
        try {
            String str = HttpUtil.doPost(url, jsonString);
            bestPayRes = JSON.parseObject(str, BestPayingRes.class);
        } catch (Exception e) {
            Footer res = new Footer();
            res.setRespCode("404");
            res.setRespMessage("请求接口时出现了异常，对方可能中断了接口！");
            bestPayRes.setFooter(res);
        }
        return bestPayRes;
    }


    /**
     * 请求接口header共同对象
     * @return
     */
    public Header getHeader(String body,ConfigFeignClient configFeignClient){
        Header header = new Header();
        header.setReqTime(new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()));
        header.setReqSeqNo(CommonUtils.getRandomNum(32));
        String parkingPayPublicKeyStr;
        if (CommonUtils.isWindows()) {
            parkingPayPublicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTz9cJ43hxlDlWZOIEiyLah+8fUfaF8NMZWvjiaUAGiuyBvg+LyWR6pgpd/oI5oX/kBsMRy8t41h39ZL01Az1FqSDkfLyAZALF6GAQ8qxnCmYuXKZf3mRO/OxcGVX6taBUoan6QpV/OsPnz30Li4YlP+ontzm6chS7V1WRHFgWYwIDAQAB";
        } else {
            parkingPayPublicKeyStr = configFeignClient.getSysConfig("PARKING_PAY_PUBLIC_KEY_STR", SysConfigParaNameEn.HUAJIFEN_BANK_CODE).getParaValue();
        }
        if(StringUtils.isEmpty(body)){
            body = parkingPayPublicKeyStr;
        }else{
            body = body+parkingPayPublicKeyStr;
        }
        header.setMac(MD5Util.encode(body));
        if (CommonUtils.isWindows()) {
            header.setReqChannel("303");
        } else {
            header.setReqChannel(configFeignClient.getSysConfig("PARKING_PAY_REQ_CHANNEL",SysConfigParaNameEn.HUAJIFEN_BANK_CODE).getParaValue());
        }
        header.setVersion("1.0");
        return header;
    }

}
