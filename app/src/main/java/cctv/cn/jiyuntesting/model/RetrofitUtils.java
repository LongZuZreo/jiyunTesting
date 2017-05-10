package cctv.cn.jiyuntesting.model;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import cctv.cn.jiyuntesting.base.App;
import cctv.cn.jiyuntesting.model.http.MyCallBack;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 张志远 on 2017/5/4.
 */

public class RetrofitUtils {

    public static RetrofitUtils retrofitUtils=null;

    private RetrofitUtils(){

    }
    public  static RetrofitUtils getInstance(){
        if (retrofitUtils==null){
            retrofitUtils=new RetrofitUtils();
        }
        return  retrofitUtils;
    }
    public <T> void postEntity(String url, Map<String,String> params, final MyCallBack<T> callBack){

        OkHttpClient okHttpClient=new OkHttpClient();

        FormBody.Builder builder=new FormBody.Builder();

        Set<String> keySet = params.keySet();

        for (String key:keySet){

            builder.add(key,params.get(key));

        }

        FormBody formBody = builder.build();

        Request request=new Request.Builder().url(url).post(formBody).build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                App.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(e.toString());
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String string = response.body().string();

                Gson gson=new Gson();

                Type[] genericInterfaces = callBack.getClass().getGenericInterfaces();

                Type[] types = ((ParameterizedType) genericInterfaces[0]).getActualTypeArguments();

                final T t=gson.fromJson(string,types[0]);

                App.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(t);
                    }
                });


            }
        });

    }
}
