package com.component.httplib.http.parser;


import io.reactivex.annotations.NonNull;
import okhttp3.HttpUrl;

public class DefaultUrlParser implements UrlParser {
    public DefaultUrlParser() {
    }

    public HttpUrl parseUrl(@NonNull HttpUrl domainUrl, @NonNull HttpUrl url) {
        return null == domainUrl ? url : url.newBuilder().scheme(domainUrl.scheme()).host(domainUrl.host()).port(domainUrl.port()).build();
    }
}