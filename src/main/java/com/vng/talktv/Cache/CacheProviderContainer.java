package com.vng.talktv.Cache;

import org.infinispan.commons.api.BasicCache;
import org.infinispan.commons.api.BasicCacheContainer;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.util.concurrent.IsolationLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton(name = "CacheProviderContainer")
@Startup
public class CacheProviderContainer {

    private static final Logger logger = LoggerFactory.getLogger(CacheProviderContainer.class);

    BasicCacheContainer manager;

    @PostConstruct
    public void initCacheContainer() {
        logger.info("********* initCacheContainer **********");
        if (manager == null) {
            GlobalConfiguration glob = new GlobalConfigurationBuilder()
                    .nonClusteredDefault() //Helper method that gets you a default constructed GlobalConfiguration, preconfigured for use in LOCAL mode
                    .globalJmxStatistics().allowDuplicateDomains(true)
                    .build(); //Builds  the GlobalConfiguration object
            Configuration loc = new ConfigurationBuilder()
                    .clustering().cacheMode(CacheMode.LOCAL) //Set Cache mode to LOCAL - Data is not replicated.
                    .locking().isolationLevel(IsolationLevel.REPEATABLE_READ) //Sets the isolation level of locking
                    .persistence().passivation(false)//.addSingleFileStore().purgeOnStartup(true) //Disable passivation and adds a SingleFileStore that is Purged on Startup
                    .build(); //Builds the Configuration object
            manager = new DefaultCacheManager(glob, loc, true);
            logger.info("********* initCacheContainer **********", manager);
        }
    }

    public <K, V> BasicCache<K, V> getCache(Class<V> clazz) {
        return this.manager.getCache(clazz.getName());
    }


    @PreDestroy
    public void cleanUp() {
        manager.stop();
        manager = null;
    }

}