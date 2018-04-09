package com.cupdata.tencent.utils;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.constant.SysConfigParaNameEn;
import com.cupdata.commons.utils.*;
import com.cupdata.commons.vo.recharge.RechargeReq;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: DingCong
 * @Description: 腾讯充值业务测试工具
 * @CreateDate: 2018/2/2 17:53
 */
public class TestUtils {
  public static void main(String[] args) throws Exception {

        //腾讯QQ充值网关URL
        String url = "http://localhost:46959/recharge/recharge/getRecharge";
        String url2 = "http://cvpa.leagpoint.com/sipService/recharge/recharge/getRecharge";
        String url3 = "http://172.17.100.6:46959/recharge/recharge/getRecharge";
        //花积分机构编号
        String org = "2018010200000001";

        //请求参数
        RechargeReq rechargeReq = new RechargeReq();
        rechargeReq.setTimestamp(DateTimeUtil.getFormatDate(new Date(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8));
        rechargeReq.setMobileNo("15857128524");
        rechargeReq.setAccount("625192155");
        rechargeReq.setOrgOrderNo("tencent040201");
        rechargeReq.setProductNo("170810R001");
        rechargeReq.setOrderDesc("30Test");

        //请求参数转化为字符串
        String reqStr = JSONObject.toJSONString(rechargeReq);
        System.out.println("请求封装对象reqStr为:"+reqStr);

        //平台公钥
        String sipPubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC65Nl9lRszYoE8RqErsqDd9zItv+1CHj2SGVZMhYDE/2yYl8kGuRROfqTecvwroA3TVmMqe46Sz8XM8wXfLew7sl6Oazw+hsUiYS02l33SWJgJ8XVtrN9F/kQ8tHSqsXNqD8gjpgH0fSZ1fqoDW3fWjr3ZR1pDvHCL8FlUnEEcEQIDAQAB";
        //机构私钥
        String orgPriKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=";
        PublicKey sipPubKey = RSAUtils.getPublicKeyFromString(sipPubKeyStr);
        PrivateKey orgPriKey = RSAUtils.getPrivateKeyFromString(orgPriKeyStr);
        //加密
        String data = RSAUtils.encrypt(reqStr, sipPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        //签名
        String sign = RSAUtils.sign(reqStr, orgPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
        String params = "org=" + org + "&data=" + URLEncoder.encode(data, "utf-8") + "&sign=" + URLEncoder.encode(sign, "utf-8");
        String res = HttpUtil.doPost(url2, params, "application/x-www-form-urlencoded;charset=UTF-8");
        System.out.print("腾讯QQ充值响应数据为" + res);

    }

}
