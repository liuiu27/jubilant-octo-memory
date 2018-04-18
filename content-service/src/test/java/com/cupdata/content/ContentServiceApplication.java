package com.cupdata.content;

import com.cupdata.content.utils.EncryptionAndEecryption;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.net.URLEncoder;
import java.util.*;

@Slf4j
public class ContentServiceApplication {

   @Test
   public void test() {

      TestRestTemplate restTemplate = new TestRestTemplate();

      String notifyurl = "https://test.wantu.cn/payGate/payNotify?method=rongshupay_app";
      Map<String, String> param = new HashMap<>();

      param.put("resultCode", "2");
      param.put("supOrderNo", "000117000077900000000000030291");//TODO 2018/4/17
      param.put("orderAmt", "0.02");
      param.put("sipOrderNo", "180417HD055316");

      notifyurl = EncryptionAndEecryption.Encryption(param,notifyurl);
      //String canshu =formatUrlMap(param,true,false);
      String s = restTemplate.postForObject(notifyurl, null, String.class);
      log.info(s);

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

}
