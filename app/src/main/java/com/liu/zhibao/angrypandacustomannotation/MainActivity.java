package com.liu.zhibao.angrypandacustomannotation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

@ContentView(R.layout.activity_main)
public class MainActivity extends Activity {

    @ViewInject(R.id.button1)
    private Button mButton1;
    @ViewInject(R.id.button2)
    private Button mButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.injectContentView(this);
        ViewUtils.injectViews(this);
        ViewUtils.injectEvents(this);

    }

    @OnClick({R.id.button1, R.id.button2})
    public void clickBtnInvoked(View view) {
        switch (view.getId()) {
            case R.id.button1:
                Toast.makeText(this, "Button1 OnClick", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:
                Toast.makeText(this, "Button2 OnClick", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
