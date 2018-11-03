package com.example.dell.weatherapp;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class Weather extends AsyncTask<Void, Void, String>  {
    public static String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?units=metric&q=";
    private static final String API_KEY = "05e2d1a19fb1a1c19bd250764cfa3606";
    private HttpURLConnection connection;


    @Override
    protected String doInBackground(Void... params) {
        URL url = null;
        String data="";
        JSONObject topLevel = null;
        try {
            url = new URL(String.format(OPEN_WEATHER_MAP_API));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }
            topLevel = new JSONObject(builder.toString());
            JSONObject main = topLevel.getJSONObject("main");
            JSONObject clouds = topLevel.getJSONObject("clouds");
            JSONObject mainWeather = topLevel.getJSONArray("weather").getJSONObject(0);
            JSONObject sys= topLevel.getJSONObject("sys");

            data = "{\"cod\":\""+topLevel.getInt("cod")+"\",\"country\":\""+sys.getString("country")+"\",\"name\":\""+topLevel.getString("name")+"\",\"temp\":\""+main.getDouble("temp")+"\",\"temp_min\":\""+String.valueOf(main.getDouble("temp_min"))+"\",\"temp_max\":\""+String.valueOf(main.getDouble("temp_max"))+"\",\"humidity\":\""+String.valueOf(main.getDouble("humidity"))+"\",\"main\":\""+mainWeather.getString("main")+"\",\"description\":\""+mainWeather.getString("description")+"\",\"clouds\":\""+clouds.getInt("all")+"\",\"dt\":\""+topLevel.getLong("dt")+"\",\"icon\":\""+mainWeather.getString("icon")+"\"}";
            Log.d("abc", "doInBackground: "+data);
            urlConnection.disconnect();
            return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            data = "{\"cod\":\"404\"}";
            return data;

        } catch (JSONException e) {
            e.printStackTrace();
            data = "{\"cod\":\"404\"}";
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            data = "{\"cod\":\"404\"}";
            return data;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            data = "{\"cod\":\"404\"}";
            return data;
        }


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
