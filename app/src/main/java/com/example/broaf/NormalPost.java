package com.example.broaf;

import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NormalPost implements Serializable {
    private String PID;
    private String writerName;
    private String content;
    private Date writeTime;
    private String isHide;
    private int likeCount;
    private double pLongitude;
    private double pLatitude;
    private int icon;
    private Date openTillDate;
    private String imgurl;
    private int openRange;

    NormalPost(){ }
    NormalPost(CreatePostFragment.PostBody postBody){
        this.writerName = ""; //
        this.PID = "";
        this.content = postBody.getText();
        this.writeTime = postBody.getWriteTime();
        this.isHide = "false";
        this.likeCount = 0;
        this.pLongitude = 0.0; //
        this.pLatitude = 0.0; //
        this.imgurl = postBody.getImgurl();
        this.openRange = postBody.getOpenRange();
        this.openTillDate = postBody.getOpentilldate();
        this.icon = postBody.getIcon();
    }

    public String getPID(){return this.PID;}
    public void setPID(String pid){this.PID = pid;}
    public String getWriterName(){return this.writerName;}
    public void setWriterName(String name){this.writerName = name;}
    public String getContents(){return this.content;}
    public void setContents(String contents){this.content = contents;}
    public Date dateGetWriteTime(){return this.writeTime;} // Date형식으로 받으려면 이걸사용
    public String getWriteTime(){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmm");
        return fmt.format(this.writeTime);} // Firebase에 String으로 저장하려고 ex)"20231130170235" 2023년 11월 30일 17시 02분
    public void setWriteTime(String date){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmm"); // Firebase에서 가져올때도?
        try {
            this.writeTime = fmt.parse(date);
        }catch (Exception e){}
    }
    public String getIsHide(){return this.isHide;}
    public void setIsHide(String tf){this.isHide = tf;}
    public int intGetLikeCount(){return this.likeCount;} // int로 받으려면 이걸사용
    public String getLikeCount(){return String.valueOf(this.likeCount);} // Firebase용
    public void setLikeCount(int cnt){this.likeCount = cnt;}
    public double doubleGetpLongitude(){return this.pLongitude;} // double
    public String getpLongitude(){return String.valueOf(this.pLongitude);} // Firebase
    public void setpLongitude(double lon){this.pLongitude = lon;}
    public double doubleGetpLatitude(){return this.pLatitude;} // double
    public String getpLatitude(){return String.valueOf(this.pLatitude);} // Firebase
    public void setpLatitude(double lat){this.pLatitude = lat;}
    public int intGetIcon(){return this.icon;} // int
    public String getIcon(){return String.valueOf(this.icon);} // Firebase
    public void setIcon(int i){this.icon = i;}
    public String getOpentilldate(){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmm");
        return fmt.format(this.openTillDate);} // Firebase에 String으로 저장하려고 ex)"20231130170235" 2023년 11월 30일 17시 02분 35초
    //WriteTime은 set할필요가 없음ㅇ
    public void setOpentilldate(String date){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmm"); // Firebase에서 가져올때도?
        try {
            this.openTillDate = fmt.parse(date);
        }catch (Exception e){}
    }
    public String getImgurl(){return this.imgurl;}
    public void setImgurl(String url){this.imgurl = url;}
    public int intGetOpenRange(){return this.openRange;} // int
    public String getOpenRange(){return String.valueOf(this.openRange);} // Firebase
    public void setOpenRange(int op){this.openRange = op;}

}
