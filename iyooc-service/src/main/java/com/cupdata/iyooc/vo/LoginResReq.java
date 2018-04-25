package com.cupdata.iyooc.vo;

import lombok.Data;

/**
 * @Auther: DingCong
 * @Description:
 * @@Date: Created in 18:03 2018/4/24
 */
@Data
public class LoginResReq {

    private String phoneNo;				 // 手机号

    private String expiresIn;				// Token超时时长，单位（秒）
}
