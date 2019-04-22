package com.example.choijh.subwayapp;

//listview 아이템 데이터 클래스 생성
public class ListViewBtnItem_lost {
    private String textStr ;
    private String textStr2 ;

    public void setText(String text) {
        textStr = text ;
    }
    public void setText2(String text) {
        textStr2 = text ;
    }

    public String getText() {
        return this.textStr ;
    }
    public String getText2() {
        return this.textStr2 ;
    }
}