package com.cupdata.sip.bestdo.vo.response;

import lombok.Data;

/**
 * @author junliang
 * @date 2018/04/16
 */
@Data
public class SaidianOrderRes extends BestaResVO {

    private String type;

    private String code;

    private String oid;

    private String cupdOrderNo;

    private String resCode;

    private String resInfo;
}
