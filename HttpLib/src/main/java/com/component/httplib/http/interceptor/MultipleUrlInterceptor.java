package com.component.httplib.http.interceptor;

import android.text.TextUtils;

import com.component.httplib.http.exception.InvalidUrlException;
import com.component.httplib.http.parser.DefaultUrlParser;
import com.component.httplib.http.parser.UrlParser;
import com.component.httplib.utils.HttpUtils;
import com.component.httplib.utils.UrlUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MultipleUrlInterceptor implements Interceptor {
    private final Map<String, String> urls = new HashMap();
    private UrlParser urlParser;
    private boolean isRun = true;

    public MultipleUrlInterceptor() {
        this.initUrlParser(new DefaultUrlParser());
    }

    private void initUrlParser(UrlParser urlParser) {
        this.urlParser = urlParser;
    }

    public void putUrl(String tag, String url) {
        Map var3 = this.urls;
        synchronized(this.urls) {
            this.urls.put(tag, url);
        }
    }

    public Response intercept(Chain chain) throws IOException {
        return !this.isRun() ? chain.proceed(chain.request()) : chain.proceed(this.processRequest(chain.request()));
    }

    public Request processRequest(Request request) {
        Request.Builder newBuilder = request.newBuilder();
        String url = HttpUtils.obtainValueFromHeaders("api", request);
        if (TextUtils.isEmpty(url)) {
            String urlTag = HttpUtils.obtainValueFromHeaders("Domain-Name", request);
            if (TextUtils.isEmpty(urlTag)) {
                return newBuilder.build();
            }

            newBuilder.removeHeader("Domain-Name");
            url = (String)this.urls.get(urlTag);
        }

        HttpUrl httpUrl = null;
        if (!TextUtils.isEmpty(url)) {
            try {
                newBuilder.removeHeader("api");
                httpUrl = UrlUtils.createHttpUrl(url);
            } catch (InvalidUrlException var6) {
                var6.printStackTrace();
            }
        }

        if (null != httpUrl) {
            HttpUrl newUrl = this.urlParser.parseUrl(httpUrl, request.url());
            return newBuilder.url(newUrl).build();
        } else {
            return newBuilder.build();
        }
    }

    public boolean isRun() {
        return this.isRun;
    }

    public void setRun(boolean run) {
        this.isRun = run;
    }
}
