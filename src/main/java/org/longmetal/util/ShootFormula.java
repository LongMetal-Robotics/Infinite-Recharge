package org.longmetal.util;

// This file exists solely to house my basic formula for the shooter
// - Brooke

/*  Prerecs:
        double LLDistance = distance taken from LimeLight (in meters)

*/

public class ShootFormula {

    /**
     * Calculate the shooter's target speed to launch the ball to the target
     *
     * @param distance The horizontal distance we are from the target (as calculated from limelight
     *     values, most likely)
     * @return The required RPM to launch the ball to the target
     */
    public static double calculateSpeed(double distance) {
        double targetHeight = 2.495555;
        double rampHeight = 0.3048;
        double height = targetHeight - rampHeight;
        double gravity = 9.81;
        double xVelocity;
        double ballVelocity = 0;
        double drumRPM = 0;
        double motorRPM = 0;

        xVelocity = Math.sqrt((distance * distance * -0.5 * gravity) / (height - distance));

        ballVelocity = xVelocity * Math.sqrt(2);

        drumRPM = ballVelocity * 375.9566;

        motorRPM = drumRPM / 3;

        return motorRPM;
    }
}
