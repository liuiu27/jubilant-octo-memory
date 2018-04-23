package com.cupdata.content;

import com.cupdata.commons.utils.RSAUtils;
import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.content.utils.EncryptionAndEecryption;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.net.URLEncoder;
import java.security.PrivateKey;
import java.util.*;

@Slf4j
public class ContentServiceApplication {

   @Test
   public void test() {

      TestRestTemplate restTemplate = new TestRestTemplate();

      String notifyurl = "https://test.wantu.cn/payGate/payNotify";
      Map<String, String> param = new HashMap<>();

      param.put("resultCode", "2");
      param.put("supOrderNo", "000117000077900000000000030291");//TODO 2018/4/17
      param.put("orderAmt", "0.02");
      param.put("sipOrderNo", "180417HD055316");

      notifyurl = EncryptionAndEecryption.Encryption(param,notifyurl);
      //String canshu =formatUrlMap(param,true,false);
      BaseResponse baseResponse = restTemplate.postForObject(notifyurl, null, BaseResponse.class);
      log.info(baseResponse.toString());

   }

   public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) {
      String buff = "";
      Map<String, String> tmpMap = paraMap;
      try {
         List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
         // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
         Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
               return (o1.getKey()).toString().compareTo(o2.getKey());
            }
         });
         // 构造URL 键值对的格式
         StringBuilder buf = new StringBuilder();
         for (Map.Entry<String, String> item : infoIds) {
            if (StringUtils.isNotBlank(item.getKey())) {
               String key = item.getKey();
               String val = null;
               if (item.getValue()==null) {
                  val = null;
               } else {
                  val = String.valueOf(item.getValue());
               }
               if (urlEncode) {
                  val = URLEncoder.encode(val, "utf-8");
               }
               if (keyToLower) {
                  buf.append(key.toLowerCase() + "=" + val);
               } else {
                  buf.append(key + "=" + val);
               }
               buf.append("&");
            }

         }
         buff = buf.toString();
         if (buff.isEmpty() == false) {
            buff = buff.substring(0, buff.length() - 1);
         }
      } catch (Exception e) {
         log.error("签名转换", e);
         return null;
      }
      return buff;
   }


   @Test
   public void test2() throws Exception {
    String data ="BxFr5ucpNungJOYNIS6N3tgyLh6XwOy2GgreqXdzeBrADcSsc+TfTG9076fTsc8MZl3SxL1e3z9y\n" +
            "/LUdeFA3hmEdBAWAsIyjdfEXH5t9K/NMUj8UyDB3II70exALjVf636QQK55ZS8P199zAIG3bfFS/\n" +
            "Z8fDSb4t6IVNw/x8J4aBHOZx+gWAAyWGUqu2+qvI+BnYFUkR2GbL/9+j8F1u1Xn/0c/pAGiDEhzE\n" +
            "M79uO2EcERSQRunki3Tq09rSNVhvnM4+IgaxiRBwmRhOdarbqSGoHjQRWqAckicnwQOTxKHcfMhU\n" +
            "TOuVmE+RycfDA7zeoA27nzcqTkC6MN98zsKFJw==";
    //data = URLDecoder.decode(data,"utf-8");
      System.out.println(data);
      PrivateKey sipPriKey = null;// 平台私钥
      sipPriKey = RSAUtils.getPrivateKeyFromString("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALrk2X2VGzNigTxGoSuyoN33Mi2/7UIePZIZVkyFgMT/bJiXyQa5FE5+pN5y/CugDdNWYyp7jpLPxczzBd8t7DuyXo5rPD6GxSJhLTaXfdJYmAnxdW2s30X+RDy0dKqxc2oPyCOmAfR9JnV+qgNbd9aOvdlHWkO8cIvwWVScQRwRAgMBAAECgYA5SGFc+3Gd20hPKDrIAPULc3O+z/+xb0Fh4UAxLg4c00j+sC8eT2Xo9SolQEsIOANkziqQ39QALYyr16TqFdI8pywmHFICisiyjKf7nIiqUfi9rVoUCiCxXrhwSmBwkGELcUcBhNupc7Bgqo7uCK+l1g8Qzj+oNtBMfv7sZrj8rQJBAPB0uIyV9ilF0QBFlQ4AaLuhKhqY9oX/vkMTspTpBkpaOv8QeOc6T+9DJAoLjkLlkXEfsLC14AHb4LdZV/kjdyMCQQDG+byuNLe3kqWqo1ecrf8mUw9tIquUkarWU0FuO9ysGjfrLdMLlsn3wlsxddU7rIelYwnLKBYBqdIkCuQiRq07AkEA1Fceyfd75EKlKEpKMI0n79mIpuhBe1+2kuGIKHwHdA1uX+QaAIe8Ixv1bXF69ZRo9a74h3R1Fu8m6ILbb0VkZQJARBcUPV0m/Xf+n000Xxaf+OJ1pfg2VSogFyX4fxuXIYH7XsyYqx+Xz+Q/xsY3CSu6Y5tnr5DxLvKJSfI8LYqYHwJBAIaXJcKpCQSsQQ+Eu8ib861dJWV4vP1jAt9xyeU90nyz5GMwWrWkQ/DkHedDVhyCURpxZTaqKpGnr9iIDIjVrD0=");
      data = RSAUtils.decrypt(data,sipPriKey,RSAUtils.ENCRYPT_ALGORITHM_PKCS1);
      log.info("decryt data:"+data);

   }

   @Test
   public void tes3t() {

      TestRestTemplate testRestTemplate = new TestRestTemplate();

      String url = "http://cvpc.leagpoint.com/mall/sip/content/contentOrderRefund.action?provider=LBHTDnJkYy4=&data=i2siVZiqoTp3GpIcLNAz88XGsxkod9xDiTC8NyZzv9RDwv1Ys5BkAXN%2F%2FpzpGB2sNFSC0djgTklUtBKexZm0mF0tUU2oMVC4DNMdQR7EREU3n4aBtauBwvE1smdgYaEDWoCNWgdHsmoc1uS9GCsNAWpsXtuYd4F%2FuvTnar%2F7x%2B0%3D&sign=ODudlxa08Y5A3VVCE8ftaBxFZWZx2Bx7fxDF%2B2arMb2QTJuvt1J6ibuxhnYtQM%2BocmQjGZS%2BBLGomPpTfSHXPT%2FHEduUdRud1Cv5zYF0l%2FGtiAj%2B8dIwKywPYKMZGhVC4HQtxIXH%2B1JjcsAfMfb3V6Bad%2FNiGsbl5yc1CZh5KSM%3D";

       String forObject = testRestTemplate.getForObject(url, String.class);

       log.info(forObject);

   }

}
