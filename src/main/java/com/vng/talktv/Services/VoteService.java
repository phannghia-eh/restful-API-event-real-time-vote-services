package com.vng.talktv.Services;

import com.vng.talktv.BOM.Vote;
import com.vng.talktv.Utility.DateTimeUtil;
import com.vng.talktv.Utility.VerifyUtil;
import com.vng.talktv.Worker.BackupWorker;
import com.vng.talktv.Worker.WorkerBean;
import org.json.simple.JSONObject;
import redis.clients.jedis.Tuple;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Set;

@Stateless
public class VoteService {
    @EJB
    RedisService redisService;

    @EJB
    VoteFacade voteFacade;

    @EJB
    WorkerBean workerBean;

    BackupWorker backupWorker = new BackupWorker();

    public String setUserJoinRoomTimeAndSetExpireTime(String userId) {
        String msg;
        //SET user:user_id:join_room_time curr_time
        String userKey = "user:" + userId + ":joinRoomTime";
        long currentTime = System.currentTimeMillis() / 1000;
        msg = redisService.SET(userKey, String.valueOf(currentTime));

        if (msg.compareTo("OK") == 0) {
            //System.out.println("Set JOIN ROOM TIME for user:" + userId + " SUCCESS");
        } else
            return "2";

        //EXPIRE user:user_id:join_room_time countdown
        int votingTime = Integer.parseInt(redisService
                .GET("votingTime"));
        int waitingTime = Integer.parseInt(redisService
                .GET("waitingTime"));
        msg = redisService.EXPIRE(userKey, votingTime + waitingTime).toString();
        if (msg == null)
            return "2";

        return "1";
    }

    public Integer doVote(String userId, String idolId, Integer stars) {

        if(VerifyUtil.verifyValidVoteByJoinRoomTime(getUserJoinRoomTime(userId), getWaitingTime()))
        {
            if(VerifyUtil.verifyStars(stars)){
                incrIdolAll3Field(userId, idolId, stars);
                incrRankForCurrentIdolInTopIdolByMonthLeaderboard(userId, idolId, stars);
                incrRankForCurrentUserInTopUserVoteForCurrentIdolTopUserVoteLeaderboard(userId, idolId, stars);
                incrRankForCurrentIdolInTopIdolLeaderboard(idolId, stars);

                deleteCurrentUserJoinRoomTimeKey(userId);
            } else {
                return 1;
            }
        } else{
            return 1;
        }

//        //For testing the function without wating enough time for vote
//        incrIdolAll3Field(userId, idolId, stars);
//        incrRankForCurrentIdolInTopIdolByMonthLeaderboard(userId, idolId, stars);
//        incrRankForCurrentUserInTopUserVoteForCurrentIdolTopUserVoteLeaderboard(userId, idolId, stars);
//        incrRankForCurrentIdolInTopIdolLeaderboard(idolId, stars);
//        //Delete join room time for make sure to one cheat on this
//        deleteCurrentUserJoinRoomTimeKey(userId);

        //Add item to background service to persist backup data
        addDataToBackupQueue(new Vote(idolId, userId, stars, DateTimeUtil.getCurrentTime()));
        return 0;
    }

    private long getUserJoinRoomTime(String userId) {
        try{
            String result = redisService.GET("user:" + userId + ":joinRoomTime");
            return Long.parseLong(result);
        } catch (NumberFormatException e){
            return -1;
        }
    }

    public JSONObject getCountDownObject() {
        Integer votingTime = getVotingTime();
        Integer waitingTime = getWaitingTime();

        JSONObject countDownObject = new JSONObject();

        countDownObject.put("votingTime", votingTime);
        countDownObject.put("watingTime", waitingTime);

        return countDownObject;
    }

    public JSONObject getIdolInfo(String user_id, String idol_id) {
        String idolKey = "idol:" + idol_id;
        JSONObject idolInfoObj = new JSONObject();
        idolInfoObj.putAll(redisService.HGETALL(idolKey));
        idolInfoObj.put("rank", String.valueOf(getIdolRank(idol_id) + 1));
        return idolInfoObj;
    }

