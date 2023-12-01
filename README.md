# Broaf
소프트웨어 공학 Broaf 깃허브



//오전 1:42 2023-11-20 SEjiji

com.example.broaf 폴더 안에 있는 Activity/Fragment.java 파일이랑 layout 안에 있는 activity_/fragment_.xml은 자유롭게 수정하시면 됩니다
지우거나 이름 바꾸셔도 ㄱㅊ
근데 나중에 하단바 만들고 나선 NoticeFragment.java랑 MyinfoFragment.java는 삭제 혹은 이름 변경을 가급적 삼가주세요
이유: 하단바 깨짐
그러나 꼭 필요하면 저한테 말하고 나서 MainActivity.java에서 메뉴바 메소드에서 연결 프래그먼트 바꾸심 됨



**
일단 하단바 노출 여부랑 페이지에 머무는 시간에 따라 임의로 Activity랑 Fragment를 분류해뒀는데 이 또한 적당히 수정 부탁

-Activity: 하단바를 사용할 수 없음. 유저가 페이지에 머무는 시간이 김. 로그인, 포스트에디터(게시글 작성 및 수정 등)
-fragment: 하단바를 사용할 수 있음*. MainActivity 안에서 fragment만 이래저래 전환되거나 열리고 닫힐 것. 유저가 짧게 머뭄.
*(하단바는 activity_main에 제작될 것임)

-MainActivity
    : 로그인, 포스트에디터에 사용될 frag를 제외한 주요 fragments들은 여기서 켜고 꺼지고 바뀌고 함(검색frag, 홈frag(지도), 알림frag, 내정보frag)
    기본적으론 지도가 있는 홈 frag가 호출되어있음

-FriendListActivity, FriendListAdapter, activity_friend_list, friend_list_item
    : 로그인된 사용자의 친구목록을 호출하고 관리할 수 있는 기능을 구현하기 위한 Class 및 xml. 
        검색창에 친구로 추가하거나 삭제하고자하는 사용자의 닉네임을 타이핑하고 하단의 버튼을 터치하여 기능을 수행한다. 

-User, DBUser
    : 사용자 정의와 데이터베이스 상호작용에 관련된 Class.

-이후 추가할 fragments (임의)
    (1) postviewer fragment 
        :홈frag의 mapview에서 label 클릭시 add, 검색 결과에서 누르면 add, 
        내 게시글 모아보기에서 누르면 add, 신고함에서 누르면 add(신고글버전.xml로 호출) 등등..

    그 외 내게시글 모아보기 등등




이렇게 하는거 맞는건가
솔직히 잘 모르겠네요
일단 임의로 잡아둔 그림이라 다 갈아엎어야 할지도
