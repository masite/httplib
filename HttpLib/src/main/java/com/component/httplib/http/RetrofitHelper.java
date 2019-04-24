package com.component.httplib.http;


import com.component.httplib.HttpSdk;
import com.component.httplib.http.exception.ExceptionFunction;
import com.component.httplib.http.interceptor.MultipleUrlInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    public static final String HEAD_NAME = "api";
    public static final String API_NAME_HEADER = "api: ";
    public static final String DOMAIN_NAME = "Domain-Name";
    public static final String DOMAIN_NAME_HEADER = "Domain-Name: ";
    private static final long TIMEOUT = 30L;
    private static String baseUrl;
    private OkHttpClient.Builder okHttpClientBuilder;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private MultipleUrlInterceptor interceptor = new MultipleUrlInterceptor();
    private Retrofit.Builder builder;

    public RetrofitHelper() {
        this.okHttpClientBuilder = (new OkHttpClient.Builder()).connectTimeout(30L, TimeUnit.SECONDS).readTimeout(30L, TimeUnit.SECONDS).readTimeout(30L, TimeUnit.SECONDS).retryOnConnectionFailure(true).sslSocketFactory(TrustManager.getUnsafeOkHttpClient()).addInterceptor(this.interceptor);
        this.builder = new Retrofit.Builder();
    }

    public OkHttpClient getOkHttpClient() {
        return this.okHttpClient;
    }

    public RetrofitHelper setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        return this;
    }

    public OkHttpClient.Builder getOkHttpClientBuilder() {
        return this.okHttpClientBuilder;
    }

    public RetrofitHelper addInterceptor(Interceptor interceptor) {
        this.okHttpClientBuilder.addInterceptor(interceptor);
        return this;
    }

    public RetrofitHelper addCache(Cache cache) {
        this.okHttpClientBuilder.cache(cache);
        return this;
    }

    public RetrofitHelper initUrl(String url) {
        baseUrl = url;
        return this;
    }

    public RetrofitHelper addConverterFactory(Converter.Factory factory) {
        this.builder.addConverterFactory(factory);
        return this;
    }

    public RetrofitHelper addCCallAdapterrFactory(retrofit2.CallAdapter.Factory factory) {
        this.builder.addCallAdapterFactory(factory);
        return this;
    }

    public RetrofitHelper build() {
        if (null == this.okHttpClient) {
            if (HttpSdk.getInstance().getIsDebug()) {
                this.okHttpClientBuilder.addInterceptor((new HttpLoggingInterceptor()).setLevel(HttpLoggingInterceptor.Level.BODY));
            }

            this.okHttpClient = this.okHttpClientBuilder.build();
        }

        this.retrofit = this.builder.baseUrl(baseUrl).client(this.okHttpClient).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        return this;
    }

    public void setRun(boolean run) {
        this.interceptor.setRun(run);
    }

    public void putUrl(String tag, String url) {
        this.interceptor.putUrl(tag, url);
    }

    public <T> T create(Class<T> service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("API declarations must be interfaces.");
        } else if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
        } else {
            final T t = this.retrofit.create(service);
            Object o = Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new InvocationHandler() {
                public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                    Object invoke = method.invoke(t, args);
                    if (invoke instanceof Observable) {
                        return ((Observable)invoke).onErrorResumeNext(new ExceptionFunction()).compose(RxHelper.rxSchedulerHelper());
                    } else {
                        return invoke instanceof Flowable ? ((Flowable)invoke).onErrorResumeNext(new ExceptionFunction()).compose(RxHelper.rxFlowableSchedulerHelper()) : invoke;
                    }
                }
            });
            return (T) o;
        }
    }
}

