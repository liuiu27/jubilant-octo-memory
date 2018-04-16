package com.cupdata.sip.bestdo.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cupdata.sip.bestdo.feign.SupplierFeignClient;
import com.cupdata.sip.bestdo.vo.request.BookDateReq;
import com.cupdata.sip.bestdo.vo.request.MerDetailReq;
import com.cupdata.sip.bestdo.vo.request.MerItemListReq;
import com.cupdata.sip.bestdo.vo.response.*;
import com.cupdata.sip.common.api.BaseResponse;
import com.cupdata.sip.common.api.orgsup.response.SupplierInfVo;
import com.cupdata.sip.common.lang.RSAHelper;
import com.cupdata.sip.common.lang.constant.ResponseCodeMsg;
import com.cupdata.sip.common.lang.exception.BestdoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
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
@CacheDefaults(cacheName = "ehcache")
public class BestdoBiz {


    @Value("${bestdo.api-address:http://test.cupd.bestdo.com}")
    String apiAddress;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SupplierFeignClient supplierFeignClient;

    public  BestaResVO<List<MerInfoRes>> getMerLists() {

        BestaResVO<List<MerInfoRes>> MerItemInfo = restTemplate.exchange(apiAddress+"/mer/merLists",
                HttpMethod.GET, null, new ParameterizedTypeReference<BestaResVO<List<MerInfoRes>>>() {
                }).getBody();

        return MerItemInfo;
    }


    @CacheResult
    public BestaResVO<List<MerItemRes>> getMerItemList(String rightproduct,String sporttype){

        MerItemListReq req = new MerItemListReq();

        req.setRightProduct(rightproduct);
        req.setSportType(sporttype);

        BestaResVO<List<MerItemRes>> resVO = restTemplate.exchange(apiAddress + "/mer/item/getMerItemList?merItemList={json}",
                HttpMethod.GET, null, new ParameterizedTypeReference<BestaResVO<List<MerItemRes>>>() {}, JSON.toJSONString(req)).getBody();
        log.info(resVO.getData().toString());

        return resVO;
    }

    @CacheResult
    public MerDetailResVO getMerDetail(@CacheKey String merItemId){

        MerDetailReq req = new MerDetailReq();

        req.setMerItemId(merItemId);

        MerDetailResVO ret = restTemplate.getForObject(apiAddress + "/mer/item/detail/merDetail?merItemInfo={merItemInfo}",
                MerDetailResVO.class, JSON.toJSONString(req));

        return ret;
    }


    public BookDateResVO  getBookDate(String rightproduct,String sporttype,String setMerItemId,String setVenueNo){

        BookDateReq req =new BookDateReq();

        req.setRightProduct(rightproduct);
        req.setSportType(sporttype);
        req.setMerItemId(setMerItemId);
        req.setVenueNo(setVenueNo);

        BookDateResVO ret = restTemplate.getForObject(apiAddress + "/mer/item/detail/getBookDate?merItemDetails={json}",
                BookDateResVO.class, JSON.toJSONString(req));

        return ret;

    }

    public SaidianOrderRes crateBestdoOrder(String parma){

        BaseResponse<SupplierInfVo> supByNo = supplierFeignClient.findSupByNo("20180409S23333376");
        if (!supByNo.getResponseCode().equals( ResponseCodeMsg.SUCCESS.getCode())){
            log.error("获取供应商密钥失败");
            throw new BestdoException("20180409S23333376","获取供应商密钥失败");
        }

        SupplierInfVo supplierInfVo = supByNo.getData();

        String supPublickey =supplierInfVo.getSupplierPubKey();

        PublicKey publicKey = RSAHelper.getPemPublicKey(supPublickey);

        if (null == publicKey){
            log.error("无法解析供应商密钥，info："+supPublickey);
            throw new BestdoException("20180409S23333376","无法解析供应商密钥");
        }
        parma = RSAHelper.encipher(parma,publicKey,8);
        parma = Base64.getEncoder().encodeToString(parma.getBytes());
        String ret = restTemplate.getForObject(apiAddress + "/orders/createOrder?orderInfo={json}",
                String.class, parma);

        String privateKey =supplierInfVo.getSipPriKey();

        log.info(privateKey);

        ret = new String(Base64.getDecoder().decode(ret));

        log.info("response result, "+ret);

        BASE64Decoder b64 = new BASE64Decoder();
        try {
            ret = RSAHelper.decipher(Base64.getEncoder().encodeToString(b64.decodeBuffer(ret)), privateKey);

            SaidianOrderRes saidianOrderRes = JSONObject.parseObject(ret,SaidianOrderRes.class);
            if (saidianOrderRes.getResCode().equals("EEEE"))
                throw new BestdoException(ResponseCodeMsg.FAIL.getCode(),saidianOrderRes.getResInfo());
            return saidianOrderRes;
        } catch (IOException e) {
            log.error("解析错误！！！");
        }

        return null;

    }


}
