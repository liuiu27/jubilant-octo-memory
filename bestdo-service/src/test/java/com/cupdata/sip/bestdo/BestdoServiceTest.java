package com.cupdata.sip.bestdo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.cupdata.sip.bestdo.vo.request.BookDateReq;
import com.cupdata.sip.bestdo.vo.request.MerDetailReq;
import com.cupdata.sip.bestdo.vo.request.MerItemListReq;
import com.cupdata.sip.bestdo.vo.response.*;
import com.cupdata.sip.common.lang.IOHelper;
import com.cupdata.sip.common.lang.RSAHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.List;

/**
 * 测试新赛点
 *
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

        BestaResVO<List<MerInfoRes>> MerItemInfo = restTemplate.exchange("http://test.cupd.bestdo.com/mer/merLists",
                HttpMethod.GET, null, new ParameterizedTypeReference<BestaResVO<List<MerInfoRes>>>() {
        }).getBody();

        log.info(MerItemInfo.toString());
    }

    //{"cityMark":"","currentPage":"1","merid":"1020102","pageSize":"20","rightName":"新赛点高端游泳健身","rightProduct":"300","rtnFlag":"","source":"CUPD","sportType":"108","tradeCode":"VENUElIST","venueName":""}
    @Test
    public void testMerItemList() {

        //302	1020279|102
        //301	1020125|107
        //301	1020106|107
        //304	1020105|101
        //305	1020103|109
        //300	1020102|108

        MerItemListReq req = new MerItemListReq();

        req.setRightProduct("301");
        req.setSportType("107");


        BestaResVO<List<MerItemRes>> resVO = restTemplate.exchange(TEST_URL + "/mer/item/getMerItemList?merItemList={json}",
                HttpMethod.GET, null, new ParameterizedTypeReference<BestaResVO<List<MerItemRes>>>() {},JSON.toJSONString(req)).getBody();
        log.info(resVO.getData().toString());

    }

    @Test
    public void testMerDetail() {

        JSONObject json = new JSONObject();
        json.put("source", "CUPD");
        json.put("tradeCode", "VENUElIST");
        json.put("merItemId", "10201051000013");

        MerDetailReq req = new MerDetailReq();

        req.setMerItemId("10201051000013");

        MerDetailResVO ret = restTemplate.getForObject(TEST_URL + "/mer/item/detail/merDetail?merItemInfo={merItemInfo}",
                MerDetailResVO.class, JSON.toJSONString(req));
        log.info(ret.toString());

    }


    @Test
    public void testGetBookDate() {
        //
        JSONObject json = new JSONObject();
        json.put("source", "CUPD");
        json.put("tradeCode", "VENUElIST");
        json.put("rightProduct", "304");
        json.put("venueNo", "1010000753001");
        json.put("sportType", "101");
        json.put("merItemId", "10201051000013");
        BookDateReq req =new BookDateReq();

        req.setRightProduct("304");
        req.setSportType("101");
        req.setMerItemId("10201051000013");
        req.setVenueNo("1010000753001");

        BookDateResVO ret = restTemplate.getForObject(TEST_URL + "/mer/item/detail/getBookDate?merItemDetails={json}",
                BookDateResVO.class, JSON.toJSONString(req));
        log.info(ret.toString());


    }

    @Test
    public void testCrateOrder() throws Exception {

        String parma = "VlN3SERxaXRXSXM3ZC9HdlhLTHRJZ2xmcEkrT2NCOUZqRzZpVVkyOS9YeldqbDFPRmZmS0F2VHN3c3J4WmhEdWtkL3dUeU5MU2FNTzRIdDQ4ZjRaaG9IRmN4cUIxQVJBNjFJbVppMTJhdlBDQ3dvb1Y5KzlpL2dza0V2TGg5UjVTalV2N3JwTCt5ZjkxSTYyZHB4a0FmcEdIYUxHalI2VFJWcHRnZ1hRbkRRVXVDU1RYaDhMMjhFckI4R044UW9EOW5vZ1hCWlNzcDJtZElFdmgrOGN2bDM1alV6dElYeWZxcEc4WmpDeVlWY1R0SFp1cDFodUx6RlZWdXNnckZ5blJUTXdvbWxiWmtrSkJBdHlQSnBscXN3YjNGa1kwN1JqUUhjeEEvSHhudlZyRVhZTmcyMzNXNlJYUDBIOXozQzFHMThHNFNpd25oTC9NWkZvWjJXVXp5Tk9lTUwvS3JRMW44Q2drQ0lsYjZQV2VkL3p6enMwMnFONi9McDI2WmFZeDJtMTlUaHBxaUNrQWJySnpaK2JYdklIYVBNYWVlb1lkdkVaU1ZhU1hrVDVqWVg5ajZ0RGx0SWpqUjZPVUJJUHJkMDdtTWVidGViRis0RmlLc2FZcjY1V0dKanpJUFV1VG9yMkZXUjhZMU5HWUsxQ3cxTnNFbENJN3BhU2l6eU1jS1lLbFovOGMvMGl2WFpyRFBDbUVoSkVrUGtGNGEvRURITTNUMXlhZ3lKYzJRNFNVYkxIVjlDaHJLb3RuL2xLYWppNzdzS3pMcDBpZzYvNDFXZVZXNE1XbEh3dUVVSVJIUXIwZmxjMUxpR1NQSnZFWHpyUis2S2xaS21weUxsUytQbEpNRm5Zb2RVbU9CSk5ZRmNTem8yai9QSTNKejFsbGhBVlBFamtMVzgybHBmdzdDT0ROdlY1UmFRZldrK3d3TG9kVEYzeDNyT3JEN1IzUGtvRkNDRUJzVUMrd1JIUEF1K0ZBUGRpM2pOZmdqSHFSNmZpNGRzZUdLZmo3cisxbW96Z2R2b01NVDYyUEVxajd6UGtHanp6SjZjb3V4UVVtNjBXeTlNbEx4TW1aYlhYR2t6dUczRlJoRWRqWmJYeDlHNTZjT083R0N2TVpwSGg3Z0VlbFl3Qmw3a1VIQTNwbDU4dlRvMlRqQ1RpZDROeXZTcFpMV3dRNDZhYUFoV0lWMVBHS1BRNS9CYlpFNjZtVjBwVXREaUMzakU0USt4RW1GcEtRUStPWjNpeExPeGdjMDBvczhnOFRWdFcwOVMraEVqWVNmbEZsamphUG1zY1NBSmlyRkdBZm1LOHNpeDZKZGtWU2RBb29GcGxSR0lxZ05tV0xMN3pnbFJuSm9JOEZRdVFpK2JOZkZNMlFPckZZQUlOYkxXek9iYkJkSk54ejRINFNNbnZaTk1aU1NUTWhwbnFFOUxIZWw1QUtnem5xT2w3Y3dETDdHQ0QzSEg0SGJYVVZNNmFISUlOS21nRHJyRExxUXBDSkMyRHkzbDhFVjR1TnNnOUVOTzh6SzMzRTA2SlBHaUlzczRCWVRvUjNIcitIMkxVcFYyUUFaM2J4dXIvM0N4Nm4rSk1MQ0kxbWVrRnhoS1E4eXRzbE0rdEM3elVNRVpTQjZySExJRWJVaWZ4Yjg0Um5kdzZXZEFhTjVRNFI0R1dyME1MM2gwVFp3Y2tzYmZjdUxJZitjakVXSFUyUG03T0M4UitQdk5zMGU2WUhtNHZlWkRWRlhuUWhpNWV3WnpHWUMxb0hZKzJoTEEwb3RDNEQzMVVyU0FyRUVrb1dobjJUVVhWYkluNDF5MjI0WW5pZTd5VmRXUmxvdUtZT0hKdFE4eHdsRG1hcTBNTUN1MnpnaCtmWk1PYnJ1bkRkZ0RIdjl2Y2xrUk01eGVMOWZpSmwwWjJ4TGNCclNzUkx2VUM0alBhUWZtVG1mZWdxTVBLLzlaQSt1RktpUElHbHpwZGJTOFV4Q1g4RXBaSzA1bURSRjFqbFFYdVBES2NqZmRBODJtaEdJQ3Jjbm12VkI4b0xsd2I4VCswV1FUVWVCaUZ2OGh1T05vL0VISjF0eHZFRXphWndnU284QzFGcjk0aHExaDBZMkdOODFiT29qT3FwcTQvcCsxVzAyNnlZRzZVOHZNUXI4aUJrbDNtQXhIVFpDai8xeFM0KzFGYnQ0ZVAwdUhKa2kwejlHK3U4ZFljcTlGV0p0anpOeVVXZlhVTU9PSk9XTTNoR291dXE4VkJPcFVDdnJBekFUOENYSmxvckMxemJuMUZFUUVrU05uOVBNWVVzNEpYQkdrNzJpUmhGRDJOWVFlRTJJR3AwbzBpSStxb2N3OUF1cmxjNDJsWTNlYnJwVDQ3Rm9EUWl5QlZvenl1ZFBDcFVxSTJwTFVyc0h0WENjc1VqYzhINlBsamp0ZHE0azF6UTdVampLVkpXVWdzVlUvemZpS2RUT3ZkOXNtVHB0SUtBZTU3TWNXMlZFb1NlT1dKSXZPQmYwTWhuaU9yb24zMCtTMzZLZzdDRVRjdm00UVhheE13RmkvUjZ4bFJNQ2JaeXl4VjZCVVdMQ2ZLaUY3UjdOL2NGb08wZm8xYjA1WnRNazRnV0xlckcrY1V4M3FGK2ZqcHVFdVVUOXZxNnZQcWtSTVBXNk5ZR2kwOUFyUnNnY1pjOG9saVlYTmVGWkNGQzM0Q2NSU0w4UFluRlpDcHEzV1dwSVRKWG5uaVFQK1VMYzhpZGVyRzdKVE1yV0tPSlZXUzV3REhSbGk0VlVTakp0bUh5UHdIUjJSbXZBQndlcG52dHZRemFmQzZ0OFF5VlA3RzEzZ0ZaWHAxT2VpVGpPYXk2c3Ziay9SSSt2RkxZWHpkZzA3SmVBejhEU2xJdERva05OYVRad3pVZEhtZlR2S0VOdDRTR3VoNll6OXgrbDVVekNxSjVncWtTWkxoSWtkQ1EyVUlVMVdaSm9YaThyNWtkeVlSZWJCVThDK3FtbTQ3Z3laSVhvZjhRY1NZT3NUY3VaTlVvcGRjQ1MzRk9TNkJITEp5QmJ4NVpiSmtwKzlFRkNoMUJtdHk5YmVWOEpoNXFYUVdUVWlWZXdNWDJwdFVRMTlNMzRKNFJtRWNvRXR5R3UzZWZaWGR2V3pKcE5sZEl0eGxHRmFLNk5tVGhkdW00eW1TU1F0UFREWFVsOHVQTW5iSDFVdjdvMkNnVVpZd0gvM1ZnWDlUYVh4VVJjUWNEWGprK1hrYldDVm5ZMDltNG1pU09DMHl0enJIcDBWTzB1YXVReG9GMXJnUmZKZEtiR0tBdXUwZ0doMThkWmpXeHNTV0pVNFpOdW5qQ0tKUytUSzZtLzFBSnFURmlDcTB4MFJLSDNKL0tLdEZ1emVXS1Q2cm1YYzRocE95U05rVUVGTGdBNTRXWTVsek50eWs3S3lQV1dBSTZDb1ZXWmNrZDFnOCtSMkZGVjhhdTAzMGUrQ3JVUSsreE5PMHZtNzZ2b2JOUHkzejNrNitpSE1Cc1J4TjJ2ZFMrQVBwRnpYQVArK01KQVJtdjV1SkpDektpb3QrbzNIRmxpOTg1YWRlbkltcTlHbFZlL2JORkVBU2xBcmxwamRueHVtNnlMSTFaZXRndVhDbXp5YUozV1VEc3FLbzF2aG9Ed2YwNDBQdlJIQU93bkNHdVd0eDNNRlBLNnV4am85WU1zOXhnamlCNFI4bXpNN3lPVzZldFE0bW12bjNrNFJKb3JFWlpEZjdGUFZVbkhJNC82RXBCNnRRcVVzanRNcktzaXBhd3JKN0VNL1hZa1ZSMEtwMHA3cFA4RHJVYmk3VVBwdkFoRzNnSTFNdFc1UkxIMXFNWlB5ZE9GUzloY1QxZURZcisvZU1HZy9qcnhEc2twVEF2R3pKYjE4R0thUkt1VWRTbHd0T2NNMFFCTk5lNWI4Qm92alRpdklYOWJvZVhsZHVSVXd1YUtTczFTeDdTRDdhMDlIQVlQUTI0bCt2OGEyeVVjUTFtSEhQRlB0bFJ5OHBOc1F3WGUvS01reFhlZElDbGhsNmcwU2hLMEFGRjRuT3RUS3oxUHZpNUZxbXFIRjZjQ3luU1Nxc0U5Vlkvc2pCcS9hSVhSZUVwUk5XT1RTa0JOMERuTTB3OUxIcUVnb1RybTF6Nmx1WjMyL3VCS1F0eUEyb1M1SVl1a0tkOGhENTFzUmpmOGlvV3F1dm8xTk5KS0hlN1FLOENOL1c5Skcwa1phQ0w4UjlsK2JvOXNUMUhqbEh3N0xoWWtxRlh4ek9vYUtvTHFlQzAzQUszbWNiMjdRZFp1a0ovSmxJWDdEY2NFVHdkT1lXY1RXTzlNZEN5VHFONjdmWVJEellGZm01dUczRkQxVVN1dHhRRFdvRmUxcis3QzV3anVSY1kram1KZnN6RlJUYklJckNzL0FpMlJwbGdPMEtFV0JiQjdiMUh6YjVHekJtaFBkRjZhWDhuODRSaVh0cGh6TUtKajlNaThyeEJBWldhbEtCQTBQRzZ1a2dWdk5pSlZUdlRpSmx3bndaYy93SVNrWDMyTjIxRng0amRVVTBnR3E1ai9NZmtPS0ttMklTQWZVS1UxNjBYU1JldUNIdDdpOVdSU2lLMmUrNEVWN1NEZW0vQ1Q1NWpqWDdMN3hwYXpNclNac2R0OTlNNXkwcWduM2xyNXZiQ1ZZQUFqdHVOdEl6VmpTc256Vk5VdUZEaG5rOWpEUndvOVp6YWJKTWgrWW1BcW9za09HbFFEODZ6WW1hOHk0RWNBRExmQU9yU1lYbTF0Yk1TMzhlcnFkUEVtNE83RVk2UkFuazRkbjVTR3YvUGNhZHlXZS9LVEtnODNiTGc1NGt1MitKQm04dzEwaDZMbEhzWjBEL2NicVJnUnJlSzVSMGRIT0JKQmdKZ240bUZXNFVmS0wzNWVaTEpDZlkvcXJEUW5LcHRBUHdSaVIxMlhSUmRWd3NETTcxV2IvVkdJUk5wcFVYQ1RtWXhyaDRlMi8rdGx2US9ZcER1MXFSdzgycnpZOWIyaFVPeEc0RDVTejM1cTd6MC85QlgxNGlsZFAyWEpDRmNrNTBVM0tJeHZ0RkhERVk5TERDRjh5VVFGUmNZWVNDeng2QndzbkRVTnZJVWVlTHNtWjROYVNYUzVaL3FFRmh4bFA4YU1hZ2ROeTZpRlp2VVVUREd3T2xyQTdncFVwVjdqOHNGZGtFd2NqQ1V3ZTJLLzFWbExQYkRuelJsem9LQUZQK1dsMUptSERkWjJoY1ZNZkJ3ODcwMnpyWGFpOUIrSXFvallnS2JaYXBFN2ZIVFhicjI2S1g4RmtjeENtbCtLMjVRRnVodXozZkV2NkxtaFV5QVhLVVEzaENyWVFtOGQzSkEwbjEvRTdCUk9obkVEK2RLaEhEc0xNOUh6VWF2SWUyeWpDOXd4UUZ1SGdOd3d4VVEzZHFrU2IyTEZiRDQ1OS9Ya1lFM2xZTmo3U0ozeDVOZTlaR2U3UVVpeHYwdHVWcUsvTkx4Y25PS0RBaXgxdFJhZG4xVkdEbitMNEJsdnFGYlQxcU5ZSXVqWHA5WlUyVkRQTTdONnNBaHUxSGpiRFhkUG1xbHJ1SnlITlJRT2tjdExlbGd5dld3eHM0MkdYb3A0SFVoYWQ1emVuNFNGYUVuSUF1YjIyaEppOVdDV0JwY3FHR2hBMktWdnZSQVgrMXlNVTNwTHI2cDFGVlJrYk51aTBqb1krN0NTVGlwWnIwNjFTdFRKUHZLNVB3SVZTakRnUHJzK05iM1RBZ0I3WnBJRWZ1WklOZDBvdEMwTXFVQ1kzL0xZUXc3dG1wS3VBTHhKbVFEbmFvSUpKcW5RbXBWV29vNXJZN29mZWFUek9wZG5XRjgxb1c0QW9Xd1VrTjZGZEM1b1ZQd0R0QlExSncwVjBjRjdPWGJEZlJoUWFRUk1WOFo0L3VtT3RTL29GZVIyZXFwZzk4MmE4RnUxOURqQTY2S2R0NVNOR0lXcnRHUUczaGt6eXRnQUJaS3pXYTRkQ0FtejZJNTM1aUFmOCtEYTlxckJwV3FRVHpwdndsQTR0YWg1SGdvQUFnbm56S040S2Jlb09YTjA3RENmRjhiWnhqVUh3blZYL1h1V01nWGZTU3cyaFhNNjNDODRxNThqb0d4UWZyYk03Y1VmWEFwK2plQkptOXFNWW9pYUZpY2FRSXZweHFEZWw3aWQ5QVVGOEhONDhZYjdCdmtsOHBuVVpHLzNlMUZ6RDVFdFdBWDBSYXpscnNwRXBFZURFUHVqNWovNmtWS1JESlM1UzZDOGlhaUVta1VCbmFiM2MxSjRhUmJQZ2Fnakxac1lNRnh2UXhWTzlmRkprVlF1aE8vNHZla2hVK0toUWZxQndXNVJTUlRMYm82ZjV1MHJZTXdyZEhldjQzakZRWVMraXhoVzJxSmVMcThkWThoTE90Y3RCZ2hJaXdTVCtCWWRuNzZuRGhjaWhERHUzOEpQanlRdDlnZGhJN292eVE5OWQ1TFZRY1IxY0s2OXNpU08yS1JNTXNHd21IOWRUN2JSdE84MzBleFBzVUxUdmU5ZjBwbHpuVXpwaFAyZFdNc2x6enBvWWpSUTVmbGVpRWw1NGdScFZPZHBaMmpxYU1hY253bDdFd0RXQlJmTWhKVHZsZ2dPbm4vNmlsL01ObkNiQXE2MXZ6VXNpUkN3eVRLRUlRdnNCRVJSODFGREVXVlREN2o5dEIwY1pqSGJFcTRvVDZXaVk4NFV4emdWMjBQY083OFp0WjIzUE9qN0ZLclVFNTAxR241eXBJOWo2N3ZaZ1UzRjBkWTczMmZtbG5VU00xdGQ5SDNucElnWktEV210MlF3b3E0ak15c2g0eEJ5NFl3MThRTXRaOXBqd2d2Y1ExR3A5MDBoenkxNlQwZzd5ZGZ1MjVHaGpOWjIvR3ZtNGx4R1ZzTFJleitHU01kank1TytCY083OUFuMDZwbGtFUzhmZGlTaExzVGExV25BdXRDTWxURWU5cEVPbUtWdWpLOUE3dlJZQjlaYzVPdkdPa2EvMEtQYWVoTnNweHN4Q2VkbHR4NDlnbEoxamowRzVOUzg4Wkt0MFNBLzlaenAzQkpDYmdWeXRrSDNGVHNhSnF2aHdpNzJ2dE52anZoZDNEalduc2FCNWVEUlk2eDhoOXBPdk4vMm43b0x6NkRyaElqczZLRElGVmxFbkFFVDl6RGxPWnV1a1VhSjVaaEdUY25nRTNGNHBZUG1Dcit4NFIrWkdYMFR6dXRmUHluVEJTUGxLazBWWkR4d1VVcDFTRVg0cUV6aEVuNnlmQlJ6RlNLNGNXRW0vSWQxRVlwWmVSaCtseDdnazdqNlpUeGh4bUZxZURQZGtxVklTMHgzL2pzQlV6NjBlMlR1aURwako1RDVqRWVJQzRrNmd1T1publUxb0RNRzBqTjBOcDExUDdJblE4MXRwNG42UXpGcTBrU25pSU82Qjg3ZG1tdW93YzA5emJBVkVidzNLeGlJblZpeE9zNlM5Z0tPUlF6aVRJaVp0T0hTR1Nwc2ZFSVA1WkhSVGQzQmdIdDRZOHJBNG8wSVE5elI2VGEvVDlCMExYc3ZBMEQwRmlKNC95ZXlHMlk0WXpsY2R2ZXppTXNEMTZjWUl2aisxYmd1Tmd5TDA2a1FKTmczcTVDTHFEZSt2a1krazJudVpKQmtYV3ZramlVN1gxWDhneU5ibXJRVjYvcUhUY1dJbXBrR01wSlE2b1cxUT0=";

        String ret = restTemplate.getForObject(TEST_URL + "/orders/createOrder?orderInfo={json}",
                String.class, parma);


        //log.info(ret);

        //私钥文件路径
      //  String privateKeyFilePath = "D:\\Work\\.sip\\bestdo\\private.key";


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


       // privateKey = IOHelper.readFileToSting(privateKeyFilePath);

        //log.info(privateKey);

        ret = new String(Base64.getDecoder().decode(ret));


        log.info(RSAHelper.oldDecipher(ret, privateKey));

    }


    @Test
    public void test2() throws Exception {

        String a = "-----BEGIN PUBLIC KEY-----\n" +
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCsMLcaCMgOri4H8YaJ69KznOxK\n" +
                "bR2/8IJmrIjo8OAgvEqfMEfJrme+y7Ppt3VWVsNR85/BqTMsScHrA+2qThf6KxLo\n" +
                "B5XoP+f0j680bobBNLe4V9BG75cXuAoU9LslE2v/ompcX0Ib05ckoNRHZg4QDhH6\n" +
                "U4yfwtnHUBc5xK6cYwIDAQAB\n" +
                "-----END PUBLIC KEY-----";

        PublicKey publicKey = RSAHelper.getPemPublicKey(a);

        a="{\"bookDay\":\"2018-04-13\",\"bookPhone\":\"13564569994\",\"cupdOrderNo\":\"180409GD054517\",\"items\":[{\"endHour\":\"22\",\"playTime\":\"07:00\",\"startHour\":\"07\"}],\"merItemId\":\"10201021000060\",\"merid\":\"1020102\",\"note\":\"\",\"rightProduct\":\"300\",\"sportType\":\"108\",\"venueNo\":\"1080003412001\"}";

        a= RSAHelper.encipher(a,publicKey,8);

        a= Base64.getEncoder().encodeToString(a.getBytes());
//AdoC8JxtyEM4BXvNwuoBHad/HAm/OcF3cJ2s1loyI+sLrRIDnliwjqViRiVHtCIyc+6RjVx5nsdp
//zTAYn40jjlcP9qmm58fH8MpDCNKltGgf43gdFvJqiHgHPaoch3jFU1y0aYYbW0fIvwR1ht2XBtL6
//XdFosyTu36zBScjMVfBpq9o4Le563v8mMGIxW6ULLarO2Fc0yzSQ1zd+NbZ7jfHLO4WTJ3qI+pdE
//izoJyrxUC/WdVrAFmnjQg2/LMmyX4uQTBYQL5u4Dk4H7Nw0585CVJbqWCK82lP+ZqP8rQ52kiCK/
//7sxPgm1eUnH2KX2WLt+RvDqyDlVZR3o8CYEjwQaL56GHZPD2f+jl34rT71vRs6pXPNYA3L1kV+5p
//qkZiUekw1El0YdeFCKDpc6rl9cTSSm5Ibh3ggGAFs6p2jTxyzcJnqwCbauxsjeuHMDz/uGgr8TGy
//iss54lY30/dxjqElJFMH3/j1mTdIjiMqB0E1TUNG5baCteGhqZUNtkLZ
        log.info(a);
//QWRvQzhKeHR5RU00Qlh2Tnd1b0JIYWQvSEFtL09jRjNjSjJzMWxveUkrc0xyUklEbmxpd2pxVmlSaVZIdENJeWMrNlJqVng1bnNkcA0KelRBWW40MGpqbGNQOXFtbTU4Zkg4TXBEQ05LbHRHZ2Y0M2dkRnZKcWlIZ0hQYW9jaDNqRlUxeTBhWVliVzBmSXZ3UjFodDJYQnRMNg0KWGRGb3N5VHUzNnpCU2NqTVZmQnBxOW80TGU1NjN2OG1NR0l4VzZVTExhck8yRmMweXpTUTF6ZCtOYlo3amZITE80V1RKM3FJK3BkRQ0KaXpvSnlyeFVDL1dkVnJBRm1ualFnMi9MTW15WDR1UVRCWVFMNXU0RGs0SDdOdzA1ODVDVkpicVdDSzgybFArWnFQOHJRNTJraUNLLw0KN3N4UGdtMWVVbkgyS1gyV0x0K1J2RHF5RGxWWlIzbzhDWUVqd1FhTDU2R0haUEQyZitqbDM0clQ3MXZSczZwWFBOWUEzTDFrVis1cA0KcWtaaVVla3cxRWwwWWRlRkNLRHBjNnJsOWNUU1NtNUliaDNnZ0dBRnM2cDJqVHh5emNKbnF3Q2JhdXhzamV1SE1Eei91R2dyOFRHeQ0KaXNzNTRsWTMwL2R4anFFbEpGTUgzL2oxbVRkSWppTXFCMEUxVFVORzViYUN0ZUdocVpVTnRrTFo=
    }


    @Test
    public void test() throws IOException {
         String ret="RFk2NFhmNTlMZFQ0eUpYYU5rQWhWQ2lBcWtFYzZDbkhiUFZFdHU1ZlZHY1FZK0pNTzduaklYVFE0UTBuc01mQk9BR0tNMkI4UEJpbApiUldXcWRsZ0g0QUgwMFNWdldhR2RqbGpXd0d0REJHYyt6YVI5L2l3K2c4ZW0xOVNzSWZsZ0R4b3VhNFErZXdkZ0FqK3NkMndPb3JRCm14RDZqV25mWGJwOFhhOWZuRVE9";

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

        ret = new String(Base64.getDecoder().decode(ret));

        log.info(ret);

        ret = RSAHelper.decipher(ret, privateKey,245);
        log.info(ret);

    }

}
