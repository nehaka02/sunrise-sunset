package com.example.blueprintapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import android.content.Intent;

import android.widget.TextView;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;


public class MainActivity extends AppCompatActivity {
    private TextView mTextViewResult;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        mQueue = Volley.newRequestQueue(this);

        Button next = (Button) findViewById(R.id.button);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("info","BEFORE entering JSONParse");
                System.out.println("before");
                jsonParse(view);
                Log.d("info","AFTER finishing JSONParse");
                System.out.println("after");
            }
        });

    }

    private void jsonParse(View view) {
        Log.d("info","INSIDE entering JSONParse");
        String[] coordinates = getCoordinates(view);
        StringBuilder searchParams = new StringBuilder();
        searchParams.append("?lat=");
        searchParams.append(coordinates[0]);
        searchParams.append("&lng=");
        searchParams.append(coordinates[1]);

        String url = "https://api.sunrise-sunset.org/json" + searchParams.toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String sunrise = "";
                        String sunset = "";
                        String status =  "";

                        try {
                            Log.d("response", response.toString());
                            if (response != null) {

                                status = (String)response.get("status");
                                if(status.equalsIgnoreCase("OK")) {

                                    JSONObject results =(JSONObject)response.get("results");
                                    if(results != null) {

                                        sunrise = (String)results.get("sunrise");
                                        sunset = (String)results.get("sunset");
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("info", sunrise);
                        Log.d("info", sunset);
                        TextView vtyay = (TextView)findViewById(R.id.textView2);
                        vtyay.setText("Sunrise: "+sunrise);
                        TextView tv = (TextView)findViewById(R.id.textView3);
                        tv.setText("Sunset: "+sunset);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        mQueue.add(request);
    }

        public void receiveInput (View v){
            EditText x = findViewById(R.id.latitude);
            EditText y = findViewById(R.id.longitude);

            String lat = x.getText().toString();
            String lon = y.getText().toString();

            Log.d("info", lat);
            Log.d("info", lon);
        }

        public String[] getCoordinates(View v){
            EditText x = findViewById(R.id.latitude);
            EditText y = findViewById(R.id.longitude);

            String lat = x.getText().toString();
            String lon = y.getText().toString();

            String[] coordinates = new String[2];
            coordinates[0] = lat;
            coordinates[1] = lon;
            return coordinates;
        }
}
