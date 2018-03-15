package com.cupdata.content.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月15日 下午2:30:31
*/

public class baseRequestVo {
	  /**
     *时间戳
     */
    @NotBlank(message="")
    String timestamp;
}
