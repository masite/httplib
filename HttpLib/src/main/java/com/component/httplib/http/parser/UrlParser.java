package com.component.httplib.http.parser;

import okhttp3.HttpUrl;

public interface UrlParser {
    HttpUrl parseUrl(HttpUrl var1, HttpUrl var2);
}
