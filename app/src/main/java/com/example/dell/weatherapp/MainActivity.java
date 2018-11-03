package com.example.dell.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/*import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;*/

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView cityName;
    TextView txtTime;
    Button btnSearch;
    ImageView img;
    TextView txtCity;
    TextView txtTemp;
    TextView txtMinMax;
    TextView txtWeather;
    TextView txtWeatherDescription;
    TextView txtHumidity;
    TextView txtCloudy;
    ConstraintLayout layoutWeather;
    JSONObject data;
    //private FusedLocationProviderClient fusedLocationClient;
    String currLat;
    String currLong;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeView();
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("abc", "onSuccess: inside if");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 8);
            Log.d("abc", "onSuccess: end if");
        }
        Log.d("abc", "onSuccess: outside if");
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                {
                    Log.d("abc", "onSuccess: inside");
                    currLat=Double.toString(location.getLatitude());
                    currLong=Double.toString(location.getLongitude());
                    Log.d("abc", "onSuccess: "+location.getLatitude()+" "+location.getLongitude());
                    initWeather();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("MapDemoActivity", "Error trying to get last GPS location");
                e.printStackTrace();
            }
        }); not working*/


        /*fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                    Log.d("abc", "onSuccess: inside");
                    Location location = (Location) task.getResult();
                    currLat=Double.toString(location.getLatitude());
                    currLong=Double.toString(location.getLongitude());
                    Log.d("abc", "onSuccess: "+location.getLatitude()+" "+location.getLongitude());
                    initWeather();
            }
        });*/
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutWeather.setVisibility(View.INVISIBLE);
                String city=cityName.getText().toString();
                Weather weather=new Weather();
                //getCurrentWeather(city);
                //data=weather.getCurrentCityWeather(getApplicationContext(), cityName.getText().toString());
                Weather.OPEN_WEATHER_MAP_API="http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=05e2d1a19fb1a1c19bd250764cfa3606&units=metric";
                try {
                    String data= new Weather().execute().get();
                    JSONObject dataObj = new JSONObject(data);
                    if(dataObj.getString("cod").equalsIgnoreCase("200")) {

                        layoutWeather.setVisibility(View.VISIBLE);
                        //txtCity.setText(dataObj.getString("name"));
                        txtCloudy.setText(dataObj.getString("clouds"));
                        Log.d("abc", "onClick: "+txtCloudy.getText().toString());
                        txtWeatherDescription.setText(dataObj.getString("main")+": "+dataObj.getString("description"));
                        //txtWeatherDescription.setText(dataObj.getString("description"));
                        txtTemp.setText(dataObj.getString("temp")+"°C");
                        txtMinMax.setText("Min " + dataObj.getString("temp_min") + "°C, Max " + dataObj.getString("temp_max") + "°C");
                        txtHumidity.setText(dataObj.getString("humidity")+"%");
                        txtCloudy.setText(dataObj.getString("clouds") + "%");
                        DateFormat df = DateFormat.getDateTimeInstance();
                        String updatedOn = df.format(new Date(Long.parseLong(dataObj.getString("dt"))*1000));
                        txtTime.setText("Last updated on "+updatedOn);

                        SpannableStringBuilder ssb = new SpannableStringBuilder(dataObj.getString("name")+","+dataObj.getString("country")+" ");
                        ssb.setSpan(new ImageSpan(getApplicationContext(), getResources().getIdentifier("@drawable/img"+dataObj.getString("icon"),"drawable",getPackageName())), ssb.length()-1, ssb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        txtCity.setText(ssb, TextView.BufferType.SPANNABLE);
                        //img.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/img"+dataObj.getString("icon"),"drawable", getPackageName())));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please enter a valid city name!",Toast.LENGTH_SHORT).show();
                    }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                    e.printStackTrace();
                    } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /*private void initWeather() {
        final String url="http://api.openweathermap.org/data/2.5/weather?lat="+currLat+"&lon="+currLong+"&APPID=05e2d1a19fb1a1c19bd250764cfa3606&units=metric";
        // String urlWithBase = url.concat(TextUtils.isEmpty(baseCurrency)?"USD":baseCurrency);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getInt("cod")==200) {
                        layoutWeather.setVisibility(View.VISIBLE);
                        JSONObject main = response.getJSONObject("main");
                        JSONObject clouds = response.getJSONObject("clouds");
                        JSONObject mainWeather = response.getJSONArray("weather").getJSONObject(0);
                        txtCity.setText(response.getString("name"));
                        txtWeather.setText(mainWeather.getString("main"));
                        txtWeatherDescription.setText(mainWeather.getString("description"));
                        txtTemp.setText(main.getString("temp")+"°C");
                        txtMinMax.setText("Min " + main.getString("temp_min") + "°C, Max " + main.getString("temp_max") + "°C");
                        txtHumidity.setText(main.getString("humidity")+"%");
                        txtCloudy.setText(clouds.getInt("all") + "%");
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please enter a valid city name!",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Please enter a valid city name!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }*/

    private void initializeView() {
        //img=findViewById(R.id.img);
        cityName=findViewById(R.id.cityName);
        btnSearch=findViewById(R.id.btnSearch);
        txtCity=findViewById(R.id.txtCity);
        txtTime=findViewById(R.id.txtTime);
        txtTemp=findViewById(R.id.txtTemp);
        txtMinMax=findViewById(R.id.txtMinMax);
        txtWeather=findViewById(R.id.txtWeather);
        txtWeatherDescription=findViewById(R.id.txtWeatherDescription);
        txtHumidity=findViewById(R.id.txtHumidity);
        txtCloudy=findViewById(R.id.txtCloudy);
        layoutWeather=findViewById(R.id.layoutWeather);
        //fusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
    }

   /* public void getCurrentWeather(String cityName)
    {
        String city=cityName.toString();
        //Log.d("abc","City :"+city);
        //final String url="http://api.openweathermap.org/data/2.5/forecast?id=524901&APPID=05e2d1a19fb1a1c19bd250764cfa3606";
        final String url="http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=05e2d1a19fb1a1c19bd250764cfa3606&units=metric";
        // String urlWithBase = url.concat(TextUtils.isEmpty(baseCurrency)?"USD":baseCurrency);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getInt("cod")==200) {
                        layoutWeather.setVisibility(View.VISIBLE);
                        JSONObject main = response.getJSONObject("main");
                        JSONObject clouds = response.getJSONObject("clouds");
                        JSONObject mainWeather = response.getJSONArray("weather").getJSONObject(0);
                        DateFormat df = DateFormat.getDateTimeInstance();
                        String updatedOn = df.format(new Date(json.getLong("dt")*1000));
                        txtCity.setText(response.getString("name"));
                        txtWeather.setText(mainWeather.getString("main"));
                        txtWeatherDescription.setText(mainWeather.getString("description"));
                        txtTemp.setText(main.getString("temp")+"°C");
                        txtMinMax.setText("Min " + main.getString("temp_min") + "°C, Max " + main.getString("temp_max") + "°C");
                        txtHumidity.setText(main.getString("humidity")+"%");
                        txtCloudy.setText(clouds.getInt("all") + "%");
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please enter a valid city name!",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Please enter a valid city name!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }*/
}
