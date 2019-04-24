package com.component.httplib.httphelper.http.intencepter.header;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.component.httplib.HttpSdk;
import com.component.httplib.http.exception.ServerException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Project Name : nio_android_toolskit
 * <p>{}</p>
 *
 * @author : yinbi.zhang.o
 *         Create at : 2018/6/2 11:40
 */
public class HeaderInterceptor implements Interceptor {
  /**
   * 网络不可用异常code.
   */
  public static final String NETWORK_NOT_WORK = "1007";

  @Override
  public Response intercept(Chain chain) throws IOException {

    if(!isConnected()){
      throw new ServerException(NETWORK_NOT_WORK, "");
    }


    Request.Builder builder = chain.request()
                                   .newBuilder();
    builder.addHeader(Header.Key.ACCEPT_LANGUAGE, Header.Values.ZH_CN)
           .addHeader(Header.Key.PLATFORM, Header.Values.PLATFORM_VALUE);
//    //传入token,进行身份验证
//    String tokenKey = CmnSpHelper.getInstance(App.getContext())
//                                 .getAccessToken();
//    if (!TextUtils.isEmpty(tokenKey)) {
//      builder.addHeader(Header.Key.TOKEN, tokenKey);
//    }
    Response response = chain.proceed(builder.build());
    return response;
  }


  /**
   * 判断网络是否连接
   * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
   *
   * @return {@code true}: 是<br>{@code false}: 否
   */
  public static boolean isConnected() {
    NetworkInfo info = getActiveNetworkInfo();
    return info != null && info.isConnected();
  }
  /**
   * 获取活动网络信息
   * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
   *
   * @return NetworkInfo
   */
  private static NetworkInfo getActiveNetworkInfo() {
    return ((ConnectivityManager) HttpSdk.getInstance().getContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
  }

}
