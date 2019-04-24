package com.component.httplib.http.exception;


import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class ExceptionFunction implements Function<Throwable, Observable> {
    public ExceptionFunction() {
    }

    public Observable apply(@NonNull Throwable throwable) throws Exception {
        return Observable.error(ExceptionHandler.handleException(throwable));
    }
}
