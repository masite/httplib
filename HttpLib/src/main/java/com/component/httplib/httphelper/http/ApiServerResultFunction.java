package com.component.httplib.httphelper.http;


import com.component.httplib.http.exception.ServerException;
import com.component.httplib.httphelper.http.model.BaseResponse;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Api请求异常转换.
 *
 * @param <T> the type parameter
 * @date 2017/11/14
 */
public class ApiServerResultFunction<T> implements Function<BaseResponse<T>, BaseResponse<T>> {
  @Override
  public BaseResponse<T> apply(@NonNull BaseResponse<T> baseResponse) throws Exception {
    if (!baseResponse.isSuccess()) {
      throw new ServerException(baseResponse.getCode(), baseResponse.getMessage());
    }
    return baseResponse;
  }
}
