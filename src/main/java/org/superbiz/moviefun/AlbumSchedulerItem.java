package org.superbiz.moviefun;

import java.sql.Timestamp;

public class AlbumSchedulerItem {
    private Timestamp startTime;

    public Timestamp getStartTime(){
        return startTime;
    }

    public void setStartTime(Timestamp startTime){
        this.startTime = startTime;
    }

    public AlbumSchedulerItem(Timestamp startTime){
        this.startTime = startTime;
    }

}
