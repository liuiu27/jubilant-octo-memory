package com.cupdata.lakala;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.lakala.feign.ConfigFeignClient;
import com.cupdata.sip.common.api.config.response.SysConfigVO;
import com.cupdata.sip.common.api.voucher.request.GetVoucherReq;
import com.cupdata.sip.common.lang.constant.SysConfigParaNameEn;
import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;
import com.cupdata.sip.common.lang.utils.HttpUtil;
import com.cupdata.sip.common.lang.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LakalaApplicationTest {

    @Resource
    private ConfigFeignClient configFeignClient;


    @Test
    public void testconfig(){

        SysConfigVO lakala_voucher_partner = configFeignClient.getSysConfig("LAKALA_VOUCHER_PARTNER", SysConfigParaNameEn.HUAJIFEN_BANK_CODE);

        log.info(lakala_voucher_partner.getParaValue());

        Assert.assertNotNull(lakala_voucher_partner);
    }


    @Test
    public void LakalaApplicationTest() throws Exception{
        //请求URL
        String url1 = "http://localhost:46959/voucher/voucher/getVoucher";
        String url2 = "http://cvpa.leagpoint.com/sipService/voucher/voucher/getVoucher";
        String url3 = "http://10.193.17.84:46959/voucher/voucher/getVoucher";
        String url4 = "https://onlinepay.cupdata.com/sipService/voucher/voucher/getVoucher";

        //请求参数
        String org = "2018010200000001";
        GetVoucherReq getVoucherReq = new GetVoucherReq();
        //getVoucherReq.setTimestamp(DateTimeUtil.getFormatDate(new Date(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8));
        getVoucherReq.setProductNo("20180105V123");
        getVoucherReq.setOrgOrderNo("T2018041301");
        getVoucherReq.setOrderDesc("LAKALATEST");
        getVoucherReq.setMobileNo("15857128524");
        getVoucherReq.setExpire("20180820");
        String reqStr = JSONObject.toJSONString(getVoucherReq);
        System.out.println("请求参数json字符串" + reqStr);

        //秘钥
        String sipPubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC65Nl9lRszYoE8RqErsqDd9zItv+1CHj2SGVZMhYDE/2yYl8kGuRROfqTecvwroA3TVmMqe46Sz8XM8wXfLew7sl6Oazw+hsUiYS02l33SWJgJ8XVtrN9F/kQ8tHSqsXNqD8gjpgH0fSZ1fqoDW3fWjr3ZR1pDvHCL8FlUnEEcEQIDAQAB";
        String orgPriKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=";
        PublicKey sipPubKey = RSAUtils.getPublicKeyFromString(sipPubKeyStr);
        PrivateKey orgPriKey = RSAUtils.getPrivateKeyFromString(orgPriKeyStr);
        String data = RSAUtils.encrypt(reqStr, sipPubKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        String sign = RSAUtils.sign(reqStr, orgPriKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);

        //打印加密数据
        System.out.println("加密请求数据:"+data);
        System.out.println("加密请求签名:"+sign);
        System.out.println("URLEncoder请求数据:"+URLEncoder.encode(data, "utf-8"));
        System.out.println("URLEncoder请求签名:"+URLEncoder.encode(sign, "utf-8"));

        //请求
        String params = "org=" + org + "&data=" + URLEncoder.encode(data, "utf-8") + "&sign=" + URLEncoder.encode(sign, "utf-8");

        //String res = HttpUtil.doPost(url1, params, "application/x-www-form-urlencoded;charset=UTF-8");
        String res = "data=pHztvhkEDAtAPCOTThYTBitmQC19R6TlNAJWLsQ7elxtDPot4cas6JUKBFzJwcwjGqEAOZ%2Fb9fz1YOFtC7c3JQNh3KJa%2Fn3SUJ2wH4bk0bqGDSr06pnZatM4CCvr0skXkTPIycZikQURTWkffYqEov2mtT86cC6R3f%2B35t3yP0M%3D&sign=D%2FemJf5%2Bb%2FdFp7K54al4n1%2BbOSiA3Pu2%2FI7QTkQJ4mhOFymvG5hM6HqOqdBdK0xtxZsalbg9QF2VmxlpZczswoWK1Ng9bIMtl%2FWNNxmXARGbBDR48Y%2FLABiTMS0VNtWHNLskHwjAHW1TAHJoJ7Twfm7yEzMzrFtgdzuOjtuCMhI%3D";
        //System.out.print("响应数据为" + res);

        //解密数据
        String[] split = res.split("&sign=");
        String s1 = (String)split[0].replace("data=","");
        String s = URLDecoder.decode(s1,"utf-8");
        String plain = RSAUtils.decrypt(s,orgPriKey,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        System.out.println("响应明文:"+plain);
    }

    @Test
    public  void checkKeyPair()throws Exception{

        String sipPubKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC65Nl9lRszYoE8RqErsqDd9zItv+1CHj2SGVZMhYDE/2yYl8kGuRROfqTecvwroA3TVmMqe46Sz8XM8wXfLew7sl6Oazw+hsUiYS02l33SWJgJ8XVtrN9F/kQ8tHSqsXNqD8gjpgH0fSZ1fqoDW3fWjr3ZR1pDvHCL8FlUnEEcEQIDAQAB";
        String orgPriKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=";
        PublicKey publicKey = RSAUtils.getPublicKeyFromString(sipPubKeyStr);
        PrivateKey privateKey = RSAUtils.getPrivateKeyFromString(orgPriKeyStr);
        String encrypt = RSAUtils.encrypt("验证秘钥成功", publicKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        String decrypt = RSAUtils.decrypt(encrypt, privateKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        System.out.print(decrypt);

    }
    }

