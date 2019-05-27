# httplib
##### 使用
1. 初始化
```
      //初始化网路环境 在application中
      UrlBean urlBean = new UrlBean();
      urlBean.urlType = "";//接口类型 。可为空
      urlBean.mockUrl = "";//mock地址 。可为空
      urlBean.baseUrl = "";//baseurl 。不可为空
      HttpSdk.getInstance().init(context,AppUtils.isAppDebug(),urlBean);
```

2. 配置请求体
```
    //DOMAIN_NAME_HEADER + HEAD_NAME 用于动态切换baseURL 。用不到的可以不加
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
CommonRetrofitFactory.getInstance("此处为临时baseurl").getService(testIn.class).getPushs(header,params)
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
