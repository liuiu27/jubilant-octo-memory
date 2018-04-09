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

    @Override
    public void customize(CacheManager cacheManager) {
        cacheManager.createCache("ehcache", new MutableConfiguration<>()
                .setExpiryPolicyFactory(TouchedExpiryPolicy.factoryOf(new Duration(TimeUnit.MINUTES, 10)))
                .setStoreByValue(false)
                .setManagementEnabled(true)
                .setStatisticsEnabled(true));
    }
}
