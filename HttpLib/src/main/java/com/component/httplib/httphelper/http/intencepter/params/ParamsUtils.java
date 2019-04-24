package com.component.httplib.httphelper.http.intencepter.params;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author by binbin.liu.o on 2017/12/4.
 */
public final class ParamsUtils {

  private static final int NUM_2    = 2;
  private static final int NUM_1000 = 1000;

  private ParamsUtils() {

  }

  /**
   * map转换为json.
   *
   * @param map the map
   * @return the string
   */
  public static String mapToJson(Map<String, Object> map) {
    String json = JSON.toJSONString(map);
    return json;
  }

  /**
   * Json to map map.
   *
   * @param json the json
   * @return the map
   */
  public static Map<String, Object> jsonToMap(String json) {
    Map<String, Object> map = JSON.parseObject(json);
    return map;
  }

  /**
   * Gets params.
   *
   * @param map the map
   * @return the params
   */
  public static Map<String, Object> getParams(Map<String, Object> map) {
    String md5 = signParams(sortParams(map));
//    map.put(ApiParamConstant.Key.SIGN, md5);
    return map;
  }

  /**
   * Gets sign.
   *
   * @param map the map
   * @return the sign
   */
  public static String getSign(Map<String, String> map) {
    String md5 = signParams(sortParamsB(map));
    return md5;
  }

  /**
   * Sort params b string.
   *
   * @param map the map
   * @return the string
   */
  public static String sortParamsB(Map<String, String> map) {
    StringBuilder build = new StringBuilder();

    List<String> list = new ArrayList<>(map.keySet());

    Collections.sort(list, new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        return o1.toLowerCase()
                 .compareTo(o2.toLowerCase());
      }
    });

    for (String key : list) {
      Object value = map.get(key);
      if (TextUtils.isEmpty((CharSequence) value)) {
        continue;
      }
      if (!TextUtils.isEmpty(build.toString())) {
        build.append("&");
      }
      build.append(key.toLowerCase());
      build.append("=");
      build.append(value);
    }
    return build.toString();
  }

  /**
   * Sort params string.
   *
   * @param map the map
   * @return the string
   */
  public static String sortParams(Map<String, Object> map) {
    StringBuilder build = new StringBuilder();

    List<String> list = new ArrayList<String>(map.keySet());

    Collections.sort(list, new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        return o1.toLowerCase()
                 .compareTo(o2.toLowerCase());
      }
    });

    for (String key : list) {
      Object value = map.get(key);
      if (value instanceof String && TextUtils.isEmpty((CharSequence) value)) {
        continue;
      }
      if (null == value) {
        continue;
      }
      if (!TextUtils.isEmpty(build.toString())) {
        build.append("&");
      }
      build.append(key.toLowerCase());
      build.append("=");
      if (value instanceof Map) {
        build.append(JSON.toJSONString(value));
      } else {
        build.append(value);
      }
    }
    return build.toString();
  }

  /**
   * Sort map map.
   *
   * @param map the map
   * @return the map
   */
  public static Map<String, Object> sortMap(Map<String, Object> map) {

    //指定排序器
    TreeMap<String, Object> treeMap = new TreeMap<String, Object>(new Comparator<String>() {

      /*
       * int compare(Object o1, Object o2) 返回一个基本类型的整型，
       * 返回负数表示：o1 小于o2，
       * 返回0 表示：o1和o2相等，
       * 返回正数表示：o1大于o2。
       */
      @Override
      public int compare(String o1, String o2) {

        //指定排序器按照降序排列
        return o1.toLowerCase()
                 .compareTo(o2.toLowerCase());
      }
    });

    for (String key : map.keySet()) {
      treeMap.put(key, map.get(key));
    }
    return treeMap;
  }

  /**
   * Sign params string.
   *
   * @param s the s
   * @return the string
   */
  public static String signParams(String s) {
    return "";
  }



  /**
   * Gets params from url.
   *
   * @param url the url
   * @return the params from url
   */
  public static Map<String, String> getParamsFromUrl(String url) {
    Map<String, String> queryPairs = new HashMap();
    if (!TextUtils.isEmpty(url) && url.contains("?")) {
      String params = url.substring(url.indexOf("?") + 1);
      if (!TextUtils.isEmpty(params)) {
        String[] paramArray = params.split("&");
        for (int i = 0; i < paramArray.length; i++) {
          String param = paramArray[i];
          if (param.contains("=")) {
            int idx = param.indexOf("=");
            try {
              String key = URLDecoder.decode(param.substring(0, idx), "UTF-8");
              String value = URLDecoder.decode(param.substring(idx + 1), "UTF-8");
              queryPairs.put(key, value);
            } catch (UnsupportedEncodingException var15) {
              var15.printStackTrace();
            }
          }
        }
      }
    }
    return queryPairs;
  }

  /**
   * 请求参数判空
   *
   * @param params 请求参数
   * @return the boolean
   */
  public static boolean checkNull(HashMap<String, Object> params) {
    if (params != null && !params.isEmpty()) {
      return true;
    }
    throw new NullPointerException("params is not empty");
  }
}
