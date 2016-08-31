package com.example.kazt.cobahttppost.MySQL;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kazt on 8/31/2016.
 */
public class Parser extends AsyncTask<Void, Void, Integer> {
    Context c;
    String data;
    ListView lv;

    ArrayList<String> names = new ArrayList<>();

    public Parser(Context c, String data, ListView lv) {
        this.c = c;
        this.data = data;
        this.lv = lv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return this.parse();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        if (integer ==1){
            // Bind to List view
            ArrayAdapter adapter = new ArrayAdapter(c, android.R.layout.simple_list_item_1,names);
            lv.setAdapter(adapter);


        }else {
            Toast.makeText(c,"unable to parse",Toast.LENGTH_LONG).show();
        }
    }

    private int parse(){
        try {
            JSONArray ja = new JSONArray(data);
            JSONObject jo = null;
            names.clear();
            for (int i = 0; i < ja.length(); i++){
                String name = jo.getString("Name");
                names.add(name);
            }
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
