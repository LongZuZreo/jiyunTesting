package cctv.cn.jiyuntesting.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 张志远 on 2017/5/6.
 */

public class ResultEntity {


    /**
     * name : 张志远
     * gender : 1
     * idCard : 130481199801170000
     * education : 2
     * period : 10
     * scope : 56
     * result : 2
     * cheat : 0
     * num : 1
     */

    @SerializedName("name")
    private String name;
    @SerializedName("gender")
    private String gender;
    @SerializedName("idCard")
    private String idCard;
    @SerializedName("education")
    private String education;
    @SerializedName("period")
    private String period;
    @SerializedName("scope")
    private String scope;
    @SerializedName("result")
    private String result;
    @SerializedName("cheat")
    private String cheat;
    @SerializedName("num")
    private String num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCheat() {
        return cheat;
    }

    public void setCheat(String cheat) {
        this.cheat = cheat;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
