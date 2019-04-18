package com.example.choijh.subwayapp;

import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;

import com.caverock.androidsvg.SVG;
import com.github.chrisbanes.photoview.PhotoView;

import androidx.appcompat.app.AppCompatActivity;

public class Subway_fullScreen extends AppCompatActivity {
    PhotoView f_photoView;
    SVG f_svg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_full_screen);

        try {
            f_svg = SVG.getFromAsset(this.getAssets(), "svg_seoul_subway_linemap.svg");
            PictureDrawable drawable = new PictureDrawable(f_svg.renderToPicture());


            f_photoView = (PhotoView) findViewById(R.id.sub_map_fullScreen);

            f_photoView.setImageDrawable(drawable);
            f_photoView.setMinimumScale(0.5f);
            f_photoView.setMaximumScale(30.0f);

            f_photoView.post(new Runnable() {
                @Override
                public void run() {
                    float x = 2862f;
                    float y = 2790f;

                    float focalX = x * f_photoView.getRight() / f_svg.getDocumentWidth();
                    float focalY = y * f_photoView.getBottom() / f_svg.getDocumentHeight();
                    f_photoView.setScale(5.0f, focalX, focalY, false);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
