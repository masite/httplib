package com.component.httplib.http.consumer;

import android.widget.Toast;

import com.component.httplib.HttpSdk;
import com.component.httplib.R;
import com.component.httplib.http.entry.BaseEntry;
import com.component.httplib.http.exception.BaseException;
import com.component.httplib.http.exception.ServerException;
import com.component.httplib.utils.HttpUtils;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<BaseEntry<T>> {
    private CompositeDisposable compositeDisposable;

    public BaseObserver(CompositeDisposable compositeDisposable) {
        this.compositeDisposable = compositeDisposable;
    }

    public void onSubscribe(Disposable d) {
        this.compositeDisposable.add(d);
    }

    public void onNext(BaseEntry<T> tBaseEntry) {
        if ("0000".equals(tBaseEntry.getResultCode())) {
            this.onSuss(tBaseEntry);
        } else {
            this.onCodeError(tBaseEntry);
        }

    }

    public void onError(Throwable e) {
        StringWriter result = null;

        try {
            result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            result.flush();
            result.close();
            Logger.e(HttpUtils.httpTag + result.toString());
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
            this.onError(new BaseException(e, String.valueOf(1000), HttpSdk.getInstance().getContext().getString(R.string.unKnow_error)));
        }

    }

    public void onComplete() {
    }

    public abstract void onError(BaseException var1);

    public abstract void onSuss(BaseEntry<T> var1);

    protected void onCodeError(BaseEntry<T> baseEntity) {
        if (null != baseEntity.getMessage()) {
            Toast.makeText(HttpSdk.getInstance().getContext(), baseEntity.getMessage(), 1).show();
        } else {
            Logger.e(HttpSdk.getInstance().getContext().getString(R.string.data_fields_may_have_error_please_check));
        }

    }
}

