package com.darkliu.mskin;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

class ViewThemeProcessor implements View.OnAttachStateChangeListener {

    @NonNull
    private final View mView;

    private final List<ISkinAttrApply> mAttrApplyList = new ArrayList<>();

    ViewThemeProcessor(@NonNull View view) {
        mView = view;
        mView.addOnAttachStateChangeListener(this);
    }

    void addAll(List<ISkinAttrApply> list) {
        mAttrApplyList.addAll(list);
    }

    void add(ISkinAttrApply apply) {
        mAttrApplyList.add(apply);
    }

    void apply(boolean isFormUser, List<IAttrAnim> attrAnimationList) {
        for (ISkinAttrApply apply : mAttrApplyList) {
            apply.apply(mView, isFormUser, attrAnimationList);
        }
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        apply(false, null);
    }

    @Override
    public void onViewDetachedFromWindow(View v) {

    }
}
