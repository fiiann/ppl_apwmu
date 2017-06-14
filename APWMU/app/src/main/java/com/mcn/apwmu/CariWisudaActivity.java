package com.mcn.apwmu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CariWisudaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVMahasiswa;
    private AdapterMahasiswa mAdapter;
    public String fakultas;
    public String URL_DETAIL_FAKULTAS="http://192.168.1.31/kuliah/ppl/driver_api/detail_fakultas.php?fakultas=";
    public String URL_DETAIL_PERIODE="http://192.168.1.31/kuliah/ppl/api/detail_periode.php?periode=";
    public String url_tahun="http://192.168.1.31/kuliah/ppl/api/detail_tahun.php";
    public String provinsi;
    public String periode;
    public String tahun_wisuda;
    public EditText cari;
    List<DataMahasiswa> data;
    List<String> data1;
    DataMahasiswa data_mhs;

    //Declaring an Spinner
    private Spinner spinner4;

    //An ArrayList for Spinner Items
    private ArrayList<String> tahun_array;

    //JSON Array
    public static final String JSON_ARRAY_TAHUN = "result";
    public static final String nama_tahun = "tahun";
    private JSONArray result;


    //    on create activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_wisuda);

//        =====================================================
        tahun_array = new ArrayList<String>();

        //Initializing Spinner
        spinner4 = (Spinner) findViewById(R.id.spinner4);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        spinner4.setOnItemSelectedListener(this);
        getData();
//        =====================================================
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fakultas_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        new AsyncFetch().execute();
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
//
        final List<String> tahun = new ArrayList<String>();
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.provinsi_array, android.R.layout.simple_spinner_item);
//// Specify the layout to use when the list of choices appears
//
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//// Apply the adapter to the spinner
//
        spinner2.setAdapter(adapter1);
        spinner2.setOnItemSelectedListener(this);



        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.periode_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner

        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(this);
//        spinner2.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        cari = (EditText) findViewById(R.id.search);




        //Make call to AsyncTask

    }

