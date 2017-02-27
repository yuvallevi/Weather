package com.yuval.weather;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button goBtn = (Button)findViewById(R.id.goBtn);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText =(EditText)findViewById(R.id.editText);
                String textFromUser = editText.getText().toString();
                String weatherApi = "http://api.openweathermap.org/data/2.5/weather?q="+ textFromUser+"&appid=a6d1c475336ee34312725147c1e2b1ba&units=metric";

                Downloader d= new Downloader();
                d.execute(weatherApi);



            }
        });








    }
    public class Downloader extends AsyncTask<String ,Void, String>
    {
        @Override
        protected String doInBackground(String... params) {

            BufferedReader input = null;
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line="";
                while ((line=input.readLine())!=null){
                    response.append(line+"\n");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if (input!=null){
                    try {
                        //must close the reader
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(connection!=null){
                    //must disconnect the connection
                    connection.disconnect();
                }
            }
            return response.toString();

        }


        @Override
        protected void onPostExecute(String jsonText) {

            Gson gson = new Gson();
            JsonWeather jsonWeather= gson.fromJson(jsonText, JsonWeather.class);

            TextView text = (TextView)findViewById(R.id.textView);
            ImageView imageView = (ImageView)findViewById(R.id.imageView);


            String imageToString = jsonWeather.weather[0].icon.toString();

            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/"+imageToString+".png").into(imageView);

            text.setText(jsonWeather.weather[0].description.toString());







        }
    }
}
