package com.arjun.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    TextView weather;
    EditText city;

    public class DownloadTask extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... urls) {

            String result="";
            URL url;
            HttpURLConnection urlConnection=null;

            try {

                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while (data!=-1){

                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;

            } catch (MalformedURLException e) {

                e.printStackTrace();


            } catch (IOException e) {

                e.printStackTrace();

            }

            return "Failed";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try {

                JSONObject jsonObject=new JSONObject(result);

                String weatherInfo=jsonObject.getString("weather");
                Log.i("Weather Content",weatherInfo);
                JSONArray arr=new JSONArray(weatherInfo);

                for(int i=0;i<arr.length();i++){

                    JSONObject jsonPart=arr.getJSONObject(i);

                    Log.i("Description",jsonPart.getString("description"));
                    weather.setText(jsonPart.getString("description").toUpperCase()+" : "+jsonPart.getString("description"));

                }

            } catch (JSONException e) {

                e.printStackTrace();

            }




        }
    }

    public void getWeather(View view){

        String cityName ="";
        try {
            cityName = URLEncoder.encode(city.getText().toString(),"UTF-8");//for city names with 2 names= san francisco

        } catch (UnsupportedEncodingException e) {

            Toast.makeText(getApplicationContext(),"Could not find weather!",Toast.LENGTH_LONG);
            e.printStackTrace();
        }
        try {

            String api = "";
            api = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=cb4ff7a9fd096c0205b4d2ad24767954";
            DownloadTask task = new DownloadTask();
            task.execute(api);
            Log.i("URLfull", api);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Could not find weather!",Toast.LENGTH_LONG);
        }

        InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(city.getWindowToken(),0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weather=(TextView)findViewById(R.id.weather);
        city=(EditText)findViewById(R.id.city);


    }
}
