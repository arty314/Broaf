//<<<<<<< HEAD
package com.example.broaf;

import java.util.List;

public class User {
    private String Email;
    private String Nickname;
    private String Pw;

    private List<String> friendlist;

    private List<String> sendRequestList;

    private List<String> receiveRequestList;

    public User(String email, String nickname, String pw, List<String> user_friendlist, List<String> user_sendRequestList, List<String> user_receiveRequestList) {
        Email = email;
        Nickname = nickname;
        Pw = pw;
        this.friendlist = user_friendlist;
        this.sendRequestList = user_sendRequestList;
        this.receiveRequestList = user_receiveRequestList;
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

    public List<String> getUser_sendRequestList() {
        return sendRequestList;
    }

    public void setUser_sendRequestList(List<String> user_sendRequestList) {
        this.sendRequestList = user_sendRequestList;
    }

    public List<String> getUser_receiveRequestList() {
        return receiveRequestList;
    }

    public void setUser_receiveRequestList(List<String> user_receiveRequestList) {
        this.receiveRequestList = user_receiveRequestList;
    }

}