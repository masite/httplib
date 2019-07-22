package com.component.httplib;

import com.component.httplib.httphelper.http.HttpRxManager;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class HttpObserver<T> implements Observer<T> {

    /**
     * 当前请求标识
     */
    private Object tag;
    /**
     * @param tag 当前请求标识
     */
    public HttpObserver(Object tag) {
        this.tag = tag;
    }
    @Override
    public void onSubscribe(Disposable d) {
        HttpRxManager.getInstance().put(tag, d);
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }

    protected void onStart(Disposable d) {
    }

    public abstract void onSuccess(T t);
}
