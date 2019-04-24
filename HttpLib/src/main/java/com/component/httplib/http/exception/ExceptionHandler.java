package com.component.httplib.http.exception;

import android.accounts.NetworkErrorException;
import android.net.ParseException;

import com.component.httplib.HttpSdk;
import com.component.httplib.R;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public class ExceptionHandler {
    public ExceptionHandler() {
    }

    public static BaseException handleException(Throwable e) {
        BaseException ex;
        if (e instanceof HttpException) {
            HttpException httpExc = (HttpException)e;
            ex = new BaseException(httpExc, String.valueOf(httpExc.code()), HttpSdk.getInstance().getContext().getString(R.string.http_exception));
            return ex;
        } else if (e instanceof ServerException) {
            ServerException serverExc = (ServerException)e;
            ex = new BaseException(serverExc, serverExc.getCode(), serverExc.getMsg());
            return ex;
        } else if (e instanceof JsonSyntaxException) {
            ex = new BaseException(e, String.valueOf(1006), HttpSdk.getInstance().getContext().getString(R.string.code_1006_json_syntax));
            return ex;
        } else if (!(e instanceof JSONException) && !(e instanceof ParseException)) {
            if (e instanceof ConnectException) {
                ex = new BaseException(e, String.valueOf(1003), HttpSdk.getInstance().getContext().getString(R.string.code_1003_connect_fail));
                return ex;
            } else if (e instanceof SocketTimeoutException) {
                ex = new BaseException(e, String.valueOf(1004), HttpSdk.getInstance().getContext().getString(R.string.code_1004_socket_time_out));
                return ex;
            } else if (e instanceof NetworkErrorException) {
                ex = new BaseException(e, String.valueOf(1007), HttpSdk.getInstance().getContext().getString(R.string.code_1007_error_exception));
                return ex;
            } else if (e instanceof UnknownHostException) {
                ex = new BaseException(e, String.valueOf(1005), HttpSdk.getInstance().getContext().getString(R.string.code_1005_host_exception));
                return ex;
            } else {
                ex = new BaseException(e, String.valueOf(1000), HttpSdk.getInstance().getContext().getString(R.string.unKnow_error));
                return ex;
            }
        } else {
            ex = new BaseException(e, String.valueOf(1001), HttpSdk.getInstance().getContext().getString(R.string.code_1001_json_parse));
            return ex;
        }
    }
}
