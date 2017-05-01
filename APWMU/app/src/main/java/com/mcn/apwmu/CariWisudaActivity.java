package com.mcn.apwmu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CariWisudaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_wisuda);
    }
    public void cariWisudawan(View view){
        Intent myintent = new Intent(this, DaftarWisudawanActivity.class);
        startActivity(myintent);
    }
}
