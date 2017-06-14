package com.mcn.apwmu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mcn.apwmu.app.AppController;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static com.mcn.apwmu.LoginActivity.KEY_NIM;
import static com.mcn.apwmu.LoginActivity.KEY_USERNAME;

public class f_identitas_pribadi extends AppCompatActivity {

    private String nama;
    TextView text_nama;
    private ProgressDialog dialog;
    private TextView jenis_kelamin;
    private TextView warga_negara;
    private TextView tempat_lahir;
    private TextView tanggal_lahir;
    private TextView email;
    private TextView alamat;
    private TextView telepon;
    private TextView pekerjaan;
    private TextView jurusan;
    private TextView fakultas;
    private TextView linkFoto;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_identitas_pribadi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        text_nama = (TextView) findViewById(R.id.d_namas);

        tanggal_lahir = (TextView) findViewById(R.id.d_tal);
        jenis_kelamin = (TextView) findViewById(R.id.d_jk);
        warga_negara= (TextView) findViewById(R.id.d_wn);
        tempat_lahir = (TextView) findViewById(R.id.d_tel);
        email= (TextView) findViewById(R.id.d_email);
        alamat= (TextView) findViewById(R.id.d_alamat);
        telepon= (TextView) findViewById(R.id.d_telp);
        pekerjaan = (TextView) findViewById(R.id.d_pekerjaan);
        fakultas= (TextView) findViewById(R.id.d_fakultass);
        jurusan= (TextView) findViewById(R.id.d_jurusann);
        linkFoto= (TextView) findViewById(R.id.i_link);


        Intent intent = getIntent();
        String dashboard_nama= intent.getStringExtra(KEY_USERNAME);
        String dashboard_nim= intent.getStringExtra(KEY_NIM);
        String dashboard_foto= intent.getStringExtra(DashboardActivity.KEY_FOTO);
//                Toast.makeText(this,dashboard_foto,Toast.LENGTH_SHORT).show();
        text_nama.setText(dashboard_nama);
        linkFoto.setText(dashboard_foto);
//        String url = "http://192.168.43.188/kuliah/ppl/driver_api/detail.php?nim="+dashboard_nim;
        String url = "http://192.168.1.31/kuliah/ppl/driver_api/detail.php?nim="+dashboard_nim;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();

                        try {
                            JSONObject body = response.getJSONObject("results");
                            jenis_kelamin.setText(body.getString("jenis_kelamin"));
                            warga_negara.setText(body.getString("warga_negara"));
                            tempat_lahir.setText(body.getString("tempat_lahir"));
                            tanggal_lahir.setText(body.getString("tanggal_lahir"));
                            email.setText(body.getString("email"));
                            alamat.setText(body.getString("alamat"));
                            telepon.setText(body.getString("telepon"));
                            pekerjaan.setText(body.getString("pekerjaan"));
                            fakultas.setText(body.getString("fakultas"));
                            jurusan.setText(body.getString("jurusan"));
                            String link = linkFoto.getText().toString().trim();
                            ImageView foto = (ImageView) findViewById(R.id.i_fotoo);
                            imageLoader = ImageLoader.getInstance();

                            imageLoader.init(new ImageLoaderConfiguration.Builder(getApplicationContext()).build());

                            imageLoader.displayImage(link,foto);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(f_identitas_pribadi.this,error.toString(),Toast.LENGTH_LONG).show();
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
    public void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
