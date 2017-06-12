package it.upo.reti2s;

import ai.api.model.AIResponse;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ai.api.GsonFactory;
import ai.api.model.Fulfillment;

import com.vdurmont.emoji.EmojiParser;
import de.fh_zwickau.informatik.sensor.IZWayApi;
import de.fh_zwickau.informatik.sensor.ZWayApiHttp;
import de.fh_zwickau.informatik.sensor.model.devices.Device;
import de.fh_zwickau.informatik.sensor.model.devices.DeviceList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

/**
 * api.ai Webhook example.
 * It gets all tasks, a specified task from a database and provides the information to
 * the api.ai service. It also allows users to create a new task, through the api.ai
 * conversational interface.
 *
 * @author <a href="mailto:luigi.derussis@uniupo.it">Luigi De Russis</a>
 * @version 1.0 (21/05/2017)
 */
public class TaskWebhook {

    public static void main(String[] args) {

        // init gson, from the api.ai factory
        Gson gson = GsonFactory.getDefaultFactory().getGson();

        // the path to our webhook
        post("/", (request, response) -> {
            Fulfillment output = new Fulfillment();

            // the "real" stuff happens here
            // notice that the webook request is a superset of the AIResponse class
            // and it should be created to tackle the differences
            doWebhook(gson.fromJson(request.body(), AIResponse.class), output);

            response.type("application/json");
            // output is automatically converted in JSON thanks to gson
            return output;
        }, gson::toJson);

    }

    /**
     * The webhook method. It is where the "magic" happens.
     * Please, notice that in this version we ignore the "urgent" field of tasks.
     *
     * @param input  the request body that comes from api.ai
     * @param output the @link(Fulfillment) response to be sent to api.ai
     */
    private static void doWebhook(AIResponse input, Fulfillment output) {
        //Stringa di risposta
        String text = "";
        //info x zwave
        Logger logger = LoggerFactory.getLogger(Zway.class);
        String ipAddress = "172.30.1.137";
        String username = "admin";
        String password = "raz4reti2";
        IZWayApi zwayApi = new ZWayApiHttp(ipAddress, 8083, "http", username, password, 0, false, new ZWaySimpleCallback());

        //collezione di dati
        ArrayList<DeviceMap> zwaveLights = new ArrayList<DeviceMap>();
        DeviceMap dev1 = new DeviceMap(zwayApi.getDevices().getDeviceById("ZWayVDev_zway_7-0-37"), "bagno");
        DeviceMap dev2 = new DeviceMap(zwayApi.getDevices().getDeviceById("ZWayVDev_zway_18-0-37"), "camera");
        zwaveLights.add(dev1);
        zwaveLights.add(dev2);

        ArrayList<String> hueLights = new ArrayList<String>();
        hueLights.add("1");
        hueLights.add("2");
        hueLights.add("3");

        ArrayList<DeviceMap> zwaveDevices = new ArrayList<DeviceMap>();
        DeviceMap dev5 = new DeviceMap(zwayApi.getDevices().getDeviceById("ZWayVDev_zway_9-0-37"), "stereo");
        zwaveDevices.add(dev5);

        //percorsi predefiniti
        ArrayList<DeviceMap> pathProvaZwave = new ArrayList<DeviceMap>();
        pathProvaZwave.add(dev1);
        pathProvaZwave.add(dev2);

        ArrayList<String> pathProvaHue = new ArrayList<String>();
        pathProvaHue.add("2");

        //allLightsOn
        if (input.getResult().getAction().equalsIgnoreCase("allLightsOn")) {
            //turn on all hue
            Hue.turnAllOn();
            //turn on all zwave lights
            Zway.turnOn(zwaveLights, logger);
            text = "Accendo le luci\n";
        }
        //allLightsOff
        if (input.getResult().getAction().equalsIgnoreCase("allLightsOff")) {
            //turn off all hue
            Hue.turnAllOff();
            //turn off all zwave lights
            Zway.turnOff(zwaveLights, logger);
            text = "Spengo le luci\n";
        }

        //path prova lightsOn
        if (input.getResult().getAction().equalsIgnoreCase("pathProvaOn")) {
            //turn on all hue lights on a path
            Hue.turnAllOn(pathProvaHue);
            //turn on all zwave lights on a path
            Zway.turnOn(pathProvaZwave, logger);
            text = "Accendo le luci del percorso prova\n";
        }

        //path prova lightsOff
        if (input.getResult().getAction().equalsIgnoreCase("pathProvaOff")) {
            //turn off all hue lights on a path
            Hue.turnAllOff(pathProvaHue);
            //turn off all zwave lights on a path
            Zway.turnOff(pathProvaZwave, logger);
            text = "Spengo le luci del percorso prova\n";
        }

        //musicOn
        if (input.getResult().getAction().equalsIgnoreCase("musicOn")) {
            //turn on all zwave lights on a path
            Zway.turnOn((zwayApi.getDevices().getDeviceById("ZWayVDev_zway_9-0-37")), logger);
            text = "Accendo lo stereo\n";
        }

        //musicOff
        if (input.getResult().getAction().equalsIgnoreCase("musicOff")) {
            //turn on all zwave lights on a path
            Zway.turnOff((zwayApi.getDevices().getDeviceById("ZWayVDev_zway_9-0-37")), logger);
            text = "Spengo lo stereo\n";
        }

        // prepare the output
        output.setSpeech(text);
        output.setDisplayText(text);
    }

}
//Roba utile
/*
    // 1 light on z wave
        if (input.getResult().getAction().equalsIgnoreCase("allLightsOn")) {
            // get all the Z-Wave devices
            Device presa = zwayApi.getDevices().getDeviceById("ZWayVDev_zway_18-0-37"); //ZWayVDev_zway_7-0-37 oppure 9 per la lampadina
            text = "Accendo le luci\n";
            ZWayExample.turnOn(presa, logger);
        }

    // 1 light off hue
        if (input.getResult().getAction().equalsIgnoreCase("allLightsOff")) {
            String lightsURL = "http://172.30.1.138" + "/api/" + "DeQdozx1Dv0GpOPfmN02HKdvmOcmXIe7e9aiCEAu" + "/lights/" + 2 + "/state";
            text = "Here we go:\n";
            Hue.turnOff(lightsURL);
        }

    // 1 light off z wave
        if (input.getResult().getAction().equalsIgnoreCase("allLightsOff")) {
            // get all the Z-Wave devices
            Device presa = zwayApi.getDevices().getDeviceById("ZWayVDev_zway_18-0-37"); //ZWayVDev_zway_7-0-37
            text = "Spengo le luci\n";
            ZWayExample.turnOff(presa, logger);
        }
    // 1 light colorLoop on hue
        if (input.getResult().getAction().equalsIgnoreCase("doColorLoop")) {
            String lightsURL = "http://172.30.1.138" + "/api/" + "DeQdozx1Dv0GpOPfmN02HKdvmOcmXIe7e9aiCEAu" + "/lights/"+ 2 + "/state";
            text = "Here we go:\n";
            Hue.doColorLoop(lightsURL);
        }
*/