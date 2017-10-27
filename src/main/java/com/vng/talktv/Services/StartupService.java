package com.vng.talktv.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.TimeZone;

@Singleton
@Startup
@DependsOn({"CacheProviderContainer"})
public class StartupService {
    private static final Logger logger = LoggerFactory.getLogger(StartupService.class);

    @EJB
    VoteFacade voteFacade;

    @PostConstruct
    public void init() {
        java.util.TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        voteFacade.initDataToCache();
        logger.info("initialize cache successfully!!!");
    }
}
