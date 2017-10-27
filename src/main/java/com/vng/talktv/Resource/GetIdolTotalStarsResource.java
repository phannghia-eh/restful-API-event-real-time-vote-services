package com.vng.talktv.Resource;

import com.vng.talktv.Services.VoteService;
import org.json.simple.JSONObject;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/GetIdolTotalStars")
public class GetIdolTotalStarsResource {
    @EJB
    VoteService voteService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getIdolTotalStars(@QueryParam("idolId") String idolId) {
        Integer retCode = 0;
        JSONObject resultObj = new JSONObject();
        JSONObject starsObj = new JSONObject();
        JSONObject respObj = new JSONObject();

        Object idolTotalStars = voteService.getIdolTotalStar(idolId);
        if (idolTotalStars == null)
            retCode = 2;

        starsObj.put("stars", idolTotalStars);
        resultObj.put("result", starsObj);

        respObj.put("retcode", retCode);
        respObj.put("result", resultObj);
        return Response.ok(respObj).build();
    }
}
