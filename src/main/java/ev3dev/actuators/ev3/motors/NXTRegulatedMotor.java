package ev3dev.actuators.ev3.motors;

/**
 * Abstraction for a  Lego NXT motors.
 * 
 */
public class NXTRegulatedMotor extends BaseRegulatedMotor {
	private static final float MOVE_P = 4f;
    private static final float MOVE_I = 0.04f;
    private static final float MOVE_D = 10f;
    private static final float HOLD_P = 2f;
    private static final float HOLD_I = 0.02f;
    private static final float HOLD_D = 8f;
    private static final int OFFSET = 0;

    private static final int MAX_SPEED = 170*360/60;

    /**
     * Constructor
     * @param motorPort motor port
     */
	public NXTRegulatedMotor(final String motorPort) {
        super(motorPort, MOVE_P, MOVE_I, MOVE_D, HOLD_P, HOLD_I, HOLD_D, OFFSET, MAX_SPEED);
	}

}
