package com.cupdata.product.utils;


import com.cupdata.commons.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TripUtil {


    /**
     * 判断请求参数是否非法 并解密 返回 jsonObj 与 error
     */
    public static Map<String, Object> isValidDecrypted(String reqJson, String bankCode, String orgNo, String method) {
        log.info(" request method is " + method + "param : reqJson is "
                + reqJson + " bankCode is " + bankCode
                + " , orgNo is " + orgNo);
        if (StringUtils.isBlank(orgNo)) {
            orgNo = bankCode;
        }
        Map<String, Object> map = new HashMap<>(3);

        return map;
    }

}
