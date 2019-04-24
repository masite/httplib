package com.component.httplib.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class HostVerifier {
    public HostVerifier() {
    }

    public static HostnameVerifier getHostnameVerifier(final String[] hostUrls) {
        HostnameVerifier TRUSTED_VERIFIER = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession sslSession) {
                boolean ret = false;
                String[] var4 = hostUrls;
                int var5 = var4.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    String host = var4[var6];
                    if (host.equalsIgnoreCase(hostname)) {
                        ret = true;
                    }
                }

                return ret;
            }
        };
        return TRUSTED_VERIFIER;
    }
}