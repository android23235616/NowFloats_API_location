package com.example.pranshooverma.nowfloats_api_location;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView loc;
    Button map,search;
    EditText ip;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(internet_connectin())
                {
                    String ip_add=ip.getText().toString();
                    progress.setMessage("Fetching the Result");
                    progress.show();
                    progress.setCancelable(false);
                    call_api(ip_add);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
                    map.setVisibility(View.INVISIBLE);
                    loc.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    private void call_api(String ip_add) {
        String Url="https://api.ipdata.co/"+ip_add;

        StringRequest a=new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                try {
                    JSONObject a=new JSONObject(response);
                    String lat=a.getString("latitude");
                    String lon=a.getString("longitude");

                    loc.setText("Latitude = "+lat+"\n"+"Longitude = "+lon);

                    Intent fd=new Intent(getApplicationContext(),MapsActivity.class);
                    fd.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bun=new Bundle();
                    bun.putString("lat",lat);
                    bun.putString("lon",lon);
                    fd.putExtras(bun);
                    startActivity(fd);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                map.setVisibility(View.VISIBLE);
                loc.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }

                Toast.makeText(MainActivity.this, "Some Error Occured. Please try again", Toast.LENGTH_SHORT).show();
                map.setVisibility(View.INVISIBLE);
                loc.setVisibility(View.INVISIBLE);
            }
        });

        RequestQueue b= Volley.newRequestQueue(getApplicationContext());
        b.add(a);
    }

    private Boolean internet_connectin()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    private void initialize() {
        loc=(TextView) findViewById(R.id.textView);
        ip=(EditText) findViewById(R.id.ip);
        map=(Button) findViewById(R.id.map_intent);
        search=(Button) findViewById(R.id.search);
        progress=new ProgressDialog(this);
        map.setVisibility(View.INVISIBLE);
        loc.setVisibility(View.INVISIBLE);
    }
}
