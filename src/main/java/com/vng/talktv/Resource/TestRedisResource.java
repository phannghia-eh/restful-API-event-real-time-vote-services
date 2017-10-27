package com.vng.talktv.Resource;

import com.vng.talktv.Services.RedisService;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("testredis")
public class TestRedisResource {
    @EJB
    RedisService redisService;

    @GET
    @Path("get")
    @Produces({MediaType.APPLICATION_JSON})
    public Response testGet(@QueryParam("key") String key) {
        return Response.ok(redisService.GET(key)).build();
    }

    @GET
    @Path("hget")
    @Produces({MediaType.APPLICATION_JSON})
    public Response testGGet(@QueryParam("key") String key, @QueryParam("field") String field) {
        return Response.ok(redisService.HGET(key, field)).build();
    }
}
