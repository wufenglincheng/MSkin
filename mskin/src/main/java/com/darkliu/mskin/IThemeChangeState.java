package com.darkliu.mskin;

/**
 * 主题改变的回调
 * 
 * @author liuting
 */
public interface IThemeChangeState {
  /**
   * 主题开始切换
   */
  void onStart();

  /**
   * 主题切换完成
   */
  void onChangeFinish();

  /**
   * 主题切换失败
   */
  void onError();
}
