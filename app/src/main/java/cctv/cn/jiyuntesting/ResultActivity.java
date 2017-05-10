package cctv.cn.jiyuntesting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cctv.cn.jiyuntesting.common.Urls;
import cctv.cn.jiyuntesting.entity.ResultEntity;
import cctv.cn.jiyuntesting.model.ResultRequest;
import cctv.cn.jiyuntesting.model.http.MyCallBack;

/**
 * Created by 张志远 on 2017/5/6.
 */

public class ResultActivity extends AppCompatActivity implements MyCallBack<ResultEntity> {

    @BindView(R.id.name_text)
    TextView nameText;
    @BindView(R.id.education_text)
    TextView educationText;
    @BindView(R.id.gender_text)
    TextView genderText;
    @BindView(R.id.periodval_text)
    TextView periodvalText;
    @BindView(R.id.idCard_text)
    TextView idCardText;
    @BindView(R.id.scope_text)
    TextView scopeText;
    @BindView(R.id.cheat_text)
    TextView cheatText;
    @BindView(R.id.result_text)
    TextView resultText;

    AlertDialog alertDialog;

    public static final int MSG_EMPTY=123;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Map<String, String> params = new HashMap<>();

        params.put("num", intent.getStringExtra("num"));

        params.put("uId", intent.getStringExtra("uId"));

        params.put("scope", intent.getStringExtra("scope"));

        params.put("cheat",intent.getStringExtra("cheat"));
        ResultRequest resultRequest = new ResultRequest();

        resultRequest.getResult(Urls.BASE_URL + Urls.RESULT_URL, params, this);

        View view = LayoutInflater.from(this).inflate(R.layout.loading_balls, null);

        alertDialog = new AlertDialog.Builder(this).setView(view).create();

        alertDialog.show();

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_EMPTY:
                    alertDialog.dismiss();
                    ResultEntity resultEntity= (ResultEntity) msg.obj;
                    nameText.setText("姓名："+resultEntity.getName());
                    String educationStr;
                    if (resultEntity.getEducation().equals("0")){
                        educationStr="本科";
                    }else if (resultEntity.getEducation().equals("1")){
                        educationStr="大专";
                    }else{
                        educationStr="高中";
                    }
                    educationText.setText("学历："+educationStr);

                    String genderStr;

                    if (resultEntity.getGender().equals("0")){

                        genderStr="男";

                    }else{

                        genderStr="女";

                    }


                    genderText.setText("性别："+genderStr);
                    String periodvalStr;

                    if (resultEntity.getPeriod().equals("10")){
                        periodvalStr="十个月";
                    }else if(resultEntity.getPeriod().equals("16")){
                        periodvalStr="十六个月";
                    }else{
                        periodvalStr="六个月";
                    }

                    periodvalText.setText("学制:"+periodvalStr);
                    idCardText.setText("身份证号:"+resultEntity.getIdCard());
                    scopeText.setText("成绩:"+resultEntity.getScope());
                    String cheatStr;
                    if (resultEntity.getCheat().equals("0")){
                        cheatStr="否";
                    }else{
                        cheatStr="是";
                    }

                    cheatText.setText("是否作弊:"+cheatStr);
                    String resultStr;
                    if (resultEntity.getResult().equals("0")){
                        resultStr="是";
                    }else{
                        resultStr="否";
                    }
                    resultText.setText("是否及格:"+resultStr);
                    break;
            }
        }
    };

    @Override
    public void onSuccess(ResultEntity resultEntity) {

        Message message = handler.obtainMessage(MSG_EMPTY, resultEntity);

        handler.sendMessage(message);


    }

    @Override
    public void onFailed(String errorStr) {
        Toast.makeText(this, "请检查网络设置", Toast.LENGTH_SHORT).show();
    }



}
