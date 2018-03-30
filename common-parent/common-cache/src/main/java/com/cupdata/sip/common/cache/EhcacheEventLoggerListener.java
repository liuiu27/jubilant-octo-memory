package com.cupdata.sip.common.cache;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
/**
 * @author Tony
 * @date 2018/03/30
 */
@Slf4j
public class EhcacheEventLoggerListener implements CacheEventListener<Object,Object> {

    @Override
    public void onEvent(CacheEvent<? extends Object, ? extends Object> event) {

        log.info("Event: " + event.getType() + " Key: " + event.getKey() + " old value: " + event.getOldValue()
                + " new value: " + event.getNewValue());

    }

}