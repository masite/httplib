package com.component.httplib.http.interceptor;

import com.component.httplib.utils.HttpUtils;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LogInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public LogInterceptor() {
    }

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUtils.logRequest(chain);

        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception var5) {
            Logger.e("HTTP FAILED: ", new Object[]{var5});
            throw var5;
        }

        this.logResponseBody(response);
        return response;
    }

    private void logResponseBody(Response response) {
        try {
            HttpUtils.logResponse(response);
        } catch (IOException var8) {
            IOException e = var8;
            StringWriter result = null;

            try {
                result = new StringWriter();
                PrintWriter printWriter = new PrintWriter(result);
                e.printStackTrace(printWriter);
                result.flush();
                result.close();
                Logger.e(result.toString());
            } catch (IOException var7) {
                if (result != null) {
                    try {
                        result.flush();
                        result.close();
                    } catch (IOException var6) {

                    }
                }
            }
        }

    }
}
