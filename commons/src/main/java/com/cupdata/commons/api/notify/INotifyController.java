package com.cupdata.commons.api.notify;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auth: liwei
 * @Description:服务通知请求接口
 * @Date: 16:22 2017/1/12
 */
@RequestMapping("/notify")
public interface INotifyController{
	    @GetMapping("/notifyToOrg3Times/{orderNo}")
	    public void notifyToOrg3Times(@PathVariable("orderNo") String orderNo);
	    
}
