package cn.tech.zhangls.mddemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.tech.zhangls.mddemo.home.activity.HomeActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Intent intent = new Intent(LaunchActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}