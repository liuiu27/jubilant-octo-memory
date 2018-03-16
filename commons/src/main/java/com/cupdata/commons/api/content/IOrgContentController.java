package com.cupdata.commons.api.content;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cupdata.commons.vo.BaseResponse;
import com.cupdata.commons.vo.content.ContentJumpReq;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年3月15日 下午3:43:02
*/
@RequestMapping("/orgContent")
public interface IOrgContentController {
	    @PostMapping("/contentJump")
	    public BaseResponse contentJump(String org,ContentJumpReq contentJumpReq, HttpServletRequest request, HttpServletResponse response);
	
}
