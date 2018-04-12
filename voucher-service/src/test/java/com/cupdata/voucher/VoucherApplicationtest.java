package com.cupdata.voucher;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.utils.HttpUtil;
import com.cupdata.commons.utils.RSAUtils;
import com.cupdata.commons.vo.voucher.GetVoucherReq;
import org.junit.Test;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class VoucherApplicationtest {
    /**
     * 外部机构获取本地券码测试
     * @throws Exception
     */
    @Test
    public void getVoucher () throws Exception {
        //获取券码网关URL
        String url = "http://localhost:46959/voucher/voucher/getVoucher";
        String url2 = "http://cvpa.leagpoint.com/sipService/voucher/voucher/getVoucher";
        String url3 = "http://172.17.100.6:46959/voucher/voucher/getVoucher";
        //花积分机构编号
        String org = "2018010200000001";
        //请求参数
        GetVoucherReq voucherReq = new GetVoucherReq();
        voucherReq.setTimestamp(DateTimeUtil.getFormatDate(new Date(), "yyyyMMddHHmmssSSS")+ CommonUtils.getCharAndNum(8));
        voucherReq.setMobileNo("15523562215");
        voucherReq.setOrderDesc("TestOfLocalVoucher");
        voucherReq.setProductNo("20180309SIP");
        voucherReq.setOrgOrderNo("TestOfLocalVoucher");
        String reqStr = JSONObject.toJSONString(voucherReq);
        System.out.print("请求参数json字符串" + reqStr);
        String sipPubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC65Nl9lRszYoE8RqErsqDd9zItv+1CHj2SGVZMhYDE/2yYl8kGuRROfqTecvwroA3TVmMqe46Sz8XM8wXfLew7sl6Oazw+hsUiYS02l33SWJgJ8XVtrN9F/kQ8tHSqsXNqD8gjpgH0fSZ1fqoDW3fWjr3ZR1pDvHCL8FlUnEEcEQIDAQAB";
        String orgPriKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=";
        PublicKey sipPubKey = RSAUtils.getPublicKeyFromString(sipPubKeyStr);
        PrivateKey orgPriKey = RSAUtils.getPrivateKeyFromString(orgPriKeyStr);
        String data = RSAUtils.encrypt(reqStr, sipPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        String sign = RSAUtils.sign(reqStr, orgPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
        String params = "org=" + org + "&data=" + URLEncoder.encode(data, "utf-8") + "&sign=" + URLEncoder.encode(sign, "utf-8");
        String res = HttpUtil.doPost(url, params, "application/x-www-form-urlencoded;charset=UTF-8");
        System.out.print("响应数据为" + res);
        String[] split = res.split("&sign=");
        String s1 = (String)split[0].replace("data=","");

        //解密数据
        String s = URLDecoder.decode(s1,"utf-8");
        String plain = RSAUtils.decrypt(s,orgPriKey,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        System.out.print("响应明文:"+plain);
    }
}
