package com.cupdata.iqiyi;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.sip.common.api.recharge.request.RechargeReq;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;
import com.cupdata.sip.common.lang.utils.HttpUtil;
import com.cupdata.sip.common.lang.utils.RSAUtils;
import org.junit.Test;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class IqiyiApplicationTest {


    @Test
    public void iqiyiRecharge() throws Exception{
        //爱奇艺获取券码充值网关URL
        String url1 = "http://localhost:46959/recharge/recharge/getRecharge";
        String url2 = "http://cvpa.leagpoint.com/sipService/recharge/recharge/getRecharge";
        String url3 = "https://onlinepay.cupdata.com/sipService/recharge/recharge/getRecharge";

        //请求参数
        String org = "2018010200000001";
        RechargeReq rechargeReq = new RechargeReq();
        rechargeReq.setTimestamp(DateTimeUtil.getFormatDate(new Date(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8));
        rechargeReq.setMobileNo("15857128524");
        rechargeReq.setAccount("1234567"); //充值账号
        rechargeReq.setOrgOrderNo("MALL001"); //机构唯一订单编号
        rechargeReq.setProductNo("171026R640");//爱奇艺产品编号
        rechargeReq.setOrderDesc("IQIYI");
        String reqStr = JSONObject.toJSONString(rechargeReq);
        System.out.print("请求参数json字符串" + reqStr);

        //秘钥
        String sipPubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC65Nl9lRszYoE8RqErsqDd9zItv+1CHj2SGVZMhYDE/2yYl8kGuRROfqTecvwroA3TVmMqe46Sz8XM8wXfLew7sl6Oazw+hsUiYS02l33SWJgJ8XVtrN9F/kQ8tHSqsXNqD8gjpgH0fSZ1fqoDW3fWjr3ZR1pDvHCL8FlUnEEcEQIDAQAB";
        String orgPriKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=";
        PublicKey sipPubKey = RSAUtils.getPublicKeyFromString(sipPubKeyStr);
        PrivateKey orgPriKey = RSAUtils.getPrivateKeyFromString(orgPriKeyStr);

        //加密
        String data = RSAUtils.encrypt(reqStr, sipPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        String sign = RSAUtils.sign(reqStr, orgPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
        String params = "org=" + org + "&data=" + URLEncoder.encode(data, "utf-8") + "&sign=" + URLEncoder.encode(sign, "utf-8");

        //打印加密数据
        System.out.println("加密请求数据:"+data);
        System.out.println("加密请求签名:"+sign);
        System.out.println("URLEncoder请求数据:"+URLEncoder.encode(data, "utf-8"));
        System.out.println("URLEncoder请求签名:"+URLEncoder.encode(sign, "utf-8"));

        //请求
        String res = HttpUtil.doPost(url2, params, "application/x-www-form-urlencoded;charset=UTF-8");
        System.out.println("响应数据为:" + res);

        //解密数据
        String[] split = res.split("&sign=");
        String s1 = (String)split[0].replace("data=","");
        String s = URLDecoder.decode(s1,"utf-8");
        String plain = RSAUtils.decrypt(s,orgPriKey,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        System.out.print("响应明文:"+plain);
    }

    /**
     * 生产测试
     * @throws Exception
     */
    @Test
    public void testdc() throws Exception{

        //机构编号
        String org = "20180412O86740479";
        //请求参数
        RechargeReq rechargeReq = new RechargeReq();
        rechargeReq.setTimestamp(DateTimeUtil.getFormatDate(new Date(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8));
        rechargeReq.setMobileNo("15857128524");
        rechargeReq.setAccount("15857128524"); //充值账号
        rechargeReq.setOrgOrderNo("666666");  //机构唯一订单编号
        rechargeReq.setProductNo("20180412RECHARGE6161");  //爱奇艺产品编号
        rechargeReq.setOrderDesc("IQIYITEST");
        String reqStr = JSONObject.toJSONString(rechargeReq);
        String url3 = "https://onlinepay.cupdata.com/sipService/recharge/recharge/getRecharge";

        String pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDRxT504szbX+yi5ejT2M5xe7ArET6DXUNKXUhWn+acfdkRLTgzvayQtyN4qtkkrVZEiBEpra1/J4R1EZ2G6wyB/9vkj1c8wCTu1tcV7DMhBfWin8Q5lXQOUjBCg81i93EwO/IF42QPw/Tm24oqTC4QuB1uTI2m56llmfy6fa6H7wIDAQAB";
        String pri = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANHFPnTizNtf7KLl6NPYznF7sCsRPoNdQ0pdSFaf5px92REtODO9rJC3I3iq2SStVkSIESmtrX8nhHURnYbrDIH/2+SPVzzAJO7W1xXsMyEF9aKfxDmVdA5SMEKDzWL3cTA78gXjZA/D9ObbiipMLhC4HW5MjabnqWWZ/Lp9rofvAgMBAAECgYAY7Ejz5UB2rgp0/kDv7pn0lMAFFerp+6oziyq9lAj3veIM7uT3DMmUdhXiT9Y1y9xsjwgO/iIXcfAEYr/nGEUnc85e5sPUZSurD1wLB9emScYmxro03PFYEmJ3xqdiZOIchrIUC37Zxrebq/zKfp6VQ4qixxEZYgTOiOWemlx3jQJBAPCcbpAIDfWn3OF8q362OnRB9r3NOWPEyUTImsz0qxXBudV7YtFFjc28VRhVDgi702G/SB/KfC9xJSaNk+BgqYsCQQDfL9qQmnu1KlwDjYb8SPdr3KbGM0+4Oom5y9w3KW5G1SSbimNvIgALYaMNi2XUYj+ie3ccxYTubajMEQ5AwH+tAkBzd+8LSgJBAOTchXLbpWIaBsn9vi4rdfXM/6RidYxhLY4cKFF88q8hq57+xVqt0E2aHCzlrMu6DMdyYAE2bc0PAkEAjz/XuRhubklR5bXg/eyXYdOt92jXshdgbrA6F+2vqicD6BFa4OmhvaxdS0Q9h6PH1DIKsZzVRXN88/2+eDEVwQJBAJsfm96s5JUjyRZBTPjYA1keKG0CAGBFDPvbodwrTdmobNRCxTUyeMcz7u5E94U2fg/BNGdLn+l8eHEvWvhV+Gw=";

        PublicKey sipPubKey = RSAUtils.getPublicKeyFromString(pub);
        PrivateKey orgPriKey = RSAUtils.getPrivateKeyFromString(pri);

        String data = RSAUtils.encrypt(reqStr, sipPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        String sign = RSAUtils.sign(reqStr, orgPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
        String params = "org=" + org + "&data=" + URLEncoder.encode(data, "utf-8") + "&sign=" + URLEncoder.encode(sign, "utf-8");

        //请求
        //String res = HttpUtil.doPost(url3, params, "application/x-www-form-urlencoded;charset=UTF-8");
        //System.out.println("响应数据为:" + res);

        System.out.println(data);
        System.out.println(sign);
        String res = "data=HwAdnKB3v%2FFf00bFckvt1JpT8LPkzVHASHRSg5PsZDKgMVZl%2F8RLpiyBHn2dJEzALAp4L%2BFfVgORLF%2B3%2FX%2FpqOr6GlhDkYAuaJVlOq54me7vQ3LhPHE5bosxMq00AXlb8b5t0XOEV%2FCRqG5OHQt6YxKnsBuYw4euC%2B5b5VhAGvxPpEw23QucFCyl%2Fn7MKRdRHK31XIsVCN%2BEKAj667k8P3ojLkG9aDjHCbprFhvca4btzhi3WjBYr9IiomG59FTXn4QFCtyZRvZ8KZqPT5yDyZt%2FVReIID40WjHykD39aUw6iK3DtRshseSEX0Vj3D2jodrRnUbxrFyz5IO9ylFv7Q%3D%3D&sign=COZ%2Fx1c5nc4ZyBjX66YNbxOUUzoZuy9p1WlwWBf7OTYbCd1CK0NNK0AjO4gOLOQugTQx1VcADsDIJS7Dq5T4eNZXoSnPbrfX3i06amr40bIk5bLFqqKfaTvu3sSuKqQ839P0KBxkpp6Z3xVYjSK7bj980L%2FcEywPK1fHQu2sO3s%3D";

        System.out.println("响应数据为:" + res);

        //解密数据
        String[] split = res.split("&sign=");
        String s1 = (String)split[0].replace("data=","");
        String s = URLDecoder.decode(s1,"utf-8");
        String plain = RSAUtils.decrypt(s,orgPriKey,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        System.out.print("响应明文:"+plain);

    }
}
