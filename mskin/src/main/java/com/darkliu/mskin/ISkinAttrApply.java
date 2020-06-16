package com.darkliu.mskin;

import android.view.View;

import java.util.List;

/**
 * @author liuting
 */
public interface ISkinAttrApply {
    /**
     * 应用新的属性
     *
     * @param view              目标View
     * @param isFormUser        是否来自用户主动触发
     * @param attrAnimationList 需要动画的属性
     */
    void apply(View view, boolean isFormUser, List<IAttrAnim> attrAnimationList);
}
