package com.example.broaf;

import java.util.Calendar;

public class Post {
    private int PID;
    private int writerUID;
    private String writerName;
    private String content;
    private Calendar writeTime;
    private boolean isHide;

    Post(int wUID, String wName){
        this.writerName = wName;
        this.writerUID = wUID;
        this.PID = -1;
        this.content = "";
        this.writeTime = Calendar.getInstance();
        this.isHide = false;
    }
}
