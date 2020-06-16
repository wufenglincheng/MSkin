package com.darkliu.mskin;

/**
 * @author liuting
 */
public interface IThemeUpdate {
  /**
   * 主题更新
   * 
   * @param isFromUser 是否来自用户主动触发
   */
  void onThemeUpdate(boolean isFromUser);
}
