//realtime-database에 User 정보를 넣기위해 정의하는 클래스

package com.example.broaf;

import java.util.List;
public class User {
    private String Email;
    private String Nickname;
    private String Pw;

    private List<String> friendlist;

    public User() {
    }

    public User(String email, String nickname, String pw) {
        Email = email;
        Nickname = nickname;
        Pw = pw;
    }
    public User(String email, String nickname, String pw, List<String> user_friendlist) {
        Email = email;
        Nickname = nickname;
        Pw = pw;
        this.friendlist = user_friendlist;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getPw() {
        return Pw;
    }

    public void setPw(String pw) {
        Pw = pw;
    }

    public List<String> getUser_friendlist() {
        return friendlist;
    }

    public void setUser_friendlist(List<String> user_friendlist) {
        this.friendlist = user_friendlist;
    }
}