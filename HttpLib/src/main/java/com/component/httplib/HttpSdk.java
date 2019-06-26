package com.component.httplib;

import android.annotation.SuppressLint;
import android.content.Context;

import com.component.httplib.httphelper.http.model.UrlBean;

public class HttpSdk {
    private Context mContext = null;
    private boolean mIsDebug = false;
    private UrlBean mUrlBean;

    public HttpSdk() {
    }

    public static HttpSdk getInstance() {
        return CoreSdkHolder.instance;
    }

    /**
      * @param isDebug 控制是否输出网络请求日志，是否允许代理  true > 允许
     *
      */
    public void init(Context context, boolean isDebug, UrlBean urlBean) {
        mContext = context;
        mIsDebug = isDebug;
        mUrlBean = urlBean;
    }

    public Context getContext() {
        return this.mContext;
    }

    public boolean getIsDebug() {
        return this.mIsDebug;
    }

    public UrlBean getmUrlBean() {
        return mUrlBean;
    }

    private static class CoreSdkHolder {
        @SuppressLint({"all"})
        private static HttpSdk instance = new HttpSdk();

        private CoreSdkHolder() {
        }
    }
}
