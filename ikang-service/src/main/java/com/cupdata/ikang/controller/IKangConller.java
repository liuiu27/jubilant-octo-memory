package com.cupdata.ikang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cupdata.ikang.feign.ConfigFeignClient;
import com.cupdata.ikang.feign.OrderFeignClient;
import com.cupdata.ikang.feign.ProductFeignClient;

import lombok.extern.slf4j.Slf4j;

/**
* @author 作者: liwei
* @createDate 创建时间：2018年4月2日 下午2:08:36
*/
@Slf4j
@RestController
public class IKangConller {

	@Autowired 
	private OrderFeignClient orderFeignClient;
	
	@Autowired 
	private ProductFeignClient productFeignClient;
	
	@Autowired
	private ConfigFeignClient ConfigFeignClient;
	
	
}
