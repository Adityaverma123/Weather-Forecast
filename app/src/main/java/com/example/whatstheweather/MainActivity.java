package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    public class downloadcontent extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection connection = null;
            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed!";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                String message="";
                JSONObject jsonObject = new JSONObject(s);
                String weatherinfo = jsonObject.getString("weather");
                JSONArray array = new JSONArray(weatherinfo);
                JSONObject mainobj=jsonObject.getJSONObject("main");
                String temp=mainobj.getString("temp");
                String humidity=mainobj.getString("humidity");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonpart = array.getJSONObject(i);
                  String main=jsonpart.getString("main");
                  String description=jsonpart.getString("description");
                  Log.i("main",main);
                  Log.i("description",description);

                    if(!main.equals("")&&!description.equals(""))
                    {
                     message+=main+": "+description+"\n"+"Temp: "+temp+(char)0x00B0+"C"+"\nHumidity:"+humidity+(char)0x00B0+"C";

                    }
                    if(!message.equals(""))
                    {
                        textView.setText(message);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could not find Weather :(",Toast.LENGTH_SHORT).show();
            }
        }
    }
        public void clickfunction(View view)
        {
            downloadcontent task = new downloadcontent();
            InputMethodManager mgr= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
            try {
                String encoder=URLEncoder.encode(editText.getText().toString(),"UTF-8");


                task.execute("https://openweathermap.org/data/2.5/weather?q="+encoder+"&appid=b6907d289e10d714a6e88b30761fae22").get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
             editText=findViewById(R.id.editText);
             textView=findViewById(R.id.textView2);
        }

    }

