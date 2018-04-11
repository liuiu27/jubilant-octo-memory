package com.cupdata.iqiyi;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.utils.CommonUtils;
import com.cupdata.commons.utils.DateTimeUtil;
import com.cupdata.commons.utils.HttpUtil;
import com.cupdata.commons.utils.RSAUtils;
import com.cupdata.commons.vo.recharge.RechargeReq;
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
        String url = "http://localhost:46959/recharge/recharge/getRecharge";
        String url2 = "http://cvpa.leagpoint.com/sipService/recharge/recharge/getRecharge";
        String url3 = "http://10.193.17.86:46959/recharge/recharge/getRecharge";
        String url4 = "http://cvpa.leagpoint.com/sipService/recharge/query/rechargeQuery";
        //花积分机构编号
        String org = "20180409O75853744";
        //请求参数
        RechargeReq rechargeReq = new RechargeReq();
        rechargeReq.setTimestamp(DateTimeUtil.getFormatDate(new Date(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8));
        rechargeReq.setMobileNo("15857128524");
        rechargeReq.setAccount("1231"); //充值账号
        rechargeReq.setOrgOrderNo("wwwwwww"); //机构唯一订单编号
        rechargeReq.setProductNo("20180409RECHARGE2527");//爱奇艺产品编号
        rechargeReq.setOrderDesc("test");
        String reqStr = JSONObject.toJSONString(rechargeReq);
        System.out.print("请求参数json字符串" + reqStr);
        String sipPubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC65Nl9lRszYoE8RqErsqDd9zItv+1CHj2SGVZMhYDE/2yYl8kGuRROfqTecvwroA3TVmMqe46Sz8XM8wXfLew7sl6Oazw+hsUiYS02l33SWJgJ8XVtrN9F/kQ8tHSqsXNqD8gjpgH0fSZ1fqoDW3fWjr3ZR1pDvHCL8FlUnEEcEQIDAQAB";
        String orgPriKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=";

        //String sipPubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZaOrUciDv9wAPYDje/5t4IiIgiJy/Em1NbB0E3USLzWnN5bx5gQnUGuoWBDZKv6hCJYDToqQ6i5uI7iSqBIStXHvNCoppbFHGeNHLT/+FyM0s27bi78SWHX0WFuIemR/GyKJN2V5Z1dGqfDe48hU/butzTCRiF0aaWxM9GlIwuQIDAQAB";
        //String orgPriKeyStr ="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANLl3bd9dZqO9VSBPfufjEL7RGgvnrAo9LzZnxImd0rbJkbL81IWZUP3llRE73bZr4NmLSFb19t4EH3sUepKj+RLLW+iEwRY6BKHchpoUtojJX1JLhfiBrWLHK2J6Szh2/Yd7f87sJTYgBOFQ16nmGkvmfQxHDQpg9zPH9drwuKDAgMBAAECgYAFxENLwSJ2F3Kd3OrU9offTP+R2bt9FaGbiFfRJzwbz+I8DVeZ5wLd2OdDZp8loM4ryZ9yShCc76uDxfJ3dZJ5ntl0NTf6G8bVYo+AGPSAAG3f18ZRLIWcv5hL1CF4Nr0ErmI3l8Qxaoe9dJgGnFCSn6wn3d/hkrJQ1F+by6AQAQJBAPccwPlo+7qFvjRua9dbIeXYsXWfVEd2LdZhFQlo/qnTlE+YLMg6WUy7mLp3K9KyGMDBEHkf+F7Gx7iNFKXr/nsCQQDae6yQEPynZHL7dZaAT5U/451OGzMyvOuIlWRw4v0L21J2fhx0EFlxUQ2aA72j35vTl9AABXSRCkr9XipPkvGZAkEAyWLzmc+HJ1GL22sKFC4/B/R7W2KH6t1TKd1gqZSZxomZ2uwnwt5anIBeTciFPGbMaXpuFGiLn6Hcbnj7lOUpNQJBANnaX+L3TTaMTqq2QW+H60Zda8kqDg8VmbYDYLe/XpqoAr3sWerRMp5vN6TO/hWdLf/6bIjY57aIEOY7Mu1NlGECQCOPHnP17B/IsedmqSO3UaE0bwCg6p2u3ac/lTwD6rXV3gS7hXBZ7OAcXya7m01WdGps6dn/Pq9lpVNxuLyd5hU=";



        PublicKey sipPubKey = RSAUtils.getPublicKeyFromString(sipPubKeyStr);
        PrivateKey orgPriKey = RSAUtils.getPrivateKeyFromString(orgPriKeyStr);
        String data = RSAUtils.encrypt(reqStr, sipPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        String sign = RSAUtils.sign(reqStr, orgPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
        String params = "org=" + org + "&data=" + URLEncoder.encode(data, "utf-8") + "&sign=" + URLEncoder.encode(sign, "utf-8");
        String res = HttpUtil.doPost(url2, params, "application/x-www-form-urlencoded;charset=UTF-8");
        System.out.println("响应数据为:" + res);
        String[] split = res.split("&sign=");
        String s1 = (String)split[0].replace("data=","");

        //解密数据
        String s = URLDecoder.decode(s1,"utf-8");
        String plain = RSAUtils.decrypt(s,orgPriKey,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        System.out.print("响应明文:"+plain);
    }
}
