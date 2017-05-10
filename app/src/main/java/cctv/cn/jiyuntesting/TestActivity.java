package cctv.cn.jiyuntesting;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cctv.cn.jiyuntesting.base.App;
import cctv.cn.jiyuntesting.common.Urls;
import cctv.cn.jiyuntesting.entity.Student;
import cctv.cn.jiyuntesting.entity.TestEntity;
import cctv.cn.jiyuntesting.model.RegisterRequest;
import cctv.cn.jiyuntesting.model.TestRequest;
import cctv.cn.jiyuntesting.model.http.MyCallBack;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * Created by 张志远 on 2017/5/4.
 */

public class TestActivity extends AppCompatActivity implements MyCallBack<TestEntity>, VerticalTabLayout.OnTabSelectedListener {

    @BindView(R.id.test_tab_layout)
    VerticalTabLayout textTabLayout;
    @BindView(R.id.my_test_text)
    TextView myTestText;
    @BindView(R.id.radio_A)
    RadioButton radioA;
    @BindView(R.id.radio_B)
    RadioButton radioB;
    @BindView(R.id.radio_C)
    RadioButton radioC;
    @BindView(R.id.radio_D)
    RadioButton radioD;
    @BindView(R.id.the_next_question)
    Button theNextQuestion;
    @BindView(R.id.test_image)
    ImageView testImage;

    public static final int MSG_SINGLE_CODE = 45;

    public static final int MSG_RESULT_CODE = 123;
    @BindView(R.id.my_chrometer)
    TextView myChrometer;
    @BindView(R.id.complete_btn)
    Button completeBtn;
    @BindView(R.id.my_radio_group)
    RadioGroup myRadioGroup;

    private List<TestEntity.MapBean.SingleBean> singleBeanList;

    private List<String> states;

    private int seconds = 0;

    private List<String> notCheckQuestion;

    private int curPosition;

    public static final int MSG_CODE = 2;

    public static final int MSG_SECONDE_CODE = 3;

    public static final int MSG_DIALOG_CODE=195;
    private List<String> theSingleAnswer;
    private int theRightCount;
    private String uId;
    private String num;
    private PopupWindow popWindow;
    private HomeReciver homeReciver;
    private String peroid;
    private View parentView;
    private View view;

    public static final int MSG_SHOW_DIALOG_CODE=250;

    private int cheatCount=0;

