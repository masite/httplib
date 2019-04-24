package com.component.httplib.httphelper.http.model;

/**
 * 应用请求响应基类.
 *
 * @param <T> 泛型类
 */
public class BaseResponse<T> {

  /**
   * 请求成功.
   */
  private static final int SUCCESS = 0;
  /**
   * 错误码.
   */
  private String code;
  /**
   * 错误信息.
   */
  private String message;

  /**
   * 泛型类.
   */
  private T data;

  /**
   * 获取错误码.
   *
   * @return 错误码
   */
  public String getCode() {
    return code;
  }

  /**
   * 设置错误码.
   *
   * @param code 错误码
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * 获取错误信息.
   *
   * @return 错误信息
   */
  public String getMessage() {
    return message;
  }

  /**
   * 设置错误信息.
   *
   * @param message 错误信息
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * 获取泛型类.
   *
   * @return 泛型类
   */
  public T getData() {
    return data;
  }

  /**
   * 设置泛型类.
   *
   * @param data 泛型类
   */
  public void setData(T data) {
    this.data = data;
  }

  /**
   * 请求是否成功.
   *
   * @return 成功--true，失败--false
   */
  public boolean isSuccess() {
    return String.valueOf(SUCCESS)
                 .equals(String.valueOf(code));
  }
}
