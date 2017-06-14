package com.mcn.apwmu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class JadwalWisudawan extends AppCompatActivity {

    private TextView t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_wisudawan);

        TextView t2 = (TextView)findViewById(R.id.link);
        t2.setClickable(true);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
