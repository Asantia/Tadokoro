package it.upo.reti2s;

import java.util.ArrayList;
import java.util.Map;


public class Hue {
    // the URL of the Philips Hue bridge
    static String baseURL = "http://172.30.1.138";
    // example username, generated by following https://www.developers.meethue.com/documentation/getting-started
    static String username = "DeQdozx1Dv0GpOPfmN02HKdvmOcmXIe7e9aiCEAu";
    // base URL for lights
    static String lightsURL = baseURL + "/api/" + username + "/lights/";
    // get the Hue lamps
    static Map<String, ?> allLights = Rest.get(lightsURL);

    public static void main(String[] args) {
    }

    // colorLoop 1 hue
    public static void doColorLoop(String lamp){
        String body = "{ \"on\" : true, \"effect\" : \"colorloop\" }";
        Rest.put(lightsURL+lamp+"/state/", body, "application/json");
    }

    // colorLoop all hue
    public static void doColorLoop(ArrayList<String> allHue){
        for (String light : allHue) {
            String body = "{ \"on\" : true, \"effect\" : \"colorloop\" }";
            Rest.put(lightsURL+light+"/state/", body, "application/json");
        }
    }

    // partyLoop 1 hue
    public static void partyLoop(String lamp){
        String body = "{ \"on\" : true, \"bri\" : 254, \"sat\" : 254, \"effect\" : \"colorloop\" }";
        Rest.put(lightsURL + lamp + "/state/", body, "application/json");
    }

    // partyLoop all hue
    public static void partyLoop(ArrayList<String> allHue){
        for (String light : allHue) {
            String body = "{ \"on\" : true, \"bri\" : 254, \"sat\" : 254, \"effect\" : \"colorloop\" }";
            Rest.put(lightsURL + light + "/state/", body, "application/json");
        }
    }

    // cromoLoop 1 hue
    public static void cromoLoop(String lamp){
        String body = "{ \"on\" : true, \"bri\" : 150, \"sat\" : 254, \"effect\" : \"colorloop\" }";
        Rest.put(lightsURL + lamp + "/state/", body, "application/json");
    }

    // cromoLoop all hue
    public static void cromoLoop(ArrayList<String> allHue){
        for (String light : allHue) {
            String body = "{ \"on\" : true, \"bri\" : 150, \"sat\" : 254, \"effect\" : \"colorloop\" }";
            Rest.put(lightsURL + light + "/state/", body, "application/json");
        }
    }

    // turn on 1 hue
    public static void turnOn(String lamp){
        String callURL = lightsURL + lamp + "/state";
        String body = "{ \"on\" : true}";
        Rest.put(callURL, body, "application/json");
    }

    //turn off 1 hue
    public static void turnOff(String lamp){
        String callURL = lightsURL + lamp + "/state";
        String body = "{ \"on\" : false}";
        Rest.put(callURL, body, "application/json");
    }

    //turn on all hue
    public static void turnAllOn(){
        for (String light : allLights.keySet()) {
            String callURL = lightsURL + light + "/state";
            String body = "{ \"on\" : true}";
            Rest.put(callURL, body, "application/json");
        }
    }

    //turn off all hue
    public static void turnAllOff(){
        for (String light : allLights.keySet()) {
            String callURL = lightsURL + light + "/state";
            String body = "{ \"on\" : false }";
            Rest.put(callURL, body, "application/json");
        }
    }

    //turn on all hue on a path
    public static void turnAllOn(ArrayList<String> path){
        for (String light : path) {
            String callURL = lightsURL + light + "/state";
            String body = "{ \"on\" : true}";
            Rest.put(callURL, body, "application/json");
        }
    }

    //turn off all hue on a path
    public static void turnAllOff(ArrayList<String> path){
        for (String light : path) {
            String callURL = lightsURL + light + "/state";
            String body = "{ \"on\" : false }";
            Rest.put(callURL, body, "application/json");
        }
    }
}