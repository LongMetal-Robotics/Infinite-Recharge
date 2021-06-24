/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.ControlType;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.File;
import java.util.Scanner;
import org.longmetal.Constants;
import org.longmetal.input.Gamepad.Axis;
import org.longmetal.input.Gamepad.Button;
import org.longmetal.input.Input;
import org.longmetal.subsystem.Climb;
import org.longmetal.subsystem.ControlPanel;
import org.longmetal.subsystem.DriveTrain;
import org.longmetal.subsystem.Intake;
import org.longmetal.subsystem.Pneumatics;
import org.longmetal.subsystem.Shooter;
import org.longmetal.subsystem.Vision;
import org.longmetal.util.Console;
import org.longmetal.util.Delay;
import org.longmetal.util.LMMath;
import org.longmetal.util.Listener;
import org.longmetal.util.ShootFormula;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    DigitalInput intakeLimit;
    Timer timer;
    Listener intakeListener;
    Listener panelListenerTurns;
    Listener panelListenerColor;

    SendableChooser<Boolean> chooserQuinnDrive;

    Listener quinnDriveListener;
    Listener reverseListener;

    boolean endgameMode = false;
    boolean shooterCheck = false;
    boolean shooterStop = true;
    boolean panelUp = false;
    double shootLow = 0;
    double shootHigh = 0;
    boolean readyClimb = false;
    double conversionFactor = 2.4;

    NetworkTable limelightTable =
            NetworkTableInstance.getDefault()
                    .getTable("limelight"); // takes in values from limelight
    NetworkTableEntry tx = limelightTable.getEntry("tx"); // distances
    NetworkTableEntry ty = limelightTable.getEntry("ty"); // height or something
    NetworkTableEntry tv = limelightTable.getEntry("tv"); // targets found

    double tX, tY, lastShooterSetPoint, shooterSetPoint, velocity;
    boolean RPMInRange = false;

    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {

        // Output current commit and branch
        try {
            File file = new File(Filesystem.getDeployDirectory(), "branch.txt");
            Scanner fs = new Scanner(file);
            String branch = "unknown", commit = "unknown";

            if (fs.hasNextLine()) {
                branch = fs.nextLine();
            }

            file = new File(Filesystem.getDeployDirectory(), "commit.txt");
            fs.close();
            fs = new Scanner(file);

            if (fs.hasNextLine()) {
                commit = fs.nextLine();
            }

            System.out.println("Commit " + commit + " or later (branch '" + branch + "')");
            fs.close();
        } catch (Exception e) {
            Console.warn("Could not determine commit or branch. (" + e.getLocalizedMessage() + ")");
        }

        intakeLimit = new DigitalInput(0);
        timer = new Timer();
        intakeListener =
                new Listener(
                        new Runnable() {
                            public void run() {
                                Intake.runHopper(Constants.kTRANSPORT_SPEED);
                            }
                        },
                        null);
        panelListenerTurns =
                new Listener(
                        new Runnable() {
                            public void run() {
                                Pneumatics.flipArmUp();
                                panelUp = true;
                                // controlPanel.turnsMode();
                                // pneumatics.flipArmDown();
                                // panelUp = false;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                Pneumatics.flipArmDown();
                                panelUp = false;
                            }
                        });

        panelListenerColor =
                new Listener(
                        new Runnable() {
                            public void run() {
                                Pneumatics.flipArmUp();
                                panelUp = true;
                                // controlPanel.colorMode();
                                // pneumatics.flipArmDown();
                                // panelUp = false;
                            }
                        },
                        new Runnable() {
                            public void run() {
                                Pneumatics.flipArmDown();
                                panelUp = false;
                            }
                        });

        timer.start();

        chooserQuinnDrive = new SendableChooser<>();
        chooserQuinnDrive.setDefaultOption("Disabled", false);
        chooserQuinnDrive.addOption("Enabled", true);
        SmartDashboard.putData("Quinn Drive Chooser", chooserQuinnDrive);

        quinnDriveListener =
                new Listener(
                        new Runnable() {

                            @Override
                            public void run() {
                                Input.setQuinnDrive(true);
                            }
                        },
                        new Runnable() {

                            @Override
                            public void run() {
                                Input.setQuinnDrive(false);
                            }
                        });

        reverseListener =
                new Listener(
                        new Runnable() {

                            @Override
                            public void run() {
                                DriveTrain.setReverseDrive(true);
                            }
                        },
                        new Runnable() {

                            @Override
                            public void run() {
                                DriveTrain.setReverseDrive(false);
                            }
                        });

        // Display PID Coefficients on SmartDashboard

        SmartDashboard.putNumber("I Gain", DriveTrain.kI);
        SmartDashboard.putNumber("P Gain", DriveTrain.kP);
        SmartDashboard.putNumber("D Gain", DriveTrain.kD);

        updateVision(false);
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {

        SmartDashboard.putData("Drive Train", DriveTrain.driveTrain);
        SmartDashboard.putBoolean("Quinn Drive", Input.isQuinnDrive());
        quinnDriveListener.update(chooserQuinnDrive.getSelected());

        // Reverse Drive mode

        boolean forwardDrive = Input.forwardStick.getRawButtonPressed(Constants.kFORWARD_BUTTON);
        boolean reverseDrive = Input.forwardStick.getRawButton(Constants.kREVERSE_BUTTON);

        if (forwardDrive ^ reverseDrive) { // XOR: a or b but not both
            reverseListener.update(reverseDrive);
        }

        SmartDashboard.putBoolean("Reverse Drive", DriveTrain.getReverseDrive());

        // Endgame mode
        SmartDashboard.putBoolean("Endgame Mode", endgameMode);

        // Shooter RPM
        SmartDashboard.putNumber("ShooterRPM", Shooter.getSpeed());

        // Shooter CorrectRPM
        // shootLow = formula.shooterSpeed(/*Limelight distance*/ 4) * 0.95;
        // shootHigh = formula.shooterSpeed(/* Limelight distance */ 4) * 1.05;
        // shooterCheck = (shooter.getSpeed() > shootLow && shooter.getSpeed() < shootHigh);
        // SmartDashboard.putBoolean("ShooterCheck", shooterCheck);

        // read PID coefficients from SmartDashboard
        double p = SmartDashboard.getNumber("P Gain", 0);
        double i = SmartDashboard.getNumber("I Gain", 0);
        double d = SmartDashboard.getNumber("D Gain", 0);

        conversionFactor = SmartDashboard.getNumber("Shoot Factor", 0);

        // if PID coefficients on SmartDashboard have changed, write new values to controller
        if ((p != DriveTrain.kP)) {
            DriveTrain.alignmentController.setP(p);
            DriveTrain.kP = p;
        }
        if ((i != DriveTrain.kI)) {
            DriveTrain.alignmentController.setI(i);
            DriveTrain.kI = i;
        }
        if ((d != DriveTrain.kD)) {
            DriveTrain.alignmentController.setD(d);
            DriveTrain.kD = d;
        }

        SmartDashboard.putNumber("Set Point", shooterSetPoint);
        SmartDashboard.putNumber("Shoot Factor", 0);
        velocity = Shooter.drumEncoder.getVelocity();
        double velocityDiff = Math.abs(shooterSetPoint - velocity);
        RPMInRange = velocityDiff <= Shooter.acceptableDiff;
        SmartDashboard.putBoolean("RPM In Range", RPMInRange);
        SmartDashboard.putNumber("RPM Diff", velocityDiff);

        // Turn on the LEDs and enable vision processing
        limelightTable.getEntry("camMode").setDouble(0.0);
    }

    @Override
    public void disabledInit() {
        Delay.setEnabled(
                false); // VERY IMPORTANT: Stops all delays from running when the robot is disabled
    }

    public void enabledInit() {
        Delay.setEnabled(true);
    }

    public void enabledPeriodic() {
        DriveTrain.alignmentCalc =
                (double) LMMath.limit(-1 * DriveTrain.alignmentController.calculate(tX), -0.5, 0.5);
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select between different
     * autonomous modes using the dashboard. The sendable chooser code works with the Java
     * SmartDashboard. If you prefer the LabVIEW Dashboard (you don't), remove all of the chooser
     * code and uncomment the getString line to get the auto name from the text box below the Gyro
     *
     * <p>You can add additional auto modes by adding additional comparisons to the switch structure
     * below with additional strings. If using the SendableChooser make sure to add them to the
     * chooser code above as well.
     */
    @Override
    public void autonomousInit() {
        enabledInit();
        Delay.setEnabled(true); // Allows Delay to be used
    }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {
        enabledPeriodic();
        boolean targetAcquired = false; // initially there should be no targets
        while (!targetAcquired) {
            // while we don't see any,
            DriveTrain.curve(0.2, 0.2, 0.0, 0.0);

            // float tv = limelightTable->GetNumber("tv"); //converts networkTable to a regular
            // float?
            /*if(tv != 0.0) //we see a target on the field
            {
                driveTrain.curve(0.0, 0.0, 0.0, 0.0); // stops the driving
                //method to make the robot drive towards / align to the target
            }*/
        }
    }

    @Override
    public void teleopInit() {
        enabledInit();
        Delay.setEnabled(true); // Allows Delay to be used
        shooterSetPoint = 0;
        lastShooterSetPoint = 0;
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {
        enabledPeriodic();

        // Left Gamepad trigger, currently used for shooter
        double lTrigger = Input.gamepad.getAxis(Axis.LT);

        // Right Gamepad trigger, currently used for intake
        double rTrigger = Input.gamepad.getAxis(Axis.RT);

        // Left stick Y axis, left climb up/down
        double lStickY = Input.gamepad.getAxis(Axis.LS_Y);

        // Right stick Y axis, right climb up/down
        double rStickY = Input.gamepad.getAxis(Axis.RS_Y);

        // LB button, used to stop shooter
        boolean lButton = Input.gamepad.getButton(Button.LB);

        // RB button, used to run reverse intake and release climb [only in climb mode]
        boolean rButton = Input.gamepad.getButton(Button.RB);

        // X button, not currently used
        boolean xButton = Input.gamepad.getButton(Button.X);

        // Y button, enables Control Panel Mode
        boolean yButton = Input.gamepad.getButton(Button.Y);

        // A button, not currently used
        boolean aButton = Input.gamepad.getButton(Button.A);

        // B button, currently prompts shooter to aim and set speed
        boolean bButton = Input.gamepad.getButton(Button.B);

        // Start button, engages Endgame Mode
        boolean startButton = Input.gamepad.getButton(Button.START);

        // Back button, disengages Endgame Mode
        boolean backButton = Input.gamepad.getButton(Button.BACK);

        boolean hopperOn = false;

        // Limelight line-up while B button is held
        if (bButton) {
            updateVision(true);
            // driveTrain.curveRaw(0, (tX / 30) / 2, true);
            SmartDashboard.putNumber("Alignment", DriveTrain.alignmentCalc);
            DriveTrain.curveRaw(0, DriveTrain.alignmentCalc, true);
        } else if (aButton) {
            updateVision(false);
            DriveTrain.curveRaw(0.5, 0, false);

            Delay.delay(
                    new Runnable() {

                        @Override
                        public void run() {
                            DriveTrain.curveRaw(0, 0, true);
                        }
                    },
                    Constants.kLOW_PORT_REVERSE_TIME);
        } /*else if (readyClimb || panelUp) { // When panel or climb up, drive slower
              updateVision(false);
              driveTrain.curve(
                      input.forwardStick.getY(),
                      input.forwardStick.getThrottle() * 0.2,
                      input.turnStick.getTwist(),
                      input.turnStick.getThrottle() * 0.5);
          }*/ else {
            updateVision(false);
            DriveTrain.curve(
                    Input.forwardStick.getY(),
                    Input.forwardStick.getThrottle(),
                    Input.turnStick.getTwist(),
                    Input.turnStick.getThrottle());
        }

        // Puts the robot into endgame mode, disabling all manipulator subsystems
        if (startButton) {
            endgameMode = true;
            Climb.resetEncoders();
        }
        if (!endgameMode) {
            Pneumatics.setRatchet(false);

            shooterSetPoint = 0;
            // I'm not sure if this is the most efficient way to do this, but I will hopefully
            // streamline it in the future

            // Stops shooter
            if (lButton) {
                shooterStop = true;
                // intake.setHopperSpeed(0);
            } else if (bButton || aButton) {
                shooterStop = false;
            }

            if (shooterStop) {
                if (backButton) {
                    Shooter.runShooter(-0.1);
                    Shooter.setSingulatorSpeed(-0.2);
                } else {
                    Shooter.runShooter(0);
                    Shooter.setSingulatorSpeed(-0.1);
                }
            } else {
                if (bButton) {
                    // SmartDashboard.getNumber("Factor", conversionFactor);

                    updateVision(true);
                    if (tY >= 10) {
                        shooterSetPoint =
                                (double)
                                        LMMath.limit(
                                                ShootFormula.shooterSpeed(
                                                                Vision.getLimelightDistance(
                                                                        tY /*, Vision.Target.POWER_PORT*/))
                                                        * 2.4,
                                                Shooter.minRPM,
                                                Shooter.maxRPM);
                    }

                    SmartDashboard.putNumber(
                            "Distance",
                            Vision.getLimelightDistance(tY /*, Vision.Target.POWER_PORT*/));

                    /*if (RPMInRange && velocity > 1500) {
                        shooter.setSingulatorSpeed(1);
                    } else {
                        shooter.setSingulatorSpeed(0);
                    }*/

                    // Singulator directly controlled by left trigger
                    // Hopper is either on or off
                    if (lTrigger > Constants.kINPUT_DEADBAND) {
                        Shooter.setSingulatorSpeed(lTrigger);
                        hopperOn = true;
                        Intake.setHopperSpeed(1);
                    } else {
                        Shooter.setSingulatorSpeed(-0.1);
                        Intake.setHopperSpeed(0);
                        hopperOn = false;
                    }
                } else if (aButton) { // Sets shooter to lower speed to place into lower port
                    updateVision(false);
                    shooterSetPoint = 1500;

                    if (RPMInRange) {
                        Shooter.setSingulatorSpeed(1);
                        hopperOn = true;
                        Intake.setHopperSpeed(1);
                    } else {
                        Shooter.setSingulatorSpeed(-0.1);
                        Intake.setHopperSpeed(0);
                        hopperOn = false;
                    }
                } else {
                    updateVision(false);
                    shooterSetPoint = Constants.kSHOOTER_MIN;
                    Shooter.setSingulatorSpeed(-0.1);
                }
            }

            SmartDashboard.putNumber("Set", shooterSetPoint);
            if (shooterSetPoint != lastShooterSetPoint) {
                Shooter.drumPID.setReference(shooterSetPoint, ControlType.kVelocity);
                // shooter.setShooterRPM(shooterSetPoint);
                lastShooterSetPoint = shooterSetPoint;
            }

            // Sets intake to a speed
            if (rTrigger > Constants.kINPUT_DEADBAND) {
                Intake.setIntakeSpeed(rTrigger);
                // intake.setHopperSpeed(rTrigger);
            } else if (rButton) { // Reverse intake
                Intake.setIntakeSpeed(-0.3);
            } else { // Stop intake
                Intake.setIntakeSpeed(0);
                // intake.setHopperSpeed(0);
            }

            // if (bButton && lTrigger > Constants.kINPUT_DEADBAND) {
            //     intake.setHopperSpeed(1);
            //     /*} else if (xButton) {
            //     intake.setHopperSpeed(0.8);*/
            // } else {
            //     intake.setHopperSpeed(0);
            // }

            if (lStickY < -0.5) {
                Intake.setHopperSpeed(1);
            } else if (!hopperOn) {
                Intake.setHopperSpeed(0);
            }

            intakeListener.update(intakeLimit.get());

            // Flip up control panel and engage based on FMS values
            if (yButton) {
                // For now, this button will just spin the motor for testing purposes
                ControlPanel.spin();
            } else {
                ControlPanel.stop();
            }

            // Temporary control for flipping arm up
            panelListenerTurns.update(!xButton);
        } else {

            if (backButton) {
                endgameMode = false;
                Pneumatics.setRatchet(true);
                Climb.setWinchSpeed(0);
                Climb.setWinchEnabled(false);
            }

            boolean sticksUp =
                    lStickY < -Constants.kINPUT_DEADBAND || rStickY < -Constants.kINPUT_DEADBAND;

            // Sticks up
            if (sticksUp) {

                // Disengage ratchet
                Pneumatics.setRatchet(false);

                if (lStickY < -Constants.kINPUT_DEADBAND) {
                    // Let out left winch
                    Climb.setLeftWinchSpeed(-lStickY / 2);
                }

                if (rStickY < -Constants.kINPUT_DEADBAND) {
                    // Let out right winch
                    Climb.setRightWinchSpeed(-rStickY / 2);
                }

            } else {
                double lClimbPosition = Climb.encoder1.getPosition();
                double rClimbPosition = -Climb.encoder2.getPosition();
                double minPosition = Math.min(lClimbPosition, rClimbPosition);
                // SmartDashboard.putNumber("lClimb", lClimbPosition);
                // SmartDashboard.putNumber("rClimb", rClimbPosition);

                // If it has spooled out a reasonable amount, allow the ratchet in
                if (minPosition >= 30) {
                    Pneumatics.setRatchet(true);
                } else {
                    Pneumatics.setRatchet(false);
                }

                // Engage ratchet

                Climb.setWinchEnabled(false);

                // Left stick down
                // Reel in left climb (raise robot)
                if (lStickY > Constants.kINPUT_DEADBAND) {
                    Climb.setLeftWinchSpeed(-lStickY);
                } else {
                    Climb.setLeftWinchSpeed(0);
                }

                // Right stick down
                // Reel in right climb (raise robot)
                if (rStickY > Constants.kINPUT_DEADBAND) {
                    Climb.setRightWinchSpeed(-rStickY);
                } else {
                    Climb.setRightWinchSpeed(0);
                }
            }
        }
    }

    private void updateVision(boolean enable) {
        if (enable) { // If `enable` is `true`, turn on the LEDs and vision processing
            limelightTable.getEntry("ledMode").setDouble(Constants.LL_ON);
            limelightTable.getEntry("pipeline").setNumber(Constants.PIPELINE_VISION);
        } else if (!enable) { // If `enable` is `false`, turn off the LEDs and vision processing
            limelightTable.getEntry("ledMode").setDouble(Constants.LL_OFF);
            limelightTable.getEntry("pipeline").setNumber(Constants.PIPELINE_DRIVE);
        } // If it's neither (`null`), don't change anything

        tX = tx.getDouble(0.0);
        tY = ty.getDouble(0.0);

        SmartDashboard.putNumber("LimelightX", tX);
        SmartDashboard.putNumber("LimelightY", tY);
    }
}
