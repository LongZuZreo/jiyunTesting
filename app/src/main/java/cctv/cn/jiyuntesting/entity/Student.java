package cctv.cn.jiyuntesting.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 张志远 on 2017/5/4.
 */

public class Student {


    /**
     * period : 10
     * num : 2
     * uId : a95cf0c7fc674c4fa476f1ff5ec7514f
     * result : success
     */

    @SerializedName("period")
    private String period;
    @SerializedName("num")
    private String num;
    @SerializedName("uId")
    private String uId;
    @SerializedName("result")
    private String result;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
