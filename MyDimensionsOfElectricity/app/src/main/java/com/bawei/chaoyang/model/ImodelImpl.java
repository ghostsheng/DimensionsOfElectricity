package com.bawei.chaoyang.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bawei.chaoyang.app.MyApp;
import com.bawei.chaoyang.utils.RetrofitUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *层实现类
 */
public class ImodelImpl implements Imodel {

    //get请求
    @Override
    public void getRequestModel(String url, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("电波无法达到");
            return;
        }
        RetrofitUtils.getInstance().get(url, new RetrofitUtils.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });
    }

    //post请求
    @Override
    public void postRequestModel(String url, Map<String, String> params, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("电波无法达到");
            return;
        }
        RetrofitUtils.getInstance().post(url, params, new RetrofitUtils.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });
    }

    @Override
    public void deleteRequestModel(String url, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("电波无法达到");
            return;
        }
        RetrofitUtils.getInstance().delete(url, new RetrofitUtils.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });

    }
    //put请求
    @Override
    public void putRequestModel(String url, Map<String, String> params, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("电波无法达到");
            return;
        }
        RetrofitUtils.getInstance().put(url, params, new RetrofitUtils.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });
    }

    @Override
    public void postImageRequestModel(String url, List<File> image_list, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("电波无法达到");
            return;
        }
        RetrofitUtils.getInstance().postImage(url, image_list, new RetrofitUtils.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });
    }

    @Override
    public void postimageRequestModel(String url, File file, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("电波无法达到");
            return;
        }
        RetrofitUtils.getInstance().postimage(url, file, new RetrofitUtils.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });
    }

    @Override
    public void postImageConRequestModel(String url, Map<String, String> params, File file, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("电波无法达到");
            return;
        }
        RetrofitUtils.getInstance().postimagecon(url,params ,file, new RetrofitUtils.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });
    }

    @Override
    public void postDuoConRequestModel(String url, Map<String, String> params, List<File> list, final Class clazz, final ModelCallBack callBack) {
        if (!isNetWork()){
            callBack.failure("电波无法达到");
            return;
        }
        RetrofitUtils.getInstance().postduocon(url,params ,list, new RetrofitUtils.ICallBack() {
            @Override
            public void success(String result) {
                Object object = getGson(result, clazz);
                callBack.success(object);
            }

            @Override
            public void failure(String error) {
                callBack.failure(error);
            }
        });
    }

    //gson解析
    private Object getGson(String result, Class clazz) {
        Object o = new Gson().fromJson(result, clazz);
        return o;
    }

    //判断网络状态
    public static boolean isNetWork(){
        ConnectivityManager cm = (ConnectivityManager) MyApp.instance.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return activeNetworkInfo!=null && activeNetworkInfo.isAvailable();
    }
}
