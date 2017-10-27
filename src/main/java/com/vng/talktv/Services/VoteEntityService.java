package com.vng.talktv.Services;

import com.vng.talktv.BOM.Vote;
import com.vng.talktv.Entity.VoteEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class VoteEntityService {
    @PersistenceContext(name = "eventvotePU")
    EntityManager em;

    public EntityManager getEm() {
        return this.em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void save(VoteEntity entity) {
        this.em.persist(entity);
    }

    public VoteEntity toEntity(Vote bom) {
        if (bom == null) {
            return null;
        }

        VoteEntity entity = new VoteEntity();

        entity.setIdolId(bom.getIdolId());
        entity.setUserId(bom.getUserId());
        entity.setStars(bom.getStars());
        entity.setTime(bom.getTime());

        return entity;
    }

    public Vote toBom(VoteEntity entity) {
        if (entity == null)
            return null;
        Vote bom = new Vote();
        bom.setId(entity.getId());
        bom.setIdolId(entity.getIdolId());
        bom.setUserId(entity.getUserId());
        bom.setStars(entity.getStars());
        bom.setTime(entity.getTime());

        return bom;
    }

    public VoteEntity findByUserId(String userId) {
        TypedQuery<VoteEntity> query = getEm().createNamedQuery(VoteEntity.FIND_BY_USER_ID, VoteEntity.class);
        query.setParameter("userId", userId);
        List<VoteEntity> resultList = query.getResultList();
        if (resultList.isEmpty())
            return null;
        else
            return resultList.get(0);
    }
}
