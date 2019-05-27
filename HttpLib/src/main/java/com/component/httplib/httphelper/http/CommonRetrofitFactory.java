package com.component.httplib.httphelper.http;

import android.text.TextUtils;

import com.component.httplib.HttpSdk;
import com.component.httplib.http.HostVerifier;
import com.component.httplib.http.RetrofitHelper;
import com.component.httplib.http.interceptor.LogInterceptor;
import com.component.httplib.httphelper.http.fastjson.FastJsonConverterFactory;
import com.component.httplib.httphelper.http.intencepter.MockInterceptor;
import com.component.httplib.utils.MockUtils;
import com.orhanobut.logger.Logger;

import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * 请求管理器
 *
 * @date 2017/11/13.
 */
public class CommonRetrofitFactory {

    private static final String TAG = "CommonRetrofitFactory";
    private static String MOCK_API_URL = "";
    /**
     * /**
     * API域名标识.
     */
    public static final String API_DOMAIN_NAME = "api";
    /**
     * MOCK域名标识.
     */
    public static final String MOCK_DOMAIN_NAME = "mock";
    /**
     * api base url.
     */
    private static String BASE_API_URL;
    /**
     * mock base url.
     */

    private static CommonRetrofitFactory INSTANCE;
    /**
     * 接口管理缓存
     */
    private Map<Class, Object> cacheService;
    private OkHttpClient.Builder okHttpClientBuilder;

    private RetrofitHelper helper;

    private CommonRetrofitFactory(String baseUrlApi) {
        cacheService = new HashMap<>();

        BASE_API_URL = baseUrlApi;
        if (HttpSdk.getInstance().getmUrlBean() != null) {
            MOCK_API_URL = HttpSdk.getInstance().getmUrlBean().mockUrl;
        }

        helper = new RetrofitHelper().initUrl(BASE_API_URL);
        okHttpClientBuilder = helper.getOkHttpClientBuilder()
                //这里根据业务需要，加入各种拦截器
                .addInterceptor(new LogInterceptor());

        if (!HttpSdk.getInstance().getIsDebug()) {
            Logger.d("CommonRetrofitFactory----:禁止代理");
            okHttpClientBuilder.proxy(Proxy.NO_PROXY);
        } else {
            Logger.d("CommonRetrofitFactory----:允许代理");
        }
        helper.addConverterFactory(FastJsonConverterFactory.create());

        if (HttpSdk.getInstance().getmUrlBean() != null && MockUtils.isMock()) {
            helper.addInterceptor(new MockInterceptor());
            helper.putUrl(MOCK_DOMAIN_NAME, MOCK_API_URL);
        }

        helper.putUrl(API_DOMAIN_NAME, BASE_API_URL);
    }

    public static CommonRetrofitFactory getInstance(String baseUrlApi) {
        synchronized (CommonRetrofitFactory.class) {
            if (INSTANCE == null) {
                INSTANCE = new CommonRetrofitFactory(baseUrlApi);
            }
            return INSTANCE;
        }
    }

    public static void clearInstance() {
        INSTANCE = null;
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
                            HostVerifier.getHostnameVerifier(new String[]{new URL(BASE_API_URL).getHost()}));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cacheService.put(serviceClass, helper.build()
                    .create(serviceClass));
            return (T) cacheService.get(serviceClass);
        }
    }
}
