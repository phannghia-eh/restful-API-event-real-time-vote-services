package com.vng.talktv.Worker;

import com.vng.talktv.BOM.Vote;
import com.vng.talktv.Services.VoteEntityService;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.LinkedList;

@Stateless
public class WorkerBean {

    private static LinkedList<Vote> queueInstance;
    @EJB
    VoteEntityService voteEntityService;

    @PostConstruct
    private void init() {
        if (queueInstance == null)
            queueInstance = new LinkedList<>();
    }

    private LinkedList<Vote> getQueueInstance() {
        return queueInstance;
    }

    public void addItem(Vote item) {
        queueInstance.add(item);
    }

    @Asynchronous
    public void doPersist() {
        while (!queueInstance.isEmpty()) {
            voteEntityService.save(voteEntityService.toEntity(queueInstance.removeFirst()));
        }
    }
}
