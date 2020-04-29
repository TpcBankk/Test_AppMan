package com.example.test_appman;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> namesList = new ArrayList<>();
    private ArrayList<String> description = new ArrayList<>();
    TextView textView;
    ListView listView;
    private PopupWindow mPopupWindow;
    LinearLayout  layout,popup;

    String Id,Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.LinearLayout1);
        popup = (LinearLayout) findViewById(R.id.popup);


        getNameList();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            //Setting a dynamic title at runtime. Here, it displays the current time.
            actionBar.setTitle("Id: "+ Id +" Name: " + Name);
        }

        listView = (ListView) findViewById(R.id.listview);

        final ArrayList<String> arrayList = new ArrayList<>();

        for (int i = 0 ; i < namesList.size(); i++ ){
            arrayList.add(namesList.get(i));
        }


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.activity_popup,null);
                textView = customView.findViewById(R.id.text);

                textView.setText(description.get(position));

                mPopupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                mPopupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        mPopupWindow.dismiss();
                    }
                }, 1000);
            }
        });




    }

    private void getNameList() {
        String myJSONStr = loadJSONFromAsset();

        try {
            JSONObject rootJsonObject = new JSONObject(myJSONStr);


            Id = rootJsonObject.getString("Id");
            Name = rootJsonObject.getString("firstName")+" "+rootJsonObject.getString("lastName");


            JSONArray dataJsonArray = rootJsonObject.getJSONArray("data");

            for (int i = 0; i < dataJsonArray.length(); i++){


                JSONObject jsonObject = dataJsonArray.getJSONObject(i);
                namesList.add(jsonObject.getString("docType"));

                JSONObject descriptionJsonObject = new JSONObject(jsonObject.getString("description"));
                description.add(descriptionJsonObject.getString("th") + " / " + descriptionJsonObject.getString("en"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("intern.json");
            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return  json;
    }
}
