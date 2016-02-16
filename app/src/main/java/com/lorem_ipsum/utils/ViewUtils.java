package com.lorem_ipsum.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

/**
 * Created by Torin on 11/10/14.
 */
public class ViewUtils {

    //-----------------------------------------------------------------------------
    //- Convert dp and px - hoangminh - 3:19 PM - 2/1/16

    public static int dp2px(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static float px2dp(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    //-----------------------------------------------------------------------------
    //- Extend view touch area - hoangminh - 4:28 PM - 2/1/16

    public static void extendTouchArea(View view, int top, int right, int bottom, int left) {
        Rect delegateArea = new Rect();
        view.getHitRect(delegateArea);

        delegateArea.top += top;
        delegateArea.right += right;
        delegateArea.bottom += bottom;
        delegateArea.right += left;

        TouchDelegate touchDelegate = new TouchDelegate(delegateArea, view);
        if (View.class.isInstance(view.getParent())) {
            ((View) view.getParent()).setTouchDelegate(touchDelegate);
        }
    }

    //-----------------------------------------------------------------------------
    //- Change number picker text color - hoangminh - 4:29 PM - 2/1/16

    // Use theme instead of this method
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (Exception e) {
                    Log.w("NumberPickerTextColor", e);
                }
            }
        }
        return false;
    }

    //-----------------------------------------------------------------------------
    //- Set view with rounded corners - hoangminh - 4:22 PM - 2/1/16

    public static void setRoundedCorners(View view, float radius, int backgroundColor) {
        //Calculate the radius
        if (radius <= 0) {
            float width = view.getWidth();
            float height = view.getHeight();
            radius = Math.min(width, height) / 2.0f;
            if (radius <= 0)
                return;
        }

        //Construct a rounded corner shape with that color
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(radius);
        shape.setColor(backgroundColor);

        if (Build.VERSION.SDK_INT > 16) {
            view.setBackground(shape);
        } else {
            view.setBackgroundDrawable(shape);
        }
    }

    public static void setRoundedCorners(View view, float radius, int borderWidth, int backgroundColor, int borderColor) {
        //Calculate the radius
        if (radius <= 0) {
            float width = view.getWidth();
            float height = view.getHeight();
            radius = Math.min(width, height) / 2.0f;
            if (radius <= 0)
                return;
        }

        //Construct a rounded corner shape with that color
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(radius);
        shape.setColor(backgroundColor);
        shape.setStroke(borderWidth, borderColor);

        if (Build.VERSION.SDK_INT > 16) {
            view.setBackground(shape);
        } else {
            view.setBackgroundDrawable(shape);
        }
    }

    public static void setRoundedCornersWithRadius(View view, float radius, int backgroundColor) {

        //Try to obtain the current background color of the view
        //Note: only works with views with simple color background
        if (backgroundColor <= 0) {
            Drawable currentBackground = view.getBackground();
            if (currentBackground instanceof ColorDrawable) {
                backgroundColor = ((ColorDrawable) currentBackground).getColor();
            }
        }

        //Construct a rounded corner shape with that color
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(radius);
        shape.setColor(backgroundColor);

        if (Build.VERSION.SDK_INT > 16) {
            view.setBackground(shape);
        } else {
            view.setBackgroundDrawable(shape);
        }
    }


}
