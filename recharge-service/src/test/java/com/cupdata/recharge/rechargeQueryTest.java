package com.cupdata.recharge;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.utils.HttpUtil;
import com.cupdata.commons.utils.RSAUtils;
import com.cupdata.commons.vo.recharge.RechargeQueryReq;
import com.cupdata.commons.vo.recharge.RechargeReq;
import org.junit.Test;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class rechargeQueryTest {

    /**
     * 充值查询请求测试类
     * @throws Exception
     */
    @Test
    public void rechargeTest() throws Exception {
        //充值结果查询网关URL
        String url1 = "http://localhost:46959/recharge/query/rechargeQuery";
        String url2 = "http://cvpa.leagpoint.com/sipService/recharge/query/rechargeQuery";
        String url3 = "https://onlinepay.cupdata.com/sipService/recharge/query/rechargeQuery";

        //请求参数
        String org = "20180412O86740479";
        RechargeQueryReq req = new RechargeQueryReq();
        req.setOrgOrderNo("180410AD054578");
        req.setTimestamp(DateTimeUtil.getFormatDate(new Date(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8));
        String reqStr = JSONObject.toJSONString(req);
        System.out.print("请求参数json字符串" + reqStr);

        //秘钥
        String sipPubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8gw6++UfAuEH7jwdL0zj6vGWi2WhS3mOyQpED/DSOWIetSVW5UJ22EvWzfBl38nDMxRgfkqqEA3fBunhJHl2TdoWFcNV8z3wSdSaqEi+2u49JZbGP5oy8wOnLLD1MKp8cHb3NFwXEIta5UDuSFy92SK8pqvy0xitQCLoV4WUNMwIDAQAB";
        String orgPriKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALyDDr75R8C4QfuPB0vTOPq8ZaLZaFLeY7JCkQP8NI5Yh61JVblQnbYS9bN8GXfycMzFGB+SqoQDd8G6eEkeXZN2hYVw1XzPfBJ1JqoSL7a7j0llsY/mjLzA6cssPUwqnxwdvc0XBcQi1rlQO5IXL3ZIrymq/LTGK1AIuhXhZQ0zAgMBAAECgYAvpjr0d8M0yf5XrUQNXQMikbtpCeYuSCtQFDZemQHZ1zPYT9icwm1S6AD2ENDJOl1bzRf3ZxW1z8CWFeGwrb2+CIF32yJCM2fOlNDKVUi8tLjpDLBMGso5gF3ko2iFFXFjw2FROUCnV6q2J78Vj9LhfQmeioQueTLrl2ikQDqmkQJBAO57wuefb0qYQ6iml8MDU+AwVnU11MYVVK7/0WQJzkdreE7J1zkXgSnt0NpaqFDvezxfBTYJCw95Pb7rZgqPGSMCQQDKW6zIfiHEzoM+R+j8EchXudIlFycvqvql3NMO0JpcmXgzimDgLl7IwUGfMMv/ERTNmY/toIt476SDzUksgWSxAkBO4mUwFI7Nj9whdymP+hPOfm66ypmdBAVE9Z2fh5bSDPx4o08rtVimM+H3uDEgxHZxG8UvgIJGFgaUlmzkZT0dAkEAmWGRfsq0N+O8cRm6jE3CSFRN59U725LCt7PAuor9ZdDh3lc2BNbA+3QYlFw9U9GTrh+Gi7xT20/xqAGTREuzkQJBAKx/hqlyPzp9ZGVzaT1I+gFHUMgcjPc9PLrKtzHwriSTILcsiEx47TzFTQy/PQT4gisfdewZWvDRP33+Se6NhG8=";
        PublicKey sipPubKey = RSAUtils.getPublicKeyFromString(sipPubKeyStr);
        PrivateKey orgPriKey = RSAUtils.getPrivateKeyFromString(orgPriKeyStr);

        //加密参数
        String sign = RSAUtils.sign(reqStr, orgPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
        String data = RSAUtils.encrypt(reqStr, sipPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        String params = "org=" + org + "&data=" + URLEncoder.encode(data, "utf-8") + "&sign=" + URLEncoder.encode(sign, "utf-8");
        String res = HttpUtil.doPost(url3, params, "application/x-www-form-urlencoded;charset=UTF-8");
        System.out.print("响应数据为" + res);

        //解密数据
        String[] split = res.split("&sign=");
        String s1 = (String)split[0].replace("data=","");
        String s = URLDecoder.decode(s1,"utf-8");
        String plain = RSAUtils.decrypt(s,orgPriKey,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        System.out.print("响应明文:"+plain);
    }

}
