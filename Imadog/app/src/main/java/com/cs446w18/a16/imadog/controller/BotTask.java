package com.cs446w18.a16.imadog.controller;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lacie on 2018-03-12.
 */

public class BotTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... info) {
        try {
            URL catsUrl = new URL(info[0]);
            JSONObject data = new JSONObject();
            data.put("userId", info[1]);
            //String query = "userId="+ URLEncoder.encode(info[1],"UTF-8");
            HttpsURLConnection con = (HttpsURLConnection) catsUrl.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty( "Content-Type", "application/json" );
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.connect();

            OutputStream os = con.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(data.toString());
            osw.flush();
            osw.close();
            System.out.println("Resp Code:"+con.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "done";
    }
}
