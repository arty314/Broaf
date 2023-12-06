package com.example.broaf;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.MapGravity;
import com.kakao.vectormap.MapView;
import com.kakao.vectormap.Poi;
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


//    String myName = "-NkVlvAINDhIXRBZ1hHL";  //"henzel@gmail.com", 헨젤, 00001111 계정
//    String[]friendNameList;
//    //strong@naver.com, wowo    //good@good.com, good   //rich@gmail.com, 리치

    ImageButton btn_search;
    EditText input_text_search;   //검색 내용(edittext)을 끌어오기 위해서.
    String searchKeyword_input;


    ImageButton btn_fit, btn_new;
    ImageButton btn_filter_global, btn_filter_friend, btn_filter_me;
    Button btn_click;
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


    //NormalPost들을 저장하기 위한 ArrayList 선언


    ArrayList<ReceiveNormalPost> postList; //ReceiveNormalPost형태의 ArrayList
    ArrayList<Label> labelList;             //postList를 Label 형태로 바꾼 결과를 저장하는 ArrayList
    Label newlabel; //새로 추가할 라벨

    ArrayList<String> friendNameList; //User의 친구 Nickname을 저장하는 String arrayList
    //    RecyclerView recyclerview;
//    //RecyclerView.Adapter adapter;
    //Auth 데이터를 가져오기 위한 변수
    FirebaseAuth mAuth = FirebaseAuth.getInstance(); //FirebaseAuth를 import
    FirebaseUser currentUser = mAuth.getCurrentUser(); //현재로그인한 유저를 저장
    String currentKey,currentUid,currentEmail, myNickname=null; //찾은 노드의키, 현재 로그인 유저 UID, 현재 로그인 유저 Email, 찾아온 nickname
    ///////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Home_onCreateView", "content: line98");
        //표시할 xml layout 선택
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton fab = getActivity().findViewById(R.id.navi_to_home);
        fab.setImageResource(R.drawable.re_writepost);
        //

        Log.e("Home_onCreateView", "content: line106");


        //여기부터 search & post viewer
        btn_search = (ImageButton) view.findViewById(R.id.btn_search);
        input_text_search = view.findViewById(R.id.input_text_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKeyword_input = input_text_search.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("fromHomeFrag", searchKeyword_input);
                bundle.putString("myNickname",myNickname);
                bundle.putStringArrayList("friendNameList", friendNameList);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setArguments(bundle);
                transaction.replace(R.id.frame_layout, searchFragment); //framg_layout영역을 searchFragment로 교체한다.
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        //여기까지 검색버튼
        Log.e("Home_onCreateView", "content: line127");



        //GPS 불러오기!!
        //버튼 눌러 GPS 불러오기
        final LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        btn_new = (ImageButton)view.findViewById(R.id.btn_new);   //새로고침 버튼 누르면 됨.
        txtResult = (TextView)view.findViewById(R.id.txtResult);
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Home_onCreateView", "content: line139");
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( getActivity(), new String[] {
                            android.Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
                    Log.e("Home_onCreateView", "content: line144");
                }
                else{
                    // 가장최근 위치정보 가져오기
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location != null) {
                        String provider = location.getProvider();
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.e("Home_onCreateView", "content: line153");

                        txtResult.setText(provider + " Lat," + latitude + " Lng," + longitude);

                        //provider: 위치 정보, latitude: 위도, longitude: 경도 (altitude: 고도)
                    }
                    else{Toast.makeText(view.getContext(), "현재 위치를 확인할 수 없습니다", Toast.LENGTH_SHORT).show();}

                    // 위치정보를 원하는 시간, 거리마다 갱신해준다. <-게시글 작성 버튼에선 이부분 빼면 됨
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                    Log.e("Home_onCreateView", "content: line165");
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                    Log.e("Home_onCreateView", "content: line170");
                }
            }
        });
        //여기까지 GPS 불러오기

        Log.e("Home_onCreateView", "content: line176");
        //화면 중심 되돌리기
        btn_fit = (ImageButton)view.findViewById(R.id.btn_fit);
        btn_fit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kakaoMap.moveCamera(CameraUpdateFactory.fitMapPoints(getSelectedPoints(), 20),
                        CameraAnimation.from(500, true, true));
                Log.e("Home_onCreateView", "content: line185");
            }
        });


        Log.e("Home_onCreateView", "content: line190");
        //1. 현재 사용자의 UID,Email
        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentEmail = currentUser.getEmail();
        postList = new ArrayList<>();
        labelList = new ArrayList<>();
        Log.e("Home_onCreateView", "content: line196");
        // 닉네임가져오기
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://broaf-72e4c-default-rtdb.firebaseio.com/");

        Log.e("Home_onCreateView", "content: line200");

        database.getReference("User").orderByChild("email").equalTo(currentEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Home_onCreateView", "content: line205");
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    currentKey = userSnapshot.getKey(); // 현재 로그인한 User key
                    myNickname = userSnapshot.child("nickname").getValue(String.class);
                    Log.e("currentNickName", "content: " + myNickname);
                    Log.e("Home_onCreateView", "content: line210");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Home_onCreateView", "content: line216");
                // 쿼리가 취소된 경우 또는 에러가 발생한 경우 처리
            }
        });
        //1. 현재 사용자의 UID가져오기(종료)


        //3. 접속자의 모든 친구 불러오기
        friendNameList = new ArrayList<>();
        if (currentUser != null) {
            Log.e("Home_onCreateView", "content: line226");
            database.getReference("User").orderByChild("email").equalTo(currentEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String currentKey = userSnapshot.getKey(); // 현재 로그인한 User key

                        friendNameList = new ArrayList<>();

                        //가져온 데이터의 child 데이터들을 User 클래스의 정의에 맞게 정리 후 리스트에 추가
                        database.getReference("User").child(currentKey).child("friendlist").orderByChild("email").addListenerForSingleValueEvent(new ValueEventListener() {
                            //현재 로그인한 사용자의 friendlist 데이터 가져오기
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Log.e("Home_onCreateView", "content: line240");
                                friendNameList.clear();
                                for(DataSnapshot friendSnapshot : snapshot.getChildren()) {
                                    Log.e("Home_onCreateView", "content: line243");
                                    String nickname = friendSnapshot.child("nickname").getValue(String.class);
                                    Log.e("Friendnickname", "nickname: " + nickname); // 이메일을 로그에 출력
                                    friendNameList.add(nickname);
                                    Log.e("Home_onCreateView", "content: line247");
                                }
                                //원래는 AdapterChange가 선언된다.
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("FriendListActivity", String.valueOf(error.toException()));
                                Log.e("Home_onCreateView", "content: line255");
                            }
                        });
                        //원래는 이부분에 Adatper연결을 한다.
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 쿼리가 취소된 경우 또는 에러가 발생한 경우 처리
                    Log.e("Home_onCreateView", "content: line264");
                }
            });
        }
        //3. 접속자의 모든 친구 불러오기(종료)


        //2. 모든 게시글 불러오기
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                postList.clear();
                labelList.clear();
                Log.e("Home_onCreateView", "content: line277");
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Log.e("Home_onCreateView", "content: line279");
                    ReceiveNormalPost receiveNormalPost = snapshot.getValue(ReceiveNormalPost.class);
                    Log.e("PostContents", "content: " + receiveNormalPost.getContents());
                    postList.add(receiveNormalPost);
                    //여기 있던 createLabel에서 계속 에러였음.
                    Log.e("Home_onCreateView", "content: line287");
                }
                //여기에 createALLPost 써야할 각
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", String.valueOf(databaseError.toException()));
                Log.e("Home_onCreateView", "content: line295");
            }
        };
        Log.e("Home_onCreateView", "content: line298");
        database.getReference("Post").child("NormalPost").addValueEventListener(postListener);
        Log.e("Home_onCreateView", "content: line300");
        //2. 모든 게시글 불러오기(종료)

        // currentEmail: 현재 접속자의 Email
        // currentUid: 현재 접속자의 Uid
        // friendList: 현재 접속자의 친구Email목록

        //여기부터 필터 버튼
        //int filterStatus=0; //0:global, 1: friend, 2: me
        Log.e("Home_onCreateView", "content: line309");

        btn_click=(Button) view.findViewById(R.id.btn_click);
        btn_filter_global=(ImageButton)view.findViewById(R.id.btn_filter_global);
        btn_filter_friend=(ImageButton)view.findViewById(R.id.btn_filter_friend);
        btn_filter_me=(ImageButton)view.findViewById(R.id.btn_filter_me);
        Log.e("Home_onCreateView", "content: line313");


        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Home_onCreateView", "content: line300");
                filterStatus=0;
                Log.e("Home_onCreateView", "content: line300");
                createALLlabels();
                Log.e("Home_onCreateView", "content: line300");


                btn_click.setVisibility(View.GONE);
            }});
        btn_filter_global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Home_onCreateView", "content: line300");
                filterStatus=0;
                Toast.makeText(getActivity(),"모든 글 표시",Toast.LENGTH_SHORT).show();
                Log.e("Home_onCreateView", "content: line300");
                createALLlabels();
                Log.e("Home_onCreateView", "content: line300");
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
                return LatLng.from(35.832038,128.754193);//이 위치로 고정
            }

            //이슈: latitude와 longitude를 GPS값으로 불러와야 하는데 안불러와진다!
            @Override
            public void onMapReady(KakaoMap map) {

                kakaoMap = map;
                labelLayer = kakaoMap.getLabelManager().getLayer(); //getLayer
                LatLng pos = kakaoMap.getCameraPosition().getPosition(); //langitude,longitude를 객체형태로 받는다.(위의 좌표가 저장되어있다)
                //LatLng pos = getPosition();
                //createLabels(pos); //현재마크를 설정하는 것



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
                        else
                            for(int i=0;i<postList.size();i++) {
                                if (labelID == postList.get(i).pid) showPostViewer(postList.get(i));
                            }


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
            createGPSLabel(LatLng.from(latitude,longitude)); //
        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };

    private void createGPSLabel(LatLng pos) {
        // 중심 라벨 생성
        centerLabel = labelLayer.addLabel(LabelOptions.from("centerLabel", pos)
                .setStyles(LabelStyle.from(R.drawable.icon_currentpospng3).setAnchorPoint(0.5f, 0.5f))
                .setRank(2));
        selectedList.clear();
        selectedList.add(centerLabel);
        centerLabel = labelLayer.addLabel(LabelOptions.from("str",pos));
    }

    private LatLng[] getSelectedPoints() {
        int count = selectedList.size();
        LatLng[] points = new LatLng[count];
        for (int i = 0; i < selectedList.size(); i++) {
            points[i] = selectedList.get(i).getPosition();
        }
        return points;
    }

    //normal post receiver 하나 보내면, 지정한 label로 변환해주는 함수.
    public Label createLabel(ReceiveNormalPost normalPost, String label_ID, int filterStatus){
        //label_ID는 post_ID로 한다.



        double pLatitude_Double = Double.parseDouble(normalPost.pLatitude);
        double pLongitude_Double = Double.parseDouble(normalPost.pLongitude);

        Label label;
        //badge 달기
        Badge[] badges = new Badge[0];

        //라벨 icon 설정
        if(normalPost.getIcon().equals("1")) {
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(pLatitude_Double, pLongitude_Double))
                    .setStyles(LabelStyle.from(R.drawable.posticon1).setAnchorPoint(0.5f, 0.5f)).setRank(1));
            if (normalPost.writerName.equals(myNickname))//case1: 내 게시글
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f, 0.9f));
            else    //case2: 내 게시글 아님.
                for (String friendNameList : friendNameList) { //case2-1: 그럼 친구게시글인가요?
                    if (normalPost.writerName.equals(friendNameList.trim()))
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f, 0.9f));
                }
            //뱃지 보이게
            for (Badge badge : badges) {
                badge.show();
            }
            label.getLabelId();
            label.setClickable(true);
            //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기

            int isMyFriend=0;
            //게시글의 공개 범위에 따라 hide여부 판독
            if (normalPost.getOpenRange().equals("3")) {  //비공개의 경우
                if (!normalPost.writerName.equals(myNickname)) {label.hide(); return label;}    //작성자 닉 == 내 닉 아닐 시 hide
            }
            else if (normalPost.getOpenRange().equals("2")) { //구현방법 변화: 자기를 구독하는 사람만 볼 수 있음. (자기가 구독하는 인물 X)
                if (normalPost.writerName.equals(myNickname)) {isMyFriend = 1;}
                for (String friendNameList : friendNameList) {
                    if (normalPost.writerName.equals(friendNameList.trim()))//만약 내가 구독하는 친구라면?
                        isMyFriend = 1;
                }
                if (isMyFriend==0) {label.hide(); return label;}
            }


                //여기서 필터 적용
                switch (filterStatus) {
                    case 0:
                        label.show();   //그냥 표시.
                        return label;
                    case 1://친구+나만 보기
                        if (normalPost.writerName.equals(myNickname)) {
                            label.show();
                            return label;
                        } //내꺼면 show하고 리턴.
                        for (String friendNameList : friendNameList) {
                            if (normalPost.writerName.equals(friendNameList.trim())) {
                                label.show();
                                return label;
                            }  //친구꺼면 show하고 리턴.
                        }
                        label.hide();   //내것도, 친구것도 아니라면 hide
                        return label;
                    case 2: //나만 보기
                        if (normalPost.writerName.equals(myNickname)) {
                            label.show();
                            return label;
                        }   //내꺼면 show하고 리턴.
                        label.hide();   //내것이 아니라면 hide
                        return label;
                }
            }

        else if (normalPost.getIcon().equals("2")){
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(pLatitude_Double,pLongitude_Double))
                    .setStyles(LabelStyle.from(R.drawable.posticon2).setAnchorPoint(0.5f, 0.5f)).setRank(1));
            if (normalPost.writerName.equals(myNickname))//case1: 내 게시글
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f));
            else    //case2: 내 게시글 아님.
                for (String friendNameList : friendNameList) { //case2-1: 그럼 친구게시글인가요?
                    if (normalPost.writerName.equals(friendNameList.trim()))
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f));
                }
            //뱃지 보이게
            for (Badge badge : badges) {
                badge.show();
            }
            label.getLabelId();
            label.setClickable(true);
            //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기

            int isMyFriend=0;
            //게시글의 공개 범위에 따라 hide여부 판독
            if (normalPost.getOpenRange().equals("3")) {  //비공개의 경우
                if (!normalPost.writerName.equals(myNickname)) {label.hide(); return label;}
            }
            else if (normalPost.getOpenRange().equals("2")) { //구현방법 변화: 자기를 구독하는 사람만 볼 수 있음. (자기가 구독하는 인물 X)
                if (normalPost.writerName.equals(myNickname)) {isMyFriend = 1;}
                for (String friendNameList : friendNameList) {
                    if (normalPost.writerName.equals(friendNameList.trim()))//만약 내가 구독하는 친구라면?
                        isMyFriend = 1;
                }
                if (isMyFriend==0) {label.hide(); return label;}
            }
            //여기서 필터 적용
            switch(filterStatus){
                case 0:
                    label.show();   //그냥 표시.
                    return label;
                case 1://친구+나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;} //내꺼면 show하고 리턴.
                    for (String friendNameList : friendNameList) {
                        if (normalPost.writerName.equals(friendNameList.trim())) {label.show(); return label;}  //친구꺼면 show하고 리턴.
                    }
                    label.hide();   //내것도, 친구것도 아니라면 hide
                    return label;
                case 2: //나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;}   //내꺼면 show하고 리턴.
                    label.hide();   //내것이 아니라면 hide
                    return label;
            }
        }
        else if (normalPost.getIcon().equals("3")){
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(pLatitude_Double,pLongitude_Double))
                    .setStyles(LabelStyle.from(R.drawable.posticon3).setAnchorPoint(0.5f, 0.5f)).setRank(1));
            if (normalPost.writerName.equals(myNickname))//case1: 내 게시글
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f));
            else    //case2: 내 게시글 아님.
                for (String friendNameList : friendNameList) { //case2-1: 그럼 친구게시글인가요?
                    if (normalPost.writerName.equals(friendNameList.trim()))
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f));
                }
            //뱃지 보이게
            for (Badge badge : badges) {
                badge.show();
            }
            label.getLabelId();
            label.setClickable(true);
            //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기

            int isMyFriend=0;
            //게시글의 공개 범위에 따라 hide여부 판독
            if (normalPost.getOpenRange().equals("3")) {  //비공개의 경우
                if (!normalPost.writerName.equals(myNickname)) {label.hide(); return label;}
            }
            else if (normalPost.getOpenRange().equals("2")) { //구현방법 변화: 자기를 구독하는 사람만 볼 수 있음. (자기가 구독하는 인물 X)
                if (normalPost.writerName.equals(myNickname)) {isMyFriend = 1;}
                for (String friendNameList : friendNameList) {
                    if (normalPost.writerName.equals(friendNameList.trim()))//만약 내가 구독하는 친구라면?
                        isMyFriend = 1;
                }
                if (isMyFriend==0) {label.hide(); return label;}
            }

            //여기서 필터 적용
            switch(filterStatus){
                case 0:
                    label.show();   //그냥 표시.
                    return label;
                case 1://친구+나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;} //내꺼면 show하고 리턴.
                    for (String friendNameList : friendNameList) {
                        if (normalPost.writerName.equals(friendNameList.trim())) {label.show(); return label;}  //친구꺼면 show하고 리턴.
                    }
                    label.hide();   //내것도, 친구것도 아니라면 hide
                    return label;
                case 2: //나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;}   //내꺼면 show하고 리턴.
                    label.hide();   //내것이 아니라면 hide
                    return label;
            }
        }
        else if (normalPost.getIcon().equals("4")){
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(pLatitude_Double,pLongitude_Double))
                    .setStyles(LabelStyle.from(R.drawable.posticon4).setAnchorPoint(0.5f, 0.5f)).setRank(1));
            if (normalPost.writerName.equals(myNickname))//case1: 내 게시글
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f));
            else    //case2: 내 게시글 아님.
                for (String friendNameList : friendNameList) { //case2-1: 그럼 친구게시글인가요?
                    if (normalPost.writerName.equals(friendNameList.trim()))
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f));
                }
            //뱃지 보이게
            for (Badge badge : badges) {
                badge.show();
            }
            label.getLabelId();
            label.setClickable(true);
            //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기

            int isMyFriend=0;
            //게시글의 공개 범위에 따라 hide여부 판독
            if (normalPost.getOpenRange().equals("3")) {  //비공개의 경우
                if (!normalPost.writerName.equals(myNickname)) {label.hide(); return label;}
            }
            else if (normalPost.getOpenRange().equals("2")) { //구현방법 변화: 자기를 구독하는 사람만 볼 수 있음. (자기가 구독하는 인물 X)
                if (normalPost.writerName.equals(myNickname)) {isMyFriend = 1;}
                for (String friendNameList : friendNameList) {
                    if (normalPost.writerName.equals(friendNameList.trim()))//만약 내가 구독하는 친구라면?
                        isMyFriend = 1;
                }
                if (isMyFriend==0) {label.hide(); return label;}
            }

            //여기서 필터 적용
            switch(filterStatus){
                case 0:
                    label.show();   //그냥 표시.
                    return label;
                case 1://친구+나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;} //내꺼면 show하고 리턴.
                    for (String friendNameList : friendNameList) {
                        if (normalPost.writerName.equals(friendNameList.trim())) {label.show(); return label;}  //친구꺼면 show하고 리턴.
                    }
                    label.hide();   //내것도, 친구것도 아니라면 hide
                    return label;
                case 2: //나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;}   //내꺼면 show하고 리턴.
                    label.hide();   //내것이 아니라면 hide
                    return label;
            }
        }
        else if (normalPost.getIcon().equals("5")){
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(pLatitude_Double,pLongitude_Double))
                    .setStyles(LabelStyle.from(R.drawable.posticon5).setAnchorPoint(0.5f, 0.5f)).setRank(1));
            if (normalPost.writerName.equals(myNickname))//case1: 내 게시글
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f));
            else    //case2: 내 게시글 아님.
                for (String friendNameList : friendNameList) { //case2-1: 그럼 친구게시글인가요?
                    if (normalPost.writerName.equals(friendNameList.trim()))
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f));
                }
            //뱃지 보이게
            for (Badge badge : badges) {
                badge.show();
            }
            label.getLabelId();
            label.setClickable(true);
            //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기

            int isMyFriend=0;
            //게시글의 공개 범위에 따라 hide여부 판독
            if (normalPost.getOpenRange().equals("3")) {  //비공개의 경우
                if (!normalPost.writerName.equals(myNickname)) {label.hide(); return label;}
            }
            else if (normalPost.getOpenRange().equals("2")) { //구현방법 변화: 자기를 구독하는 사람만 볼 수 있음. (자기가 구독하는 인물 X)
                if (normalPost.writerName.equals(myNickname)) {isMyFriend = 1;}
                for (String friendNameList : friendNameList) {
                    if (normalPost.writerName.equals(friendNameList.trim()))//만약 내가 구독하는 친구라면?
                        isMyFriend = 1;
                }
                if (isMyFriend==0) {label.hide(); return label;}
            }

            //여기서 필터 적용
            switch(filterStatus){
                case 0:
                    label.show();   //그냥 표시.
                    return label;
                case 1://친구+나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;} //내꺼면 show하고 리턴.
                    for (String friendNameList : friendNameList) {
                        if (normalPost.writerName.equals(friendNameList.trim())) {label.show(); return label;}  //친구꺼면 show하고 리턴.
                    }
                    label.hide();   //내것도, 친구것도 아니라면 hide
                    return label;
                case 2: //나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;}   //내꺼면 show하고 리턴.
                    label.hide();   //내것이 아니라면 hide
                    return label;
            }
        }
        else if (normalPost.getIcon().equals("6")){
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(pLatitude_Double,pLongitude_Double))
                    .setStyles(LabelStyle.from(R.drawable.posticon6).setAnchorPoint(0.5f, 0.5f)).setRank(1));
            if (normalPost.writerName.equals(myNickname))//case1: 내 게시글
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f));
            else    //case2: 내 게시글 아님.
                for (String friendNameList : friendNameList) { //case2-1: 그럼 친구게시글인가요?
                    if (normalPost.writerName.equals(friendNameList.trim()))
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f));
                }
            //뱃지 보이게
            for (Badge badge : badges) {
                badge.show();
            }
            label.getLabelId();
            label.setClickable(true);
            //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기

            int isMyFriend=0;
            //게시글의 공개 범위에 따라 hide여부 판독
            if (normalPost.getOpenRange().equals("3")) {  //비공개의 경우
                if (!normalPost.writerName.equals(myNickname)) {label.hide(); return label;}
            }
            else if (normalPost.getOpenRange().equals("2")) { //구현방법 변화: 자기를 구독하는 사람만 볼 수 있음. (자기가 구독하는 인물 X)
                if (normalPost.writerName.equals(myNickname)) {isMyFriend = 1;}
                for (String friendNameList : friendNameList) {
                    if (normalPost.writerName.equals(friendNameList.trim()))//만약 내가 구독하는 친구라면?
                        isMyFriend = 1;
                }
                if (isMyFriend==0) {label.hide(); return label;}
            }

            //여기서 필터 적용
            switch(filterStatus){
                case 0:
                    label.show();   //그냥 표시.
                    return label;
                case 1://친구+나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;} //내꺼면 show하고 리턴.
                    for (String friendNameList : friendNameList) {
                        if (normalPost.writerName.equals(friendNameList.trim())) {label.show(); return label;}  //친구꺼면 show하고 리턴.
                    }
                    label.hide();   //내것도, 친구것도 아니라면 hide
                    return label;
                case 2: //나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;}   //내꺼면 show하고 리턴.
                    label.hide();   //내것이 아니라면 hide
                    return label;
            }
        }
        else if (normalPost.getIcon().equals("7")){
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(pLatitude_Double,pLongitude_Double))
                    .setStyles(LabelStyle.from(R.drawable.posticon7).setAnchorPoint(0.5f, 0.5f)).setRank(1));
            if (normalPost.writerName.equals(myNickname))//case1: 내 게시글
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f));
            else    //case2: 내 게시글 아님.
                for (String friendNameList : friendNameList) { //case2-1: 그럼 친구게시글인가요?
                    if (normalPost.writerName.equals(friendNameList.trim()))
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f));
                }
            //뱃지 보이게
            for (Badge badge : badges) {
                badge.show();
            }
            label.getLabelId();
            label.setClickable(true);
            //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기
            int isMyFriend=0;
            //게시글의 공개 범위에 따라 hide여부 판독
            if (normalPost.getOpenRange().equals("3")) {  //비공개의 경우
                if (!normalPost.writerName.equals(myNickname)) {label.hide(); return label;}
            }
            else if (normalPost.getOpenRange().equals("2")) { //구현방법 변화: 자기를 구독하는 사람만 볼 수 있음. (자기가 구독하는 인물 X)
                if (normalPost.writerName.equals(myNickname)) {isMyFriend = 1;}
                for (String friendNameList : friendNameList) {
                    if (normalPost.writerName.equals(friendNameList.trim()))//만약 내가 구독하는 친구라면?
                        isMyFriend = 1;
                }
                if (isMyFriend==0) {label.hide(); return label;}
            }

            //여기서 필터 적용
            switch(filterStatus){
                case 0:
                    label.show();   //그냥 표시.
                    return label;
                case 1://친구+나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;} //내꺼면 show하고 리턴.
                    for (String friendNameList : friendNameList) {
                        if (normalPost.writerName.equals(friendNameList.trim())) {label.show(); return label;}  //친구꺼면 show하고 리턴.
                    }
                    label.hide();   //내것도, 친구것도 아니라면 hide
                    return label;
                case 2: //나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;}   //내꺼면 show하고 리턴.
                    label.hide();   //내것이 아니라면 hide
                    return label;
            }
        }
        else if (normalPost.getIcon().equals("8")){
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(pLatitude_Double,pLongitude_Double))
                    .setStyles(LabelStyle.from(R.drawable.posticon8).setAnchorPoint(0.5f, 0.5f)).setRank(1));
            if (normalPost.writerName.equals(myNickname))//case1: 내 게시글
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f));
            else    //case2: 내 게시글 아님.
                for (String friendNameList : friendNameList) { //case2-1: 그럼 친구게시글인가요?
                    if (normalPost.writerName.equals(friendNameList.trim()))
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f));
                }
            //뱃지 보이게
            for (Badge badge : badges) {
                badge.show();
            }
            label.getLabelId();
            label.setClickable(true);
            //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기
            int isMyFriend=0;
            //게시글의 공개 범위에 따라 hide여부 판독
            if (normalPost.getOpenRange().equals("3")) {  //비공개의 경우
                if (!normalPost.writerName.equals(myNickname)) {label.hide(); return label;}
            }
            else if (normalPost.getOpenRange().equals("2")) { //구현방법 변화: 자기를 구독하는 사람만 볼 수 있음. (자기가 구독하는 인물 X)
                if (normalPost.writerName.equals(myNickname)) {isMyFriend = 1;}
                for (String friendNameList : friendNameList) {
                    if (normalPost.writerName.equals(friendNameList.trim()))//만약 내가 구독하는 친구라면?
                        isMyFriend = 1;
                }
                if (isMyFriend==0) {label.hide(); return label;}
            }

            //여기서 필터 적용
            switch(filterStatus){
                case 0:
                    label.show();   //그냥 표시.
                    return label;
                case 1://친구+나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;} //내꺼면 show하고 리턴.
                    for (String friendNameList : friendNameList) {
                        if (normalPost.writerName.equals(friendNameList.trim())) {label.show(); return label;}  //친구꺼면 show하고 리턴.
                    }
                    label.hide();   //내것도, 친구것도 아니라면 hide
                    return label;
                case 2: //나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;}   //내꺼면 show하고 리턴.
                    label.hide();   //내것이 아니라면 hide
                    return label;
            }
        }
        else if (normalPost.getIcon().equals("9")){
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(pLatitude_Double,pLongitude_Double))
                    .setStyles(LabelStyle.from(R.drawable.posticon9).setAnchorPoint(0.5f, 0.5f)).setRank(1));
            if (normalPost.writerName.equals(myNickname))//case1: 내 게시글
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f));
            else    //case2: 내 게시글 아님.
                for (String friendNameList : friendNameList) { //case2-1: 그럼 친구게시글인가요?
                    if (normalPost.writerName.equals(friendNameList.trim()))
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f));
                }
            //뱃지 보이게
            for (Badge badge : badges) {
                badge.show();
            }
            label.getLabelId();
            label.setClickable(true);
            //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기
            int isMyFriend=0;
            //게시글의 공개 범위에 따라 hide여부 판독
            if (normalPost.getOpenRange().equals("3")) {  //비공개의 경우
                if (!normalPost.writerName.equals(myNickname)) {label.hide(); return label;}
            }
            else if (normalPost.getOpenRange().equals("2")) { //구현방법 변화: 자기를 구독하는 사람만 볼 수 있음. (자기가 구독하는 인물 X)
                if (normalPost.writerName.equals(myNickname)) {isMyFriend = 1;}
                for (String friendNameList : friendNameList) {
                    if (normalPost.writerName.equals(friendNameList.trim()))//만약 내가 구독하는 친구라면?
                        isMyFriend = 1;
                }
                if (isMyFriend==0) {label.hide(); return label;}
            }

            //여기서 필터 적용
            switch(filterStatus){
                case 0:
                    label.show();   //그냥 표시.
                    return label;
                case 1://친구+나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;} //내꺼면 show하고 리턴.
                    for (String friendNameList : friendNameList) {
                        if (normalPost.writerName.equals(friendNameList.trim())) {label.show(); return label;}  //친구꺼면 show하고 리턴.
                    }
                    label.hide();   //내것도, 친구것도 아니라면 hide
                    return label;
                case 2: //나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;}   //내꺼면 show하고 리턴.
                    label.hide();   //내것이 아니라면 hide
                    return label;
            }
        }
        else if (normalPost.getIcon().equals("10")){
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(pLatitude_Double,pLongitude_Double))
                    .setStyles(LabelStyle.from(R.drawable.posticon10).setAnchorPoint(0.5f, 0.5f)).setRank(1));
            if (normalPost.writerName.equals(myNickname))//case1: 내 게시글
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f));
            else    //case2: 내 게시글 아님.
                for (String friendNameList : friendNameList) { //case2-1: 그럼 친구게시글인가요?
                    if (normalPost.writerName.equals(friendNameList.trim()))
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f));
                }
            //뱃지 보이게
            for (Badge badge : badges) {
                badge.show();
            }
            label.getLabelId();
            label.setClickable(true);
            //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기
            int isMyFriend=0;
            //게시글의 공개 범위에 따라 hide여부 판독
            if (normalPost.getOpenRange().equals("3")) {  //비공개의 경우
                if (!normalPost.writerName.equals(myNickname)) {label.hide(); return label;}
            }
            else if (normalPost.getOpenRange().equals("2")) { //구현방법 변화: 자기를 구독하는 사람만 볼 수 있음. (자기가 구독하는 인물 X)
                if (normalPost.writerName.equals(myNickname)) {isMyFriend = 1;}
                for (String friendNameList : friendNameList) {
                    if (normalPost.writerName.equals(friendNameList.trim()))//만약 내가 구독하는 친구라면?
                        isMyFriend = 1;
                }
                if (isMyFriend==0) {label.hide(); return label;}
            }

            //여기서 필터 적용
            switch(filterStatus){
                case 0:
                    label.show();   //그냥 표시.
                    return label;
                case 1://친구+나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;} //내꺼면 show하고 리턴.
                    for (String friendNameList : friendNameList) {
                        if (normalPost.writerName.equals(friendNameList.trim())) {label.show(); return label;}  //친구꺼면 show하고 리턴.
                    }
                    label.hide();   //내것도, 친구것도 아니라면 hide
                    return label;
                case 2: //나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;}   //내꺼면 show하고 리턴.
                    label.hide();   //내것이 아니라면 hide
                    return label;
            }
        }
        else if (normalPost.getIcon().equals("11")){
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(pLatitude_Double,pLongitude_Double))
                    .setStyles(LabelStyle.from(R.drawable.posticon11).setAnchorPoint(0.5f, 0.5f)).setRank(1));
            if (normalPost.writerName.equals(myNickname))//case1: 내 게시글
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f));
            else    //case2: 내 게시글 아님.
                for (String friendNameList : friendNameList) { //case2-1: 그럼 친구게시글인가요?
                    if (normalPost.writerName.equals(friendNameList.trim()))
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f));
                }
            //뱃지 보이게
            for (Badge badge : badges) {
                badge.show();
            }
            label.getLabelId();
            label.setClickable(true);
            //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기
            int isMyFriend=0;
            //게시글의 공개 범위에 따라 hide여부 판독
            if (normalPost.getOpenRange().equals("3")) {  //비공개의 경우
                if (!normalPost.writerName.equals(myNickname)) {label.hide(); return label;}
            }
            else if (normalPost.getOpenRange().equals("2")) { //구현방법 변화: 자기를 구독하는 사람만 볼 수 있음. (자기가 구독하는 인물 X)
                if (normalPost.writerName.equals(myNickname)) {isMyFriend = 1;}
                for (String friendNameList : friendNameList) {
                    if (normalPost.writerName.equals(friendNameList.trim()))//만약 내가 구독하는 친구라면?
                        isMyFriend = 1;
                }
                if (isMyFriend==0) {label.hide(); return label;}
            }

            //여기서 필터 적용
            switch(filterStatus){
                case 0:
                    label.show();   //그냥 표시.
                    return label;
                case 1://친구+나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;} //내꺼면 show하고 리턴.
                    for (String friendNameList : friendNameList) {
                        if (normalPost.writerName.equals(friendNameList.trim())) {label.show(); return label;}  //친구꺼면 show하고 리턴.
                    }
                    label.hide();   //내것도, 친구것도 아니라면 hide
                    return label;
                case 2: //나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;}   //내꺼면 show하고 리턴.
                    label.hide();   //내것이 아니라면 hide
                    return label;
            }
        }
        else if (normalPost.getIcon().equals("12")){
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(pLatitude_Double,pLongitude_Double))
                    .setStyles(LabelStyle.from(R.drawable.posticon12).setAnchorPoint(0.5f, 0.5f)).setRank(1));
            if (normalPost.writerName.equals(myNickname))//case1: 내 게시글
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f));
            else    //case2: 내 게시글 아님.
                for (String friendNameList : friendNameList) { //case2-1: 그럼 친구게시글인가요?
                    if (normalPost.writerName.equals(friendNameList.trim()))
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f));
                }
            //뱃지 보이게
            for (Badge badge : badges) {
                badge.show();
            }
            label.getLabelId();
            label.setClickable(true);
            //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기
            int isMyFriend=0;
            //게시글의 공개 범위에 따라 hide여부 판독
            if (normalPost.getOpenRange().equals("3")) {  //비공개의 경우
                if (!normalPost.writerName.equals(myNickname)) {label.hide(); return label;}
            }
            else if (normalPost.getOpenRange().equals("2")) { //구현방법 변화: 자기를 구독하는 사람만 볼 수 있음. (자기가 구독하는 인물 X)
                if (normalPost.writerName.equals(myNickname)) {isMyFriend = 1;}
                for (String friendNameList : friendNameList) {
                    if (normalPost.writerName.equals(friendNameList.trim()))//만약 내가 구독하는 친구라면?
                        isMyFriend = 1;
                }
                if (isMyFriend==0) {label.hide(); return label;}
            }

            //여기서 필터 적용
            switch(filterStatus){
                case 0:
                    label.show();   //그냥 표시.
                    return label;
                case 1://친구+나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;} //내꺼면 show하고 리턴.
                    for (String friendNameList : friendNameList) {
                        if (normalPost.writerName.equals(friendNameList.trim())) {label.show(); return label;}  //친구꺼면 show하고 리턴.
                    }
                    label.hide();   //내것도, 친구것도 아니라면 hide
                    return label;
                case 2: //나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;}   //내꺼면 show하고 리턴.
                    label.hide();   //내것이 아니라면 hide
                    return label;
            }
        }
        else if (normalPost.getIcon().equals("13")){
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(pLatitude_Double,pLongitude_Double))
                    .setStyles(LabelStyle.from(R.drawable.posticon13).setAnchorPoint(0.5f, 0.5f)).setRank(1));
            if (normalPost.writerName.equals(myNickname))//case1: 내 게시글
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f));
            else    //case2: 내 게시글 아님.
                for (String friendNameList : friendNameList) { //case2-1: 그럼 친구게시글인가요?
                    if (normalPost.writerName.equals(friendNameList.trim()))
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f));
                }
            //뱃지 보이게
            for (Badge badge : badges) {
                badge.show();
            }
            label.getLabelId();
            label.setClickable(true);
            //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기
            int isMyFriend=0;
            //게시글의 공개 범위에 따라 hide여부 판독
            if (normalPost.getOpenRange().equals("3")) {  //비공개의 경우
                if (!normalPost.writerName.equals(myNickname)) {label.hide(); return label;}
            }
            else if (normalPost.getOpenRange().equals("2")) { //구현방법 변화: 자기를 구독하는 사람만 볼 수 있음. (자기가 구독하는 인물 X)
                if (normalPost.writerName.equals(myNickname)) {isMyFriend = 1;}
                for (String friendNameList : friendNameList) {
                    if (normalPost.writerName.equals(friendNameList.trim()))//만약 내가 구독하는 친구라면?
                        isMyFriend = 1;
                }
                if (isMyFriend==0) {label.hide(); return label;}
            }

            //여기서 필터 적용
            switch(filterStatus){
                case 0:
                    label.show();   //그냥 표시.
                    return label;
                case 1://친구+나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;} //내꺼면 show하고 리턴.
                    for (String friendNameList : friendNameList) {
                        if (normalPost.writerName.equals(friendNameList.trim())) {label.show(); return label;}  //친구꺼면 show하고 리턴.
                    }
                    label.hide();   //내것도, 친구것도 아니라면 hide
                    return label;
                case 2: //나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;}   //내꺼면 show하고 리턴.
                    label.hide();   //내것이 아니라면 hide
                    return label;
            }
        }
        else if (normalPost.getIcon().equals("14")){
            label = labelLayer.addLabel(LabelOptions.from(label_ID, LatLng.from(pLatitude_Double,pLongitude_Double))
                    .setStyles(LabelStyle.from(R.drawable.posticon14).setAnchorPoint(0.5f, 0.5f)).setRank(1));
            if (normalPost.writerName.equals(myNickname))//case1: 내 게시글
                badges = label.addBadge(BadgeOptions.with(R.drawable.badge_mine).setOffset(0.0f,0.9f));
            else    //case2: 내 게시글 아님.
                for (String friendNameList : friendNameList) { //case2-1: 그럼 친구게시글인가요?
                    if (normalPost.writerName.equals(friendNameList.trim()))
                        badges = label.addBadge(BadgeOptions.with(R.drawable.badge_friend).setOffset(0.0f,0.9f));
                }
            //뱃지 보이게
            for (Badge badge : badges) {
                badge.show();
            }
            label.getLabelId();
            label.setClickable(true);
            //이제 할 작업: 클릭이벤트 달기. 클릭 시, label에 지정된 pid에 해당하는 내용을 viewer에 띄우기
            int isMyFriend=0;
            //게시글의 공개 범위에 따라 hide여부 판독
            if (normalPost.getOpenRange().equals("3")) {  //비공개의 경우
                if (!normalPost.writerName.equals(myNickname)) {label.hide(); return label;}
            }
            else if (normalPost.getOpenRange().equals("2")) { //구현방법 변화: 자기를 구독하는 사람만 볼 수 있음. (자기가 구독하는 인물 X)
                if (normalPost.writerName.equals(myNickname)) {isMyFriend = 1;}
                for (String friendNameList : friendNameList) {
                    if (normalPost.writerName.equals(friendNameList.trim()))//만약 내가 구독하는 친구라면?
                        isMyFriend = 1;
                }
                if (isMyFriend==0) {label.hide(); return label;}
            }

            //여기서 필터 적용
            switch(filterStatus){
                case 0:
                    label.show();   //그냥 표시.
                    return label;
                case 1://친구+나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;} //내꺼면 show하고 리턴.
                    for (String friendNameList : friendNameList) {
                        if (normalPost.writerName.equals(friendNameList.trim())) {label.show(); return label;}  //친구꺼면 show하고 리턴.
                    }
                    label.hide();   //내것도, 친구것도 아니라면 hide
                    return label;
                case 2: //나만 보기
                    if (normalPost.writerName.equals(myNickname)) {label.show(); return label;}   //내꺼면 show하고 리턴.
                    label.hide();   //내것이 아니라면 hide
                    return label;
            }
        }





        //writer의 id = 친구목록에 있다면 friend 뱃지 달기   <- 친구 목록 구현 성공시 else if로 달 것
        //게시글의 attachImageURL이 null이 아니면 image 뱃지 달기


        return null;
    }

    void showPostViewer(ReceiveNormalPost normalPost){
        //지도 상의 버튼을 누르면 해당 normalPost의 정보와 myNickname을 들고 postViewerFragment를 add한다.

        //번들에 짐 싸기
        Bundle postBundle = new Bundle();

        //userID
        postBundle.putString("myNickname",myNickname);
        postBundle.putSerializable("normalPost",normalPost);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PostViewerFragment postViewerFragment = new PostViewerFragment();

        postViewerFragment.setArguments(postBundle);    //내용물을 viewer로 보내기

        fragmentTransaction.add(R.id.frame_layout_post_viewer, postViewerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    void createALLlabels(){
        labelList.clear();  //현재 라벨 리스트 초기화
        for(int i=0; i<postList.size();i++){
            labelList.add(createLabel(postList.get(i),postList.get(i).pid,filterStatus));
        }

        for(int i=0; i<postList.size();i++){
            labelList.add(createLabel(postList.get(i),postList.get(i).pid,filterStatus));
        }
    }


}