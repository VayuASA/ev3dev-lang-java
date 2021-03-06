package fake_ev3dev.ev3dev.sensors.mindsensors;

import ev3dev.hardware.EV3DevPlatform;
import fake_ev3dev.ev3dev.sensors.FakeLegoSensor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class FakeAbsolutIMUSensor extends FakeLegoSensor {

    public FakeAbsolutIMUSensor(EV3DevPlatform ev3DevPlatform) throws IOException {

        super(ev3DevPlatform);

        populateValues(Arrays.asList(2, 100, 200));

        //TODO It is possible to generalize a bit more
        Path mode = Paths.get(
                EV3DEV_FAKE_SYSTEM_PATH + "/" +
                        LEGO_SENSOR_PATH + "/" +
                        SENSOR1 + "/" +
                        SENSOR_MODE);
        createFile(mode);

        Path command = Paths.get(
                EV3DEV_FAKE_SYSTEM_PATH + "/" +
                        LEGO_SENSOR_PATH + "/" +
                        SENSOR1 + "/" +
                        SENSOR_COMMAND);
        createFile(command);

    }

}
