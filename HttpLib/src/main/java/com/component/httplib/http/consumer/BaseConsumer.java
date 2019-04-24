package com.component.httplib.http.consumer;

import android.widget.Toast;

import com.component.httplib.HttpSdk;
import com.component.httplib.http.entry.BaseEntry;

import io.reactivex.functions.Consumer;


public abstract class BaseConsumer<T> implements Consumer<BaseEntry<T>> {
    public BaseConsumer() {
    }

    public void accept(BaseEntry<T> tBaseEntry) throws Exception {
        if ("0000".equals(tBaseEntry.getResultCode())) {
            this.onSuss(tBaseEntry);
        } else {
            this.onCodeError(tBaseEntry);
        }

    }

    public abstract void onSuss(BaseEntry<T> var1);

    protected void onCodeError(BaseEntry<T> baseEntity) {
        if (null != baseEntity.getMessage()) {
            Toast.makeText(HttpSdk.getInstance().getContext(), baseEntity.getMessage(), 1).show();
        } else {

        }

    }
}
