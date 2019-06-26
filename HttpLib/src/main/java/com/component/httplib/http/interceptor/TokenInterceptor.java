package com.component.httplib.http.interceptor;

import android.net.Uri;

import com.component.httplib.http.provider.ISignProvider;
import com.component.httplib.utils.HttpUtils;
import com.component.httplib.utils.Utility;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.annotations.NonNull;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String TAG = "TokenInterceptor";
    private static final String PARAM_TAG = "params:";
    private Map<String, String> pubParam;
    private ISignProvider provider;

    public TokenInterceptor(@NonNull ISignProvider provider) {
        this(provider, (Map)null);
    }

    public TokenInterceptor(@NonNull ISignProvider provider, Map<String, String> pubParam) {
        this.provider = provider;
        this.pubParam = pubParam;
    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = this.handlerRequest(request);
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception var5) {
            Logger.e("HTTP FAILED: ", new Object[]{var5});
            throw var5;
        }
        return response;
    }

    private Request handlerRequest(Request request) {
        Map<String, String> params = this.parseParams(request);
        if (params == null) {
            params = new HashMap();
        }

        if (this.pubParam != null) {
            Iterator var3 = this.pubParam.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var3.next();
                ((Map)params).put(entry.getKey(), entry.getValue());
            }
        }

        String param = (new Gson()).toJson(params);
        Logger.d("params:" + param);
        if (null == this.provider) {
            return request;
        } else {
            ((Map)params).put("application", this.provider.getAppId());
            ((Map)params).put("accessToken", this.provider.getAccessToken());
            String parameter = Utility.sort((Map)params);
            String digest = Utility.md5hash(parameter + this.provider.getAppSecret());
            ((Map)params).put("digest", digest);
            String method = request.method();
            if ("GET".equals(method)) {
                HttpUrl url = request.url();
                StringBuilder sb = new StringBuilder();
                sb.append(url.scheme()).append("://").append(url.host());
                List<String> paths = url.pathSegments();

                for(int i = 0; i < paths.size(); ++i) {
                    sb.append("/").append((String)paths.get(i));
                }

                return request.newBuilder().url(this.appendParametersToUrl(sb.toString(), (Map)params)).build();
            } else if (("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method) || "PATCH".equals(method)) && request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                Set keySet = ((Map)params).keySet();
                Iterator entryIterator = keySet.iterator();

                while(entryIterator.hasNext()) {
                    String key = entryIterator.next().toString();
                    String value = (String)((Map)params).get(key);
                    bodyBuilder.add(key, value);
                }

                return request.newBuilder().method(method, bodyBuilder.build()).build();
            } else {
                return request;
            }
        }
    }

    public Map<String, String> parseParams(Request request) {
        String method = request.method();
        Map<String, String> params = null;
        if ("GET".equals(method)) {
            params = this.doGet(request);
        } else if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method) || "PATCH".equals(method)) {
            RequestBody body = request.body();
            if (body != null && body instanceof FormBody) {
                params = this.doForm(request);
            }
        }

        return params;
    }

    private String appendParametersToUrl(String url, Map<String, String> params) {
        android.net.Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
        if (params != null && !params.isEmpty()) {
            Iterator var4 = params.keySet().iterator();

            while(var4.hasNext()) {
                String key = (String)var4.next();
                Object value = params.get(key);
                if (this.isSupportedParameterType(value)) {
                    value = Utility.parameterToString(value);
                    uriBuilder.appendQueryParameter(key, value.toString());
                }
            }

            return uriBuilder.toString();
        } else {
            return uriBuilder.toString();
        }
    }

    private Map<String, String> doGet(Request request) {
        Map<String, String> params = null;
        HttpUrl url = request.url();
        Set<String> strings = url.queryParameterNames();
        if (strings != null) {
            Iterator<String> iterator = strings.iterator();
            params = new HashMap();

            for(int i = 0; iterator.hasNext(); ++i) {
                String name = (String)iterator.next();
                String value = url.queryParameterValue(i);
                params.put(name, value);
            }
        }

        return params;
    }

    private Map<String, String> doForm(Request request) {
        Map<String, String> params = null;
        FormBody body = null;

        try {
            body = (FormBody)request.body();
        } catch (ClassCastException var6) {
            var6.printStackTrace();
        }

        if (body != null) {
            int size = body.size();
            if (size > 0) {
                params = new HashMap();

                for(int i = 0; i < size; ++i) {
                    params.put(body.name(i), body.value(i));
                }
            }
        }

        return params;
    }

    private boolean isSupportedParameterType(Object value) {
        return value instanceof String || value instanceof Boolean || value instanceof Number || value instanceof Date;
    }
}
