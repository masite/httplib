package com.component.httplib.httphelper.http;


import com.component.httplib.HttpSdk;

/**
 * 请求管理器
 *
 * @author mars.yu
 * @date 2017/11/13.
 */
public class RetrofitFactory {
    private static CommonRetrofitFactory INSTANCE;

    public static CommonRetrofitFactory getInstance() {
        synchronized (RetrofitFactory.class) {
            if (INSTANCE == null) {
                INSTANCE = CommonRetrofitFactory.getInstance(HttpSdk.getInstance().getmUrlBean().baseUrl);
            }
            return INSTANCE;
        }
    }

    public static void setINSTANCE(CommonRetrofitFactory instance) {
        RetrofitFactory.INSTANCE = instance;
        if (null == instance) {
            CommonRetrofitFactory.clearInstance();
        }
    }
}
