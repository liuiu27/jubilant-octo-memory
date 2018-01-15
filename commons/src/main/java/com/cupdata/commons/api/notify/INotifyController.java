package com.cupdata.commons.api.notify;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cupdata.commons.vo.notify.OrderNotifyWait;

/**
 * @Auth: liwei
 * @Description:服务通知请求接口
 * @Date: 16:22 2017/1/12
 */
@RequestMapping("/notify")
public interface INotifyController{
	    @PostMapping("/notifyToOrg")
	    public void notifyToOrg(@RequestBody OrderNotifyWait orderNotifyWait);
	    
}
