package com.cupdata.cache.controller;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cupdata.cache.ehCacheUtils.EhCacheManager;

import java.net.URL;
/**
* @author 作者: liwei
* @createDate 创建时间：2018年4月10日 上午9:47:14
*/
@Slf4j
@Controller
@RequestMapping("/EhCacheTest")
public class EhCacheTest {
	
    @GetMapping("/test")
    public void test(){
    	EhCacheManager cacheManager = new EhCacheManager();
    	System.out.println("================================="+
    			cacheManager.getSysConfig("CUPD", "LOGIN_PRIVATE_KEY_FILE_PATH"));
    }

}