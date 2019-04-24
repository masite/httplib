package com.component.httplib.httphelper.http.intencepter;

import com.component.httplib.utils.MockUtils;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * User: jin.li.o
 * date: 2018/7/11
 * Description: 用于mock数据访问时URL的拦截处理
 * FIXME:
 */
public class MockInterceptor implements Interceptor {
  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    Request.Builder reqBuilder = request.newBuilder();

    String url = request.url().url().toString();
    String[] splits = url.split(MockUtils.MOCK_HOST);
    Logger.d("req split uri - [0]: " + splits[0] + " [1]: " + splits[1]);
    Request req = reqBuilder.url(MockUtils.MOCK_HOST + "mock/62798/" + splits[1]).build();
    return chain.proceed(req);
  }
}
