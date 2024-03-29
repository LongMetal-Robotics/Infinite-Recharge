package org.longmetal.subsystem;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.longmetal.Constants;

/**
 * While one could argue that this is a subsystem, you are probably going to be driving the robot no
 * matter what, so we'll leave out the Subsystem methods.
 */
public class DriveTrain {
    public static DifferentialDrive driveTrain;

    public static CANSparkMax mRearLeft, mFrontLeft, mRearRight, mFrontRight;
    public static SpeedControllerGroup leftMotors, rightMotors;

    public static PIDController alignmentController;
    public static double kP = 0.0175, kI = 0.0003, kD = 0.00004, alignmentCalc = 0;

    private static boolean reverseDrive = true;
    private static double MAX_SPEED_MULT = 0.5;

    public static void init() {
        init(0.0, 0.0, 0.0);
    }

    public static void init(double kP, double kI, double kD) {
        mRearLeft = new CANSparkMax(Constants.kP_REAR_LEFT, MotorType.kBrushless);
        mRearLeft.setIdleMode(IdleMode.kCoast);
        mFrontLeft = new CANSparkMax(Constants.kP_FRONT_LEFT, MotorType.kBrushless);
        mFrontLeft.setIdleMode(IdleMode.kCoast);
        leftMotors = new SpeedControllerGroup(mRearLeft, mFrontLeft);

        mRearRight = new CANSparkMax(Constants.kP_REAR_RIGHT, MotorType.kBrushless);
        mRearRight.setIdleMode(IdleMode.kCoast);
        mFrontRight = new CANSparkMax(Constants.kP_FRONT_RIGHT, MotorType.kBrushless);
        mFrontRight.setIdleMode(IdleMode.kCoast);
        rightMotors = new SpeedControllerGroup(mRearRight, mFrontRight);

        driveTrain = new DifferentialDrive(leftMotors, rightMotors);

        alignmentController = new PIDController(kP, kI, kD);
        alignmentController.setSetpoint(0);
        alignmentController.setTolerance(Constants.kLINEUP_TOLERANCE);
    }

    public static void setReverseDrive(boolean newReverseDrive) {
        reverseDrive = newReverseDrive;
    }

    public static boolean getReverseDrive() {
        return reverseDrive;
    }

    public static void setMaxSpeed(double maxSpeed) {
        MAX_SPEED_MULT = maxSpeed;
    }

    public static double getMaxSpeed() {
        return MAX_SPEED_MULT;
    }

    public static void curve(
            double speedRaw,
            double speedThrottleRaw,
            double curvatureRaw,
            double curvatureThrottleRaw) {
        double modifierX =
                (Constants.kSPEED_MODIFIER * speedThrottleRaw - Constants.kTHROTTLE_SHIFT)
                        / 2; // Create a speed modifier
        double modifierZ =
                (curvatureThrottleRaw - 1)
                        * Constants.kCURVE_MODIFIER; // Create a curvature modifier

        double driveX = speedRaw * modifierX * Constants.kMAX_SPEED_MULT; // Calculate the speed
        double driveZ = curvatureRaw * modifierZ; // Calculate the curvature

        if (reverseDrive) {
            // Reverse drive values
            driveX *= -1;
        }

        driveTrain.curvatureDrive(driveX, driveZ, true); // Drive
    }

    public static void curveRaw(double xSpeed, double zRotation, boolean isQuickTurn) {
        driveTrain.curvatureDrive(xSpeed, zRotation, isQuickTurn);
    }
}
