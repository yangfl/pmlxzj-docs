package com.tlxsoft.lxeplayerapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_about);
        ((TextView) findViewById(R.id.textView_web)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("www.tlxsoft.com/lxeplayer.htm"));
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                AboutActivity.this.startActivity(intent);
            }
        });
    }
}
