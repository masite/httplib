package com.component.httplib.utils;

import com.component.httplib.BaseUrlEnum;
import com.component.httplib.HttpSdk;

/**
 * Description: mock模式工具类
 * FIXME:
 */
public class MockUtils {

  /**
   * mock host
   */
  public static final String MOCK_HOST = "http://yapi.demo.qunar.com/";

  private MockUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * 是否为mock模式
   */
  public static boolean isMock() {

    return BaseUrlEnum.MOCK.equals(HttpSdk.getInstance().getmUrlBean().urlType) && MOCK_HOST.equals(HttpSdk.getInstance().getmUrlBean().mockUrl);
  }
}
