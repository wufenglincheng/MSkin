package com.example.mskindemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import java.lang.reflect.Field;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by liuting on 18/8/2.
 */
public class FeedListView extends RecyclerView {
    public FeedListView(Context context) {
        super(context);
    }

    public FeedListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FeedListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        ViewGroup.class.cast(getParent()).requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    private LinearLayoutManager.SavedState scrollState;

    public void setPositionWithOffset(int position, int offset) {
        if (scrollState != null) {
            try {
                Field mAnchorPosition = scrollState.getClass().getField("mAnchorPosition");
                Field mAnchorOffset = scrollState.getClass().getField("mAnchorOffset");
                mAnchorPosition.setAccessible(true);
                mAnchorOffset.setAccessible(true);
                mAnchorPosition.set(scrollState, position);
                mAnchorOffset.set(scrollState, offset);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            getLayoutManager().onRestoreInstanceState(scrollState);
        } else {
            scrollToPosition(position);
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (layout instanceof LinearLayoutManager) {
            scrollState = (LinearLayoutManager.SavedState) layout.onSaveInstanceState();
        }
    }
}
