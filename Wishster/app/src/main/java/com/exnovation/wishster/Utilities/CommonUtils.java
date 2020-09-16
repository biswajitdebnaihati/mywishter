package com.exnovation.wishster.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by einfo on 21/03/18.
 */

public class CommonUtils {

    private static final long animationDuration = 300;

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static void scaleImageFileAndReduceSize(File file) {
        int area = 60000;  //prev vlue 60000
        try {
            Bitmap b = BitmapFactory.decodeFile(file.getAbsolutePath());
            int w = b.getWidth(), h = b.getHeight();
            if (w * h <= area + 10)
                return;
            double ratio = Math.sqrt(w * h / area);
            //LogUtils.e(TAG, w +"-"+ h);
            //LogUtils.e(TAG, (int)(w/ratio) +"-"+ (int)(h/ratio));
            Bitmap out = Bitmap.createScaledBitmap(b, (int) (w / ratio), (int) (h / ratio), true);
            FileOutputStream fOut;
            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.JPEG, 60, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {
        }
    }

    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1, 0, 1);
        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? LinearLayout.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(scaleAnimation);
        set.addAnimation(a);
        a.setDuration(animationDuration);
        set.setDuration(animationDuration);

        v.startAnimation(set);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1, 1, 0);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(scaleAnimation);
        set.addAnimation(a);
        set.setDuration(animationDuration);

        v.startAnimation(set);
    }

    public static String getDoubleVal(double value) {

        //return new DecimalFormat("##.##").format(value);
        //return String.valueOf(value);
        return String.format("%.2f", value);

    }

    public static void closeKey(Context context, View view) {

        if (view != null) {
            try {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
