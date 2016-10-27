package com.example.admin.icareapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by ADMIN on 19-Oct-16.
 */
public class JSONParser {
    private String url_str;
    private BufferedReader reader;
    private JSONArray jArray;
    private HttpURLConnection urlConnection;
    private URL url;

    private static JSONParser instance;

    public static JSONParser getInstance() {
        if (instance == null)
            instance = new JSONParser();

        return instance;
    }

    private JSONParser() {

    }

    public void getURL(String url){
        this.url_str = url;
    }


    public String getNumberOfCustomers(){
        try{
            url = new URL(url_str);
        }catch (MalformedURLException e){
            System.out.println("No legal protocol is found in URL String or URL String cannot be parsed");
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        }catch (IOException e){
            System.out.println("Cannot open URL connection");
        }

        String line = "";

        try{
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            line = reader.readLine();
        }catch (IOException ioe){
            System.out.println("Cannot open IO Stream");
        }

        return line;
    }

    public String getAuthenticateAndInsertNewUser(String login_id, String password){
        try{
            url = new URL(url_str + "?login_id=" + login_id + "&password=" + password);
        }catch (MalformedURLException e){
            System.out.println("No legal protocol is found in URL String or URL String cannot be parsed");
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        }catch (IOException e){
            System.out.println("Cannot open URL connection");
        }

        String line = "";

        try{
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            line = reader.readLine();
        }catch (IOException ioe){
            System.out.println("Cannot open IO Stream");
        }

        return line;
    }

    public String checkUserExistence(String login_id){
        try{
            url = new URL(url_str + "?login_id=" + login_id);
        }catch (MalformedURLException e){
            System.out.println("No legal protocol is found in URL String or URL String cannot be parsed");
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        }catch (IOException e){
            System.out.println("Cannot open URL connection");
        }

        String line = "";

        try{
            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            line = reader.readLine();
        }catch (IOException ioe){
            System.out.println("Cannot open IO Stream");
        }

        return line;
    }

    public String updateUser(String m){
        try{
            url = new URL(url_str);
            System.out.println(url.toString());
        }catch (MalformedURLException e){
            System.out.println("No legal protocol is found in URL String or URL String cannot be parsed");
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
        }catch (IOException ioe){
            System.out.println("Cannot open URL connection");
        }

        try {

            PrintWriter writer = new PrintWriter(urlConnection.getOutputStream(), true);
            writer.print(m);
            writer.close();
        }catch (IOException ioe){
            System.out.println("Cannot open output stream");
        }catch (Exception e){
            e.printStackTrace();
        }

        String line = "";

        try{
            InputStream is = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine();
            reader.close();
            is.close();
        }catch (IOException ioe){
            System.out.println("Cannot open input Stream");
        }

        return line;
    }

    public String getPostData(Map<String,String> m) throws Exception{
        StringBuilder result = new StringBuilder();
        Boolean firstPara = true;

        for (String para: m.keySet()){
            if (!firstPara){
                result.append("&");
            }

            result.append(URLEncoder.encode(para, "UTF-8")).append("=").append(URLEncoder.encode(m.get(para), "UTF-8"));
            firstPara = false;
        }
        System.out.println(result.toString());
        return result.toString();
    }
}
