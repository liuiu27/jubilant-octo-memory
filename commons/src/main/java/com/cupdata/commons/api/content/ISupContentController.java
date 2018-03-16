package com.cupdata.commons.api.content;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.content.ContentLoginReq;
import com.cupdata.commons.vo.content.ContentQueryOrderReq;
import com.cupdata.commons.vo.content.ContentQueryOrderRes;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月15日 下午3:43:02
*/
@RequestMapping("/supContent")
public interface ISupContentController {
	    @PostMapping("/contentLogin")
	    public BaseResponse contentLogin(String sup,ContentLoginReq contentLoginReq, HttpServletRequest request, HttpServletResponse response);
	    
	    @PostMapping("/contentQueryOrder")
	    public BaseResponse<ContentQueryOrderRes> contentQueryOrder(String sup,ContentQueryOrderReq contentQueryOrderReq, HttpServletRequest request, HttpServletResponse response);
	    
}
