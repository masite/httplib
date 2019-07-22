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

#### 必要的继承http工具类
```
public class RetrofitFactory {
    private static CommonRetrofitFactory INSTANCE;

    /**
      * 同一时间，baseurl会是一样的。 单利的原因
      */

    public static CommonRetrofitFactory getInstance() {
        synchronized (RetrofitFactory.class) {
            if (INSTANCE == null) {
                INSTANCE = CommonRetrofitFactory.getInstance(HttpSdk.getInstance().getmUrlBean().baseUrl);
            }
            if (!TextUtils.isEmpty(INSTANCE.getHelper().getBaseUrl())
                    && !INSTANCE.getHelper().getBaseUrl().equals(HttpSdk.getInstance().getmUrlBean().baseUrl)) {
                INSTANCE.putBaseUrl(HttpSdk.getInstance().getmUrlBean().baseUrl);
            }

            return INSTANCE;
        }
    }
}

```

```
**
 * 请求管理器
 *
 * 
 */
public class CommonRetrofitFactory {
    /**
     * /**
     * API域名标识.
     */
    public static final String API_DOMAIN_NAME = "api";
    /**
     * mock base url.
     */
    private String mBaseUrl;

    private static CommonRetrofitFactory INSTANCE;
    /**
     * 接口管理缓存
     */
    private Map<Class, Object> cacheService;
    private OkHttpClient.Builder okHttpClientBuilder;

    private RetrofitHelper helper;

    private CommonRetrofitFactory(String baseUrlApi) {
        cacheService = new HashMap<>();
        mBaseUrl = baseUrlApi;
        initHttpHelper(baseUrlApi);
    }

    private void initHttpHelper(String baseUrlApi) {
        helper = new RetrofitHelper().initUrl(baseUrlApi);
        okHttpClientBuilder = helper.getOkHttpClientBuilder();
        if (HttpSdk.getInstance().getIsDebug()) {
            okHttpClientBuilder.proxy(Proxy.NO_PROXY);
        }
        helper.addConverterFactory(FastJsonConverterFactory.create())
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new ResponseHeaderInterceptor());
        helper.putUrl(API_DOMAIN_NAME, baseUrlApi);
    }

    public static CommonRetrofitFactory getInstance(String baseUrlApi) {
        synchronized (CommonRetrofitFactory.class) {
            if (INSTANCE == null) {
                INSTANCE = new CommonRetrofitFactory(baseUrlApi);
            }
            return INSTANCE;
        }
    }

    public static void setINSTANCE(CommonRetrofitFactory INSTANCE) {
        CommonRetrofitFactory.INSTANCE = INSTANCE;
    }

    /**
     * 获取接口管理.
     *
     * @param <T>          接口类型
     * @param serviceClass 接口 class
     * @return 接口
     */
    public <T> T getService(Class<T> serviceClass) {
        return getService(serviceClass, true);
    }

    /**
     * 获取接口管理.
     *
     * @param <T>          接口类型
     * @param serviceClass 接口 class
     * @param https        是否是https
     * @return 接口
     */
    public <T> T getService(Class<T> serviceClass, boolean https) {
        if (cacheService.containsKey(serviceClass)) {
            return (T) cacheService.get(serviceClass);
        } else {
            if (https) {
                try {
                    okHttpClientBuilder.hostnameVerifier(
                            HostVerifier.getHostnameVerifier(new String[]{new URL(mBaseUrl).getHost()}));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cacheService.put(serviceClass, helper.build()
                    .create(serviceClass));
            return (T) cacheService.get(serviceClass);
        }
    }

    public RetrofitHelper getHelper() {
        return helper;
    }

    public CommonRetrofitFactory putBaseUrl(String baseUrl){
        mBaseUrl = baseUrl;
        getHelper().putUrl(API_DOMAIN_NAME, baseUrl);
        initHttpHelper(mBaseUrl);
        cacheService.clear();
        return this;
    }
}

```

##### 关于网络请求数据返回的统一处理
```
public abstract class BaseObserver<T> extends HttpObserver<T> {
    /**
     * @param tag 当前请求标识
     */
    public BaseObserver(Object tag) {
        super(tag);
    }

    @Override
    public void onError(Throwable e) {
        if(e instanceof BaseException){
            BaseException baseException = (BaseException) e;
            Toast.makeText(App.getApp(), baseException.getMsg(), Toast.LENGTH_SHORT).show();
            if(baseException.getCode().equals("401")){
                EventBus.getDefault().post(new LogoutEvent());
            }
            onFail(baseException);
        }
    }
    public abstract void onFail(BaseException baseEx);
```

##### 常规的一些header拦截器处理
```
public class HeaderInterceptor implements Interceptor {
    /**
     * 网络不可用异常code.
     */
    public static final String NETWORK_NOT_WORK = "1007";

    @Override
    public Response intercept(Chain chain) throws IOException {

        if (!isConnected()) {
            throw new ServerException(NETWORK_NOT_WORK, "");
        }


        Request request = chain.request();

        Request.Builder builder = request.newBuilder();
        String token = CmnSpHelper.getInstance(App.getApp()).getToken();
        String[] split = token.split("\\n");
        //传入token,进行身份验证
        if (!TextUtils.isEmpty(token)) {
            builder.addHeader(Header.Key.TOKEN, "bearer " + split[0]);
        }
        builder.addHeader("X-App-Version", HttpSdk.getInstance().getmUrlBean().mExtra1);


        String headers = HttpUtils.obtainValueFromHeaders("X-Api-Realm", request);

        Logger.d("X-Api-Realm == :  "+headers);
        if(TextUtils.isEmpty(headers)){
            builder.header("X-Api-Realm", "tools-matrix");
        }

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
```


