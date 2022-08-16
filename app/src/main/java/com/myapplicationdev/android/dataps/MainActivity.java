package com.myapplicationdev.android.dataps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvTraffic;
    AsyncHttpClient client;
    ArrayList<Traffic> alTraffic;
    ArrayAdapter<Traffic> aaTraffic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTraffic = findViewById(R.id.lvTraffic);
        client = new AsyncHttpClient();
        alTraffic = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        client.get("https://api.data.gov.sg/v1/transport/traffic-images",
                new JsonHttpResponseHandler() {

                    String timestamp;
                    String camera_id;

                    @Override
                    public void onSuccess(int statusCode, PreferenceActivity.Header[] headers, JSONObject response) {
                        try {
                            JSONArray jsonArrItems = response.getJSONArray("items");
                            JSONObject firstObj = jsonArrItems.getJSONObject(0);
                            JSONArray jsonArrForecasts = firstObj.getJSONArray("forecasts");
                            for (int i = 0; i < jsonArrForecasts.length(); i++) {
                                JSONObject jsonObjForecast = jsonArrForecasts.getJSONObject(i);
                                timestamp = jsonObjForecast.getString("timestamp");
                                camera_id = jsonObjForecast.getString("camera_id");
                                Traffic traffic = new Traffic(timestamp, camera_id);
                                alTraffic.add(traffic);
                            }
                        } catch (JSONException e) {

                        }

                        //POINT X – Code to display List View
                        aaTraffic = new ArrayAdapter<Traffic>(MainActivity.this, android.R.layout.simple_list_item_1, alTraffic);
                        lvTraffic.setAdapter(aaTraffic);


                    }//end onSuccess
                });
    }//end onResume
}