//    ====================================================
private void getData(){
    //Creating a string request
    StringRequest stringRequest = new StringRequest(url_tahun,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject j = null;
                    try {
                        //Parsing the fetched Json String to JSON Object
                        j = new JSONObject(response);

                        //Storing the Array of JSON String to our JSON Array
//                        result = j.getJSONArray();

                        //Calling method getTahun to get the students from the JSON Array
                        getTahunArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

    //Creating a request queue
    RequestQueue requestQueue = Volley.newRequestQueue(this);

    //Adding request to the queue
    requestQueue.add(stringRequest);
}

    private void getTahunArray(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                tahun_array.add(json.getString(nama_tahun));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner4.setAdapter(new ArrayAdapter<String>(CariWisudaActivity.this, android.R.layout.simple_spinner_dropdown_item, tahun_array));
    }

    //Method to get student name of a particular position
    private String getTahun(int position){
        String tahun="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            tahun = json.getString("tahun");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return tahun;
    }
//    ====================================================


    //    on spinner selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        fakultas= parent.getItemAtPosition(position).toString();
        provinsi= parent.getItemAtPosition(position).toString();
        periode= parent.getItemAtPosition(position).toString();
        tahun_wisuda= parent.getItemAtPosition(position).toString();
        // Spinner view value
//        String spinner11 = spinner1.getSelectedItem().toString().trim();
        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.spinner1)
        {
            new AsyncFetch1().execute();
        }
        else if(spinner.getId() == R.id.spinner2)
        {
            Toast.makeText(this,provinsi,Toast.LENGTH_SHORT).show();
        }
        else if(spinner.getId() == R.id.spinner3)
        {
            //do this
//            Toast.makeText(this,periode,Toast.LENGTH_SHORT).show();
            new AsyncFetch2().execute();
        }



    }

    //  on spinner not selected
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        new AsyncFetch().execute();
    }


    //    read all
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(CariWisudaActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

//                url = new URL("http://192.168.43.188/kuliah/ppl/driver_api/read.php");
                url = new URL("http://192.168.1.4/kuliah/ppl/driver_api/read.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            List<DataMahasiswa> data=new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataMahasiswa data_mhs = new DataMahasiswa();
                    data_mhs.m_nama= json_data.getString("username");
                    data_mhs.m_nim= json_data.getString("nim");
                    data_mhs.m_fakultas= json_data.getString("fakultas");
                    data_mhs.m_jurusan= json_data.getString("jurusan");
                    data_mhs.m_foto= json_data.getString("foto");
//                    Toast.makeText(CariWisudaActivity.this, data_mhs.m_nama, Toast.LENGTH_LONG).show();
                    data.add(data_mhs);
                }

                // Setup and Handover data to recyclerview
                mRVMahasiswa = (RecyclerView)findViewById(R.id.dataMahasiswaList);
                mAdapter = new AdapterMahasiswa(CariWisudaActivity.this, data);
                mRVMahasiswa.setAdapter(mAdapter);

                mRVMahasiswa.setLayoutManager(new LinearLayoutManager(CariWisudaActivity.this));


            } catch (JSONException e) {
                Toast.makeText(CariWisudaActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }


    //    detail fakultas
    private class AsyncFetch1 extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(CariWisudaActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
//                url = new URL("http://192.168.43.188/kuliah/ppl/driver_api/detail_fakultas.php?fakultas="+fakultas);
//                url = new URL("http://192.168.1.26/kuliah/ppl/driver_api/detail_fakultas.php?fakultas="+fakultas);
                url = new URL(URL_DETAIL_FAKULTAS+fakultas);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        public void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            data=new ArrayList<>();
            data1=new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    data_mhs = new DataMahasiswa();
                    data_mhs.m_nama= json_data.getString("username");
                    data_mhs.m_nim= json_data.getString("nim");
                    data_mhs.m_fakultas= json_data.getString("fakultas");
                    data_mhs.m_jurusan= json_data.getString("jurusan");
                    data_mhs.m_foto= json_data.getString("foto");
//                    Toast.makeText(CariWisudaActivity.this, data_mhs.m_nama.toString(), Toast.LENGTH_LONG).show();
                    data.add(data_mhs);
                    data1.add(data_mhs.m_nama);

//                    data1.add(data_mhs.m_nama);
//                    data1.add(data_mhs.m_fakultas);
//                    data1.add(data_mhs.m_nim);
                }

                // Setup and Handover data to recyclerview
                mRVMahasiswa = (RecyclerView)findViewById(R.id.dataMahasiswaList);
                mRVMahasiswa.setHasFixedSize(true);

//                mAdapter = new AdapterMahasiswa(CariWisudaActivity.this, data1);
                mAdapter = new AdapterMahasiswa(CariWisudaActivity.this, data);
                mRVMahasiswa.setAdapter(mAdapter);
                if (mAdapter.getItemCount()==0) {
                    Toast.makeText(CariWisudaActivity.this,"Data kosong",Toast.LENGTH_SHORT).show();
                }
                mRVMahasiswa.setLayoutManager(new LinearLayoutManager(CariWisudaActivity.this));
                addTextListener();

            } catch (JSONException e) {
                Toast.makeText(CariWisudaActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    //    detail periode
    private class AsyncFetch2 extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(CariWisudaActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
//                url = new URL("http://192.168.43.188/kuliah/ppl/api/detail_periode.php?periode="+periode);
//                url = new URL("http://192.168.1.26/kuliah/ppl/api/detail_periode.php?periode="+periode);
                url = new URL(URL_DETAIL_PERIODE+periode);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        public void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();
            data=new ArrayList<>();
            data1=new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    data_mhs = new DataMahasiswa();
                    data_mhs.m_nama= json_data.getString("username");
                    data_mhs.m_nim= json_data.getString("nim");
//                    data_mhs.m_periode= json_data.getString("periode");

//                    Toast.makeText(CariWisudaActivity.this, data_mhs.m_nama.toString(), Toast.LENGTH_LONG).show();
                    data.add(data_mhs);
                    data1.add(data_mhs.m_nama);

//                    data1.add(data_mhs.m_nama);
//                    data1.add(data_mhs.m_fakultas);
//                    data1.add(data_mhs.m_nim);
                }

                // Setup and Handover data to recyclerview
                mRVMahasiswa = (RecyclerView)findViewById(R.id.dataMahasiswaList);
                mRVMahasiswa.setHasFixedSize(true);
//                mAdapter = new AdapterMahasiswa(CariWisudaActivity.this, data1);
                mAdapter = new AdapterMahasiswa(CariWisudaActivity.this, data);
                mRVMahasiswa.setAdapter(mAdapter);
                if (mAdapter.getItemCount()==0) {
                    Toast.makeText(CariWisudaActivity.this,"Data kosong",Toast.LENGTH_SHORT).show();
                }
                mRVMahasiswa.setLayoutManager(new LinearLayoutManager(CariWisudaActivity.this));
                addTextListener1();



            } catch (JSONException e) {
                Toast.makeText(CariWisudaActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

    //    search onlistener fakultas
    public void addTextListener(){
        cari.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

//                    final ArrayList<String> filteredList = new ArrayList<>();
                final List<DataMahasiswa> data_filter=new ArrayList<>();

                for (int i = 0; i < data.size(); i++) {

                    final String cari_nama = data.get(i).m_nama.toString().toLowerCase();
                    final String cari_nim = data.get(i).m_nim.toString().toLowerCase();
                    final String cari_fakultas = data.get(i).m_fakultas.toString().toLowerCase();

                    if (cari_nama.contains(query)||cari_nim.contains(query)||cari_fakultas.contains(query)) {

                        data_filter.add(data.get(i));
                    }
                }

                mRVMahasiswa.setLayoutManager(new LinearLayoutManager(CariWisudaActivity.this));
                mAdapter = new AdapterMahasiswa(CariWisudaActivity.this, data_filter);
                mRVMahasiswa.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }

    //    search onlistener periode
    public void addTextListener1(){
        cari.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

//                    final ArrayList<String> filteredList = new ArrayList<>();
                final List<DataMahasiswa> data_filter=new ArrayList<>();

                for (int i = 0; i < data.size(); i++) {

                    final String cari_nama = data.get(i).m_nama.toString().toLowerCase();
                    final String cari_nim = data.get(i).m_nim.toString().toLowerCase();
//                    final String cari_periode= data.get(i).m_periode.toString().toLowerCase();

                    if (cari_nama.contains(query)||cari_nim.contains(query)) {

                        data_filter.add(data.get(i));
                    }
                }

                mRVMahasiswa.setLayoutManager(new LinearLayoutManager(CariWisudaActivity.this));
                mAdapter = new AdapterMahasiswa(CariWisudaActivity.this, data_filter);
                mRVMahasiswa.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }
}

