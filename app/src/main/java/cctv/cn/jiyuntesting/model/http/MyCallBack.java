package cctv.cn.jiyuntesting.model.http;

/**
 * Created by 张志远 on 2017/5/4.
 */

public interface MyCallBack<T> {

    void onSuccess(T t);

    void onFailed(String errorStr);
}
