package com.sdg.updateapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.sdg.update.interfac.CheckCallback;
import com.sdg.update.utils.VersionUtil;

public class MainActivity extends AppCompatActivity {

    //正式版本升级路径
    public static final String AppUpdateUrl = "http://120.79.146.36:18095/icsscore/api/getAppInfo";
    public static final String appHttp = "http://120.79.146.36:18095";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VersionUtil.getInstance().checkVersion(this, AppUpdateUrl, new CheckCallback() {
            @Override
            public void update() {
                Log.i("MainActivity","update");
            }

            @Override
            public void noNewVersion() {
                Log.i("MainActivity","noNewVersion");
            }

            @Override
            public void cancelUpdate() {
                Log.i("MainActivity","noNewVersion");
            }
        });
    }
}
