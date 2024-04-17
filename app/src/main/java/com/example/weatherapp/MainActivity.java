package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    final String API = "57a1582759cafc47aa1681302737968d";
    final String weatherUrl = "https://api.openweathermap.org/data/2.5/weather";
    final long minTime = 5000;
    final float minDistance = 1000;
    final int reqCode = 101;
    final int REQUEST_CODE = 102; // Define REQUEST_CODE constant

    String LocationProvider = LocationManager.GPS_PROVIDER;
    TextView nameofcity, weatherstate, temp;
    ImageView weathericon;
    LocationManager mlm;
    LocationListener mll;
    EditText edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weathericon = findViewById(R.id.icon);
        temp = findViewById(R.id.temp);
        weatherstate = findViewById(R.id.weathercond);
        nameofcity = findViewById(R.id.cityname);
        edt=findViewById(R.id.entercityname);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getweatherofcurrentlocation();
    }

    @SuppressLint("MissingPermission")
    private void getweatherofcurrentlocation() {
        mlm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mll = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());
                RequestParams params = new RequestParams();
                params.put("lat", Latitude);
                params.put("lon", Longitude);
                params.put("appid", API);
                letsdosomenetworking(params);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {}

            @Override
            public void onProviderDisabled(@NonNull String provider) {}

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
        };

        // Check if permission is granted before requesting location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        mlm.requestLocationUpdates(LocationProvider, minTime, minDistance, mll);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == reqCode) {
            if (grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Location Fetched", Toast.LENGTH_SHORT).show();
                getweatherofcurrentlocation();
            }
        }
    }

    private void letsdosomenetworking(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(weatherUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(MainActivity.this, "Data Get Success", Toast.LENGTH_SHORT).show();
                weatherdata weatherData = weatherdata.fromJson(response);
                updateUI(weatherData);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(MainActivity.this, "Data Get Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUI(weatherdata weather) {
        temp.setText(weather.getmTemp());
        nameofcity.setText(weather.getmCity());
        weatherstate.setText(weather.getMweatherType());
        int resourceID = getResources().getIdentifier(weather.getmIcon(), "drawable", getPackageName());
        weathericon.setImageResource(resourceID);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mlm != null) {
            mlm.removeUpdates(mll);
        }
    }
}