package com.vng.talktv.Resource;

import com.vng.talktv.Services.VoteService;
import com.vng.talktv.Utility.JsonUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import redis.clients.jedis.Tuple;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("GetTopIdolByMonth")
public class GetTopIdolByMonth {
    @EJB
    VoteService voteService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getTopIdol(@QueryParam("month") String month, @QueryParam("index") Integer index) {
        Integer retCode = 0;
        Boolean isLastPage = false;
        String leaderboardKey = "leaderboard:topIdolByMonth:" + month;

        JSONObject respObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject resultObj = new JSONObject();

        //check isLastPage
        isLastPage = voteService.isLastPage("leaderboard:topIdolByMonth:" + month, index);

        //get list top 10 user from leaderboard
        Set<Tuple> listTop10 = voteService.getTop10ItemInLeaderboard(leaderboardKey, index);
        jsonArray = JsonUtil.convertSetTupleToJsonArray(listTop10, index);

        resultObj.put("isLastPage", isLastPage.toString());
        resultObj.put("rankList", jsonArray);
        respObj.put("retcode", retCode);
        respObj.put("result", resultObj);


        return Response.ok(respObj).build();
    }
}
