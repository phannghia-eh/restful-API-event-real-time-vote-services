package com.vng.talktv.BOM;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Vote implements Serializable {

    private static final long serialVersionUID = 7133463046732600080L;

    private Integer id;

    @NotNull
    private String idolId;

    @NotNull
    private String userId;

    @Min(0)
    @Max(5)
    private Integer stars;

    private LocalDateTime time;

    public Vote() {
    }

    public Vote(Integer id, String idolId, String userId, Integer stars, LocalDateTime time) {
        this.id = id;
        this.idolId = idolId;
        this.userId = userId;
        this.stars = stars;
        this.time = time;
    }

    public Vote(String idolId, String userId, Integer stars, LocalDateTime time) {
        this.idolId = idolId;
        this.userId = userId;
        this.stars = stars;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getIdolId() {
        return idolId;
    }

    public void setIdolId(String idolId) {
        this.idolId = idolId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vote vote = (Vote) o;

        if (idolId != null ? !idolId.equals(vote.idolId) : vote.idolId != null) return false;
        if (userId != null ? !userId.equals(vote.userId) : vote.userId != null) return false;
        if (stars != null ? !stars.equals(vote.stars) : vote.stars != null) return false;
        return time != null ? time.equals(vote.time) : vote.time == null;
    }

    @Override
    public int hashCode() {
        int result = idolId != null ? idolId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (stars != null ? stars.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "idolId='" + idolId + '\'' +
                ", userId='" + userId + '\'' +
                ", stars=" + stars +
                ", time=" + time +
                '}';
    }
}
