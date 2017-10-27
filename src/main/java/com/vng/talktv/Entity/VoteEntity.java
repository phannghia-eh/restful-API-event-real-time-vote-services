package com.vng.talktv.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "voteinfo")
@NamedQueries({
        @NamedQuery(name = VoteEntity.FIND_ALL, query = "SELECT ve FROM VoteEntity ve"),
        @NamedQuery(name = VoteEntity.FIND_BY_IDOL_ID, query = "SELECT ve FROM VoteEntity ve WHERE ve.idolId = :idolId"),
        @NamedQuery(name = VoteEntity.FIND_BY_USER_ID, query = "SELECT ve FROM VoteEntity ve WHERE ve.userId = :userId")
})
public class VoteEntity implements Serializable {

    private static final String PREFIX = "com.vng.talktv.Entity.VoteEntity";

    public static final String FIND_ALL = PREFIX + ".findAll";
    public static final String FIND_BY_IDOL_ID = PREFIX + ".findByIdolId";
    public static final String FIND_BY_USER_ID = PREFIX + ".findByUserId";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "idolId", nullable = false)
    private String idolId;

    @Column(name = "stars", nullable = false)
    private Integer stars;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    public VoteEntity() {
        this.time = LocalDateTime.now();
    }

    public VoteEntity(String userId, String idolId, Integer stars, LocalDateTime time) {
        this.userId = userId;
        this.idolId = idolId;
        this.stars = stars;
        this.time = time;
    }

    public VoteEntity(String userId, String idolId, Integer stars) {
        this.userId = userId;
        this.idolId = idolId;
        this.stars = stars;
        this.time = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdolId() {
        return idolId;
    }

    public void setIdolId(String idolId) {
        this.idolId = idolId;
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

        VoteEntity that = (VoteEntity) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (idolId != null ? !idolId.equals(that.idolId) : that.idolId != null) return false;
        if (stars != null ? !stars.equals(that.stars) : that.stars != null) return false;
        return time != null ? time.equals(that.time) : that.time == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (idolId != null ? idolId.hashCode() : 0);
        result = 31 * result + (stars != null ? stars.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VoteEntity{" +
                "userId='" + userId + '\'' +
                ", idolId='" + idolId + '\'' +
                ", stars=" + stars +
                ", time=" + time +
                '}';
    }
}
