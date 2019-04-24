package com.component.httplib.utils;


import com.component.httplib.http.exception.InvalidUrlException;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.HttpUrl;

public class UrlUtils {
    private UrlUtils() {
        throw new IllegalStateException("do not instantiation me");
    }

    public static HttpUrl createHttpUrl(String url) {
        HttpUrl parseUrl = HttpUrl.parse(url);
        if (null == parseUrl) {
            throw new InvalidUrlException(url);
        } else {
            return parseUrl;
        }
    }

    public static String sort(Map map) {
        Map<String, Object> sortMap = new TreeMap(new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        sortMap.putAll(map);
        StringBuilder sb = new StringBuilder();
        Iterator var3 = sortMap.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)var3.next();
            sb.append("&" + (String)entry.getKey() + "=" + entry.getValue());
        }

        sb.replace(0, 1, "");
        return sb.toString();
    }
}
