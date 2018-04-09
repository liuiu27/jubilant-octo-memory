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
import java.security.PublicKey;
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

        String publickey ="";

        PublicKey publicKey = RSAHelper.getPemPublicKey(publickey);

        parma = RSAHelper.encipher(parma,publicKey,8);

        String ret = restTemplate.getForObject(apiAddress + "/orders/createOrder?orderInfo={json}",
                String.class, parma);

        String privateKey = "";

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
