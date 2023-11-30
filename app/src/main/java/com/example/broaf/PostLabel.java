package com.example.broaf;

import java.io.Serializable;

public class PostLabel implements Serializable {

    public int postID;
    public int icon_no;    //아이콘 번호
    public String attachImageURL;
    public String content;
    public Boolean isHide;
    public  double latitude;
    public  double longitude;
    public int openRange;
    public int likeCount;
    public String openToDateTime;
    public String writerName;
    public String writerUID;
    public String writtenDateTime;

    public PostLabel() {
        postID = 19000;
        icon_no = 1;
        attachImageURL=null;
        content = "Post 내용물입니당";
        isHide=false;
        latitude=35.831272;
        longitude=128.755840;
        likeCount=0;
        openRange = 1;
        writtenDateTime = "202311300042";
        openToDateTime = "2080123140042";
        writerName = "Nickname";
        writerUID = "10001";
    }

    public PostLabel(int postID, int icon_no, String attachImageURL, String content, Boolean isHide,
                     double latitude, double longitude, int openRange, int likeCount, String openToDateTime,
                     String writerName, String writerUID, String writtenDateTime){

        this.postID=postID; this.icon_no=icon_no; this.attachImageURL=attachImageURL; this.content=content; this.isHide=isHide;
        this.latitude=latitude; this.longitude=longitude; this.openRange=openRange; this.likeCount=likeCount; this.openToDateTime=openToDateTime;
        this.writerName=writerName; this.writerUID=writerUID; this.writtenDateTime=writtenDateTime;

    }
}
