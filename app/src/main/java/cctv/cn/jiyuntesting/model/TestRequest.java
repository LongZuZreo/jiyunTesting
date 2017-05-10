package cctv.cn.jiyuntesting.model;

import java.util.Map;

import cctv.cn.jiyuntesting.model.http.MyCallBack;

/**
 * Created by 张志远 on 2017/5/5.
 */

public class TestRequest extends IRequest {

    public <T> void getTest(String url, Map<String,String> params, MyCallBack<T> myCallBack){
        retrofitUtils.postEntity(url,params,myCallBack);
    }

}
