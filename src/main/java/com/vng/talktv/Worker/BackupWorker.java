package com.vng.talktv.Worker;

import com.vng.talktv.BOM.Vote;
import com.vng.talktv.Services.VoteEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.LinkedList;

@Singleton
@Startup
public class BackupWorker extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackupWorker.class);
    private static Thread persistThread = null;
    private static LinkedList<Vote> dataQueue;
    @EJB
    VoteEntityService voteEntityService;

    public BackupWorker() {
    }

    @PostConstruct
    public void init() {
        dataQueue = new LinkedList<>();
        persistThread = new Thread(this);
        persistThread.start();
        LOGGER.debug("Start Persistent Thread");
        System.out.println("Start Persistent Thread");
    }

    @PreDestroy
    public void preDestroy() {
        try {
            persistThread.join();
        } catch (InterruptedException ex) {
            LOGGER.debug("Interupt while waiting for another Thread to shutdown", ex);
        }
    }

    public void resumeThread() {
        persistThread.resume();
    }

    public void addItemToQueue(Vote item) {
        LOGGER.debug("Add new Vote Info to data queue to persist to database");
        dataQueue.add(item);
        //resumeThread();
    }

    @Override
    public void run() {
        while (!dataQueue.isEmpty()) {
            Vote item = dataQueue.getFirst();
            voteEntityService.save(voteEntityService.toEntity(item));
        }
        //persistThread.suspend();
    }
}
