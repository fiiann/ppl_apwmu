package com.mcn.apwmu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mcn.apwmu.app.AppController;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mcn.apwmu.LoginActivity.KEY_NIM;
import static com.mcn.apwmu.LoginActivity.KEY_USERNAME;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView nama;
    private TextView nim;
    private ProgressDialog dialog;
    private TextView fakultas;
    private TextView jurusan;
    private TextView link_foto;
    private TextView link_status;
    public static final String KEY_PRODI="prodi";
    public static final String KEY_FAKULTAS="fakultas";
    public static final String KEY_FOTO="foto";
    public static final String KEY_STATUS="status";
    private ImageView foto;
    private ImageView status;
    private ImageLoader imageLoader;//foto mahasiswa
    private ImageLoader imageLoader1;//status verifikasi

//    oncreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nama = (TextView) findViewById(R.id.d_nama);
        nim = (TextView) findViewById(R.id.d_nim);
        fakultas= (TextView) findViewById(R.id.d_fakultas);
        jurusan = (TextView) findViewById(R.id.d_prodi);
        link_foto = (TextView) findViewById(R.id.foto);
        link_status= (TextView) findViewById(R.id.text_status);
        foto = (ImageView) findViewById(R.id.d_foto);
        status= (ImageView) findViewById(R.id.d_status);


//        nav_nama1 = (TextView) findViewById(R.id.nav_nama);

        Intent intent = getIntent();
        String dashboard_nim = intent.getStringExtra(KEY_NIM);
        String dashboard_nama= intent.getStringExtra(KEY_USERNAME);
        String nav_nama= intent.getStringExtra(KEY_USERNAME);
        String dashboard_foto = intent.getStringExtra(KEY_FOTO);
        final String dashboard_status= intent.getStringExtra(KEY_STATUS);
        link_foto.setText(dashboard_foto);
        link_status.setText(dashboard_status);

        String nim1 = nim.toString();
        nama.setText(dashboard_nama);
        nim.setText(dashboard_nim);
//        nav_nama1.setText(dashboard_nama);
//        Toast.makeText(DashboardActivity.this, dashboard_nim, Toast.LENGTH_LONG).show();
//        String url = "http://192.168.43.188/kuliah/ppl/driver_api/detail.php?nim="+dashboard_nim;
        String url = "http://192.168.1.31/kuliah/ppl/driver_api/detail.php?nim="+dashboard_nim;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();

                        try {
                            JSONObject body = response.getJSONObject("results");
                            String faculty = body.getString("fakultas");
                            String photo = body.getString("foto");
                            fakultas.setText(faculty);
                            String prodi = body.getString("jurusan");
                            jurusan.setText(prodi);
                            link_foto.setText(photo);
                            String link = link_foto.getText().toString().trim();
//                            Toast.makeText(DashboardActivity.this, "link: "+link,Toast.LENGTH_SHORT).show();
                            ImageView foto = (ImageView) findViewById(R.id.d_foto);
                            ImageView status= (ImageView) findViewById(R.id.d_status);
//                            ImageView imageView1 = (ImageView) findViewById(R.id.status);


                            imageLoader = ImageLoader.getInstance();
//                            imageLoader1 = ImageLoader.getInstance();
                            imageLoader.init(new ImageLoaderConfiguration.Builder(getApplicationContext()).build());
//                            imageLoader1.init(new ImageLoaderConfiguration.Builder(getApplicationContext()).build());
                            imageLoader.displayImage(link,foto);
//                            imageLoader1.displayImage(url_status,imageView1);

                            link_status.setText(dashboard_status);
                            String link_statuss = link_status.getText().toString().trim();


//                            Toast.makeText(DashboardActivity.this,link_statuss,Toast.LENGTH_SHORT).show();
                            imageLoader1 = ImageLoader.getInstance();
//                            imageLoader1 = ImageLoader.getInstance();
                            imageLoader1.init(new ImageLoaderConfiguration.Builder(getApplicationContext()).build());
//                            imageLoader1.init(new ImageLoaderConfiguration.Builder(getApplicationContext()).build());
                            imageLoader1.displayImage(link_statuss,status);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String r_fakultas= null;
                        String r_jurusan= null;

                        try {
                            JSONObject body = response.getJSONObject("results");
                            r_fakultas= body.getString("fakultas");
                            r_jurusan= body.getString("jurusan");
//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("fakultas", r_fakultas); // Storing string
                        editor.putString("jurusan", r_jurusan);


                        editor.commit(); // commit changes

                        String fakultas= pref.getString("fakultas", null); // getting String
                        String jurusan= pref.getString("jurusan", null); // getting Integer
//                        Toast.makeText(DashboardActivity.this,fakultas,Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DashboardActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

                String apiKey = pref.getString("apiKey", null);
                params.put("Authorization", apiKey);
                return params;
            }
        };
        AppController.getmInstance().addToRequesQueue(jsonObjectRequest);


    }

