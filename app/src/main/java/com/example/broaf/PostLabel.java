package com.example.broaf;

import com.kakao.vectormap.label.Label;

public class PostLabel {

    public int postID;
    public int icon_no;    //아이콘 번호
    public String attachImageURL;
    public String content;
    public Boolean isHide;
    public  double latitude;
    public  double longitude;
    int openRange;
    int likeCount;
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

    public PostLabel setPostLabel(int postID, int icon_no, String attachImageURL, String content, Boolean isHide,
                     double latitude, double longitude, int openRange, int likeCount, String openToDateTime,
                     String writerName, String writerUID, String writtenDateTime){
        this.postID=postID; this.icon_no=icon_no; this.attachImageURL=attachImageURL; this.content=content; this.isHide=isHide;
        this.latitude=latitude; this.longitude=longitude; this.openRange=openRange; this.likeCount=likeCount; this.openToDateTime=openToDateTime;
        this.writerName=writerName; this.writerUID=writerUID; this.writtenDateTime=writtenDateTime;
        return null;
    }
    public PostLabel[] MakePosts() {
        PostLabel[] postLabel = new PostLabel[5];

        postLabel[0].setPostLabel(10001,1, null,"Post 내용물입니당",false,
                35.831272, 128.755840, 0,1,"2080123140042",
                "닉네임","10001","202311300042");

        postLabel[1].setPostLabel(10002,8, null,"Post 내용물입니당2",false,
                35.832645, 128.757779, 0,1,"2080123140042",
                "닉네임2","10002","202311300042");

        postLabel[2].setPostLabel(10003,13, null,"Post 내용물입니당3",false,
                35.829907, 128.755500, 0,1,"2080123140042",
                "닉네임3","10003","202311300042");

        postLabel[3].setPostLabel(10004,4, null,"Post 내용물입니당4",false,
                35.832735, 128.753172, 0,1,"2080123140042",
                "닉네임4","10004","202311300042");

        postLabel[4].setPostLabel(10005,9, null,"Post 내용물입니당5",false,
                35.828979, 128.754296, 0,1,"2080123140042",
                "닉네임5","10005","202311300042");



        return postLabel;
    }



//    public String getPostIcon(PostClass post){
//      //구현안: icon_no를 String 형태의 @drawable/posticon숫자 형태로 변환
//        switch (post.icon_no){
//            case 1:
//                break;
//        }
//    }

}
