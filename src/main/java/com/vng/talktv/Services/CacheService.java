package com.vng.talktv.Services;

import com.vng.talktv.Cache.CacheProviderContainer;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class CacheService extends GenericCacheService<String, String> {

    @EJB
    CacheProviderContainer cacheProviderContainer;

    public CacheService() {
        super();
    }

    @PostConstruct
    public void initCache() {
        setCache(cacheProviderContainer.<String, String>getCache(String.class));
    }

}
