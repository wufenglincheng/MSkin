package com.darkliu.mskin.anim;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.darkliu.mskin.IAttrAnim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AnimBackgroundDrawable extends Drawable implements IAttrAnim {

    @Nullable
    private Drawable src;
    private Drawable dst;
    private float mCurrentValue;

    public AnimBackgroundDrawable(@Nullable Drawable src, @NonNull Drawable dst) {
        this.src = src;
        this.dst = dst;
    }

    public void setCurrentValue(float value) {
        if (value >= 1f) {
            mCurrentValue = 1f;
            finishAnim();
            invalidateSelf();
            return;
        }
        mCurrentValue = value;
        invalidateSelf();
        Log.e("testliuting", hashCode() + "---" + mCurrentValue + "");
    }

    private void finishAnim() {
        src = null;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (src != null) {
            src.setAlpha((int) ((1f - mCurrentValue) * 255));
            src.draw(canvas);
        }
        dst.setAlpha((int) ((mCurrentValue) * 255));
        dst.draw(canvas);
    }

    @Override
    public void setAlpha(int alpha) {
        if (mCurrentValue >= 1) {
            // dst.setAlpha(alpha);
            Log.e("testliuting", "alpha+" + mCurrentValue + "");
        }
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        dst.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return dst.getOpacity();
    }

    @Override
    protected boolean onStateChange(int[] state) {
        return dst.setState(state);
    }

    @Override
    public boolean setState(@NonNull int[] stateSet) {
        return dst.setState(stateSet);
    }

    @Override
    public boolean isStateful() {
        return dst.isStateful();
    }

    @Override
    public int getIntrinsicHeight() {
        return dst.getIntrinsicHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        return dst.getIntrinsicWidth();
    }

    @NonNull
    @Override
    public Drawable getCurrent() {
        return dst.getCurrent();
    }

    @NonNull
    @Override
    public int[] getState() {
        return dst.getState();
    }

    @NonNull
    @Override
    public Rect getDirtyBounds() {
        return dst.getDirtyBounds();
    }

    @Override
    public boolean getPadding(@NonNull Rect padding) {
        return dst.getPadding(padding);
    }

    @Override
    public void getOutline(@NonNull Outline outline) {
        dst.getOutline(outline);
    }

    @Override
    public void setDither(boolean dither) {
        dst.setDither(dither);
    }

    @Override
    public void setTintList(@Nullable ColorStateList tint) {
        dst.setTintList(tint);
    }

    @Override
    public void setTintMode(@NonNull PorterDuff.Mode tintMode) {
        dst.setTintMode(tintMode);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        dst.setBounds(bounds);
    }

    @Override
    public void setAutoMirrored(boolean mirrored) {
        dst.setAutoMirrored(mirrored);
    }

    @Override
    public boolean isAutoMirrored() {
        return dst.isAutoMirrored();
    }

    @Override
    public void jumpToCurrentState() {
        dst.jumpToCurrentState();
    }

    @Override
    public void setHotspot(float x, float y) {
        dst.setHotspot(x, y);
    }

    @Override
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        dst.setHotspotBounds(left, top, right, bottom);
    }

    @Override
    public void getHotspotBounds(@NonNull Rect outRect) {
        dst.getHotspotBounds(outRect);
    }

    @Override
    protected boolean onLevelChange(int level) {
        return dst.setLevel(level);
    }

    @Override
    public boolean onLayoutDirectionChanged(int layoutDirection) {
        return dst.onLayoutDirectionChanged(layoutDirection);
    }

    @Override
    public int getMinimumWidth() {
        return dst.getMinimumWidth();
    }

    @Override
    public int getMinimumHeight() {
        return dst.getMinimumHeight();
    }

    @Override
    public boolean setVisible(boolean visible, boolean restart) {
        return dst.setVisible(visible, restart);
    }

    @Override
    public void onValueChange(float value) {
        setCurrentValue(value);
    }

    public void changeDstDrawable(Drawable drawable) {
        src = dst;
        dst = drawable;
        mCurrentValue = 0f;
    }
}
