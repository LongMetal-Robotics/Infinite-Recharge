package org.longmetal.subsystem;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.longmetal.Constants;
import org.longmetal.util.LMMath;

/*
 * While one could argue that this is a subsystem, you are probably going to be driving the robot no matter what, so we'll leave out the Subsystem methods.
 */
public class DriveTrain {
    public DifferentialDrive driveTrain;

    public CANEncoder leftEncoder, rightEncoder;
    public CANSparkMax mRearLeft, mFrontLeft, mRearRight, mFrontRight;
    public SpeedControllerGroup leftMotors, rightMotors;

    public PIDController alignmentController;
    public double kP = 0.0175, kI = 0.0003, kD = 0.00004, alignmentCalc = 0;

    private boolean reverseDrive = true;
    private double MAX_SPEED_MULT = 0.5;
    private double autoDistance = 0;
    private boolean autoDrive = false;

    public DriveTrain() {
        this(0.0, 0.0, 0.0);
    }

    public DriveTrain(double kP, double kI, double kD) {
        mRearLeft = new CANSparkMax(Constants.kP_REAR_LEFT, MotorType.kBrushless);
        mRearLeft.setIdleMode(IdleMode.kCoast);
        mFrontLeft = new CANSparkMax(Constants.kP_FRONT_LEFT, MotorType.kBrushless);
        mFrontLeft.setIdleMode(IdleMode.kCoast);
        leftMotors = new SpeedControllerGroup(mRearLeft, mFrontLeft);
        leftEncoder = mFrontLeft.getEncoder();
        leftEncoder.setPositionConversionFactor(Constants.MOTOR_TO_FEET);

        mRearRight = new CANSparkMax(Constants.kP_REAR_RIGHT, MotorType.kBrushless);
        mRearRight.setIdleMode(IdleMode.kCoast);
        mFrontRight = new CANSparkMax(Constants.kP_FRONT_RIGHT, MotorType.kBrushless);
        mFrontRight.setIdleMode(IdleMode.kCoast);
        rightMotors = new SpeedControllerGroup(mRearRight, mFrontRight);
        rightEncoder = mFrontRight.getEncoder();
        rightEncoder.setPositionConversionFactor(Constants.MOTOR_TO_FEET);

        driveTrain = new DifferentialDrive(leftMotors, rightMotors);

        alignmentController = new PIDController(kP, kI, kD);
        alignmentController.setSetpoint(0);
        alignmentController.setTolerance(Constants.kLINEUP_TOLERANCE);
    }

    public void setReverseDrive(boolean newReverseDrive) {
        reverseDrive = newReverseDrive;
    }

    public boolean getReverseDrive() {
        return reverseDrive;
    }

    public void setMaxSpeed(double maxSpeed) {
        MAX_SPEED_MULT = maxSpeed;
    }

    public double getMaxSpeed() {
        return MAX_SPEED_MULT;
    }

    public void curve(
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

    public void curveRaw(double xSpeed, double zRotation, boolean isQuickTurn) {
        driveTrain.curvatureDrive(xSpeed, zRotation, isQuickTurn);
    }

    /**
     * Setup straight driving for a distance
     *
     * @param distance The distance to drive (in feet)
     */
    public void setupDistance(double distance) {
        autoDistance = distance;
        autoDrive = true;
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    /**
     * Drive for the distance set up by setupDistance. Includes drift correction.
     *
     * @return whether the robot is running the distance. If false, it has completed its maneuver
     */
    public boolean runDistance() {
        double left = leftEncoder.getPosition();
        double right = rightEncoder.getPosition();
        double minDistance = LMMath.min(left, right).doubleValue();
        double sideDifference = right - left;
        // double averageDistance = (leftEncoder.getPosition() + rightEncoder.getPosition()) / 2;
        boolean run = autoDrive && autoDistance > 0 && minDistance < autoDistance;
        if (run) {
            driveTrain.curvatureDrive(
                    0.4, sideDifference * Constants.DRIFT_CORRECTION_FACTOR, false);
        } else {
            autoDistance = 0;
            autoDrive = false;
            driveTrain.curvatureDrive(0, 0, false);
        }
        return run;
    }
}
