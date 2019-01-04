package com.yuntian.androidaop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yuntian.aoplib.annotation.DebugTrace;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv).setOnClickListener(v->{
            test();
        });
    }

    @DebugTrace
    public void  test(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
