package com.example.broaf;

public class PostClass {

    int postID;
    int icon_no;    //아이콘 번호
    String attachImageURL;
    String content;
    Boolean isHide;
    double latitude;
    double longitude;
    int openRange;
    String openToDateTime;
    String writerName;
    String writerUID;
    String writtenDateTime;

    public PostClass() {
        postID = 19000;
        icon_no = 1;
        attachImageURL=null;
        content = "Post 내용물입니당";
        isHide=false;
        latitude=35.83127272522216;
        longitude=128.75584071186105;
        writtenDateTime = "202311300042";
        openToDateTime = "2080123140042";
        writerName = "Nickname";
        openRange = 1;
        content = "게시글 내용";
    }


    public void setPostANDicon(int postID, int setIcon_no){
        this.postID=postID; this.icon_no=icon_no;
    }
    public void setPostLatLng(double latitude, double longitude){
        this.latitude=latitude; this.longitude=longitude;
    }
    public void setPostContents(String attachImageURL, String content){
        this.attachImageURL=attachImageURL; this.content=content;
    }
    public void setVisibility(Boolean isHide, int openRange){
        this.isHide=isHide; this.openRange=openRange;
    }
    public void setWriter(String writerName, String writerUID){
        this.writerName=writerName; this.writerUID=writerUID;
    }
    public void setTime(String writtenDateTime, String openToDateTime){
        this.writtenDateTime=writtenDateTime; this.openToDateTime=openToDateTime;
    }

//    public String getPostIcon(PostClass post){
//      //구현안: icon_no를 String 형태의 @drawable/posticon숫자 형태로 변환
//        switch (post.icon_no){
//            case 1:
//                break;
//        }
//    }

}
