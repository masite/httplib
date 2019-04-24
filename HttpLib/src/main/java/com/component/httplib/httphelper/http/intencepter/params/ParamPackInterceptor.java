package com.component.httplib.httphelper.http.intencepter.params;

import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by binbin.liu on 2018/7/20.
 */

public class ParamPackInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    if (request.method().equals("GET")) {
      //如果是get请求
      HttpUrl url = request.url();
      Map<String, String> params = ParamsUtils.getParamsFromUrl(url.toString());

      String sign = ParamsUtils.getSign(params);
      HttpUrl newUrl = url.newBuilder()
          .addEncodedQueryParameter(ApiParamConstant.Key.SIGN, sign).build();
      request = request.newBuilder().url(newUrl).build();
    } else if (request.method().equals("POST")) {
      //如果是post请求
      HttpUrl url = request.url();
      if (!TextUtils.equals(url.encodedPath(), "/toolkit/api/app")) {
        RequestBody requestBody = request.body();
        if (requestBody instanceof FormBody) {
          //处理FormBody
          RequestBody requestBodyNew = signBody(requestBody);
          request = request.newBuilder().method(request.method(), requestBodyNew).build();
        } else if (requestBody instanceof MultipartBody) {
          //处理MultipartBody
          //MultipartBody body = (MultipartBody) requestBody;
          //MultipartBody.Builder builder = new MultipartBody.Builder();
          //for (MultipartBody.Part part : body.parts()) {
          //  RequestBody subBody = part.body();
          //  FormBody subBodyNew = signBody(subBody);
          //  if (null != subBodyNew) {
          //    builder.addPart(MultipartBody.Part.create(subBodyNew));
          //  } else {
          //    builder.addPart(part);
          //  }
          //}
          //request = request.newBuilder().method(request.method(), builder.build()).build();
          //此处处理多个body请求
        }
      }
    }

    Response response = chain.proceed(request);
    return response;
  }

  private FormBody signBody(RequestBody requestBody) {
    FormBody.Builder newFormBody = null;
    if (requestBody instanceof FormBody) {
      newFormBody = new FormBody.Builder();
      FormBody oldFormBody = (FormBody) requestBody;
      Map<String, String> map = new HashMap<>();
      for (int i = 0; i < oldFormBody.size(); i++) {
        newFormBody.add(oldFormBody.name(i), oldFormBody.value(i));
        map.put(oldFormBody.name(i), oldFormBody.value(i));
      }

//      newFormBody.add(ApiParamConstant.Key.TIME_STAMP, timeStamp);
//      map.put(ApiParamConstant.Key.TIME_STAMP, timeStamp);
      String sign = ParamsUtils.getSign(map);
      newFormBody.add(ApiParamConstant.Key.SIGN, sign);
    }
    return newFormBody.build();
  }
}
