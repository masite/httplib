package com.component.httplib.httphelper.http.intencepter.header;

/**
 * 请求头常量
 *
 * @author mars.yu
 * @date 2018/1/17
 */
public class Header {
  /**
   * 请求头key
   */
  public static class Key {

    /**
     * app版本名称
     */
    public static final String APP_VERSION_NAME        = "versionName";
    /**
     * app包名
     */
    public static final String APP_PACKAGE_NAME        = "applicationId";
    /**
     * 设备型号
     */
    public static final String DEVICE_MODEL            = "deviceModel";
    /**
     * 设备id
     */
    public static final String DEVICE_ID               = "deviceId";
    /**
     * 设备厂商
     */
    public static final String DEVICE_MANUFACTURER     = "brand";
    /**
     * 应用渠道名称 如:toolkit,niopartner
     */
    public static final String CHANNEL_NAME            = "channelName";
    /**
     * 设备sdk版本
     */
    public static final String DEVICE_SDK_VERSION      = "osVersionName";
    /**
     * 语言
     */
    public static final String ACCEPT_LANGUAGE         = "Accept-Language";
    /**
     * 操作平台 如：android、ios
     */
    public static final String PLATFORM                = "platform";
    /**
     * TOKEN
     */
    public static final String TOKEN                   = "token";
    /**
     * APP_ID
     */
    public static final String APP_ID                   = "appid";
    /**
     * app版本号
     */
    public static final String APP_VERSION_CODE        = "app_version_code";
    /**
     * 设备高
     */
    public static final String DEVICE_HEIGHT           = "device_height";
    /**
     * 设备宽
     */
    public static final String DEVICE_WIDTH            = "device_width";
    /**
     * 设备宽
     */
    public static final String DEVICE_DPI              = "device_dpi";
    /**
     * 设备sdk版本名称
     */
    public static final String DEVICE_SDK_VERSION_NAME = "device_sdk_version_name";
    /**
     * 横屏还是竖屏
     */
    public static final String BROWSE_MODE             = "browseMode";
  }

  public static class Values {
    /**
     * 操作平台 如：android、ios
     */
    public static final String PLATFORM_VALUE = "android";

    public static final String ZH_CN = "zh_CN";
  }
}
