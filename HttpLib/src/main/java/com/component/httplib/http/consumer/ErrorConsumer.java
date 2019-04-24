package com.component.httplib.http.consumer;


import com.component.httplib.http.exception.BaseException;
import com.component.httplib.http.exception.ServerException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import io.reactivex.functions.Consumer;

public abstract class ErrorConsumer implements Consumer<Throwable> {
    public ErrorConsumer() {
    }

    public void accept(Throwable e) {
        StringWriter result = null;

        try {
            result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            result.flush();
            result.close();
//            Logger.e(HttpUtils.httpTag + result.toString());
        } catch (IOException var6) {
            if (result != null) {
                try {
                    result.flush();
                    result.close();
                } catch (IOException var5) {
                    ;
                }
            }
        }

        if (e instanceof ServerException) {
            this.onError(new BaseException(e, ((ServerException)e).getCode(), ((ServerException)e).getMsg()));
        } else if (e instanceof BaseException) {
            this.onError((BaseException)e);
        } else {
//            this.onError(new BaseException(e, String.valueOf(1000), HttpSdk.getInstance().getContext().getString(string.unKnow_error)));
            this.onError(new BaseException(e, String.valueOf(1000), ""));

        }

    }

    public abstract void onError(BaseException var1);
}

