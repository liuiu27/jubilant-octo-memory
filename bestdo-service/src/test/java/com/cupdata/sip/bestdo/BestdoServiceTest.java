package com.cupdata.sip.bestdo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.cupdata.sip.common.lang.IOHelper;
import com.cupdata.sip.common.lang.RSAHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;

import java.util.Base64;

/**
 * 测试新赛点
 * @author Tony
 * @date 2018/03/30
 */
@Slf4j
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
public class BestdoServiceTest {


    private final static String TEST_URL = "http://test.cupd.bestdo.com";

    //@Autowired
    //@Qualifier("restTemplate")
    private RestTemplate restTemplate;



    @Before
    public void createRestTemplate() {
        restTemplate = new RestTemplate();
        //restTemplate.getMessageConverters().clear();
        restTemplate.getMessageConverters().add(new FastJsonHttpMessageConverter());
    }


    @Test
    public void merLists() {

        String ret =  restTemplate.getForObject("http://test.cupd.bestdo.com/mer/merLists",String.class);

        log.info(ret);
    }

    @Test
    public void testMerDetail() {
        //merItemId
        //1020125	高尔夫练习场
        //1020279	羽毛球
        //1020102	银联数据-健身
        //1020105	银联数据-网球
        //1020103	银联数据-游泳
        //1020107	银联数据-星级酒店游泳
        JSONObject json = new JSONObject();
        json.put("source", "CUPD");
        json.put("tradeCode", "VENUElIST");
        json.put("merItemId", "10201051000013");

        String ret = restTemplate.getForObject(TEST_URL + "/mer/item/detail/merDetail?merItemInfo={merItemInfo}",
                String.class, json.toJSONString());
        log.info(ret);

    }

    @Test
    public void testMerItemList() {
       // restTemplate =new RestTemplate();
        JSONObject json = new JSONObject();
        json.put("source", "CUPD");
        json.put("tradeCode", "VENUElIST");
        json.put("rightProduct", "304");
        json.put("rightName", "新赛点网球");
        json.put("cityMark", "52");
        json.put("sportType", "101");
        json.put("merid", "1020105");
        json.put("venueName", "");
        json.put("currentPage", "1");
        json.put("pageSize", "20");
        json.put("rtnFlag", "");

        String ret = restTemplate.getForObject(TEST_URL + "/mer/item/getMerItemList?merItemList={json}",
                String.class, json.toJSONString());
        log.info(ret);

    }

    @Test
    public void testGetBookDate() {
       //
        JSONObject json = new JSONObject();
        json.put("source", "CUPD");
        json.put("tradeCode", "VENUElIST");
        json.put("rightProduct", "304");
        json.put("venueNo", "1010000753001");
        json.put("cityMark", "52");
        json.put("sportType", "101");
        json.put("merItemId", "10201051000013");


        String ret = restTemplate.getForObject(TEST_URL + "/mer/item/detail/getBookDate?merItemDetails={json}",
                String.class, json.toJSONString());
        log.info(ret);


    }