    private boolean isCheat=false;
    private Intent intent;
    private RegisterRequest registerRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = LayoutInflater.from(this).inflate(R.layout.activity_test,null);
        setContentView(parentView);
        ButterKnife.bind(this);
        homeReciver = new HomeReciver();
        registerReceiver(homeReciver,new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        App.activity = this;
        notCheckQuestion = new ArrayList<>();

        registerRequest = new RegisterRequest();

        intent = getIntent();

        states = new ArrayList<>();

        textTabLayout.setOnTabSelectedListener(this);

        view = LayoutInflater.from(this).inflate(R.layout.loading_balls,null);

        popWindow = new PopupWindow();

        popWindow.setContentView(view);
        popWindow.setHeight(dip2px(200));
        popWindow.setWidth(dip2px(200));
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        getWindow().setAttributes(lp);

        popWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));

        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (isCheat){
                    View view=LayoutInflater.from(TestActivity.this).inflate(R.layout.cheat_window,null);

                    PopupWindow popupWindow=new PopupWindow();

                    popupWindow.setContentView(view);

                    popupWindow.setHeight(dip2px(150));

                    popupWindow.setWidth(dip2px(130));

                    popupWindow.setBackgroundDrawable(new ColorDrawable());

                    popupWindow.setOutsideTouchable(true);

                    TextView warningView= (TextView) view.findViewById(R.id.warning_text);

                    warningView.setText("你还有"+dig2Chinese(3-cheatCount)+"次考试机会");

                    popupWindow.showAtLocation(view,Gravity.CENTER,0,0);

                }
            }
        });

        getTests();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(MSG_SHOW_DIALOG_CODE,500);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void getTests() {
        TestRequest testRequest = new TestRequest();

        Intent intent = getIntent();

        Map<String, String> params = new HashMap<>();

        uId = intent.getStringExtra("uId");

        num = intent.getStringExtra("num");

        peroid = intent.getStringExtra("period");
        params.put("num", num);

        params.put("period", peroid);

        params.put("uId", uId);

        testRequest.getTest(Urls.BASE_URL + Urls.TEST_URL, params, this);
    }

    @OnClick({R.id.radio_A, R.id.radio_B, R.id.radio_C, R.id.radio_D, R.id.the_next_question, R.id.complete_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.radio_A:
                states.set(curPosition, "A");
                break;
            case R.id.radio_B:
                states.set(curPosition, "B");
                break;
            case R.id.radio_C:
                states.set(curPosition, "C");
                break;
            case R.id.radio_D:
                states.set(curPosition, "D");
                break;
            case R.id.the_next_question:
                if (curPosition == states.size() - 1) {
                    textTabLayout.setTabSelected(0);
                } else {
                    textTabLayout.setTabSelected(curPosition + 1);
                }
                break;
            case R.id.complete_btn:
                obtainResult();
                break;
        }
    }

    private void obtainResult() {
        notCheckQuestion.clear();
        for (int i = 0; i < states.size(); i++) {
            if (states.get(i).equals("")) {
                notCheckQuestion.add("第" + dig2Chinese(i + 1) + "题");
            }
        }
        if (notCheckQuestion.size() >= 1) {

            StringBuffer sb = new StringBuffer();

            sb.append("你还有");
            for (int i = 0; i < notCheckQuestion.size(); i++) {
                sb.append(notCheckQuestion.get(i) + "、");
            }
            sb.deleteCharAt(sb.length() - 1).append("没有做，确定要交卷？");

            showResultDialog(sb);


        } else {

            StringBuffer sb = new StringBuffer("时间还没到，确定要交卷？");

            showResultDialog(sb);
        }
    }

    private void showResultDialog(StringBuffer sb) {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(sb).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendResult();

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();

        dialog.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getTests();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        getWindow().setAttributes(lp);
        popWindow.showAtLocation(getWindow().getDecorView().findViewById(android.R.id.content), Gravity.CENTER,0,0);
        if (cheatCount==2){
            android.os.Process.killProcess(android.os.Process.myPid());
        }

        Map<String,String> params=new HashMap<>();

        params.put("name", intent.getStringExtra("name"));

        intent.getStringExtra("gender");

        params.put("gender", intent.getStringExtra("gender"));

        params.put("idCard", intent.getStringExtra("idCard"));

        params.put("age", intent.getStringExtra("age"));

        params.put("education", intent.getStringExtra("education"));

        params.put("periodval",intent.getStringExtra("periodval"));
        registerRequest.getRegisterMsg(Urls.BASE_URL + Urls.REGISTER_URL, params, new MyCallBack<Student>() {
            @Override
            public void onSuccess(Student student) {
                student.getUId();
            }

            @Override
            public void onFailed(String errorStr) {
                errorStr.toString();
            }
        });
        cheatCount++;
        isCheat=true;
    }

    private void sendResult() {
        theRightCount = 0;
        for (int i = 0; i < states.size(); i++) {
            if (states.get(i).equals(theSingleAnswer.get(i))) {
                theRightCount++;
            }
        }
        int scope = theRightCount * 2;
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("uId", uId);
        intent.putExtra("num", num);
        intent.putExtra("scope", scope+"");
        intent.putExtra("cheat",homeReciver.ischeat+"");
        finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        startActivity(intent);

    }

    @Override
    public void onSuccess(TestEntity testEntity) {
        states.clear();
        if (testEntity.getResult().equals("success")) {

            theSingleAnswer = testEntity.getSingleAnswer();

            TestEntity.MapBean map = testEntity.getMap();

            singleBeanList = map.getSingle();

            for (int i = 0; i < singleBeanList.size(); i++) {

                textTabLayout.addTab(new QTabView(this).setTitle(new QTabView.TabTitle.Builder(this).setTextSize(20).setContent("第" + dig2Chinese(i + 1) + "题").build()));

                states.add("");
            }

        } else {
            Toast.makeText(this, "申请失败请检查网络是否正确", Toast.LENGTH_SHORT).show();
        }

        handler.removeMessages(MSG_CODE);

        handler.removeMessages(MSG_SINGLE_CODE);

        handler.removeMessages(MSG_RESULT_CODE);

        handler.removeMessages(MSG_DIALOG_CODE);

        seconds = 0;

        handler.sendEmptyMessageDelayed(MSG_CODE, 1200000);

        handler.sendEmptyMessageDelayed(MSG_SINGLE_CODE, 4000);

        handler.sendEmptyMessageDelayed(MSG_RESULT_CODE, 1800000);

        handler.sendEmptyMessage(MSG_DIALOG_CODE);

        showQuestionByPosition(0);

        textTabLayout.setTabSelected(0);
    }

    @Override
    public void onFailed(String errorStr) {
        Toast.makeText(this, "申请失败请检查网络", Toast.LENGTH_SHORT).show();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CODE:
                    myChrometer.setTextColor(Color.parseColor("#FF0000"));
                    Toast.makeText(TestActivity.this, "你还有十分钟答题时间", Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(MSG_SECONDE_CODE, 300000);
                    break;
                case MSG_SECONDE_CODE:
                    Toast.makeText(TestActivity.this, "你还有五分钟答题时间", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_SINGLE_CODE:
                    seconds = seconds + 1;
                    myChrometer.setText(CountToTime(seconds));
                    handler.sendEmptyMessageDelayed(MSG_SINGLE_CODE, 1000);
                    break;
                case MSG_RESULT_CODE:
                    sendResult();
                    break;
                case MSG_DIALOG_CODE:
                    popWindow.dismiss();
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1.0f; //0.0-1.0
                    getWindow().setAttributes(lp);
                    break;
                case MSG_SHOW_DIALOG_CODE:
                    popWindow.showAtLocation(getWindow().getDecorView().findViewById(android.R.id.content), Gravity.CENTER,0,0);
                    break;
            }
        }
    };

    private String CountToTime(int seconds) {
        int mSeconds = 60 - (seconds % 60);
        int minute;
        if (seconds == 0) {
            minute = 0;
        } else {
            minute = 29 - seconds / 60;
        }
        if (seconds % 60 == 0) {
            mSeconds = 0;
        }
        if (minute < 10 && mSeconds < 10)
            return "0" + minute + ":0" + mSeconds;
        if (minute < 10 && mSeconds >= 10)
            return "0" + minute + ":" + mSeconds;
        if (minute >= 10 && mSeconds < 10)
            return minute + ":0" + mSeconds;

        return minute + ":" + mSeconds;
    }


    private String dig2Chinese(int i) {

        String str = new String();

        str = str.valueOf(i);

        if (str.length() == 1) {

            char[] chars = str.toCharArray();

            return num2Char(chars[0]) + "";

        } else if (str.length() == 2) {
            char[] chars = str.toCharArray();
            if (chars[0] == '1') {
                if (chars[1] == '0') {
                    return "十";
                } else {
                    return "十" + num2Char(chars[1]);
                }

            } else {
                if (chars[1] == '0') {
                    return num2Char(chars[0]) + "十";
                } else {
                    return num2Char(chars[0]) + "十" + num2Char(chars[1]);
                }

            }
        }
        return "";
    }

    private char num2Char(char num) {
        switch (num) {
            case '1':
                return '一';
            case '2':
                return '二';
            case '3':
                return '三';
            case '4':
                return '四';
            case '5':
                return '五';
            case '6':
                return '六';
            case '7':
                return '七';
            case '8':
                return '八';
            case '9':
                return '九';
            case '0':
                return '零';
        }
        return ' ';
    }


    @Override
    public void onTabSelected(TabView tab, int position) {
        showQuestionByPosition(position);

    }

    private void showQuestionByPosition(int position) {
        TestEntity.MapBean.SingleBean singleBean = singleBeanList.get(position);
        myTestText.setText(singleBean.getSubject());
        List<List<String>> option = singleBean.getOption();
        curPosition = position;
        for (int i = 0; i < option.size(); i++) {

            List<String> mOption = option.get(i);
            switch (mOption.get(0)) {
                case "A":
                    radioA.setText("A:" + mOption.get(1));
                    break;
                case "B":
                    radioB.setText("B:" + mOption.get(1));
                    break;
                case "C":
                    radioC.setText("C:" + mOption.get(1));
                    break;
                case "D":
                    radioD.setText("D:" + mOption.get(1));
                    break;
            }


        }
        switch (states.get(position)) {
            case "A":
                radioA.setChecked(true);
                break;
            case "B":
                radioB.setChecked(true);
                break;
            case "C":
                radioC.setChecked(true);
                break;
            case "D":
                radioD.setChecked(true);
                break;
            case "":
                myRadioGroup.clearCheck();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            Toast.makeText(this, "考试期间不能点击返回。", Toast.LENGTH_SHORT).show();
            return true;
        }else if (keyCode==KeyEvent.KEYCODE_MENU){
            Toast.makeText(this, "考试期间不能使用菜单键。", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTabReselected(TabView tab, int position) {

    }

    public int dip2px(int dpValue){

        return (int) (getResources().getDisplayMetrics().density*dpValue+0.5f);
    }
}
