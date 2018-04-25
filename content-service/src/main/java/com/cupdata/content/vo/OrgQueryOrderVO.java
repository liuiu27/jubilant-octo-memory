package com.cupdata.content.vo;

import com.cupdata.sip.common.lang.utils.CommonUtils;
import com.cupdata.sip.common.lang.utils.DateTimeUtil;
import lombok.Data;

/**
 * @author junliang
 * @date 2018/04/25
 */
@Data
public class OrgQueryOrderVO {

    String sipOrderNo;

    String tranType;

    String timestamp = DateTimeUtil.getFormatDate(DateTimeUtil.getCurrentTime(), "yyyyMMddHHmmssSSS") + CommonUtils.getCharAndNum(8);


    public OrgQueryOrderVO(String sipOrderNo,String tranType){
        this.sipOrderNo=sipOrderNo;
        this.tranType=tranType;
    }

}