    @Test
    public void testCrateOrder() throws Exception {

        String parma ="US81TXBDc044R1QxL3FKWUtqT09GWkJQQnNuY01TcmZEQ3ZvcU9IMnRFT3BZOEN5dWFGaWR2OHRjSGtsNGhLUTRBckNTVUptclFITQ0KMm9CSGFZcStkd0xsS0lPZ1RVODdOSkRReXlFNU1kZU4yY2k1NmRWOU9Qd0NiZzc5TFJ4amF2Q1UwYkY3SzFXWHk5QlQvdXR5UHNKag0KaVYyWmhqNjlML0dPdTFzM24wRXBtSmhBVytaeFBZWjdxd3M3Y3VYQlVFSXFWTXdhSFFXVTlKL1lta1ArakJoQzFuRTFTSHAzVkxYSA0KQUtwaVRVUXVYeElKVzRSUUVHL3RCandJN3E4U3JPVXM0VDNFWUt1SDdTYjJ0cktRYjhpUVJmaStRSTNka2FYVmZ6V0h6Y3kwY1dVNA0KdWQvaWZpSUFVOFp2Qzlja3kxTzhmYmM5MzI3cUlTRXovVlRXWVYrNGkxMGVaS0owaktjNVRQQUJmaGd6dmNOMjM1em9LcFUraGtoRw0KRk5MK2M0U0t6emNHQW13WjlKZmsrSjN2MWpJSlBzTkVVbDJvN014aXhFZHpKOHBjWkp3WlZQc1BCQVNnN0VEeS8rcmhvQ3cxTU1VNg0Kcm1oWnp0MWlZN0p5bWt6QTlMZWtBY2RKQW55Mkx2MnZ5UVVBZ3ZESFIrSmVGeGhhcGx0TFRYa2FuU2YvRm9OTzBwZmZ5cmVCUlJIaQ0KK3ZpSUhENnlnT3h3L0EwQVZLQmZXYUZ1MFZ2MGtGYXVqbmZqZDdmeUYvTjdBN2Foc1NWMEpYQWN6L1RoVXo3YkJyNHFadjdhRDV5Sw0KQmc3NUdFOEg2a3JmS1JPdFlzc1VLbERqV1cxWHdNemt0VmJKbWJBdHlFTkZBd1JsSTczaktMNWl0SDNuUzM4Mjlrc2NCY2tyNXY4PQ==";

        String ret = restTemplate.getForObject(TEST_URL + "/orders/createOrder?orderInfo={json}",
                String.class, parma);

        log.info(ret);

        //私钥文件路径
        String privateKeyFilePath = "D:\\Work\\.sip\\bestdo\\private.key";


        String privateKey ="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI8U/zj1piPd741OtdAMdSZSdBRE" +
                "+3iq8vURpjs3zwCmiO3chcyR1hW3aIUc3WYkEWga4/Gm7eKlXxaitd5hENTwnjxAOpSuAByOFHPb" +
                "Q/WIsMuoiRzoYV7gIJy9WekSCIcGL9JW7wLijJCpTf8uhXFLCfAo3CI/gbi46xgSVW77AgMBAAEC" +
                "gYAElCbvQEktdu14mR2gzUSHAKVMZmQtjd4u9ttlpAHJgCITLRnpBTZCOY7PSpkh5Qt+dvS9EHI9" +
                "7QI1kxd867dzt6vI1Y4v0PSHIlgRxYqODp0hw/3tjOiK/RvyKU9wleh7FgcxDETepUEMTXDeo647" +
                "tU4TF7J4+GyUJxz9+/eXMQJBAMPTAQVjwQmFiGuUV8BjEWyoFfT+tWPUcqDllz62BAqzQ6hm42D2" +
                "uVDzP+9eg+6Mzhp5NWEqmeADC+etFLIJ1ysCQQC7DOWK3hJwbmIi/GzkwgNVDfksClz2YGz8XPB9" +
                "gc/m7XPMNabQRcxvoTy24o92Wbt3DqRb36LznadeFrDFLd9xAkBcQFkoytezvp6H37h/P6yDvaOq" +
                "aRvWzcy6k65uspyw1ca33NCda13eDto90A7jIJ4vxo4pGkKnT4gaOmWXgh9FAkAzHsAxJqYVciWB" +
                "+EjucBOnEC2UGrTzZMEEa4YSVwLx0t195v/TFfBcZc2JEfwxVS7FyAulTEZlnCWcskjXasURAkBl" +
                "dDc7I28Xq70rVji003hq6qrqNPaKfqr7TwUjHY07BNuA9v+EO4G8TPfPmVCxksetRX69BUn1BMTU" +
                "8asBsgbp";

        log.info(privateKey);


        privateKey = IOHelper.readFileToSting(privateKeyFilePath);

        log.info("111");
        log.info(privateKey);

        ret =new String(Base64.getDecoder().decode(ret));

        log.info(ret);

        BASE64Decoder b64=new BASE64Decoder();

        log.info(RSAHelper.decipher(Base64.getEncoder().encodeToString(b64.decodeBuffer(ret)),privateKey,256));

    }


    @Test
    public void test() throws Exception {

       String a ="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI8U/zj1piPd741OtdAMdSZSdBRE\n" +
               "+3iq8vURpjs3zwCmiO3chcyR1hW3aIUc3WYkEWga4/Gm7eKlXxaitd5hENTwnjxAOpSuAByOFHPb\n" +
               "Q/WIsMuoiRzoYV7gIJy9WekSCIcGL9JW7wLijJCpTf8uhXFLCfAo3CI/gbi46xgSVW77AgMBAAEC\n" +
               "gYAElCbvQEktdu14mR2gzUSHAKVMZmQtjd4u9ttlpAHJgCITLRnpBTZCOY7PSpkh5Qt+dvS9EHI9\n" +
               "7QI1kxd867dzt6vI1Y4v0PSHIlgRxYqODp0hw/3tjOiK/RvyKU9wleh7FgcxDETepUEMTXDeo647\n" +
               "tU4TF7J4+GyUJxz9+/eXMQJBAMPTAQVjwQmFiGuUV8BjEWyoFfT+tWPUcqDllz62BAqzQ6hm42D2\n" +
               "uVDzP+9eg+6Mzhp5NWEqmeADC+etFLIJ1ysCQQC7DOWK3hJwbmIi/GzkwgNVDfksClz2YGz8XPB9\n" +
               "gc/m7XPMNabQRcxvoTy24o92Wbt3DqRb36LznadeFrDFLd9xAkBcQFkoytezvp6H37h/P6yDvaOq\n" +
               "aRvWzcy6k65uspyw1ca33NCda13eDto90A7jIJ4vxo4pGkKnT4gaOmWXgh9FAkAzHsAxJqYVciWB\n" +
               "+EjucBOnEC2UGrTzZMEEa4YSVwLx0t195v/TFfBcZc2JEfwxVS7FyAulTEZlnCWcskjXasURAkBl\n" +
               "dDc7I28Xq70rVji003hq6qrqNPaKfqr7TwUjHY07BNuA9v+EO4G8TPfPmVCxksetRX69BUn1BMTU\n" +
               "8asBsgbp";

       log.info(a.replace(StringUtils.LF,""));

    }

}
