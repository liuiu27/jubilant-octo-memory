package com.cupdata.sip.common.cache;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.stereotype.Component;

import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;
import java.util.concurrent.TimeUnit;
/**
 * @author Tony
 * @date 2018/04/09
 */
@Component
public class CachingSetup implements JCacheManagerCustomizer {

    private final static String  DEFAULT_CACHE ="ehcache";


    @Override
    public void customize(CacheManager cacheManager) {
        //配置默认缓存
        cacheManager.createCache(DEFAULT_CACHE, new MutableConfiguration<>()
                .setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.MINUTES, 10)))
                .setStoreByValue(false)
                .setManagementEnabled(true)
                .setStatisticsEnabled(true));

        //TODO 2018/4/16  可根据需要 配置多个缓存 对象。
    }
}
