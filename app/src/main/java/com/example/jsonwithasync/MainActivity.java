package com.example.jsonwithasync;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jsonwithasync.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String id,name,username,email,street,suite,city,zipcode,address;
    private RecyclerView recyclerView;
    private JsonAdapter adapter;
    private ArrayList<User> usersArrayList;
    private Button btnGetData;
    private BufferedReader reader = null;
    private HttpURLConnection connection = null;
    ProgressDialog progressDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGetData = (Button) findViewById(R.id.btnGetData);

        mSwipeRefreshLayout =(SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Async task = new Async();
                task.execute();
            }
        });


        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Async task = new Async();
                task.execute();
            }
        });


    }
    public String loadJSONFromAsset() {
        String json = null;
        try{
            InputStream is = getAssets().open("user.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer,"UTF-8");
        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public  class Async extends AsyncTask<String,String,String>{

        @Override
        protected  void onPreExecute(){

            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


    @Override
    protected String doInBackground(String...param){
        usersArrayList = new ArrayList<>();
        try{
            //JSONObject obj = new JSONObject(getData());
            //JSONArray userArray = obj.getJSONArray("user");
            JSONArray userArray = new JSONArray(getData());
            for(int i=0; i< userArray.length();i++) {
                JSONObject userDetail = userArray.getJSONObject(i);
                id = userDetail.getString("id");
                name = userDetail.getString("name");
                username = userDetail.getString("username");
                email = userDetail.getString("email");


            JSONObject addres = userDetail.getJSONObject("address");
            street = addres.getString("street");
            suite = addres.getString("suite");
            city = addres.getString("city");
            zipcode = addres.getString("zipcode");

            address = street+", "+suite+", "+city+", "+zipcode;
                usersArrayList.add(new User(id,name,username,email,address));
            }}

        catch (JSONException e){
            e.printStackTrace();
        }
        return  null;
    }

        @Override
        protected  void onPostExecute(String result){
            mSwipeRefreshLayout.setRefreshing(false);
            progressDialog.dismiss();
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            adapter = new JsonAdapter(usersArrayList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
    }
}

public String getData(){
    String line = " ";
    try{
        URL url = new URL("https://jsonplaceholder.typicode.com/users");
        connection =(HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) !=null){
            buffer.append(line);
        }
        return buffer.toString();
    }catch (MalformedURLException e){
        e.printStackTrace();
    }catch (IOException e){
        e.printStackTrace();
    }finally {
        if(connection !=null)connection.disconnect();
        try{
            if(reader !=null)reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    return null;
}
}
