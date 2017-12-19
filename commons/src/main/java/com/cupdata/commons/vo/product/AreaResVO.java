package com.cupdata.commons.vo.product;


import lombok.Data;

@Data
public class AreaResVO {

    /**
     * 机场编号
     * */
    private int airportId;
    /**
     * 机场名称
     * */
    private String airportName;
    /**
     * 三字简码（高铁不存在三字简码）
     * */
    private String code;
}
