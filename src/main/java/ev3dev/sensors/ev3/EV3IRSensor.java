package ev3dev.sensors.ev3;

import ev3dev.sensors.BaseSensor;
import ev3dev.sensors.EV3DevSensorMode;
import ev3dev.utils.Sysfs;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.SensorMode;

import java.io.File;

import static ev3dev.sensors.EV3DevSensorMode.*;

/**
 * <b>EV3 Infra Red sensors</b><br>
 * The digital EV3 Infrared Seeking Sensor detects proximity to the robot and reads signals emitted by the EV3 Infrared Beacon. The sensors can alse be used as a receiver for a Lego Ev3 IR remote control.
 * 
 *
 * 
 * <b>EV3 Infra Red sensors</b><br>
 * 
 * The sensors can be used as a receiver for up to four Lego Ev3 IR remote controls using the methods.
*  
 * 
 * See <a href="http://www.ev-3.net/en/archives/848"> Sensor Product page </a>
 * See <a href="http://sourceforge.net/p/lejos/wiki/Sensor%20Framework/"> The
 *      leJOS sensors framework</a>
 * See {@link lejos.robotics.SampleProvider leJOS conventions for
 *      SampleProviders}
 * 
 *      <p>
 * 
 * 
 * @author Andy Shaw
 * @author Juan Antonio Breña Moral
 * 
 */
public class EV3IRSensor extends BaseSensor {

    private static final String LEGO_EV3_IR = "lego-ev3-ir";

    public static float MIN_RANGE = 5f; //cm
    public static float MAX_RANGE = 100f; //cm

    private static final String MODE_DISTANCE = "IR-PROX";
    private static final String MODE_SEEK = "IR-SEEK";
    private static final String MODE_REMOTE = "IR-REMOTE";

    public final static int IR_CHANNELS = 4;

    public EV3IRSensor(final Port portName) {
        super(portName, LEGO_UART_SENSOR, LEGO_EV3_IR);
		init();
	}

    private void init() {
        setModes(new SensorMode[] {
                new DistanceMode(this.PATH_DEVICE),
                new SeekMode(this.PATH_DEVICE)
        });
    }
	
    /**
     * <b>EV3 Infra Red sensors, Distance mode</b><br>
     * Measures the distance to an object in front of the sensors.
     * 
     * <p>
     * <b>Size and content of the sample</b><br>
     * The sample contains one element giving the distance to an object in front of the sensors. The distance provided is very roughly equivalent to meters
     * but needs conversion to give better distance. See product page for details. <br>
     * The effective range of the sensors in Distance mode  is about 5 to 50 centimeters. Outside this range a zero is returned
     * for low values and positive infinity for high values.
     * 
     * 
     * @return A sampleProvider
     * See {@link lejos.robotics.SampleProvider leJOS conventions for
     *      SampleProviders}
     * See <a href="http://www.ev-3.net/en/archives/848"> Sensor Product page </a>
     */    
	public SensorMode getDistanceMode() {
        switchMode(MODE_DISTANCE, SWITCH_DELAY);
	    return getMode(0);
    }

    private class DistanceMode extends EV3DevSensorMode {

        private final File pathDevice;
    	
        public DistanceMode(final File pathDevice) {
        	this.pathDevice = pathDevice;
        }

		@Override
        public int sampleSize() {
            return 1;
        }

        @Override
        public void fetchSample(float[] sample, int offset) {

            float rawValue = Sysfs.readFloat(this.pathDevice + "/" +  VALUE0);

            if (rawValue < MIN_RANGE) {
                sample[offset] = 0;
            } else if (rawValue >= MAX_RANGE) {
                sample[offset] = Float.POSITIVE_INFINITY;
            } else {
                sample[offset] = rawValue;
            }
        }

        @Override
        public String getName() {
            return "Distance";
        }
        
    }

