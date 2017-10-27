package com.vng.talktv.Resource;

import com.vng.talktv.Services.VoteService;
import com.vng.talktv.Utility.JsonUtil;
import org.json.simple.JSONObject;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/GetIdolInfo")
public class GetIdolInfoResource {
    @EJB
    VoteService voteService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getIdolInfor(@QueryParam("userId") String userId, @QueryParam("idolId") String idol_id) {
        JSONObject respObj = new JSONObject();
        JSONObject resultObj = new JSONObject();
        JSONObject userInfoObj = JsonUtil.JSONObjGenerator(voteService.getUserVoteForCurrIdol(userId, idol_id));
        JSONObject userObj = new JSONObject();

        JSONObject idolInfoObj = JsonUtil.JSONObjGenerator(voteService.getIdolInfo(userId, idol_id));
        JSONObject idolObj = new JSONObject();

        Integer retCode = 0;

        userObj.put("user", userInfoObj);
        resultObj.putAll(userObj);
        idolObj.put("idol", idolInfoObj);
        resultObj.putAll(idolObj);

        respObj.put("result", resultObj);
        respObj.put("retcode", retCode);

        return Response.ok(respObj).build();
    }
}
