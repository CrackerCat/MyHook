package org.xk.xpexample.frontend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.elvishew.xlog.XLog;
import com.elvishew.xlog.LogLevel;

import org.xk.xpexample.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XLog.init(LogLevel.ALL);
    }
}
