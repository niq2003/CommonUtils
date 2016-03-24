package me.joni.util;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.joni.util.log.CommonLogger;
import me.joni.util.log.SimpleLogger;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CommonLogger.d(TAG, TAG + " oncreate.");
        CommonLogger.e(TAG, "error message");
        CommonLogger.e("heheheehehe");
        SimpleLogger.e("MainActivity", "simple logger");
    }
}
