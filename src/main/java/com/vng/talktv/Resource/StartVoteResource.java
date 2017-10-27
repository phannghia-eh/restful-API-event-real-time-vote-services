package com.vng.talktv.Resource;

import com.vng.talktv.Services.VoteFacade;
import com.vng.talktv.Services.VoteService;
import org.json.simple.JSONObject;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/StartVote")
public class StartVoteResource {
    @EJB
    VoteService voteService;

    @EJB
    VoteFacade voteFacade;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response startVote(@QueryParam("userId") String userId, @QueryParam("idolId") String idolId) {

        Integer retCode = 0;
        JSONObject respObj = new JSONObject();
        JSONObject resultObj = voteFacade.getCountDownObject();

        resultObj.put("stars", voteService.getIdolTotalStar(idolId).toString());

        voteService.setUserJoinRoomTimeAndSetExpireTime(userId);

        respObj.put("result", resultObj);
        respObj.put("retcode", retCode);

        return Response.ok(respObj).build();
    }
}
