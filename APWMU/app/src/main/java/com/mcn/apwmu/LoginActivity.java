package com.mcn.apwmu;




        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.android.volley.AuthFailureError;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.Collection;
        import java.util.HashMap;
        import java.util.Map;
        import java.util.Set;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String LOGIN_URL = "http://192.168.1.15/ppl/api/login.php";

    public static final String KEY_NIM="nim";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_USERNAME="username";

    private EditText editTextNim;
    private EditText editTextPassword;
    private Button buttonLogin;

    private String nim;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextNim= (EditText) findViewById(R.id.nim);
        editTextPassword = (EditText) findViewById(R.id.password);

        buttonLogin = (Button) findViewById(R.id.btn_sign_in);

        buttonLogin.setOnClickListener(this);
    }


    private void userLogin() {
        nim = editTextNim.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject mainObject = null;
                        try {
                            mainObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String r_error = null;
                        try {
                            r_error = mainObject.getString("error");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String r_apiKey = null;
                        String r_nim= null;
                        String r_username= null;
                        try {
                            r_apiKey = mainObject.getString("apiKey");
                            r_nim= mainObject.getString("nim");
                            r_username= mainObject.getString("username");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();


                        editor.putString("apiKey", r_apiKey); // Storing string
                        editor.putString("nim", r_nim);
                        editor.putString("username", r_username);
//

                        editor.commit(); // commit changes

                        String apiKey = pref.getString("apiKey", null); // getting String
                        String nim= pref.getString("nim", null); // getting Integer
                        String username= pref.getString("username", null); // getting Integer
//


                        Toast.makeText(LoginActivity.this,nim,Toast.LENGTH_LONG).show();

                        if(apiKey!=null && nim!=null){
                            Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                            intent.putExtra(KEY_NIM, nim);
                            intent.putExtra(KEY_USERNAME, username);
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_NIM,nim);
                map.put(KEY_PASSWORD,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        userLogin();
    }
}