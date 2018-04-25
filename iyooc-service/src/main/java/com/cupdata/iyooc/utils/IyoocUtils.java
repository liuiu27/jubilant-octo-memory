package com.cupdata.iyooc.utils;

import com.alibaba.fastjson.JSON;
import com.cupdata.iyooc.feign.ConfigFeignClient;
import com.cupdata.iyooc.vo.BestPayingReq;
import com.cupdata.iyooc.vo.BestPayingRes;
import com.cupdata.iyooc.vo.LoginResReq;
import com.cupdata.sip.common.lang.constant.SysConfigParaNameEn;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 17:52 2018/4/24
 */
public class IyoocUtils {

    /**
     * 日志
     */
    protected static Logger log = Logger.getLogger(IyoocUtils.class);

    /**
     * 地球半径
     */
    private static final Integer EARTH_RADIUS = 6371;

    ParkingPayUtils bpi = new ParkingPayUtils();        // 接口实体类
    BestPayingReq bpr = new BestPayingReq();            // 请求接口的实体类
    BestPayingRes response = new BestPayingRes();        // 接口响应实体类



}
