package org.longmetal.subsystem;

// This file exists solely to house my basic formula for the shooter
// -Ben

/*  Prerecs:
        double LLDistance = distance taken from LimeLight (in meters)

*/

public class ShootFormula {
    public ShootFormula() {}

    public double shooterSpeed(double distance) {
        double targetHeight = 2.495555;
        double rampHeight = 0.3048;
        double height = targetHeight - rampHeight;
        double gravity = 9.81;
        double xVelocity;
        double ballVelocity = 0;
        double drumRPM = 0;
        double motorRPM = 0;
        double motorSpeed = 0;

        xVelocity = Math.sqrt((distance * distance * -0.5 * gravity) / (height - distance));

        ballVelocity = xVelocity * Math.sqrt(2);

        drumRPM = ballVelocity * 375.9566;

        motorRPM = drumRPM / 3;

        return motorRPM;
    }

    public double shooterPercent(double distance) {
        double motorRPM = shooterSpeed(distance);

        return motorRPM
                / 4000; // will need to be changed to account for actual limelight conversion
        // or completely deleted when we use PID controls instead of PercentMode
    }
}
