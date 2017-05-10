package cctv.cn.jiyuntesting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cctv.cn.jiyuntesting.base.App;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.i_agree_check)
    CheckBox iAgreeCheck;
    @BindView(R.id.to_start_image)
    ImageView toStartImage;
    @BindView(R.id.i_agree_btn)
    Button iAgreeBtn;
    private boolean iAgree;

    public static final int MSG_CODE=56;

    private int i=30;

    private boolean startAnswer=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        App.activity=this;

        handler.sendEmptyMessageDelayed(MSG_CODE,1000);

    }

    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (MSG_CODE){
                case MSG_CODE:
                    if (i==0){
                        startAnswer=true;
                        iAgreeBtn.setText("开始答题");
                    }else{
                        i--;
                        iAgreeBtn.setText(i+"s后可点击");
                        handler.sendEmptyMessageDelayed(MSG_CODE,1000);
                    }
                    break;
            }
        }
    };

    @OnClick({R.id.i_agree_check, R.id.to_start_image,R.id.i_agree_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.i_agree_check:
                iAgree = ((CheckBox) view).isChecked();
                break;
            case R.id.to_start_image:
                break;
            case R.id.i_agree_btn:
                if (startAnswer) {
                    if (iAgree) {

                        Intent fromIntent = getIntent();

                        Intent intent = new Intent(MainActivity.this, TestActivity.class);

                        intent.putExtra("num", fromIntent.getStringExtra("num"));

                        intent.putExtra("uId", fromIntent.getStringExtra("uId"));

                        intent.putExtra("period", fromIntent.getStringExtra("period"));

                        intent.putExtra("name", fromIntent.getStringExtra("name"));

                        intent.putExtra("gender",fromIntent.getStringExtra("gender"));

                        intent.putExtra("idCard",fromIntent.getStringExtra("idCard"));

                        intent.putExtra("age",fromIntent.getStringExtra("age"));

                        intent.putExtra("education",fromIntent.getStringExtra("education"));

                        intent.putExtra("periodval",fromIntent.getStringExtra("periodval"));

                        startActivity(intent);

                        finish();

                        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

                    } else {
                        Toast.makeText(this, "请选中我已阅读", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


}
