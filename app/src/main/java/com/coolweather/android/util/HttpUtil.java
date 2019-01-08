package com.coolweather.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by admin on 2017/12/27.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();      //实例化
        Request request = new Request.Builder().url(address).build();    //传入请求地址
        client.newCall(request).enqueue(callback);                  //注册回调来响应
    }
}
