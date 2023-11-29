package com.example.broaf;

import java.util.Calendar;
import java.util.Date;

public class NormalPost {
    private String PID;
    private String writerUID;
    private String writerName;
    private String content;
    private Date writeTime;
    private boolean isHide;
    private int likeCount;
    private double pLongitude;
    private double pLatitude;
    //private int? badge; // 이모티콘 구분자는 뭘로할지 몰라서 일단 주석처리
    private Date opentilldate;
    private String imgurl;
    private int openRange;

    NormalPost(CreatePostFragment.PostBody postBody){
        this.writerName = ""; //
        this.writerUID = ""; //
        this.PID = "";
        this.content = postBody.getText();
        this.writeTime = postBody.getWriteTime();
        this.isHide = false;
        this.likeCount = 0;
        this.pLongitude = 0.0; //
        this.pLatitude = 0.0; //
        this.imgurl = postBody.getImgurl();
        this.openRange = postBody.getOpenRange();
        this.opentilldate = postBody.getOpentilldate();
    }

    public void setOpenRange(int i){this.openRange = i;}
}
