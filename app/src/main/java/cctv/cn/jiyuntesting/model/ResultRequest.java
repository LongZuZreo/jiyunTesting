package cctv.cn.jiyuntesting.model;

import android.widget.TextView;

import java.util.Map;

import cctv.cn.jiyuntesting.model.http.MyCallBack;

/**
 * Created by 张志远 on 2017/5/6.
 */

public class ResultRequest extends IRequest{

    public<T> void getResult(String url, Map<String,String> params, MyCallBack<T> callBack){
        retrofitUtils.postEntity(url,params,callBack);
    }
}
