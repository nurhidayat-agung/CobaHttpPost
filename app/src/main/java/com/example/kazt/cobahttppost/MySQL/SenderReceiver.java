package com.example.kazt.cobahttppost.MySQL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by kazt on 8/31/2016.
 */
public class SenderReceiver extends AsyncTask<Void, Void, String>{
    Context c;
    String urlAddress;
    String query;
    ListView lv;
    ImageView noDataImg, noNetWorkImg;

    ProgressDialog pd;

    public SenderReceiver(Context c, String urlAddress, String query, ListView lv, ImageView...imageViews) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.query = query;
        this.lv = lv;

        this.noDataImg = imageViews[0];
        this.noNetWorkImg = imageViews[1];
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(c);
        pd.setTitle("Search");
        pd.setMessage("Searching ... Please wait");
        pd.show();

    }

    @Override
    protected String doInBackground(Void... voids) {
        return this.sendAndReceive();
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pd.dismiss();

        // set lv to empty
        lv.setAdapter(null);


        if (s != null){
            if (!s.contains("null")){
                Parser p = new Parser(c,s,lv);
                p.execute();


            }else {
                noNetWorkImg.setVisibility(View.INVISIBLE);
                noDataImg.setVisibility(View.VISIBLE);
            }

        }else {
            noNetWorkImg.setVisibility(View.VISIBLE);
            noDataImg.setVisibility(View.INVISIBLE);
        }

    }

    private String sendAndReceive(){
        HttpURLConnection con = Connector.connect(urlAddress);
        if (con==null){
            return null;
        }
        try {
            OutputStream os = con.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(new DataPackager(query).packageData());
            bw.flush();

            // release res
            bw.close();
            os.close();

            // some response
            int responseCode = con.getResponseCode();
            if (responseCode==con.HTTP_OK){
                // return saome data
                InputStream is = con.getInputStream();

                //read
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();

                if (br != null){
                    while ((line=br.readLine()) != null){
                        response.append(line + "\n");
                    }
                }else {
                    return null;
                }
                return response.toString();


            }else {
                return String.valueOf(responseCode);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
