package com.example.choijh.subwayapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.IOException;
import java.io.InputStream;

public class ScalableSVGImageView extends View {
    //    private Bitmap mBitmap;
    private SVG svg = null;
    private ScaleGestureDetector mScaleDetector = null;
    private float mScaleFactor = 1.0f;
    private PictureDrawable drawable;


    public ScalableSVGImageView(Context context) {
        super(context);
        init();
    }

    public ScalableSVGImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScalableSVGImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mScaleFactor *= detector.getScaleFactor();
                invalidate();
                return true;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        if (svg != null && svg.getDocumentWidth() != -1) {
            canvas.scale(mScaleFactor, mScaleFactor);
            drawable.draw(canvas);
//            (canvas, new RectF(0, 0, 100, 200));
        }
        canvas.restore();

    }

    public boolean internalSetImageAsset(String filename) {
        try {
            InputStream is = getContext().getAssets().open(filename);
            new LoadURITask().execute(is);
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class LoadURITask extends AsyncTask<InputStream, Integer, SVG> {
        protected SVG doInBackground(InputStream... is) {
            try {
                return SVG.getFromInputStream(is[0]);
            } catch (SVGParseException e) {
                Log.e("SVGImageView", "Parse error loading URI: " + e.getMessage());
            } finally {
                try {
                    is[0].close();
                } catch (IOException e) { /* do nothing */ }
            }
            return null;
        }

        protected void onPostExecute(SVG svg) {
            ScalableSVGImageView.this.svg = svg;
            drawable = new PictureDrawable(svg.renderToPicture());
//            if (svg.getDocumentWidth() != -1) {
//                Bitmap mBitmap = Bitmap.createBitmap((int) Math.ceil(svg.getDocumentWidth()),
//                        (int) Math.ceil(svg.getDocumentHeight()),
//                        Bitmap.Config.ARGB_8888);
//                Canvas bmcanvas = new Canvas(mBitmap);
//
//                // Clear background to white
//                bmcanvas.drawRGB(255, 255, 255);
//
//                // Render our document onto our canvas
//                svg.renderToCanvas(bmcanvas);
//            }

            ScalableSVGImageView.this.invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        return true;
    }
}
