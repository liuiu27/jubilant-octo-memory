package com.cupdata.iyooc.vo;

import lombok.Data;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 13:58 2018/4/24
 */
@Data
public class BestPayingRes extends BestPayingReq{

    private Footer footer;
    private String body;
}
