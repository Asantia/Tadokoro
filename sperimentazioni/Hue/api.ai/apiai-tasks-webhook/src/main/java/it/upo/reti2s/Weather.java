package it.upo.reti2s;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrea on 21/06/2017.
 */
public class Weather {

    private static final Gson gson = new Gson();
    static String tempCond;
     public static void  main(String args[]){

    }

    public static String ritornaMeteo(){
        Map<String, String> response = new HashMap<>();

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://api.apixu.com/v1/current.json?key=5b036993eb794f0c92c132223172106&q=Vercelli");
        CloseableHttpResponse result = null;

        try {
            result = httpclient.execute(request);
            String json = EntityUtils.toString(result.getEntity());
            // do something useful with the response body
            response = gson.fromJson(json, Map.class);
            // should be inside a finally...
            result.close();
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String responseString =response.toString();
        String parts[] = responseString.split(",");
        String parts3[] = parts[10].split("=");
        String temp= parts3[1];
        String parts2[] = parts[13].split("text=");
        String cond= parts2[1];
        tempCond = temp + " °C " +cond;
        return tempCond;
    }
}
