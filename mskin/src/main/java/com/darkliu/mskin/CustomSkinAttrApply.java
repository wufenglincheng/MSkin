package com.darkliu.mskin;

import android.content.Context;
import android.view.View;

import java.util.List;

import androidx.core.util.Consumer;

class CustomSkinAttrApply<T> implements ISkinAttrApply {
    private final int mRes;
    private final Function<T> mTFunction;
    private final Consumer<T> mConsumer;

    CustomSkinAttrApply(int res, Function<T> function, Consumer<T> consumer) {
        mRes = res;
        mTFunction = function;
        mConsumer = consumer;
    }

    @Override
    public void apply(View view, boolean isFormUser, List<IAttrAnim> attrAnimationList) {
        mConsumer.accept(mTFunction.apply(view.getContext(), mRes));
    }

    public void apply(View view) {
        apply(view, false, null);
    }

    interface Function<T> {
        T apply(Context context, int res);
    }
}
