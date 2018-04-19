package org.superbiz.moviefun.albums;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.superbiz.moviefun.AlbumSchedulerItem;
import org.superbiz.moviefun.JdbcSchedulerRepository;
import org.superbiz.moviefun.SchedulerRepository;

import java.sql.Time;
import java.sql.Timestamp;

@Configuration
@EnableAsync
@EnableScheduling
public class AlbumsUpdateScheduler {

    private static final long SECONDS = 1000;
    private static final long MINUTES = 60 * SECONDS;
    public static final int SCHEDULER_DELAY = 3;

    private final AlbumsUpdater albumsUpdater;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SchedulerRepository schedulerRepository;

    public AlbumsUpdateScheduler(AlbumsUpdater albumsUpdater, SchedulerRepository schedulerRepository) {
        this.albumsUpdater = albumsUpdater;
        this.schedulerRepository = schedulerRepository;
    }


    @Scheduled(initialDelay = 15 * SECONDS, fixedRate = 15 * SECONDS)
    public void run() {
        try {
            Timestamp nowTime = new Timestamp(System.currentTimeMillis());
            Timestamp threeMinsAgo = new Timestamp(nowTime.getTime() - (60 * SCHEDULER_DELAY * 1000));
            AlbumSchedulerItem lastScheduledRun = schedulerRepository.findLatest();

            if(lastScheduledRun.getStartTime() == null || lastScheduledRun.getStartTime().before(threeMinsAgo)){
                schedulerRepository.create(new AlbumSchedulerItem(nowTime));

                logger.debug("Starting albums update");
                albumsUpdater.update();

                logger.debug("Finished albums update");
            }
            else
                logger.debug("Waiting for next scheduled time");


        } catch (Throwable e) {
            logger.error("Error while updating albums", e);
        }
    }
}
