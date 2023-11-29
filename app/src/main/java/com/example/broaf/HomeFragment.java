package com.example.broaf;

import static android.graphics.Bitmap.createBitmap;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.kakao.vectormap.MapLifeCycleCallback;
import com.kakao.vectormap.MapView;
import com.kakao.vectormap.label.Label;
import com.kakao.vectormap.label.LabelLayer;
import com.kakao.vectormap.label.LabelLayerOptions;
import com.kakao.vectormap.label.LabelOptions;
import com.kakao.vectormap.label.LabelStyle;
import com.kakao.vectormap.label.LabelStyles;
import com.kakao.vectormap.label.OrderingType;
import com.kakao.vectormap.label.TrackingManager;
import com.kakao.vectormap.shape.Polygon;
import com.kakao.vectormap.shape.ShapeAnimator;



//홈 frag 구현 방향
//1. 검색창이 켜져있다 => 검색 창은 edittext.
//      edittext에서 엔터를 입력하거나, imagebutton인 검색 버튼을 누르면 search frag로 입력된 문자열 전달
//
//2. 지도가 켜져있다 => mapview
//      gps로 현재 위치 받아와서 표시
//      firebase에서 열람 가능 기간 내의 인근 게시글 목록 따옴 (후보 게시글 목록: 일단 엄청 적은 양의 data만 불러옴)
//      login acti에서 받아온 계정 정보와 후보 게시글의 id 정보를 이용하여 게시글 필터링 수행
//      후보 게시글 목록 중에 열람 가능한 게시글만 visible
//                  //Idea: 어케 잘 캐싱하면 게시글 재로드 속도 향상?
//      열람 가능 여부 테스트 하고 나면 어케저게 잘 게시글 label 별로 icon 변경 및 badge 달아주기
//                  //Idea: 이 또한 어케 잘 캐싱하면 게시글 재로드 속도 향상? 친구 프사, 미리보기 등

public class HomeFragment extends Fragment {

    int tempUID = 10001;

    ImageButton btn_search;
    EditText input_text_search;   //검색 내용(edittext)을 끌어오기 위해서.
    String searchKeyword_input;
    //Bundle search_bundle;       //끌어온 검색 내용(input_text_search)를 search frag로 내보내기 위하여
    Button viewpost_map_other;

    ImageButton btn_follow_my_pos, btn_new;
    boolean isTrackingMode = false; //trackingmode의 온오프 여부를 기록하는 변수. btn_follow_my_pos 버튼을 누를 시 토글

    TextView txtResult;
    double longitude, latitude, altitude;   //현재 GPS 위치


    //지도에 현재 마커 표시
    private KakaoMap kakaoMap;
    private ShapeAnimator shapeAnimator;
    private LabelLayer labelLayer;
    private int duration = 500;
    private Label centerLabel;
    private Polygon animationPolygon;

    ///////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //표시할 xml layout 선택
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        FloatingActionButton fab = getActivity().findViewById(R.id.navi_to_home);
        fab.setImageResource(R.drawable.re_writepost);


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


        //viewpost 버튼을 누르면 postviewer frag를 add
        viewpost_map_other = (Button) view.findViewById(R.id.viewpost_map_other);
        viewpost_map_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //포스트 뷰어 add (아직 구현 덜함)
                Fragment newPostViewerFragment = new PostViewerFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.frame_layout_post_viewer, newPostViewerFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        //여기까지 포스트 뷰어 add




//        //tracking mode 온오프
//        btn_follow_my_pos = (ImageButton)view.findViewById(R.id.btn_follow_my_pos);
//        btn_follow_my_pos.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TrackingManager trackingManager = kakaoMap.getTrackingManager();
//                if(isTrackingMode==false){
//                    //지도의 중심이 현재 위치 마커를 추적한다.
//                    //false에서 버튼 클릭 시 true
//                    Toast.makeText(getActivity(), "tracking mode ON", Toast.LENGTH_SHORT).show();
//                    isTrackingMode=true;
//                }
//                else{
//                    //추적을 멈춘다.
//                    Toast.makeText(getActivity(), "tracking mode OFF", Toast.LENGTH_SHORT).show();
//                    isTrackingMode=false;
//                }
//            }
//        });

        //GPS 불러오기
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
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        altitude = location.getAltitude();

                        txtResult.setText("위치정보 : " + provider + "\n" +
                                "위도 : " + longitude + "\n" +
                                "경도 : " + latitude + "\n" +
                                "고도  : " + altitude);

                    }

                    // 위치정보를 원하는 시간, 거리마다 갱신해준다.
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


        //여기부터 카카오맵
        MapView mapView = view.findViewById(R.id.map_view);
        mapView.start(new MapLifeCycleCallback() {
            @Override
            public void onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
            }

            @Override
            public void onMapError(Exception error) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
            }
        }, new KakaoMapReadyCallback() {
            
            //현재 위치 좌표값 설정
            @Override
            public LatLng getPosition() {
                return LatLng.from(37.394660,127.111182);
            }

            @Override
            public void onMapReady(KakaoMap map) {
                //지도 실행시 수행
                //현재 위치 핀 생성
                LatLng pos = kakaoMap.getCameraPosition().getPosition();

                centerLabel = labelLayer.addLabel(LabelOptions.from("dotLabel", pos)
                        .setStyles(LabelStyle.from(R.drawable.icon_currentpos).setAnchorPoint(0.5f, 0.5f))
                        .setRank(5));
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
            double altitude = location.getAltitude(); // 고도
            txtResult.setText("위치정보 : " + provider + "\n" + "위도 : " + longitude + "\n" + "경도 : " + latitude + "\n" + "고도 : " + altitude);
        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };


    //label level 설정
//    private Bitmap getRankBitmap(float rank, int bgResId) {
//        View rankView = LayoutInflater.from(getBaseContext()).inflate(R.layout.layout_rank_label, null);
//        rankView.setBackgroundResource(bgResId);
//        ((TextView) rankView.findViewById(R.id.tv_rank)).setText("Rank\n" + (int)rank);
//
//        int width = convertDpToPixels(convertPixelToDp(113 * 2));
//        int height = convertDpToPixels(convertPixelToDp(152 * 2));
//        return createBitmap(rankView, width, height);
//    }
    //label level 끝


//    //trackingmode ON/OFF
//    public void setTrackingMode(boolean isTrackingMode) {
//        TrackingManager trackingManager = kakaoMap.getTrackingManager();
//        if(isTrackingMode==true)
//            if (centerLabel != null)   //centerlabel이 없다면 추적 안하게
//                trackingManager.startTracking(centerLabel);
//             else
//                Toast.makeText(getActivity(), "내 위치 핀이 없습니다.", Toast.LENGTH_SHORT).show();
//        else
//            trackingManager.stopTracking();
//    }
//    //


}