    /**
     * <b>EV3 Infra Red sensor, Seek mode</b><br>
     * In seek mode the sensor locates up to four beacons and provides bearing and distance of each beacon.
     *
     * <p>
     * <b>Size and content of the sample</b><br>
     * The sample contains four pairs of elements in a single sample. Each pair gives bearing of  and distance to the beacon.
     * The first pair of elements is associated with a beacon transmitting on channel 0, the second pair with a beacon transmitting on channel 1 etc.<br>
     * The bearing values range from -25 to +25 (with values increasing clockwise
     * when looking from behind the sensor). A bearing of 0 indicates the beacon is
     * directly in front of the sensor. <br>
     * Distance values are not to scale. Al increasing values indicate increasing distance. <br>
     * If no beacon is detected both bearing is set to zero, and distance to positive infinity.
     *
     * <p>
     *
     * @return A sampleProvider
     * See {@link lejos.robotics.SampleProvider leJOS conventions for
     *      SampleProviders}
     * See <a href="http://www.ev-3.net/en/archives/848"> Sensor Product page </a>
     */
    public SensorMode getSeekMode() {
        switchMode(MODE_SEEK, SWITCH_DELAY);
        return getMode(1);
    }

    private class SeekMode extends EV3DevSensorMode {

        private final File pathDevice;

        public SeekMode(final File pathDevice) {
            this.pathDevice = pathDevice;
        }

        @Override
        public int sampleSize() {
            return 8;
        }

        @Override
        public void fetchSample(float[] sample, int offset) {
            sample[offset++] = Sysfs.readFloat(this.pathDevice + "/" +  VALUE0);
            sample[offset++] = Sysfs.readFloat(this.pathDevice + "/" +  VALUE1);
            sample[offset++] = Sysfs.readFloat(this.pathDevice + "/" +  VALUE2);
            sample[offset++] = Sysfs.readFloat(this.pathDevice + "/" +  VALUE3);
            sample[offset++] = Sysfs.readFloat(this.pathDevice + "/" +  VALUE4);
            sample[offset++] = Sysfs.readFloat(this.pathDevice + "/" +  VALUE5);
            sample[offset++] = Sysfs.readFloat(this.pathDevice + "/" +  VALUE6);
            sample[offset++] = Sysfs.readFloat(this.pathDevice + "/" +  VALUE7);
        }

        @Override
        public String getName() {
            return "Seek";
        }

    }

    /**
     * Return the current remote command from the specified channel. Remote commands
     * are a single numeric value  which represents which button on the Lego IR
     * remote is currently pressed (0 means no buttons pressed). Four channels are
     * supported (0-3) which correspond to 1-4 on the remote. The button values are:<br>
     * 1 TOP-LEFT<br>
     * 2 BOTTOM-LEFT<br>
     * 3 TOP-RIGHT<br>
     * 4 BOTTOM-RIGHT<br>
     * 5 TOP-LEFT + TOP-RIGHT<br>
     * 6 TOP-LEFT + BOTTOM-RIGHT<br>
     * 7 BOTTOM-LEFT + TOP-RIGHT<br>
     * 8 BOTTOM-LEFT + BOTTOM-RIGHT<br>
     * 9 CENTRE/BEACON<br>
     * 10 BOTTOM-LEFT + TOP-LEFT<br>
     * 11 TOP-RIGHT + BOTTOM-RIGHT<br>
     * @param chan channel to obtain the command for
     * @return the current command
     */
    public int getRemoteCommand(int chan) {
        switchMode(MODE_REMOTE, SWITCH_DELAY);

        if(chan == 0) {
            return Sysfs.readInteger(this.PATH_DEVICE + "/" + VALUE0);
        } else if(chan == 1) {
            return  Sysfs.readInteger(this.PATH_DEVICE + "/" +  VALUE1);
        } else if(chan == 2) {
            return  Sysfs.readInteger(this.PATH_DEVICE + "/" +  VALUE2);
        } else if(chan == 3) {
            return Sysfs.readInteger(this.PATH_DEVICE + "/" + VALUE3);
        } else {
            throw new IllegalArgumentException("Bad channel");
        }
    }

    /**
     * Obtain the commands associated with one or more channels. Each element of
     * the array contains the command for the associated channel (0-3).
     * @param cmds the array to store the commands
     * @param offset the offset to start storing
     * @param len the number of commands to store.
     */
    public void getRemoteCommands(byte[] cmds, int offset, int len) {
        switchMode(MODE_REMOTE, SWITCH_DELAY);

        cmds[0] = (byte) Sysfs.readInteger(this.PATH_DEVICE + "/" +  VALUE0);
        cmds[1] = (byte) Sysfs.readInteger(this.PATH_DEVICE + "/" +  VALUE1);
        cmds[2] = (byte) Sysfs.readInteger(this.PATH_DEVICE + "/" +  VALUE2);
        cmds[3] = (byte) Sysfs.readInteger(this.PATH_DEVICE + "/" +  VALUE3);
    }

}
