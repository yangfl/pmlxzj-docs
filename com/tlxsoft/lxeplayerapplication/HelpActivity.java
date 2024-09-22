package com.tlxsoft.lxeplayerapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_help);
        ((TextView) findViewById(R.id.textView)).setText(R.string.HELP1);
    }
}
