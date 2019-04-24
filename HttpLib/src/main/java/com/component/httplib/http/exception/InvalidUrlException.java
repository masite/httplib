package com.component.httplib.http.exception;

import android.text.TextUtils;

public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException(String url) {
        super("You've configured an invalid url : " + (TextUtils.isEmpty(url) ? "EMPTY_OR_NULL_URL" : url));
    }
}