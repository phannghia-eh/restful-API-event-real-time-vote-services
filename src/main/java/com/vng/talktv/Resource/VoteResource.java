package com.vng.talktv.Resource;

import com.vng.talktv.Services.VoteService;
import org.json.simple.JSONObject;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/Vote")
public class VoteResource extends Application {
    @EJB
    VoteService voteService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response startVote(@QueryParam("userId") String userId, @QueryParam("idolId") String idolId, @QueryParam("stars") Integer stars) {
        Integer retCode = 0;

        JSONObject respObj = new JSONObject();
        retCode = voteService.doVote(userId, idolId, stars);

        respObj.put("retcode", retCode);
        return Response.ok(respObj).build();
    }

}
