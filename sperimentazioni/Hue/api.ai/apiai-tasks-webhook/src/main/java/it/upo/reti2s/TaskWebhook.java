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
        DeviceMap lampCorridoio = new DeviceMap(zwayApi.getDevices().getDeviceById("ZWayVDev_zway_7-0-37"), "corridoio");
        DeviceMap lampCamera = new DeviceMap(zwayApi.getDevices().getDeviceById("ZWayVDev_zway_18-0-37"), "camera");
        zwaveLights.add(lampCorridoio);
        zwaveLights.add(lampCamera);

        /*ArrayList<String> hueLights = new ArrayList<String>();
        hueLights.add("1");
        hueLights.add("2");
        hueLights.add("3");
        */
        ArrayList<HueMap> hueLights = new ArrayList<HueMap>();
        hueLights.add("1", "bagno");
        hueLights.add("2", "cucina");
        hueLights.add("3", "sala");

        ArrayList<DeviceMap> zwaveDevices = new ArrayList<DeviceMap>();
        DeviceMap stereo = new DeviceMap(zwayApi.getDevices().getDeviceById("ZWayVDev_zway_9-0-37"), "stereo");
        zwaveDevices.add(stereo);

        ArrayList<String> taskList = new ArrayList<String>();
        taskList.add("pathCucinaBagnoOn");
        taskList.add("pathCucinaCameraOn");
        taskList.add("pathCucinaSalaOn");
        taskList.add("pathCucinaCorridoioOn");
        taskList.add("pathBagnoCameraOn");
        taskList.add("pathBagnoSalaOn");
        taskList.add("pathBagnoCorridoioOn");
        taskList.add("pathCameraSalaOn");
        taskList.add("pathCameraCorridoioOn");
        taskList.add("pathSalaCorridoioOn");

        taskList.add("pathCucinaBagnoOff");
        taskList.add("pathCucinaCameraOff");
        taskList.add("pathCucinaSalaOff");
        taskList.add("pathCucinaCorridoioOff");
        taskList.add("pathBagnoCameraOff");
        taskList.add("pathBagnoSalaOff");
        taskList.add("pathBagnoCorridoioOff");
        taskList.add("pathCameraSalaOff");
        taskList.add("pathCameraCorridoioOff");
        taskList.add("pathSalaCorridoioOff");

        taskList.add("allLightsOn");
        taskList.add("partyModeOn");
        taskList.add("cromoterapiaOn");
        taskList.add("musicOn");

        taskList.add("allLightsOff");
        taskList.add("partyModeOff");
        taskList.add("cromoterapiaOff");
        taskList.add("musicOff");

        /////////////////////////////PERCORSO CUCINA BAGNO/////////////////////////////
        ArrayList<DeviceMap> cucinaBagno = new ArrayList<DeviceMap>();
        cucinaBagno.add(lampCorridoio);
        ArrayList<String> cucinaBagnoHue = new ArrayList<String>();
        cucinaBagnoHue.add("1");//luce del bagno      
        cucinaBagnoHue.add("2");//luce della cucina

        if (input.getResult().getAction().equalsIgnoreCase("pathCucinaBagnoOn")) {
            //turn on all hue lights on a path
            Hue.turnAllOn(cucinaBagnoHue);
            //turn on all zwave lights on a path
            Zway.turnOn(cucinaBagno, logger);
            text = "ok sarà fatto!!\n";
        }


        if (input.getResult().getAction().equalsIgnoreCase("pathCucinaBagnoOff")) {
            //turn off all hue lights on a path
            Hue.turnAllOff(cucinaBagnoHue);
            //turn off all zwave lights on a path
            Zway.turnOff(cucinaBagno, logger);
            text = "Spengo le luci!!\n";
        }

        /////////////////////////PERCORSO CUCINA CAMERA/////////////////////////////
        ArrayList<DeviceMap> cucinaCamera = new ArrayList<DeviceMap>();
        cucinaCamera.add(lampCorridoio);
        cucinaCamera.add(lampCamera);
        ArrayList<String> cucinaCameraHue = new ArrayList<String>();
        cucinaCameraHue.add("2");

        if (input.getResult().getAction().equalsIgnoreCase("pathCucinaCameraOn")) {
            //turn on all hue lights on a path
            Hue.turnAllOn(cucinaCameraHue);
            //turn on all zwave lights on a path
            Zway.turnOn(cucinaCamera, logger);
            text = "ok sarà fatto!!\n";
        }


        if (input.getResult().getAction().equalsIgnoreCase("pathCucinaCameraOff")) {
            //turn off all hue lights on a path
            Hue.turnAllOff(cucinaCameraHue);
            //turn off all zwave lights on a path
            Zway.turnOff(cucinaCamera, logger);
            text = "Spengo le luci!!\n";
        }

        /////////////////////////PERCORSO CUCINA SALA/////////////////////////////
        ArrayList<DeviceMap> cucinaSala = new ArrayList<DeviceMap>();
        cucinaSala.add(lampCorridoio);

        ArrayList<String> cucinaSalaHue = new ArrayList<String>();
        cucinaSalaHue.add("2");
        cucinaSalaHue.add("3");

        if (input.getResult().getAction().equalsIgnoreCase("pathCucinaSalaOn")) {
            //turn on all hue lights on a path
            Hue.turnAllOn(cucinaSalaHue);
            //turn on all zwave lights on a path
            Zway.turnOn(cucinaSala, logger);
            text = "ok sarà fatto!!\n";
        }


        if (input.getResult().getAction().equalsIgnoreCase("pathCucinaSalaOff")) {
            //turn off all hue lights on a path
            Hue.turnAllOff(cucinaSalaHue);
            //turn off all zwave lights on a path
            Zway.turnOff(cucinaSala, logger);
            text = "Spengo le luci!!\n";
        }

        /////////////////////////PERCORSO CUCINA CORRIDOIO/////////////////////////////
        ArrayList<DeviceMap> cucinaCorridoio = new ArrayList<DeviceMap>();
        cucinaSala.add(lampCorridoio);

        ArrayList<String> cucinaCorridoioHue = new ArrayList<String>();
        cucinaCorridoioHue.add("2");

        if (input.getResult().getAction().equalsIgnoreCase("pathCucinaCorridoioOn")) {
            //turn on all hue lights on a path
            Hue.turnAllOn(cucinaCorridoioHue);
            //turn on all zwave lights on a path
            Zway.turnOn(cucinaCorridoio, logger);
            text = "ok sarà fatto!!\n";
        }


        if (input.getResult().getAction().equalsIgnoreCase("pathCucinaCorridoioOff")) {
            //turn off all hue lights on a path
            Hue.turnAllOff(cucinaCorridoioHue);
            //turn off all zwave lights on a path
            Zway.turnOff(cucinaCorridoio, logger);
            text = "Spengo le luci!!\n";
        }

        /////////////////////////PERCORSO BAGNO CAMERA/////////////////////////////
        ArrayList<DeviceMap> bagnoCamera = new ArrayList<DeviceMap>();
        bagnoCamera.add(lampCorridoio);
        bagnoCamera.add(lampCamera);

        ArrayList<String> bagnoCameraHue = new ArrayList<String>();
        bagnoCameraHue.add("1");

        if (input.getResult().getAction().equalsIgnoreCase("pathBagnoCameraOn")) {
            //turn on all hue lights on a path
            Hue.turnAllOn(bagnoCameraHue);
            //turn on all zwave lights on a path
            Zway.turnOn(bagnoCamera, logger);
            text = "ok sarà fatto!!\n";
        }


        if (input.getResult().getAction().equalsIgnoreCase("pathBagnoCameraOff")) {
            //turn off all hue lights on a path
            Hue.turnAllOff(bagnoCameraHue);
            //turn off all zwave lights on a path
            Zway.turnOff(bagnoCamera, logger);
            text = "Spengo le luci!!\n";
        }

        /////////////////////////PERCORSO BAGNO SALA/////////////////////////////
        ArrayList<DeviceMap> bagnoSala = new ArrayList<DeviceMap>();
        bagnoSala.add(lampCorridoio);

        ArrayList<String> bagnoSalaHue = new ArrayList<String>();
        bagnoSalaHue.add("1");
        bagnoSalaHue.add("3");

        if (input.getResult().getAction().equalsIgnoreCase("pathBagnoSalaOn")) {
            //turn on all hue lights on a path
            Hue.turnAllOn(bagnoSalaHue);
            //turn on all zwave lights on a path
            Zway.turnOn(bagnoSala, logger);
            text = "ok sarà fatto!!\n";
        }


        if (input.getResult().getAction().equalsIgnoreCase("pathBagnoSalaOff")) {
            //turn off all hue lights on a path
            Hue.turnAllOff(bagnoSalaHue);
            //turn off all zwave lights on a path
            Zway.turnOff(bagnoSala, logger);
            text = "Spengo le luci!!\n";
        }

      /////////////////////////PERCORSO BAGNO CORRIDOIO/////////////////////////////
        ArrayList<DeviceMap> bagnoCorridoio = new ArrayList<DeviceMap>();
        bagnoCorridoio.add(lampCorridoio);

        ArrayList<String> bagnoCorridoioHue = new ArrayList<String>();
        bagnoCorridoioHue.add("1");

        if (input.getResult().getAction().equalsIgnoreCase("pathBagnoCorridoioOn")) {
            //turn on all hue lights on a path
            Hue.turnAllOn(bagnoCorridoioHue);
            //turn on all zwave lights on a path
            Zway.turnOn(bagnoCorridoio, logger);
            text = "ok sarà fatto!!\n";
        }


        if (input.getResult().getAction().equalsIgnoreCase("pathBagnoCorridoioOff")) {
            //turn off all hue lights on a path
            Hue.turnAllOff(bagnoCorridoioHue);
            //turn off all zwave lights on a path
            Zway.turnOff(bagnoCorridoio, logger);
            text = "Spengo le luci!!\n";
        }

        /////////////////////////PERCORSO CAMERA SALA/////////////////////////////
        ArrayList<DeviceMap> cameraSala = new ArrayList<DeviceMap>();
        cameraSala.add(lampCorridoio);
        cameraSala.add(lampCamera);

        ArrayList<String> cameraSalaHue = new ArrayList<String>();
        cameraSalaHue.add("3");

        if (input.getResult().getAction().equalsIgnoreCase("pathCameraSalaOn")) {
            //turn on all hue lights on a path
            Hue.turnAllOn(cameraSalaHue);
            //turn on all zwave lights on a path
            Zway.turnOn(cameraSala, logger);
            text = "ok sarà fatto!!\n";
        }


        if (input.getResult().getAction().equalsIgnoreCase("pathCameraSalaOff")) {
            //turn off all hue lights on a path
            Hue.turnAllOff(cameraSalaHue);
            //turn off all zwave lights on a path
            Zway.turnOff(cameraSala, logger);
            text = "Spengo le luci!!\n";
        }


        /////////////////////////PERCORSO CAMERA CORRIDOIO/////////////////////////////
        ArrayList<DeviceMap> cameraCorridoio = new ArrayList<DeviceMap>();
        cameraCorridoio.add(lampCorridoio);
        cameraCorridoio.add(lampCamera);

        if (input.getResult().getAction().equalsIgnoreCase("pathCameraCorridoioOn")) {
            //turn on all zwave lights on a path
            Zway.turnOn(cameraCorridoio, logger);
            text = "ok sarà fatto!!\n";
        }


        if (input.getResult().getAction().equalsIgnoreCase("pathCameraCorridoioOff")) {

            //turn off all zwave lights on a path
            Zway.turnOff(cameraCorridoio, logger);
            text = "Spengo le luci!!\n";
        }

        /////////////////////////PERCORSO SALA CORRIDOIO/////////////////////////////
        ArrayList<DeviceMap> salaCorridoio = new ArrayList<DeviceMap>();
        cameraCorridoio.add(lampCorridoio);

        ArrayList<String> salaCorridoioHue = new ArrayList<String>();
        salaCorridoioHue.add("3");

        if (input.getResult().getAction().equalsIgnoreCase("pathSalaCorridoioOn")) {
            //turn on all hue lights on a path
            Hue.turnAllOn(salaCorridoioHue);
            //turn on all zwave lights on a path
            Zway.turnOn(salaCorridoio, logger);
            text = "ok sarà fatto!!\n";
        }


        if (input.getResult().getAction().equalsIgnoreCase("pathSalaCorridoioOff")) {
            //turn off all hue lights on a path
            Hue.turnAllOff(cucinaBagnoHue);
            //turn off all zwave lights on a path
            Zway.turnOff(cucinaBagno, logger);
            text = "Spengo le luci!!\n";
        }



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


        //partyModeOn
        if (input.getResult().getAction().equalsIgnoreCase("partyModeOn")) {
            //turn on all hue do color loop
            for(HueMap hue : hueLights){
                if(!(hue.getDescr().equals("bagno")))
                    Hue.partyLoop(hue.getHue());
            }
            for(DeviceMap dev : zwaveDevices){
                if(dev.getDescr().equals("stereo")) {
                    Zway.turnOn();
                    text = "Accendo lo stereo\n";
                }
            }
            //Hue.doColorLoop(hueLights);
            //Zway.turnOn((zwayApi.getDevices().getDeviceById("ZWayVDev_zway_9-0-37")), logger);
            text = "Accendo la modalità party\n";
        }
        //partyModeOff
        if (input.getResult().getAction().equalsIgnoreCase("partyModeOff")) {
            //turn off all hue
            for(HueMap hue : hueLights){
                if(!(hue.getDescr().equals("bagno")))
                    Hue.turnOff(hue.getHue());
            }
            for(DeviceMap dev : zwaveDevices){
                if(dev.getDescr().equals("stereo")) {
                    Zway.turnOff();
                    text = "Spengo lo stereo\n";
                }
            }
            //Hue.turnAllOff();
            //Zway.turnOff((zwayApi.getDevices().getDeviceById("ZWayVDev_zway_9-0-37")), logger);
            text = "Spengo le luci\n";
        }

        //cromoterapiaOn
        if (input.getResult().getAction().equalsIgnoreCase("cromoterapiaOn")) {
            //turn on all hue do color loop
            for(HueMap hue : hueLights){
                if(hue.getDescr().equals("bagno"))
                    Hue.cromoLoop(hue.getHue());
            }

            text = "Accendo la modalità cromoterapia\n";
        }
        //cromoterapiaOff
        if (input.getResult().getAction().equalsIgnoreCase("cromoterapiaOff")) {
            //turn off all hue
            for(HueMap hue : hueLights){
                if(hue.getDescr().equals("bagno"))
                    Hue.turnOff(hue.getHue());
            }

            text = "Spengo la modalita' cromoterapia\n";
        }


        //musicOn
        if (input.getResult().getAction().equalsIgnoreCase("musicOn")) {
            //turn on all zwave lights on a path
            //Zway.turnOn((zwayApi.getDevices().getDeviceById("ZWayVDev_zway_9-0-37")), logger);
            for(DeviceMap dev : zwaveDevices){
                if(dev.getDescr().equals("stereo")) {
                    Zway.turnOn();
                    text = "Accendo lo stereo\n";
                }
            }
        }


        //musicOff
        if (input.getResult().getAction().equalsIgnoreCase("musicOff")) {
            //turn on all zwave lights on a path
            //Zway.turnOff((zwayApi.getDevices().getDeviceById("ZWayVDev_zway_9-0-37")), logger);
            for(DeviceMap dev : zwaveDevices){
                if(dev.getDescr().equals("stereo")) {
                    Zway.turnOff();
                    text = "Spengo lo stereo\n";
                }
            }
        }

        //tadokoroTasks
        if (input.getResult().getAction().equalsIgnoreCase("tadokoroTasks")) {
            String s = "";
            for(String task : taskList){
                s+= " "+task;
            }
            text = "Questi sono tutti i tasks di Tadokoro\n" + s;
        }

        //tadokoroTasks ---> CANCELLARE QUESTO
        if (input.getResult().getAction().equalsIgnoreCase("tadokoroTasks")) {
            String s = "";
            for(String task : taskList){
                s+= " "+task;
            }
            text = "Questi sono tutti i tasks di Tadokoro\n" + s;
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