# httplib
##### 使用
1. 初始化
```
      //初始化网路环境 在application中
       UrlBean urlBean = new UrlBean();
       urlBean.baseUrl = "https://toolsapi.****.cc";
       //第二个参数 是否打印网络日志且允许代理 。true 》 是
       HttpSdk.getInstance().init(this,true,urlBean);


       //如果需要加拦截器。可以这样做
        OkHttpClient.Builder okHttpClientBuilder = RetrofitFactory.getInstance().getHelper().getOkHttpClientBuilder();
        okHttpClientBuilder.addInterceptor(new HeaderInterceptor());
```

2. 配置请求体
```
    //DOMAIN_NAME_HEADER + HEAD_NAME 用于动态切换baseURL. 不需要切换baseurl时，可不加
    @Headers({"Content-type:application/json;charset=UTF-8", DOMAIN_NAME_HEADER + HEAD_NAME})
    @GET("goods")
    Observable<BaseResponse<JSONObject>> getPushs(@HeaderMap Map<String, String> headers, @QueryMap Map<String,String> params);

```

3. 使用
```
//activity 需要继承自RxAppCompatActivity 才可以用bindToLifecycle。用于避免页面回收造成的内存泄漏问题
RetrofitFactory.getInstance().getService(testIn.class).getPushs(header,params)
                        .map(new ApiServerResultFunction<JSONObject>())
                        .compose(MainActivity.this.<BaseResponse<JSONObject>>bindToLifecycle())
                        .subscribe(new HttpObserver<JSONObject>("Test") {
                            @Override
                            protected void onFail(BaseException e) {
                                Logger.d("====onFail" + JSON.toJSON(e));
                            }

                            @Override
                            protected void onSuccess(BaseResponse<JSONObject> response) {
                                Logger.d("====onSuccess" + JSON.toJSON(response));
                            }
                        });
```
4. 如果某次请求 ，baseurl和 基础的不一致。可以这么做
```
 RetrofitFactory.getInstance().putBaseUrl("临时baseurl").getService(testIn.class).getPushs(header,params)
                        .subscribe(new BaseObserver<JSONArray>("getCardData") {

                                                                                  @Override
                                                                                  public void onFail(BaseException baseEx) {
                                                                                      Logger.d("====》》》》》》》" + baseEx.getCode() + baseEx.getMsg());

                                                                                  }

                                                                                  @Override
                                                                                  public void onSuccess(JSONArray baseEx) {

                                                                                  }
                                                                              }
                                                                   );
```
