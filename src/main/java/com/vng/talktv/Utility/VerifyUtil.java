package com.vng.talktv.Utility;

public class VerifyUtil {
    public static boolean verifyStars(Integer stars) {
        return (stars <= 5 && stars > 0);
    }

    public static boolean verifyValidVoteByJoinRoomTime(long userJoinRoomTime, long watingTime) {
        if(userJoinRoomTime == -1)
            return false;
        else {
            long currentTimeInSecond = System.currentTimeMillis() / 1000;
            return ((currentTimeInSecond - userJoinRoomTime) > watingTime);
        }
    }
}
