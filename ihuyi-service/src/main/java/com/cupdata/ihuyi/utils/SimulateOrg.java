package com.cupdata.ihuyi.utils;

import com.alibaba.fastjson.JSONObject;
import com.cupdata.commons.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.PrivateKey;
import java.security.PublicKey;

@Slf4j
@RestController
public class SimulateOrg {

    /**
     * 用于模拟机构：sip调用机构接口将结果通知给机构,返回SUCCESS则为接收成功
     * @param req
     * @return
     */
    @RequestMapping("/givenOrg")
    public String simulateOrgInterface(HttpServletRequest req) throws Exception {

        String s = "fail";
        log.info("本地模拟通机构接口.....");
        String data = req.getParameter("data");
        log.info("请求数据:"+data);
        String sign = req.getParameter("sign");
        log.info("签名:"+sign);

        //机构的私钥进行解密数据
        String simulatePriKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=";
        PrivateKey privateKey = RSAUtils.getPrivateKeyFromString(simulatePriKey);
        //解密密文数据
        String dataPlain = RSAUtils.decrypt(data, privateKey, RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
        log.info("明文数据:"+dataPlain);
        rechargeNotifyVo rechargeNotifyVo = JSONObject.parseObject(dataPlain, rechargeNotifyVo.class);

        //sip的公钥进行验验签
        String sipPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC65Nl9lRszYoE8RqErsqDd9zItv+1CHj2SGVZMhYDE/2yYl8kGuRROfqTecvwroA3TVmMqe46Sz8XM8wXfLew7sl6Oazw+hsUiYS02l33SWJgJ8XVtrN9F/kQ8tHSqsXNqD8gjpgH0fSZ1fqoDW3fWjr3ZR1pDvHCL8FlUnEEcEQIDAQAB";
        PublicKey PublicKey = RSAUtils.getPublicKeyFromString(sipPubKey);
        boolean isPass = RSAUtils.checkSign(dataPlain, sign, PublicKey, RSAUtils.SIGN_ALGORITHMS_MGF1, RSAUtils.UTF_8);
        log.info("签名数据:"+isPass);
        //验签成功即可完成接受数据
        if(!isPass){
            s = "SUCCESS";
            log.info("对象："+rechargeNotifyVo);
        }
        return s;
    }

}