    public JSONObject getUserVoteForCurrIdol(String userId, String idolId) {
        String userKey = "user:" + userId;
        JSONObject userObj = new JSONObject();
        userObj.put("totalVotes", redisService.HGET(userKey, idolId));
        return userObj;
    }

    private void incrIdolAll3Field(String userId, String idolId, Integer stars) {
        String userKey = "user:" + userId;
        String idolKey = "idol:" + idolId;

        //Increase count for total_user_vote if this user didnt vote for current Idol before
        if (!redisService.HEXIST(userKey, idolId)) {
            redisService.HINCRBY(idolKey, "totalUserVote", 1);
        } else {
        }
        //Increase vote point between current user and current idol
        redisService.HINCRBY(userKey, idolId, stars);

        //Increase total vote for current idol
        redisService.HINCRBY(idolKey, "totalVote", 1);

        //Increase total star for current idol
        redisService.HINCRBY(idolKey, "totalStars", stars);
    }

    private void incrRankForCurrentIdolInTopIdolByMonthLeaderboard(String userId, String idolId, Integer stars) {
        String topIdolByMonthLeaderboard = "leaderboard:topIdolByMonth:" + DateTimeUtil.getCurrentMonthAndYear();
        //Increase rank for TOP IDOL BY MONTH LEADERBOARD
        redisService.ZINCRBY(topIdolByMonthLeaderboard, stars, idolId);
    }

    private void incrRankForCurrentUserInTopUserVoteForCurrentIdolTopUserVoteLeaderboard(String userId, String idolId, Integer stars) {
        String topUserVoteForCurrIdol = "leaderboard:userVote:" + idolId;
        //Increase rank for curr user in TOP VOTE LEADERBOARD for curr idol
        //Only increase for top 10100 user in this user_vote_leaderboard
        //Count the number of elements in TOP VOTE LEADERBOARD
        if (redisService.ZCOUNTALL(topUserVoteForCurrIdol) < 10100) {
            //Get the rank of curr user in the TOP VOTE LEADERBOARD
            long rank = redisService.ZREVRANK(topUserVoteForCurrIdol, userId);
            if (rank + 1 < 10100 || rank == -1) {
                redisService.ZINCRBY(topUserVoteForCurrIdol, stars, userId);
            }
        }
    }

    private void incrRankForCurrentIdolInTopIdolLeaderboard(String idolId, Integer stars) {
        String topIdol = "leaderboard:topIdol";
        //Increase rank for curr idol in TOP IDOL LEADERBOARD
        redisService.ZINCRBY(topIdol, stars, idolId);
    }

    private void deleteCurrentUserJoinRoomTimeKey(String userId) {
        String userKey = "user:" + userId + ":joinRoomTime";
        //Delete key USER_JOIN_ROOM_TIME
        redisService.DEL(userKey);
    }

    public String getIdolTotalStar(String idolId) {
        String idolKey = "idol:" + idolId;
        Object totalStars = redisService.HGET(idolKey, "totalStars");
        return (totalStars != null) ? (String) totalStars : "0";
    }

    private Integer getWaitingTime() {
        return Integer.parseInt(voteFacade.getWaitingTime());
    }

    private Integer getVotingTime() {
        return Integer.parseInt(redisService.GET("votingTime"));
    }

    private long getIdolRank(String idolId) {
        String leaderboardTopIdol = "leaderboard:topIdol";
        return redisService.ZREVRANK(leaderboardTopIdol, idolId);
    }

    public Set<Tuple> getTop10ItemInLeaderboard(String leaderboard, Integer index) {
        Set<Tuple> setTop10UserVote = redisService.ZREVRANGEWITHSCORES(leaderboard, index * 10 - 10, index * 10 - 1);
        return setTop10UserVote;
    }

    public boolean isLastPage(String leaderboard, Integer index) {
        return (((index + 1) * 10) > redisService.ZCOUNTALL(leaderboard));
    }

    private void addDataToBackupQueue(Vote item) {
//        backupWorker.addItemToQueue(item);
//        voteEntityService.save(voteEntityService.toEntity(item));
        workerBean.addItem(item);
        workerBean.doPersist();
    }
}
