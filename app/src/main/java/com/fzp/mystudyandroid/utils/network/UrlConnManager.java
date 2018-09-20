package com.fzp.mystudyandroid.utils.network;

import android.text.TextUtils;
import org.apache.http.NameValuePair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * HttpURLConnection操作工具类
 */
public class UrlConnManager {
    private static final int TIME_OUT = 15000;

    /**
     *
     * @param url               请求地址
     * @param requestMethod     请求方式{"GET","POST"}
     * @return                  HttpURLConnection对象
     */
    public static HttpURLConnection getHttpURLConnection(String url, String requestMethod){
        HttpURLConnection mHttpUrlConn = null;

        try {
            URL mUrl = new URL(url);
            mHttpUrlConn = (HttpURLConnection) mUrl.openConnection();
            mHttpUrlConn.setConnectTimeout(TIME_OUT);//设置连接超时
            mHttpUrlConn.setReadTimeout(TIME_OUT);//设置读取超时
            mHttpUrlConn.setRequestMethod(requestMethod);//设置请求方式
            mHttpUrlConn.setRequestProperty("Connection", "Keep-Alive");//设置头部相应属性
            mHttpUrlConn.setDoInput(true);//接收输入流
            mHttpUrlConn.setDoOutput(requestMethod.equals("POST"));//传递参数时需要开启
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  mHttpUrlConn;
    }

    /**
     * 组织请求参数
     * @param os            请求输出流
     * @param paramList     参数键值对列表
     */
    public static void postParams(OutputStream os, List<NameValuePair> paramList) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (NameValuePair pair: paramList) {
            if(!TextUtils.isEmpty(sb)){
                sb.append("&");
            }
            sb.append(URLEncoder.encode(pair.getName(),"UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(pair.getValue(),"UTF-8"));
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        bw.write(sb.toString());
        bw.flush();
        bw.close();
    }

}
