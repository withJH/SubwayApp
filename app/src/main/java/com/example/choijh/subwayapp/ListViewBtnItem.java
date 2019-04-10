package com.example.choijh.subwayapp;

import android.graphics.drawable.Drawable;

//listview 아이템 데이터 클래스 생성
public class ListViewBtnItem {
    private Drawable iconDrawable ;
    private String textStr ;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setText(String text) {
        textStr = text ;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getText() {
        return this.textStr ;
    }
}