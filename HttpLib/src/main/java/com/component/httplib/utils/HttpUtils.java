package com.component.httplib.utils;

import com.orhanobut.logger.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;

import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class HttpUtils {
    public static String httpTag = System.currentTimeMillis() + "----->";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public HttpUtils() {
    }

    public static void logRequest(Interceptor.Chain chain) {
        Request request = chain.request();
        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage = request.method() + ' ' + request.url() + ' ' + protocol;
        Logger.d(httpTag + requestStartMessage);
    }

    public static void logResponse(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        source.request(9223372036854775807L);
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException var9) {
                Logger.e(httpTag + "Couldn't decode the response body; charset is likely malformed.");
                Logger.e(httpTag + "END HTTP");
                return;
            }
        }

        if (!isPlaintext(buffer)) {
            Logger.d(httpTag + "END HTTP (binary " + buffer.size() + "-byte body omitted)");
        } else {
            if (contentLength != 0L) {
                Logger.d(httpTag + buffer.clone().readString(charset));
            }

        }
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64L ? buffer.size() : 64L;
            buffer.copyTo(prefix, 0L, byteCount);

            for(int i = 0; i < 16 && !prefix.exhausted(); ++i) {
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }

            return true;
        } catch (EOFException var6) {
            return false;
        }
    }

    public static String obtainValueFromHeaders(String headName, Request request) {
        List<String> headers = request.headers(headName);
        if (headers != null && headers.size() != 0) {
            if (headers.size() > 1) {
                throw new IllegalArgumentException("Only one Domain-Name in the headers");
            } else {
                return request.header(headName);
            }
        } else {
            return null;
        }
    }
}
