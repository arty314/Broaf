package com.example.broaf;

import java.util.Calendar;

public class NormalPost {
    private String PID;
    private String writerUID;
    private String writerName;
    private String content;
    private Calendar writeTime;
    private boolean isHide;
    private int likeCount;
    private double[] pLocation;
    //private int? badge; // 이모티콘 구분자는 뭘로할지 몰라서 일단 주석처리
    private Calendar opentilldate;
    private String imgurl;
    private int openRange;

    NormalPost(String wUID, String wName){
        this.writerName = wName;
        this.writerUID = wUID;
        this.PID = "";
        this.content = "";
        this.writeTime = Calendar.getInstance();
        this.isHide = false;
        this.likeCount = 0;
        this.pLocation[0] = 0.0;
        this.pLocation[1] = 0.0;
        this.imgurl = "";
        this.openRange = 1;
        this.opentilldate = null;
    }
}
