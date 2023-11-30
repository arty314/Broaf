package com.example.broaf;

import com.kakao.vectormap.label.Label;

public class PostClass {

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

    public PostClass() {
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
    public PostClass[] MakePosts() {
        PostClass[] postClasses = new PostClass[5];

        postClasses[0].postID = 10001;
        postClasses[0].attachImageURL=null;
        postClasses[0].content = "Post 내용물입니당";
        postClasses[0].icon_no = 1;
        postClasses[0].isHide=false;
        postClasses[0].latitude=35.831272;
        postClasses[0].longitude=128.755840;
        postClasses[0].likeCount=0;
        postClasses[0].openRange = 1;
        postClasses[0].writtenDateTime = "202311300042";
        postClasses[0].openToDateTime = "2080123140042";
        postClasses[0].writerName = "Nickname";
        postClasses[0].writerUID = "10001";

        postClasses[1].postID = 10001;
        postClasses[1].icon_no = 1;
        postClasses[1].attachImageURL=null;
        postClasses[1].content = "게시글 예시내용 2000자 이내길이, openRange는 1이 전체 2가 친구 3이 비공개";
        postClasses[1].isHide=false;
        postClasses[1].latitude=35.857934;
        postClasses[1].longitude=128.520584;
        postClasses[1].writtenDateTime = "202311300042";
        postClasses[1].openToDateTime = "2080123140042";
        postClasses[1].writerName = "Nickname";
        postClasses[1].openRange = 1;
        postClasses[1].content = "게시글 내용";


//        이렇게 다섯개 만들어야 됨

        return postClasses;
    }



//    public String getPostIcon(PostClass post){
//      //구현안: icon_no를 String 형태의 @drawable/posticon숫자 형태로 변환
//        switch (post.icon_no){
//            case 1:
//                break;
//        }
//    }

}
