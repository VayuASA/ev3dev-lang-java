package ev3dev.actuators.ev3.motors;

import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;

/**
 * Motor class contains 3 instances of regulated motors.
 * <p>
 * Example:<p>
 * <code><pre>
 *   Motor.A.setSpeed(720);// 2 RPM
 *   Motor.C.setSpeed(720);
 *   Motor.A.forward();
 *   Motor.C.forward();
 *   Thread.sleep (1000);
 *   Motor.A.stop();
 *   Motor.C.stop();
 *   Motor.A.rotateTo( 360);
 *   Motor.A.rotate(-720,true);
 *   while(Motor.A.isMoving() :Thread.yield();
 *   int angle = Motor.A.getTachoCount(); // should be -360
 *   LCD.drawInt(angle,0,0);
 * </pre></code>
 * @author Roger Glassey/Andy Shaw/Juan Antonio Breña Moral
 */
public class Motor {

    /**
     * Motor A.
     */
    public static RegulatedMotor A = new EV3LargeRegulatedMotor(MotorPort.A);

    /**
     * Motor B.
     */
    public static RegulatedMotor B = new EV3LargeRegulatedMotor(MotorPort.B);

    /**
     * Motor C.
     */
    public static RegulatedMotor C = new EV3LargeRegulatedMotor(MotorPort.C);

    /**
     * Motor D.
     */
    public static RegulatedMotor D = new EV3LargeRegulatedMotor(MotorPort.D);

    private Motor() {
        // Motor class cannot be instantiated
    }

}
