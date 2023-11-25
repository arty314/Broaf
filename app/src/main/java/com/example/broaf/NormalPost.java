package com.example.broaf;

import java.util.Calendar;

public class NormalPost extends Post{
    private int likeCount;
    //private int reportCount;
    private double[] pLocation;
    //private int? badge;
    private Calendar opentodate;
    private String imgurl;
    private int openRange;

    NormalPost(int wUID, String wName){
        super(wUID, wName);
        this.likeCount = 0;
        //this.reportCount = 0;
        this.openRange = 1; // 나중에 전체공개 1, 친구공개 2, 비공개 3 등등 으로
        pLocation = new double[2];
        this.opentodate = Calendar.getInstance();
        //this.badge = int?;
        this.imgurl = "";
    }

    public void setpLocation(double longitude, double latitude){
        this.pLocation[0] = longitude;
        this.pLocation[1] = latitude;
    }

}
