package com.component.httplib.http.converter;

import com.component.httplib.http.entry.BaseEntry;
import com.component.httplib.utils.EntryUtil;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        Object t = this.adapter.fromJson(json);

        try {
            if (t instanceof BaseEntry) {
                EntryUtil annotation = (EntryUtil)t.getClass().getAnnotation(EntryUtil.class);
                if (annotation != null) {
                    JSONObject jsonObject = new JSONObject(json);
                    String resultCode = jsonObject.getString(annotation.code());
                    String message = jsonObject.getString(annotation.msg());
                    ((BaseEntry)t).setResultCode(resultCode);
                    ((BaseEntry)t).setMessage(message);
                }
            }
        } catch (JSONException var11) {
            var11.printStackTrace();
        } finally {
            value.close();
        }

        return (T) t;
    }
}
