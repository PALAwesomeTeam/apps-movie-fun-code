package org.superbiz.moviefun;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public interface SchedulerRepository {
    AlbumSchedulerItem create(AlbumSchedulerItem schedulerItem);
    AlbumSchedulerItem find(Timestamp startTime);
    AlbumSchedulerItem findLatest();
    List<AlbumSchedulerItem> list();
    AlbumSchedulerItem update(Timestamp startTime, AlbumSchedulerItem schedulerItem);
    void delete(AlbumSchedulerItem schedulerItem);

}
