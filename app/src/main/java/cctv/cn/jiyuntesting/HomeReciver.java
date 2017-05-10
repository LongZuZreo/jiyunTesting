package cctv.cn.jiyuntesting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by 张志远 on 2017/5/7.
 */

public class HomeReciver extends BroadcastReceiver {

    static final String SYSTEM_REASON = "reason";
    static final String SYSTEM_HOME_KEY = "homekey";//home key
    static final String SYSTEM_RECENT_APPS = "recentapps";//long home key
   public boolean ischeat=false;
    private boolean isfirstClick=true;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_REASON);
            if (reason != null) {
                if (reason.equals(SYSTEM_HOME_KEY)) {
                    // home key处理点
                    if (isfirstClick) {
                        Toast.makeText(context, "已经重置考试试题", Toast.LENGTH_SHORT).show();
                    }
                } else if (reason.equals(SYSTEM_RECENT_APPS)) {
                    // long home key处理点
                }
            }
        }
    }
}
