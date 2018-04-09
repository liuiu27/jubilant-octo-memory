package com.cupdata.sip.bestdo.biz;

import com.alibaba.fastjson.JSON;
import com.cupdata.sip.bestdo.vo.request.BookDateReq;
import com.cupdata.sip.bestdo.vo.request.MerDetailReq;
import com.cupdata.sip.bestdo.vo.request.MerItemListReq;
import com.cupdata.sip.bestdo.vo.response.*;
import com.cupdata.sip.common.lang.RSAHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * @author Tony
 * @date 2018/03/30
 */
@Service
@Slf4j
public class BestdoBiz {


    @Value("${bestdo.api-address:http://test.cupd.bestdo.com}")
    String apiAddress;

    @Autowired
    private RestTemplate restTemplate;


    @Cacheable(cacheNames = "ehcache")
    public  BestaResVO<List<MerInfoRes>> getMerLists() {

        BestaResVO<List<MerInfoRes>> MerItemInfo = restTemplate.exchange(apiAddress+"/mer/merLists",
                HttpMethod.GET, null, new ParameterizedTypeReference<BestaResVO<List<MerInfoRes>>>() {
                }).getBody();

        return MerItemInfo;
    }


    public BestaResVO getMerItemList(String rightproduct,String sporttype){

        MerItemListReq req = new MerItemListReq();

        req.setRightProduct(rightproduct);
        req.setSportType(sporttype);

        BestaResVO<List<MerItemRes>> resVO = restTemplate.exchange(apiAddress + "/mer/item/getMerItemList?merItemList={json}",
                HttpMethod.GET, null, new ParameterizedTypeReference<BestaResVO<List<MerItemRes>>>() {}, JSON.toJSONString(req)).getBody();
        log.info(resVO.getData().toString());

        return resVO;
    }


    @Cacheable
    public BestaResVO getMerDetail(String merItemId){

        MerDetailReq req = new MerDetailReq();

        req.setMerItemId(merItemId);

        MerDetailResVO ret = restTemplate.getForObject(apiAddress + "/mer/item/detail/merDetail?merItemInfo={merItemInfo}",
                MerDetailResVO.class, JSON.toJSONString(req));

        return ret;
    }


    public BestaResVO  getBookDate(String rightproduct,String sporttype,String setMerItemId,String setVenueNo){

        BookDateReq req =new BookDateReq();

        req.setRightProduct(rightproduct);
        req.setSportType(sporttype);
        req.setMerItemId(setMerItemId);
        req.setVenueNo(setVenueNo);

        BookDateResVO ret = restTemplate.getForObject(apiAddress + "/mer/item/detail/getBookDate?merItemDetails={json}",
                BookDateResVO.class, JSON.toJSONString(req));

        return ret;

    }

    public String crateBestdoOrder(String parma){

        String ret = restTemplate.getForObject(apiAddress + "/orders/createOrder?orderInfo={json}",
                String.class, parma);

        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI8U/zj1piPd741OtdAMdSZSdBRE" +
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

        ret = new String(Base64.getDecoder().decode(ret));

        log.info(ret);

        BASE64Decoder b64 = new BASE64Decoder();
        try {
            ret = RSAHelper.decipher(Base64.getEncoder().encodeToString(b64.decodeBuffer(ret)), privateKey);
            return ret;
        } catch (IOException e) {
            log.error("解析错误！！！");
        }
        return null;

    }


}
