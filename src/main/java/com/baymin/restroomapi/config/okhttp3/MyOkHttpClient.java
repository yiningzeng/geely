package com.baymin.restroomapi.config.okhttp3;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

/**
 * Created by baymin on 17-8-24.
 */
@Slf4j
public class MyOkHttpClient {


    private static MyOkHttpClient myOkHttpClient;
    private OkHttpClient okHttpClient;
    private Handler handler;

    public MyOkHttpClient()
    {
        //okHttpClient=new OkHttpClient();
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static MyOkHttpClient getInstance() {
        if (myOkHttpClient == null) {
            synchronized (MyOkHttpClient.class) {
                if (myOkHttpClient == null) {
                    myOkHttpClient = new MyOkHttpClient();
                }
            }
        }
        return myOkHttpClient;
    }


    public String aiPost(String url,String imgBase64String) {
        try {
            FormBody formBody = new FormBody
                    .Builder()
                    .add("file", imgBase64String)//设置参数名称和参数值
                    .build();
            Request request = new Request.Builder()
                    .url(url)
//                .header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
                    .post(formBody)
                    .build();
            return okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
//            e.printStackTrace();
            log.debug("eeeee-----访问ai接口出错：{}",e);
            return "{\"num\":-1, \"data\":\"" + e.getMessage() + "\"}";
        }
    }


    /**
     * 获取当前vin后面车型的数据
     * @param workStationRuleName 当前岗位的编号 T00-T?? 都是T开头
     * @param num 返回的数据个数
     * @param nowVin 当前vin
     * @return
     */
    public String nextVinInfo(String workStationRuleName,Integer num,String nowVin) {
        try {
            String bodyStr = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:rem=\"http://remote.mm.mes.infohow.cn/\"><soapenv:Header/><soapenv:Body><rem:selectPrdListWithVinForService><!--Optional:-->" +
                    "<arg0>" + workStationRuleName + "</arg0><!--Optional:-->" +
                    "<arg1>" + num + "</arg1><!--Optional:-->" +
                    "<arg2>" + nowVin + "</arg2>" +
                    "</rem:selectPrdListWithVinForService></soapenv:Body></soapenv:Envelope>";
            RequestBody body = RequestBody.create(MediaType.parse("text/xml;charset=UTF-8"), bodyStr);
            Request request = new Request.Builder()
                    .url("http://10.50.102.213:7002/sfwmsjob/cxf/setMesOutInfoData?wsdl")
                    .post(body)
                    .build();

            return okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
//            e.printStackTrace();
            log.debug("eeeee-----获取当前vin后面车型的数据出错：{}",e);
            return "{\"code\":-1, \"msg\":\"" + e.getMessage() + "\"}";
        }
    }

    /**
     * 通过vin获取车型
     * @param vin
     * @return
     */
    public String getCarModelByVin(String vin) {
        try {
            String bodyStr = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:rem=\"http://remote.ei.mes.infohow.cn/\">" +
                    "<soapenv:Header/>" +
                    "<soapenv:Body>" +
                    "<rem:getBodytypeConcatWeibiaoByVin>" +
                    "<!--Optional:-->" +
                    "<arg0>" + vin + "</arg0>" +
                    "</rem:getBodytypeConcatWeibiaoByVin>" +
                    "</soapenv:Body>" +
                    "</soapenv:Envelope>";
            RequestBody body = RequestBody.create(MediaType.parse("text/xml;charset=UTF-8"), bodyStr);
            Request request = new Request.Builder()
                    .url("http://10.50.102.223:7001/ghis/remote/VRInvokeMes?wsdl")
                    .post(body)
                    .build();

            return okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
//            e.printStackTrace();
            log.debug("eeeee-----通过vin获取车型：{}",e);
            return "{\"code\":-1, \"msg\":\"" + e.getMessage() + "\"}";
        }
    }





    public String get(String url)
    {
        Request request = new Request.Builder().url(url).build();
        try {
            return okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String platePost(String url,String body)
    {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text"), body);
        Request request = new Request.Builder()
                .url(url).header("Authorization","APPCODE f6f3f2d2dffe4e06908e77a5e38e50f1")
                .header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
                .post(requestBody)
                .build();
        try {
            return okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String lplatePost(String url,String imgBase64String)
    {
        FormBody formBody = new FormBody
                .Builder()
                .add("image",imgBase64String)//设置参数名称和参数值
                .build();
        Request request = new Request.Builder()
                .url(url).header("Authorization","APPCODE f6f3f2d2dffe4e06908e77a5e38e50f1")
                .header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
                .post(formBody)
                .build();
        try {
            return okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void asyncGet(String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }


    public void asyncPost(String url, FormBody formBody, Callback callback) {
        Request request = new Request.Builder().url(url).post(formBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void asyncPostJson(String url, String json, Callback callback) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }
    public String postJson(String url, String json) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        try {
            return okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void asyncDelete(String url, String headerName,String headerValue, Callback callback) {
        Request request = new Request.Builder().url(url).header(headerName,headerValue).delete().build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public String push(String url,String json)
    {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url).header("Authorization","Basic MTM4N2ZlZjNmYzE0OGQ2ZTI0NjljNDhlOmUyZDk2Njg5ZGFjNTY2MGZjMTU1NjU5Mg==")
                .post(requestBody)
                .build();
        try {
            return okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
