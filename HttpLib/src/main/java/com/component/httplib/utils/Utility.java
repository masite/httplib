package com.component.httplib.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class Utility {
    private static final String HASH_ALGORITHM_MD5 = "MD5";

    public Utility() {
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

    public static String parameterToString(Object value) throws IllegalArgumentException {
        if (value instanceof String) {
            return (String)value;
        } else if (!(value instanceof Boolean) && !(value instanceof Number)) {
            if (value instanceof Date) {
                SimpleDateFormat iso8601DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.CHINA);
                return iso8601DateFormat.format(value);
            } else {
                if (value != null) {
                    String str = String.valueOf(value);
                    if (TextUtils.isDigitsOnly(str)) {
                        return str;
                    }
                }

                throw new IllegalArgumentException("Unsupported parameter type " + (value == null ? "null" : value.getClass()));
            }
        } else {
            return value.toString();
        }
    }

    public static String md5hash(String key) {
        return hashWithAlgorithm("MD5", key);
    }

    private static String hashWithAlgorithm(String algorithm, String key) {
        return hashWithAlgorithm(algorithm, key.getBytes());
    }

    private static String hashWithAlgorithm(String algorithm, byte[] bytes) {
        MessageDigest hash;
        try {
            hash = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException var4) {
            return null;
        }

        return hashBytes(hash, bytes);
    }

    private static String hashBytes(MessageDigest hash, byte[] bytes) {
        hash.update(bytes);
        byte[] digest = hash.digest();
        StringBuilder builder = new StringBuilder();
        byte[] var4 = digest;
        int var5 = digest.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            int b = var4[var6];
            builder.append(Integer.toHexString(b >> 4 & 15));
            builder.append(Integer.toHexString(b >> 0 & 15));
        }

        return builder.toString();
    }
}