//    dialog
    public void hideDialog(){
        if (dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
    }
    //    public static final String key;

//    identitas pribadi intent
    public void identitasPribadi(View view){
        String namaku = nama.getText().toString().trim();
        String nimku = nim.getText().toString().trim();
        String fotoku = link_foto.getText().toString().trim();
//        String statusku= link_foto.getText().toString().trim();
//        Toast.makeText(this,fotoku,Toast.LENGTH_SHORT).show();
        Intent myintent = new Intent(this, f_identitas_pribadi.class);
        myintent.putExtra(KEY_NIM, nimku);
        myintent.putExtra(KEY_USERNAME, namaku);
        myintent.putExtra(KEY_FOTO, fotoku);
        startActivity(myintent);
    }


    //    informasi TA intent
    public void informasiTugasAkhir(View view){

        String nimku = nim.getText().toString().trim();
        String namaku = nama.getText().toString().trim();
        String fakultasku = fakultas.getText().toString().trim();
        String prodiku= jurusan.getText().toString().trim();
        String fotoku = link_foto.getText().toString().trim();
//        Toast.makeText(this,fotoku,Toast.LENGTH_SHORT).show();
        Intent myintents = new Intent(this, f_tugas_akhir.class);
        myintents.putExtra(KEY_NIM, nimku);
        myintents.putExtra(KEY_USERNAME, namaku);
        myintents.putExtra(KEY_FAKULTAS, fakultasku);
        myintents.putExtra(KEY_PRODI, prodiku);
        myintents.putExtra(KEY_FOTO, fotoku);
//        Toast.makeText(this,"fakultas: "+fakultasku+"jurusan "+prodiku,Toast.LENGTH_SHORT).show();
        startActivity(myintents);
    }

    //    Cari wisudawan intent
    public void cariWisudawanUndip(View view){
        Intent myintent = new Intent(this, CariWisudaActivity.class);
        startActivity(myintent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
//    nav bar samping
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            String namaku = nama.getText().toString().trim();
            String nimku= nim.getText().toString().trim();
            String statusku= link_status.getText().toString().trim();
            Intent myintent = new Intent(this, DashboardActivity.class);
            myintent.putExtra(KEY_USERNAME, namaku);
            myintent.putExtra(KEY_NIM, nimku);
            myintent.putExtra(KEY_STATUS, statusku);
            startActivity(myintent);
        } else if (id == R.id.nav_identitas) {
            String nimku = nim.getText().toString().trim();
            String fotoku = link_foto.getText().toString().trim();
            String namaku = nama.getText().toString().trim();
            Intent myintent = new Intent(this, f_identitas_pribadi.class);
            myintent.putExtra(KEY_NIM, nimku);
            myintent.putExtra(KEY_FOTO, fotoku);
            myintent.putExtra(KEY_USERNAME, namaku);
            startActivity(myintent);
        } else if (id == R.id.nav_ta) {
            String nimku = nim.getText().toString().trim();
            String namaku = nama.getText().toString().trim();
            String fakultasku = fakultas.getText().toString().trim();
            String prodiku= jurusan.getText().toString().trim();
            String fotoku = link_foto.getText().toString().trim();
            Intent myintents = new Intent(this, f_tugas_akhir.class);
            myintents.putExtra(KEY_NIM, nimku);
            myintents.putExtra(KEY_USERNAME, namaku);
            myintents.putExtra(KEY_FAKULTAS, fakultasku);
            myintents.putExtra(KEY_PRODI, prodiku);
            myintents.putExtra(KEY_FOTO, fotoku);
            startActivity(myintents);
        } else if (id == R.id.nav_cari) {
            Toast.makeText(this, "pindah ke cari", Toast.LENGTH_SHORT).show();
            Intent myintent = new Intent(this, PencarianActivity.class);
            startActivity(myintent);
        } else if (id == R.id.nav_jadwal) {
            Intent myintent = new Intent(this, JadwalWisudawan.class);
            startActivity(myintent);
        } else if (id == R.id.nav_logout) {
            Intent myintent = new Intent(this, MainActivity.class);
            startActivity(myintent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
