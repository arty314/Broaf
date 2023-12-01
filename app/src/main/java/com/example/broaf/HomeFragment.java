package com.example.broaf;

import com.example.broaf.R;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.MapGravity;
import com.kakao.vectormap.MapView;
import com.kakao.vectormap.camera.CameraAnimation;
import com.kakao.vectormap.camera.CameraUpdateFactory;
import com.kakao.vectormap.label.Badge;
import com.kakao.vectormap.label.BadgeOptions;
import com.kakao.vectormap.label.Label;
import com.kakao.vectormap.label.LabelLayer;
import com.kakao.vectormap.label.LabelOptions;
import com.kakao.vectormap.label.LabelStyle;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    String myUID = "-NkVlvAINDhIXRBZ1hHL";  //"henzel@gmail.com", 헨젤, 00001111 계정
    String[]friendsID={"\n" +
            "-NkCD1GOweVWBk7mhJJ9",
            "-Nk5C6FKxdEHbn5hxDo0",
            "-NkVnLvZ0TdAMTwY0w96",};
    //strong@naver.com, wowo    //good@good.com, good   //rich@gmail.com, 리치

    ImageButton btn_search;
    EditText input_text_search;   //검색 내용(edittext)을 끌어오기 위해서.
    String searchKeyword_input;


    ImageButton btn_fit, btn_new;
    ImageButton btn_filter_global, btn_filter_friend, btn_filter_me;
    int filterStatus=0; //0:global, 1: friend, 2: me

    //현재 GPS 위치
    double longitude=35.8318293, latitude=128.7544701;
    TextView txtResult; //이건 GPS 임시 뷰어
    //

    //지도에 현재 마커 표시
    private KakaoMap kakaoMap;
    private LabelLayer labelLayer;
    private Label centerLabel;
    private List<Label> selectedList = new ArrayList<>();
    Label label0, label1, label2, label3, label4, label5, label6, label7, label8, label9, label10,
            label11, label12, label13, label14, label15, label16, label17, label18, label19, label20,
            label21, label22, label23, label24, label25, label26, label27, label28, label29, label30, label31;





    //일단 db 대신 쓸 post 객체 생성. 수정 시 MyinfoFragment에 있는 것도 같이 수정해주세요....

    PostLabel postLabel0=new PostLabel(10001,1, null,"Post내용물입니당",false,
            35.831272, 128.755840, 0,6,"2080123140042",
            "헨젤","-NkVlvAINDhIXRBZ1hHL","202311300042");
    PostLabel postLabel1=new PostLabel(10002,8, "qwerty","Post내용물입니당2",false,
            35.832645, 128.757779, 0,43,"2080123140042",
            "나주","10001","202311260213");;
    PostLabel postLabel2=new PostLabel(10003,13, null,"Post내용물입니당3",false,
            35.839907, 128.755500, 0,2,"2080123140042",
            "헨젤","-NkVlvAINDhIXRBZ1hHL","202311261147");
    PostLabel postLabel3=new PostLabel(10004,4, null,"Post내용물입니당4",false,
            35.832735, 128.753172, 0,11,"2080123140042",
            "오리너구리","10004","202311261922");
    PostLabel postLabel4=new PostLabel(10005,3, null,"Post내용물입니당5",false,
            35.838979, 128.754296, 0,0,"2080123140042",
            "헨젤","-NkVlvAINDhIXRBZ1hHL","202311270136");
    PostLabel postLabel5=new PostLabel(10001,7, null,"Post내용물입니당",false,
            35.831272, 128.759159, 0,6,"2080123140042",
            "David","10005","202311270851");
    PostLabel postLabel6=new PostLabel(10002,8, "qwerty","Post내용물입니당2",false,
            35.826150, 128.755304, 0,43,"2080123140042",
            "치즈","10006","202311271605");;
    PostLabel postLabel7=new PostLabel(10003,13, null,"Post내용물입니당3",false,
            35.826756, 128.759264, 0,2,"2080123140042",
            "캔따개","10007","202311280019");
    PostLabel postLabel8=new PostLabel(10004,1, null,"Post내용물입니당4",false,
            35.827336, 128.759925, 0,11,"2080123140042",
            "wowo","-NkCD1GOweVWBk7mhJJ9","202311280733");
    PostLabel postLabel9=new PostLabel(10005,1, null,"Post내용물입니당5",false,
            35.830263,128.750771 , 0,0,"2080123140042",
            "wowo","-NkCD1GOweVWBk7mhJJ9","202311281448");
    PostLabel postLabel10=new PostLabel(10002,10, "qwerty","Post내용물입니당2",false,
            35.831140, 128.753568, 0,43,"2080123140042",
            "wowo","-NkCD1GOweVWBk7mhJJ9","202311282202");;
    PostLabel postLabel11=new PostLabel(10003,13, null,"Post내용물입니당3",false,
            35.834808, 128.755337, 0,2,"2080123140042",
            "good","-Nk5C6FKxdEHbn5hxDo0","202311290516");
    PostLabel postLabel12=new PostLabel(10004,4, null,"Post내용물입니당4",false,
            35.838494, 128.759727, 0,11,"2080123140042",
            "good","-Nk5C6FKxdEHbn5hxDo0","202311291231");
    PostLabel postLabel13=new PostLabel(10005,9, null,"Post내용물입니당5",false,
            35.826276, 128.758904, 0,0,"2080123140042",
            "리치","-NkVnLvZ0TdAMTwY0w96","202311291945");
    PostLabel postLabel14=new PostLabel(10002,6, "qwerty","Post내용물입니당2",false,
            35.834563, 128.752793, 0,43,"2080123140042",
            "리치","-NkVnLvZ0TdAMTwY0w96","202311300259");;
    PostLabel postLabel15=new PostLabel(10003,1, null,"Post내용물입니당3",false,
            35.835961, 755500, 0,2,"2080123140042",
            "리치","-NkVnLvZ0TdAMTwY0w96","202311301014");
    PostLabel postLabel16=new PostLabel(10004,2, null,"Post내용물입니당4",false,
            35.822753, 128.758879, 0,11,"2080123140042",
            "리치","-NkVnLvZ0TdAMTwY0w96","202311301728");
    PostLabel postLabel17=new PostLabel(10005,3, null,"Post내용물입니당5",false,
            35.837781, 128.754831, 0,0,"2080123140042",
            "나주","10001","202311302342");
    PostLabel postLabel18=new PostLabel(10003,4, null,"Post내용물입니당3",false,
            35.823499, 128.754017, 0,2,"2080123140042",
            "나주","10001","202312010657");
    PostLabel postLabel19=new PostLabel(10004,5, null,"Post내용물입니당4",false,
            35.826888, 128.752606, 0,11,"2080123140042",
            "나주","10001","202312011411");
    PostLabel postLabel20=new PostLabel(10005,6, null,"Post내용물입니당5",false,
            35.831330, 128.750808, 0,0,"2080123140042",
            "감귤","10002","202312012125");
    PostLabel postLabel21=new PostLabel(10002,7, "qwerty","Post내용물입니당2",false,
            35.839653, 128.753539, 0,43,"2080123140042",
            "감귤","10002","202311260528");;
    PostLabel postLabel22=new PostLabel(10003,8, null,"Post내용물입니당3",false,
            35.822492, 128.757284, 0,2,"2080123140042",
            "감귤","10002","202311261342");
    PostLabel postLabel23=new PostLabel(10004,9, null,"Post내용물입니당4",false,
            35.823360, 128.753468, 0,11,"2080123140042",
            "영석","10003","202311262157");
    PostLabel postLabel24=new PostLabel(10005,10, null,"Post내용물입니당5",false,
            35.828453, 128.754673, 0,0,"2080123140042",
            "영석","10003","202311270611");
    PostLabel postLabel25=new PostLabel(10003,11, null,"Post내용물입니당3",false,
            35.833135, 128.759240, 0,2,"2080123140042",
            "오리너구리","10004","202311271425");
    PostLabel postLabel26=new PostLabel(10004,12, null,"Post내용물입니당4",false,
            35.834984, 128.752745, 0,11,"2080123140042",
            "오리너구리","10004","202311272240");
    PostLabel postLabel27=new PostLabel(10005,3, null,"Post내용물입니당5",false,
            35.837526, 128.758078, 0,0,"2080123140042",
            "오리너구리","10004","202311281508");
    PostLabel postLabel28=new PostLabel(10002,14, "qwerty","Post내용물입니당2",false,
            35.833029, 128.759484, 0,43,"2080123140042",
            "David","10005","202311282323");;
    PostLabel postLabel29=new PostLabel(10003,2, null,"Post내용물입니당3",false,
            35.839327, 128.754154, 0,2,"2080123140042",
            "치즈","10006","202311300042");
    PostLabel postLabel30=new PostLabel(10004,14, null,"Post내용물입니당4",false,
            35.832735, 128.752636, 0,11,"2080123140042",
            "캔따개","10007","202311290737");
    PostLabel postLabel31=new PostLabel(10005,1, null,"Post내용물입니당5",false,
            35.838979, 128.757078, 0,0,"2080123140042",
            "캔따개","10007","202312010052");







    ///////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //표시할 xml layout 선택
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton fab = getActivity().findViewById(R.id.navi_to_home);
        fab.setImageResource(R.drawable.re_writepost);
        //



        //여기부터 search & post viewer
        btn_search = (ImageButton) view.findViewById(R.id.btn_search);
        input_text_search = view.findViewById(R.id.input_text_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKeyword_input = input_text_search.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("fromHomeFrag", searchKeyword_input);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setArguments(bundle);
                transaction.replace(R.id.frame_layout, searchFragment); //framg_layout영역을 searchFragment로 교체한다.
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        //여기까지 검색버튼



        //GPS 불러오기!!
        btn_new = (ImageButton)view.findViewById(R.id.btn_new);   //새로고침 버튼 누르면 됨.
        txtResult = (TextView)view.findViewById(R.id.txtResult);
        final LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( getActivity(), new String[] {
                            android.Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
                }
                else{
                    // 가장최근 위치정보 가져오기
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location != null) {
                        String provider = location.getProvider();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        txtResult.setText(provider + " Lat," + latitude + " Lng," + longitude);
                        //provider: 위치 정보, latitude: 위도, longitude: 경도 (altitude: 고도)
                    }

                    // 위치정보를 원하는 시간, 거리마다 갱신해준다. <-게시글 작성 버튼에선 이부분 빼면 됨
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                }
            }
        });
        //여기까지 GPS 불러오기

        //화면 중심 되돌리기
        btn_fit = (ImageButton)view.findViewById(R.id.btn_fit);
        btn_fit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakaoMap.moveCamera(CameraUpdateFactory.fitMapPoints(getSelectedPoints(), 20),
                        CameraAnimation.from(500, true, true));
            }
        });


        //여기부터 필터 버튼
        //    int filterStatus=0; //0:global, 1: friend, 2: me
        btn_filter_global=(ImageButton)view.findViewById(R.id.btn_filter_global);
        btn_filter_friend=(ImageButton)view.findViewById(R.id.btn_filter_friend);
        btn_filter_me=(ImageButton)view.findViewById(R.id.btn_filter_me);
        btn_filter_global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterStatus=0;
                Toast.makeText(getActivity(),"모든 글 표시",Toast.LENGTH_SHORT).show();
                createALLlabels();
            }});
        btn_filter_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterStatus=1;
                Toast.makeText(getActivity(),"나와 친구들의 글만 표시",Toast.LENGTH_SHORT).show();
                createALLlabels();
            }});
        btn_filter_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterStatus=2;
                Toast.makeText(getActivity(),"내가 작성한 글만 표시",Toast.LENGTH_SHORT).show();
                createALLlabels();
            }});





        //여기까지 필터 버튼





        //여기부터 카카오맵
        MapView mapView = view.findViewById(R.id.map_view);
        mapView.start(new KakaoMapReadyCallback() {


            @Override
            public LatLng getPosition() {
                return LatLng.from(35.832038,128.754193);
            }
            //이슈: latitude와 longitude를 GPS값으로 불러와야 하는데 안불러와진다!
            @Override
            public void onMapReady(KakaoMap map) {
                kakaoMap = map;
                labelLayer = kakaoMap.getLabelManager().getLayer();
                LatLng pos = kakaoMap.getCameraPosition().getPosition();
                createLabels(pos);


                kakaoMap.getLogo().setPosition(MapGravity.BOTTOM|MapGravity.RIGHT,50, 200 );

                //여기서 만약.. filterStatus를 준다면?
                //postlabel 달기
                createALLlabels();


                //postlabel 클릭 리스너들

                kakaoMap.setOnLabelClickListener(new KakaoMap.OnLabelClickListener() {
                    @Override
                    public void onLabelClicked(KakaoMap kakaoMap, LabelLayer layer, Label label) {
                        String labelID = label.getLabelId();

                        if(labelID=="centerLabel"){
                            kakaoMap.moveCamera(CameraUpdateFactory.fitMapPoints(getSelectedPoints(), 20),
                                    CameraAnimation.from(500, true, true));
                        }
                        else if(labelID=="label0")     showPostViewer(postLabel0);
                        else if(labelID=="label1")     showPostViewer(postLabel1);
                        else if(labelID=="label2")     showPostViewer(postLabel2);
                        else if(labelID=="label3")     showPostViewer(postLabel3);
                        else if(labelID=="label4")     showPostViewer(postLabel4);
                        else if(labelID.equals("label5"))     showPostViewer(postLabel5);
                        else if(labelID.equals("label6"))     showPostViewer(postLabel6);
                        else if(labelID.equals("label7"))     showPostViewer(postLabel7);
                        else if(labelID.equals("label8"))     showPostViewer(postLabel8);
                        else if(labelID.equals("label9"))     showPostViewer(postLabel9);
                        else if(labelID.equals("label10"))     showPostViewer(postLabel10);
                        else if(labelID.equals("label11"))     showPostViewer(postLabel11);
                        else if(labelID.equals("label12"))     showPostViewer(postLabel12);
                        else if(labelID.equals("label13"))     showPostViewer(postLabel13);
                        else if(labelID.equals("label14"))     showPostViewer(postLabel14);
                        else if(labelID.equals("label15"))     showPostViewer(postLabel15);
                        else if(labelID.equals("label16"))     showPostViewer(postLabel16);
                        else if(labelID.equals("label17"))     showPostViewer(postLabel17);
                        else if(labelID.equals("label18"))     showPostViewer(postLabel18);
                        else if(labelID.equals("label19"))     showPostViewer(postLabel19);
                        else if(labelID.equals("label20"))     showPostViewer(postLabel20);
                        else if(labelID.equals("label21"))     showPostViewer(postLabel21);
                        else if(labelID.equals("label22"))     showPostViewer(postLabel22);
                        else if(labelID.equals("label23"))     showPostViewer(postLabel23);
                        else if(labelID.equals("label24"))     showPostViewer(postLabel24);
                        else if(labelID.equals("label25"))     showPostViewer(postLabel25);
                        else if(labelID.equals("label26"))     showPostViewer(postLabel26);
                        else if(labelID.equals("label27"))     showPostViewer(postLabel27);
                        else if(labelID.equals("label28"))     showPostViewer(postLabel28);
                        else if(labelID.equals("label29"))     showPostViewer(postLabel29);
                        else if(labelID.equals("label30"))     showPostViewer(postLabel30);
                        else if(labelID.equals("label31"))     showPostViewer(postLabel31);


                    }
                });

            }
        });
        /**여기까지 카카오맵**/


        return view;
    }


    //gps 수신 메소드
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
            String provider = location.getProvider();  // 위치정보
            double longitude = location.getLongitude(); // 위도
            double latitude = location.getLatitude(); // 경도
            txtResult.setText(provider + " Lat," + latitude + " Lng," + longitude);
        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };

    private void createLabels(LatLng pos) {
        // 중심 라벨 생성
        centerLabel = labelLayer.addLabel(LabelOptions.from("centerLabel", pos)
                .setStyles(LabelStyle.from(R.drawable.icon_currentpospng2).setAnchorPoint(0.5f, 0.5f))
                .setRank(1));
        selectedList.add(centerLabel);
        }

    private LatLng[] getSelectedPoints() {
        int count = selectedList.size();
        LatLng[] points = new LatLng[count];
        for (int i = 0; i < selectedList.size(); i++) {
            points[i] = selectedList.get(i).getPosition();
        }
        return points;
    }

    //PostClass를 하나 보내면, 지정한 label로 변환해주는 함수.
    public Label createPostLabel(PostLabel postLabel, Label label,String label_ID,int filterStatus){

        //라벨 icon 설정
        if(postLabel.icon_no==1)
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(postLabel.latitude,postLabel.longitude))
                .setStyles(LabelStyle.from(R.drawable.posticon1).setAnchorPoint(0.5f, 0.5f)).setRank(1));
        else if (postLabel.icon_no==2)
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(postLabel.latitude,postLabel.longitude))
                    .setStyles(LabelStyle.from(R.drawable.posticon2).setAnchorPoint(0.5f, 0.5f)).setRank(1));
        else if (postLabel.icon_no==3)
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(postLabel.latitude,postLabel.longitude))
                    .setStyles(LabelStyle.from(R.drawable.posticon3).setAnchorPoint(0.5f, 0.5f)).setRank(1));
        else if (postLabel.icon_no==4)
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(postLabel.latitude,postLabel.longitude))
                    .setStyles(LabelStyle.from(R.drawable.posticon4).setAnchorPoint(0.5f, 0.5f)).setRank(1));
        else if (postLabel.icon_no==5)
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(postLabel.latitude,postLabel.longitude))
                    .setStyles(LabelStyle.from(R.drawable.posticon5).setAnchorPoint(0.5f, 0.5f)).setRank(1));
        else if (postLabel.icon_no==6)
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(postLabel.latitude,postLabel.longitude))
                    .setStyles(LabelStyle.from(R.drawable.posticon6).setAnchorPoint(0.5f, 0.5f)).setRank(1));
        else if (postLabel.icon_no==7)
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(postLabel.latitude,postLabel.longitude))
                    .setStyles(LabelStyle.from(R.drawable.posticon7).setAnchorPoint(0.5f, 0.5f)).setRank(1));
        else if (postLabel.icon_no==8)
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(postLabel.latitude,postLabel.longitude))
                    .setStyles(LabelStyle.from(R.drawable.posticon8).setAnchorPoint(0.5f, 0.5f)).setRank(1));
        else if (postLabel.icon_no==9)
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(postLabel.latitude,postLabel.longitude))
                    .setStyles(LabelStyle.from(R.drawable.posticon9).setAnchorPoint(0.5f, 0.5f)).setRank(1));
        else if (postLabel.icon_no==10)
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(postLabel.latitude,postLabel.longitude))
                    .setStyles(LabelStyle.from(R.drawable.posticon10).setAnchorPoint(0.5f, 0.5f)).setRank(1));
        else if (postLabel.icon_no==11)
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(postLabel.latitude,postLabel.longitude))
                    .setStyles(LabelStyle.from(R.drawable.posticon11).setAnchorPoint(0.5f, 0.5f)).setRank(1));
        else if (postLabel.icon_no==12)
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(postLabel.latitude,postLabel.longitude))
                    .setStyles(LabelStyle.from(R.drawable.posticon12).setAnchorPoint(0.5f, 0.5f)).setRank(1));
        else if (postLabel.icon_no==13)
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(postLabel.latitude,postLabel.longitude))
                    .setStyles(LabelStyle.from(R.drawable.posticon13).setAnchorPoint(0.5f, 0.5f)).setRank(1));
        else if (postLabel.icon_no==14)
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(postLabel.latitude,postLabel.longitude))
                    .setStyles(LabelStyle.from(R.drawable.posticon14).setAnchorPoint(0.5f, 0.5f)).setRank(1));

        //badge 달기
        Badge[] badges = new Badge[0];


        if (postLabel.writerUID.equals(myUID)){//case1: 내 게시글
            if(postLabel.attachImageURL!=null)
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f),
                        BadgeOptions.with(R.drawable.badge_withimg).setOffset(0.1f, 0.1f));
            else
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f));
        } else{   //case2: 내 게시글 아님.
            for (String friendID : friendsID) { //case2-1: 그럼 친구게시글인가요?
                if (postLabel.writerUID.equals(friendID.trim())) {
                    if(postLabel.attachImageURL!=null)
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f),
                                BadgeOptions.with(R.drawable.badge_withimg).setOffset(0.1f, 0.1f));
                    else
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f));
                }
            }
            //case2-2: 친구것도 아닙니다..
            if(postLabel.attachImageURL!=null)
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_withimg).setOffset(0.1f, 0.1f));
            //else 내 게시글도 아니고 image도 없음 -> 아무것도 안함
        }
        //writer의 id = 친구목록에 있다면 friend 뱃지 달기   <- 친구 목록 구현 성공시 else if로 달 것
        //게시글의 attachImageURL이 null이 아니면 image 뱃지 달기

        //뱃지 보이게
        for (Badge badge : badges) {
            badge.show();
        }
        label.getLabelId();
        label.setClickable(true);
        //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기

        //여기서 필터 적용
        switch(filterStatus){
            case 0:
                label.show();
                return label;
            case 1://친구+나만 보기
                if (postLabel.writerUID.equals(myUID)) {label.show(); return label;}
                for (String friendID : friendsID) {
                    if (postLabel.writerUID.equals(friendID.trim())) {label.show(); return label;}
                }
                label.hide();
            case 2: //나만 보기
                if (postLabel.writerUID.equals(myUID)) {return label;}
                label.hide();
        }




        return label;
    }


    void showPostViewer(PostLabel postLabel){
        //지도 상의 버튼을 누르면 해당 postlabel의 정보와 uid를 들고 postViewerFragment를 add한다.

        //번들에 짐 싸기
        Bundle postBundle = new Bundle();

        //userID
        postBundle.putString("myUID",myUID);
        postBundle.putSerializable("postLabel",postLabel);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PostViewerFragment postViewerFragment = new PostViewerFragment();

        postViewerFragment.setArguments(postBundle);    //내용물을 viewer로 보내기

        fragmentTransaction.add(R.id.frame_layout_post_viewer, postViewerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    void createALLlabels(){
        createPostLabel(postLabel0,label0,"label0", filterStatus);
        createPostLabel(postLabel1,label1,"label1", filterStatus);
        createPostLabel(postLabel2,label2,"label2", filterStatus);
        createPostLabel(postLabel3,label3,"label3", filterStatus);
        createPostLabel(postLabel4,label4,"label4", filterStatus);
        createPostLabel(postLabel5,label5,"label5", filterStatus);
        createPostLabel(postLabel6,label6,"label6", filterStatus);
        createPostLabel(postLabel7,label7,"label7", filterStatus);
        createPostLabel(postLabel8,label8,"label8", filterStatus);
        createPostLabel(postLabel9,label9,"label9", filterStatus);
        createPostLabel(postLabel10,label10,"label10", filterStatus);
        createPostLabel(postLabel11,label11,"label11", filterStatus);
        createPostLabel(postLabel12,label12,"label12", filterStatus);
        createPostLabel(postLabel13,label13,"label13", filterStatus);
        createPostLabel(postLabel14,label14,"label14", filterStatus);
        createPostLabel(postLabel15,label15,"label15", filterStatus);
        createPostLabel(postLabel16,label16,"label16", filterStatus);
        createPostLabel(postLabel17,label17,"label17", filterStatus);
        createPostLabel(postLabel18,label18,"label18", filterStatus);
        createPostLabel(postLabel19,label19,"label19", filterStatus);
        createPostLabel(postLabel20,label20,"label20", filterStatus);
        createPostLabel(postLabel21,label21,"label21", filterStatus);
        createPostLabel(postLabel22,label22,"label22", filterStatus);
        createPostLabel(postLabel23,label23,"label23", filterStatus);
        createPostLabel(postLabel24,label24,"label24", filterStatus);
        createPostLabel(postLabel25,label25,"label25", filterStatus);
        createPostLabel(postLabel26,label26,"label26", filterStatus);
        createPostLabel(postLabel27,label27,"label27", filterStatus);
        createPostLabel(postLabel28,label28,"label28", filterStatus);
        createPostLabel(postLabel29,label29,"label29", filterStatus);
        createPostLabel(postLabel30,label30,"label30", filterStatus);
        createPostLabel(postLabel31,label31,"label31", filterStatus);
    }


}