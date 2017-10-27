package com.vng.talktv.Services;

import org.json.simple.JSONObject;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class VoteFacade {

    @EJB
    RedisService redisService;

    @EJB
    CacheService cacheService;

    public JSONObject getCountDownObject() {
        JSONObject countdownObj = new JSONObject();
        countdownObj.put("waitingTime", cacheService.get("waitingTime"));
        countdownObj.put("votingTime", cacheService.get("votingTime"));
        return countdownObj;
    }

    public String getWaitingTime(){
        return cacheService.get("waitingTime");
    }


    public void initDataToCache() {
        this.cacheService.put("waitingTime", redisService.GET("waitingTime"));
        this.cacheService.put("votingTime", redisService.GET("votingTime"));
    }
}
