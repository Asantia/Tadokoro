package it.upo.reti2s;

import de.fh_zwickau.informatik.sensor.IZWayApi;
import de.fh_zwickau.informatik.sensor.ZWayApiHttp;
import de.fh_zwickau.informatik.sensor.model.devices.Device;
import de.fh_zwickau.informatik.sensor.model.devices.DeviceList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Sample usage of the Z-Way API. It looks for all sensors and power outlets.
 * It reports the temperature and power consumption of available sensors and, then, turn power outlets on for 10 seconds.
 * <p>
 * It uses the Z-Way library for Java included in the lib folder of the project.
 *
 * @author <a href="mailto:luigi.derussis@uniupo.it">Luigi De Russis</a>
 * @version 1.0 (24/05/2017)
 * @see <a href="https://github.com/pathec/ZWay-library-for-Java">Z-Way Library on GitHub</a> for documentation about the used library
 */
public class Zway {

    public static void main(String[] args) {

        // search all power outlets
       /* for (Device dev : allDevices.getAllDevices()) {
            if (dev.getDeviceType().equalsIgnoreCase("SwitchBinary")) {
                logger.debug("Device " + dev.getNodeId() + " is a " + dev.getDeviceType());
                // turn it on
                logger.info("Turn device " + dev.getNodeId() + " on...");
                dev.on();
            }
        }

        // search again all power outlets
        for (Device dev : allDevices.getAllDevices()) {
            logger.debug("Device " + dev.getNodeId() + " is a " + dev.getDeviceType());
            // turn it off
            logger.info("Turn device " + dev.getNodeId() + " off...");
            dev.off();
        }*/
    }

    // turn on 1 device
    public static void turnOn(DeviceMap dev, Logger logger){
        logger.info("Turn device " + dev.getDev() + " on...");
        dev.getDev().on();
    }

    // turn off 1 device
    public static void turnOff(DeviceMap dev, Logger logger){
        logger.info("Turn device " + dev.getDev() + " off...");
        dev.getDev().off();
    }

    //turn on a list of devices
    public static void turnOn(List<DeviceMap> devices, Logger logger){
        for (DeviceMap dev : devices) {
            logger.info("Turn device " + dev.getDev() + " on...");
            dev.getDev().on();
        }
    }

    //turn off a list of devices
    public static void turnOff(List<DeviceMap> devices, Logger logger){
        for (DeviceMap dev : devices) {
            logger.info("Turn device " + dev.getDev() + " off...");
            dev.getDev().off();
        }
    }
}
