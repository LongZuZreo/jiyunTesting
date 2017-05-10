package cctv.cn.jiyuntesting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cctv.cn.jiyuntesting.base.App;
import cctv.cn.jiyuntesting.common.Urls;
import cctv.cn.jiyuntesting.entity.Student;
import cctv.cn.jiyuntesting.model.RegisterRequest;
import cctv.cn.jiyuntesting.model.http.MyCallBack;

/**
 * Created by 张志远 on 2017/5/4.
 */

public class LoginActivity extends Activity implements MyCallBack<Student>{


    @BindView(R.id.name_text)
    TextView nameText;
    @BindView(R.id.name_edit)
    EditText nameEdit;
    @BindView(R.id.linear_name)
    LinearLayout linearName;
    @BindView(R.id.idCard_text)
    TextView idCardText;
    @BindView(R.id.idCar_edit)
    EditText idCarEdit;
    @BindView(R.id.linear_idCard)
    LinearLayout linearIdCard;
    @BindView(R.id.age_text)
    TextView ageText;
    @BindView(R.id.age_edit)
    EditText ageEdit;
    @BindView(R.id.linear_age)
    LinearLayout linearAge;
    @BindView(R.id.education_text)
    TextView educationText;
    @BindView(R.id.education_edit)
    Spinner educationEdit;
    @BindView(R.id.linear_education)
    LinearLayout linearEducation;
    @BindView(R.id.periodval_text)
    TextView periodvalText;
    @BindView(R.id.periodval_edit)
    Spinner periodvalEdit;
    @BindView(R.id.linear_periodval)
    LinearLayout linearPeriodval;
    @BindView(R.id.sex_text)
    TextView sexText;
    @BindView(R.id.linear_sex)
    LinearLayout linearSex;
    @BindView(R.id.test_time)
    TextView testTime;
    @BindView(R.id.test_time_year)
    TextView testTimeYear;
    @BindView(R.id.test_time_day)
    TextView testTimeDay;
    @BindView(R.id.reset_btn)
    Button resetBtn;
    @BindView(R.id.submit_btn)
    Button submitBtn;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.sex_radio_man)
    RadioButton sexRadioMan;
    @BindView(R.id.sex_radio_women)
    RadioButton sexRadioWomen;

    private String educationStr="2";

    private String periodvalStr="10";
    private RegisterRequest registerRequest;

    private String sexStr="男";
    private String nameStr;
    private String idCardStr;
    private String ageStr;

    public static final int MSG_CODE=5;
    private SimpleDateFormat simpleDateFormatDay;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intent = new Intent(this,MainActivity.class);

        App.activity=this;
        ButterKnife.bind(this);

        registerRequest = new RegisterRequest();

        final String[] eduStr = {"高中", "本科","专科"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, eduStr);

        educationEdit.setAdapter(arrayAdapter);

        educationEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (eduStr[position].equals("高中")){
                    educationStr="2";
                }else if(eduStr[position].equals("本科")){
                    educationStr="0";
                }else if (eduStr[position].equals("专科")){
                    educationStr="1";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String[] perStr = {"十个月", "六个月", "十六个月"};

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, perStr);

        periodvalEdit.setAdapter(arrayAdapter1);

        periodvalEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (perStr[position].equals("六个月")){
                    periodvalStr="6";
                }else if (perStr[position].equals("十个月")){
                    periodvalStr="10";
                }else if (perStr[position].equals("十六个月")){
                    periodvalStr="16";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SimpleDateFormat simpleDateFormatYear=new SimpleDateFormat("yyyy年MM月dd日");

        String yearTime=simpleDateFormatYear.format(new Date());

        simpleDateFormatDay = new SimpleDateFormat("HH时mm分ss秒");

        testTimeYear.setText(yearTime);

        handler.sendEmptyMessageDelayed(MSG_CODE,1000);

    }



    @OnClick({R.id.name_edit, R.id.idCar_edit, R.id.age_edit,  R.id.reset_btn, R.id.submit_btn,R.id.sex_radio_man,R.id.sex_radio_women})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reset_btn:
                nameEdit.setText("");
                idCarEdit.setText("");
                ageEdit.setText("");
                sexRadioMan.setChecked(true);
                break;
            case R.id.submit_btn:
                if (!nameEdit.getText().toString().equals("") && !ageEdit.getText().equals("")){
                    if (idCarEdit.getText().toString().matches("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$") || idCarEdit.getText().toString().matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$")){

                        Map<String,String> params=new HashMap<>();

                        nameStr = nameEdit.getText().toString();

                        intent.putExtra("name",nameStr);

                        params.put("name", nameStr);

                        intent.putExtra("gender",sexStr);

                        params.put("gender",sexStr);

                        idCardStr = idCarEdit.getText().toString();

                        intent.putExtra("idCard",idCardStr);

                        params.put("idCard", idCardStr);

                        ageStr = ageEdit.getText().toString();

                        intent.putExtra("age",ageStr);

                        params.put("age", ageStr);

                        intent.putExtra("education",educationStr);

                        params.put("education",educationStr);

                        intent.putExtra("periodval",periodvalStr);

                        params.put("periodval",periodvalStr);

                        registerRequest.getRegisterMsg(Urls.BASE_URL+Urls.REGISTER_URL,params,this);
                    }else{
                        Toast.makeText(this, "请填写正确的身份证", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "姓名或年龄不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sex_radio_man:
                if (((RadioButton)view).isChecked()){
                    sexStr="男";
                }
                break;
            case R.id.sex_radio_women:
                if (((RadioButton)view).isChecked()){
                    sexStr="女";
                }
                break;
        }
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_CODE:
                    String dayTime=simpleDateFormatDay.format(new Date());

                    testTimeDay.setText(dayTime);

                    handler.sendEmptyMessageDelayed(MSG_CODE,1000);
                    break;
            }
        }
    };


    @Override
    public void onSuccess(Student student) {
        if (student.getNum()==null){
            Toast.makeText(this, "你的考试次数已超过三次，考试资格已取消", Toast.LENGTH_SHORT).show();
        }else{

            intent.putExtra("num",student.getNum());

            intent.putExtra("uId",student.getUId());

            intent.putExtra("period",student.getPeriod());
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_top);
            finish();
        }
    }

    @Override
    public void onFailed(String errorStr) {
        String str=errorStr;
        Toast.makeText(this, "注册失败请检查网络设置。", Toast.LENGTH_SHORT).show();
    }

}
