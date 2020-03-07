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
import org.longmetal.exception.SubsystemDisabledException;
import org.longmetal.exception.SubsystemException;
import org.longmetal.exception.SubsystemUninitializedException;
import org.longmetal.input.Gamepad.Axis;
import org.longmetal.input.Gamepad.Button;
import org.longmetal.input.Input;
import org.longmetal.subsystem.Climb;
import org.longmetal.subsystem.ControlPanel;
import org.longmetal.subsystem.DriveTrain;
import org.longmetal.subsystem.Intake;
import org.longmetal.subsystem.Pneumatics;
import org.longmetal.subsystem.Shooter;
import org.longmetal.subsystem.SubsystemManager;
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

    Input input;
    DriveTrain driveTrain;
    Intake intake;
    Shooter shooter;
    Climb climb;
    ControlPanel controlPanel;
    SubsystemManager manager;
    DigitalInput intakeLimit;
    Timer timer;
    Listener intakeListener;
    Listener panelListenerTurns;
    Listener panelListenerColor;
    ShootFormula formula;
    Pneumatics pneumatics;

    enum AutoMode {
        DO_NOTHING,
        DRIVE_BACK,
        SHOOT1,
        SHOOT2,
        SHOOT3,
        COLLECT1,
        COLLECT2,
        COLLECT3
    }

    SendableChooser<Boolean> chooserQuinnDrive;
    SendableChooser<AutoMode> autoModeChooser;

    Listener quinnDriveListener;
    Listener reverseListener;

    boolean endgameMode = false;
    boolean shooterCheck = false;
    boolean shooterStop = true;
    boolean panelUp = false;
    double shootLow = 0;
    double shootHigh = 0;
    boolean readyClimb = false;
    double conversionFactor = 2.35;
    int autoMode = 0;

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

        input = new Input();
        driveTrain = new DriveTrain();
        intake = new Intake(true);
        shooter = new Shooter(true);
        climb = new Climb(true);
        controlPanel = new ControlPanel(true);
        manager = new SubsystemManager();
        formula = new ShootFormula();
        intakeLimit = new DigitalInput(0);
        timer = new Timer();
        pneumatics = new Pneumatics(true);
        intakeListener =
                new Listener(
                        new Runnable() {
                            public void run() {
                                try {
                                    intake.runHopper(Constants.kTRANSPORT_SPEED);
                                } catch (SubsystemException e) {
                                    Console.log(e.getMessage());
                                }
                            }
                        },
                        null,
                        true);
        panelListenerTurns =
                new Listener(
                        new Runnable() {
                            public void run() {
                                try {
                                    pneumatics.flipArmUp();
                                    panelUp = true;
                                    // controlPanel.turnsMode();
                                    // pneumatics.flipArmDown();
                                    // panelUp = false;
                                } catch (SubsystemException e) {
                                    Console.log(e.getMessage());
                                }
                            }
                        },
                        new Runnable() {
                            public void run() {
                                try {
                                    pneumatics.flipArmDown();
                                    panelUp = false;
                                } catch (SubsystemException e) {
                                    Console.log(e.getMessage());
                                }
                            }
                        },
                        // null,
                        false);

        panelListenerColor =
                new Listener(
                        new Runnable() {
                            public void run() {
                                try {
                                    pneumatics.flipArmUp();
                                    panelUp = true;
                                    // controlPanel.colorMode();
                                    // pneumatics.flipArmDown();
                                    // panelUp = false;

                                } catch (SubsystemException e) {
                                    Console.log(e.getMessage());
                                }
                            }
                        },
                        new Runnable() {
                            public void run() {
                                try {
                                    pneumatics.flipArmDown();
                                    panelUp = false;
                                } catch (SubsystemException e) {
                                    Console.log(e.getMessage());
                                }
                            }
                        },
                        // null,
                        false);

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
                                input.setQuinnDrive(true);
                            }
                        },
                        new Runnable() {

                            @Override
                            public void run() {
                                input.setQuinnDrive(false);
                            }
                        },
                        false);

        reverseListener =
                new Listener(
                        new Runnable() {

                            @Override
                            public void run() {
                                driveTrain.setReverseDrive(true);
                            }
                        },
                        new Runnable() {

                            @Override
                            public void run() {
                                driveTrain.setReverseDrive(false);
                            }
                        },
                        false);

        // Display PID Coefficients on SmartDashboard

        SmartDashboard.putNumber("P Gain", shooter.kP);
        SmartDashboard.putNumber("I Gain", shooter.kI);
        SmartDashboard.putNumber("D Gain", shooter.kD);
        SmartDashboard.putNumber("I Zone", shooter.kIz);
        SmartDashboard.putNumber("Feed Forward", shooter.kFF);
        SmartDashboard.putNumber("Max Output", shooter.kMaxOutput);
        SmartDashboard.putNumber("Min Output", shooter.kMinOutput);

        autoModeChooser = new SendableChooser<AutoMode>();
        autoModeChooser.setDefaultOption("Do Nothing", AutoMode.DO_NOTHING);
        autoModeChooser.addOption("Drive Back", AutoMode.DRIVE_BACK);
        autoModeChooser.addOption("Shoot 1", AutoMode.SHOOT1);
        autoModeChooser.addOption("Shoot 2", AutoMode.SHOOT2);
        autoModeChooser.addOption("Shoot 3", AutoMode.SHOOT3);
        autoModeChooser.addOption("Collect 1", AutoMode.COLLECT1);
        autoModeChooser.addOption("Collect 2", AutoMode.COLLECT2);
        autoModeChooser.addOption("Collect 3", AutoMode.COLLECT3);
        SmartDashboard.putData("Auto Mode", autoModeChooser);

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

        SmartDashboard.putData("Drive Train", driveTrain.driveTrain);
        SmartDashboard.putBoolean("Quinn Drive", input.isQuinnDrive());
        quinnDriveListener.update(chooserQuinnDrive.getSelected());

        // Reverse Drive mode

        boolean forwardDrive = input.forwardStick.getRawButtonPressed(Constants.kFORWARD_BUTTON);
        boolean reverseDrive = input.forwardStick.getRawButton(Constants.kREVERSE_BUTTON);

        if (forwardDrive ^ reverseDrive) { // XOR: a or b but not both
            reverseListener.update(reverseDrive);
        }

        SmartDashboard.putBoolean("Reverse Drive", driveTrain.getReverseDrive());

        // Endgame mode
        SmartDashboard.putBoolean("Endgame Mode", endgameMode);

        // Shooter RPM
        SmartDashboard.putNumber("ShooterRPM", shooter.getSpeed());

        // Shooter CorrectRPM
        // shootLow = formula.shooterSpeed(/*Limelight distance*/ 4) * 0.95;
        // shootHigh = formula.shooterSpeed(/* Limelight distance */ 4) * 1.05;
        // shooterCheck = (shooter.getSpeed() > shootLow && shooter.getSpeed() < shootHigh);
        // SmartDashboard.putBoolean("ShooterCheck", shooterCheck);

        manager.checkSendables();

        // read PID coefficients from SmartDashboard
        double p = SmartDashboard.getNumber("P Gain", 0);
        double i = SmartDashboard.getNumber("I Gain", 0);
        double d = SmartDashboard.getNumber("D Gain", 0);
        double iz = SmartDashboard.getNumber("I Zone", 0);
        double ff = SmartDashboard.getNumber("Feed Forward", 0);
        double max = SmartDashboard.getNumber("Max Output", 0);
        double min = SmartDashboard.getNumber("Min Output", 0);

        conversionFactor = SmartDashboard.getNumber("Shoot Factor", 0);

        // if PID coefficients on SmartDashboard have changed, write new values to controller
        if ((p != shooter.kP)) {
            shooter.drumPID.setP(p);
            shooter.kP = p;
        }
        if ((i != shooter.kI)) {
            shooter.drumPID.setI(i);
            shooter.kI = i;
        }
        if ((d != shooter.kD)) {
            shooter.drumPID.setD(d);
            shooter.kD = d;
        }
        if ((iz != shooter.kIz)) {
            shooter.drumPID.setIZone(iz);
            shooter.kIz = iz;
        }
        if ((ff != shooter.kFF)) {
            shooter.drumPID.setFF(ff);
            shooter.kFF = ff;
        }
        if ((max != shooter.kMaxOutput) || (min != shooter.kMinOutput)) {
            shooter.drumPID.setOutputRange(min, max);
            shooter.kMinOutput = min;
            shooter.kMaxOutput = max;
        }

        SmartDashboard.putNumber("Set Point", shooterSetPoint);
        SmartDashboard.putNumber("Shoot Factor", 0);
        velocity = shooter.drumEncoder.getVelocity();
        double velocityDiff = Math.abs(shooterSetPoint - velocity);
        RPMInRange = velocityDiff <= shooter.acceptableDiff;
        SmartDashboard.putBoolean("RPM In Range", RPMInRange);
        SmartDashboard.putNumber("RPM Diff", velocityDiff);

        // Turn on the LEDs and enable vision processing
        limelightTable.getEntry("camMode").setDouble(0.0);
    }

    @Override
    public void disabledInit() {
        Delay.setEnabled(false);
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
        timer.reset();
        timer.start();
        Delay.setEnabled(true); // I think these do the same thing, but we need to fix it either way

        AutoMode autoModeEnum = autoModeChooser.getSelected();

        switch (autoModeEnum) {
            case DO_NOTHING:
                autoMode = 0;
                break;

            case DRIVE_BACK:
                autoMode = 1;
                break;

            case SHOOT1:
                autoMode = 11;
                break;

            case SHOOT2:
                autoMode = 12;
                break;

            case SHOOT3:
                autoMode = 13;
                break;

            case COLLECT1:
                autoMode = 21;
                break;

            case COLLECT2:
                autoMode = 22;
                break;

            case COLLECT3:
                autoMode = 23;
                break;
        }
    }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {
        boolean hasCollected = false;
        boolean hasTurned = false;
        if (autoMode == 0) {
            System.out.println("Auto works!");
            System.out.println("Do Nothing");
        } else if (autoMode == 1) {
            System.out.println("Drive back");
            // Make robot drive off initiation line
        } else if (autoMode == 11) {
            if (timer.get() < 4.0) //
            {
                try {
                    intake.setIntakeSpeed(1.0);
                    intake.setHopperSpeed(0.8);
                } catch (SubsystemException e) {
                    System.out.println("ERROR: Intake could not be turned on.");
                }
                driveTrain.curve(-0.2, -0.2, 0.0, 0.0);
            } else {
                try {
                    intake.setIntakeSpeed(0.0);
                    intake.setHopperSpeed(0.0);
                } catch (SubsystemException e) {
                    System.out.println("ERROR: Intake could not be turned off.");
                }

                driveTrain.curve(0.0, 0.0, 0.0, 0.0);
                hasCollected = true;
            }

            if (hasCollected) {
                updateVision(true);
                driveTrain.curveRaw(0, (tX / 30) / 2, true);
                if (tX < 0.05) {
                    updateVision(false);
                    driveTrain.curve(0.0, 0.0, 0.0, 0.0);
                    hasCollected = false;
                    hasTurned = true;
                }
            }

            if (hasTurned) {
                try {
                    // if (bButton) {
                    if (RPMInRange && velocity > 1500) {
                        shooter.setSingulatorSpeed(0.8);
                        intake.setHopperSpeed(1);
                    } else {
                        shooter.setSingulatorSpeed(0);
                        intake.setHopperSpeed(0.0);
                    }

                } catch (SubsystemException e) {
                    Console.error("Shooter Problem: " + problemName(e) + ". Stack Trace:");
                    e.printStackTrace();

                    boolean isUninitialized =
                            e.getClass().isInstance(SubsystemUninitializedException.class);
                    if (Shooter.getEnabled() && isUninitialized) {

                        shooter.init();
                    }
                }

                shooterSetPoint = SmartDashboard.getNumber("Set RPM", 0);
                shooter.drumPID.setReference(shooterSetPoint, ControlType.kVelocity);

                if (timer.get() > 14.5) hasTurned = false;
            }

            if (timer.get() > 14.5) { // Stops robot to prepare for tele-op
                driveTrain.curve(0.0, 0.0, 0.0, 0.0);
                try {
                    intake.setHopperSpeed(0.0);
                    shooter.setSingulatorSpeed(0);
                } catch (SubsystemException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else if (autoMode == 12) {

        } else if (autoMode == 13) {

        } else if (autoMode == 21) {

        } else if (autoMode == 22) {

        } else if (autoMode == 23) {

        } else {
            Console.log("Auto error");
        }

        //
        //
        //
        // I think this is a separate auto mode than the above code, but I don't know yet how to
        // separate them with a switcher
        boolean targetAcquired = false; // initially there should be no targets
        while (!targetAcquired) // while we don't see any,
        {
            driveTrain.curve(0.2, 0.2, 0.0, 0.0);

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
        Delay.setEnabled(true);
        shooterSetPoint = 0;
        lastShooterSetPoint = 0;
    }

    @Override
    /** This function is called periodically during operator control. */
    public void teleopPeriodic() {

        // Left Gamepad trigger, currently used for shooter
        double lTrigger = input.gamepad.getAxis(Axis.LT);

        // Right Gamepad trigger, currently used for intake
        double rTrigger = input.gamepad.getAxis(Axis.RT);

        // Left stick Y axis, left climb up/down
        double lStickY = input.gamepad.getAxis(Axis.LS_Y);

        // Right stick Y axis, right climb up/down
        double rStickY = input.gamepad.getAxis(Axis.RS_Y);

        // LB button, used to stop shooter
        boolean lButton = input.gamepad.getButton(Button.LB);

        // RB button, used to run reverse intake and release climb [only in climb mode]
        boolean rButton = input.gamepad.getButton(Button.RB);

        // X button, not currently used
        boolean xButton = input.gamepad.getButton(Button.X);

        // Y button, enables Control Panel Mode
        boolean yButton = input.gamepad.getButton(Button.Y);

        // A button, not currently used
        boolean aButton = input.gamepad.getButton(Button.A);

        // B button, currently prompts shooter to aim and set speed
        boolean bButton = input.gamepad.getButton(Button.B);

        // Start button, engages Endgame Mode
        boolean startButton = input.gamepad.getButton(Button.START);

        // Back button, disengages Endgame Mode
        boolean backButton = input.gamepad.getButton(Button.BACK);

        // Limelight line-up while B button is held
        if (bButton) {
            updateVision(true);
            driveTrain.curveRaw(0, (tX / 30) / 2, true);
        } else if (readyClimb || panelUp) { // When panel or climb up, drive slower
            updateVision(false);
            driveTrain.curve(
                    input.forwardStick.getY(),
                    input.forwardStick.getThrottle() * 0.1,
                    input.turnStick.getTwist(),
                    input.turnStick.getThrottle() * 0.5);
        } else {
            updateVision(false);
            driveTrain.curve(
                    input.forwardStick.getY(),
                    input.forwardStick.getThrottle(),
                    input.turnStick.getTwist(),
                    input.turnStick.getThrottle());
        }

        String currentSubsystem = "Subsystem";

        if (!endgameMode) {

            currentSubsystem = "Shooter";
            try {
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
                    shooter.setShooterRPM(0);
                    shooter.setSingulatorSpeed(0);
                } else {
                    if (bButton) {
                        // SmartDashboard.getNumber("Factor", conversionFactor);

                        updateVision(true);
                        if (tY >= 10) {
                            shooterSetPoint =
                                    (double)
                                            LMMath.limit(
                                                    formula.shooterSpeed(
                                                                    Vision.getLimelightDistance(
                                                                            tY /*, Vision.Target.POWER_PORT*/))
                                                            * 2.35,
                                                    shooter.minRPM,
                                                    shooter.maxRPM);
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
                            shooter.setSingulatorSpeed(lTrigger);
                            intake.setHopperSpeed(1);
                            // These don't work for some reason, so they're duplicated in the intake
                            // section
                        } else {
                            shooter.setSingulatorSpeed(0);
                            intake.setHopperSpeed(0);
                        }
                    } else if (aButton) { // Sets shooter to lower speed to place into lower port
                        updateVision(false);
                        shooterSetPoint = 1500;

                        if (RPMInRange) {
                            shooter.setSingulatorSpeed(1);
                            intake.setHopperSpeed(1);
                        } else {
                            shooter.setSingulatorSpeed(0);
                            intake.setHopperSpeed(0);
                        }
                    } else {
                        updateVision(false);
                        shooterSetPoint = shooter.minRPM;
                    }
                }

                SmartDashboard.putNumber("Set", shooterSetPoint);
                if (shooterSetPoint != lastShooterSetPoint) {
                    shooter.drumPID.setReference(shooterSetPoint, ControlType.kVelocity);
                    // shooter.setShooterRPM(shooterSetPoint);
                    lastShooterSetPoint = shooterSetPoint;
                }

            } catch (SubsystemException e) {
                Console.error(currentSubsystem + " Problem: " + problemName(e) + ". Stack Trace:");
                e.printStackTrace();

                boolean isUninitialized =
                        e.getClass().isInstance(SubsystemUninitializedException.class);
                if (Shooter.getEnabled() && isUninitialized) {

                    shooter.init();
                }
            }

            currentSubsystem = "Intake";
            try {
                // Sets intake to a speed
                if (rTrigger > Constants.kINPUT_DEADBAND) {
                    intake.setIntakeSpeed(rTrigger);
                    intake.setHopperSpeed(rTrigger);
                } else if (rButton) { // Reverse intake
                    intake.setIntakeSpeed(-0.3);
                } else { // Stop intake
                    intake.setIntakeSpeed(0);
                    intake.setHopperSpeed(0);
                }

                // if (bButton && lTrigger > Constants.kINPUT_DEADBAND) {
                //     intake.setHopperSpeed(1);
                //     /*} else if (xButton) {
                //     intake.setHopperSpeed(0.8);*/
                // } else {
                //     intake.setHopperSpeed(0);
                // }

                intakeListener.update(intakeLimit.get());

            } catch (SubsystemException e) {
                Console.error(currentSubsystem + " Problem: " + problemName(e) + ". Stack Trace:");
                e.printStackTrace();

                boolean isUninitialized =
                        e.getClass().isInstance(SubsystemUninitializedException.class);
                if (currentSubsystem.equals("Intake") && Intake.getEnabled() && isUninitialized) {

                    intake.init();
                }
            }

            currentSubsystem = "Control Panel";
            try {
                // Flip up control panel and engage based on FMS values
                if (yButton) {
                    // For now, this button will just spin the motor for testing purposes
                    controlPanel.spin();
                } else {
                    controlPanel.stop();
                }

                // Temporary control for flipping arm up
                panelListenerTurns.update(!xButton);

            } catch (SubsystemException e) {
                Console.error(currentSubsystem + " Problem: " + problemName(e) + ". Stack Trace:");
                e.printStackTrace();

                boolean isUninitialized =
                        e.getClass().isInstance(SubsystemUninitializedException.class);
                if (ControlPanel.getEnabled() && isUninitialized) {

                    controlPanel.init();
                }
            }

            // Puts the robot into endgame mode, disabling all manipulator subsystems
            if (startButton) {
                endgameMode = true;
            }
        } else {
            currentSubsystem = "Climb";
            try {
                if (rButton) {
                    // Release climb upwards, disengage solenoids
                    pneumatics.setRatchet(false);
                    readyClimb = true;
                    climb.setLeftWinchSpeed(Constants.CLIMB_SPEED);
                    climb.setRightWinchSpeed(-Constants.CLIMB_SPEED);
                }

                if (backButton) {
                    endgameMode = false;
                    climb.setWinchSpeed(0);
                }

                if (readyClimb) {

                    // Sticks up
                    if (lStickY < -Constants.kINPUT_DEADBAND
                            || rStickY < -Constants.kINPUT_DEADBAND) {

                        // Disengage ratchet
                        pneumatics.setRatchet(false);

                        // Add 0.5 second  delay after ratchet disengages, before motors go
                        // if (timer.hasElapsed(0.5)) {
                        if (lStickY < -Constants.kINPUT_DEADBAND) {
                            // Let out left winch
                            climb.setLeftWinchSpeed(-lStickY / 2);
                        }

                        if (rStickY < -Constants.kINPUT_DEADBAND) {
                            // Let out right winch
                            climb.setRightWinchSpeed(rStickY / 2);
                        }
                        // }

                    } else {
                        // Engage ratchet
                        pneumatics.setRatchet(true);

                        // Left stick down
                        // Reel in left climb (raise robot)
                        if (lStickY > Constants.kINPUT_DEADBAND) {
                            climb.setLeftWinchSpeed(-lStickY);
                        } else {
                            climb.setLeftWinchSpeed(0);
                        }

                        // Right stick down
                        // Reel in right climb (raise robot)
                        if (rStickY > Constants.kINPUT_DEADBAND) {
                            climb.setRightWinchSpeed(rStickY);
                        } else {
                            climb.setRightWinchSpeed(0);
                        }
                    }
                }
            } catch (SubsystemException e) {
                Console.error(currentSubsystem + " Problem: " + problemName(e) + ". Stack Trace:");
                e.printStackTrace();

                boolean isUninitialized =
                        e.getClass().isInstance(SubsystemUninitializedException.class);
                if (Climb.getEnabled() && isUninitialized) {

                    climb.init();
                }
            }
        }
    }

    private String problemName(SubsystemException e) {
        if (e.getClass().isInstance(SubsystemDisabledException.class)) {
            return "Subsystem Disabled";
        } else if (e.getClass().isInstance(SubsystemUninitializedException.class)) {
            return "Subsystem Unitialized";
        } else {
            return "Generic Subsystem Problem";
        }
    }

    @Override
    public void testInit() {
        SmartDashboard.putNumber("Set RPM", 0);
    }

    /** This function is called periodically during test mode. */
    // For now, testPeriodic() will allow us to test all the controls directly, bypassing our
    // automated features
    @Override
    public void testPeriodic() {
        // Limelight line-up while 1 button is held
        if (input.forwardStick.getRawButton(1)) {
            updateVision(true);
            driveTrain.curveRaw(0, (tX / 30) / 2, true);
        } else {
            updateVision(false);
            driveTrain.curve(
                    input.forwardStick.getY(),
                    input.forwardStick.getThrottle(),
                    input.turnStick.getTwist(),
                    input.turnStick.getThrottle());
        }

        // Left Gamepad trigger, currently used for shooter
        double lTrigger = input.gamepad.getAxis(Axis.LT);

        // Right Gamepad trigger, currently used for intake
        double rTrigger = input.gamepad.getAxis(Axis.RT);

        // Left stick Y axis, left climb up/down
        double lStickY = input.gamepad.getAxis(Axis.LS_Y);

        // Right stick Y axis, right climb up/down
        double rStickY = input.gamepad.getAxis(Axis.RS_Y);

        // LB button, used to stop shooter
        boolean lButton = input.gamepad.getButton(Button.LB);

        // RB button, used to run reverse intake and release climb [only in climb mode]
        boolean rButton = input.gamepad.getButton(Button.RB);

        // X button, not currently used
        boolean xButton = input.gamepad.getButton(Button.X);

        // Y button, enables Control Panel Mode
        boolean yButton = input.gamepad.getButton(Button.Y);

        // A button, not currently used
        boolean aButton = input.gamepad.getButton(Button.A);

        // B button, currently prompts shooter to aim and set speed
        boolean bButton = input.gamepad.getButton(Button.B);

        // Start button, engages Endgame Mode
        boolean startButton = input.gamepad.getButton(Button.START);

        String currentSubsystem = "Subsystem";

        if (!endgameMode) {

            currentSubsystem = "Shooter";
            try {
                // if (lTrigger > Constants.kINPUT_DEADBAND) {
                //     shooter.runShooter(lTrigger);
                // }

                // Stops shooter
                // if (lButton) {
                //     shooter.stop();
                // }

                // if (bButton) {
                if (RPMInRange && velocity > 1500) {
                    shooter.setSingulatorSpeed(0.8);
                } else {
                    shooter.setSingulatorSpeed(0);
                }

            } catch (SubsystemException e) {
                Console.error(currentSubsystem + " Problem: " + problemName(e) + ". Stack Trace:");
                e.printStackTrace();

                boolean isUninitialized =
                        e.getClass().isInstance(SubsystemUninitializedException.class);
                if (Shooter.getEnabled() && isUninitialized) {

                    shooter.init();
                }
            }

            currentSubsystem = "Intake";
            try {
                // Sets intake to a speed
                if (rTrigger > Constants.kINPUT_DEADBAND) {
                    intake.setIntakeSpeed(rTrigger);
                } else if (rButton) { // Reverse intake
                    intake.setIntakeSpeed(-0.3);
                } else { // Stop intake
                    intake.setIntakeSpeed(0);
                }

                if (aButton) {
                    intake.setHopperSpeed(0.8);
                } else {
                    intake.setHopperSpeed(0);
                }

            } catch (SubsystemException e) {
                Console.error(currentSubsystem + " Problem: " + problemName(e) + ". Stack Trace:");
                e.printStackTrace();

                boolean isUninitialized =
                        e.getClass().isInstance(SubsystemUninitializedException.class);
                if (currentSubsystem.equals("Intake") && Intake.getEnabled() && isUninitialized) {

                    intake.init();
                }
            }

            currentSubsystem = "Control Panel";
            try {
                // Flip up control panel and engage based on FMS values
                if (yButton) {
                    controlPanel.spin();
                } else {
                    controlPanel.stop();
                }

                // Temporary control for flipping arm up
                panelListenerTurns.update(xButton);

            } catch (SubsystemException e) {
                Console.error(currentSubsystem + " Problem: " + problemName(e) + ". Stack Trace:");
                e.printStackTrace();

                boolean isUninitialized =
                        e.getClass().isInstance(SubsystemUninitializedException.class);
                if (ControlPanel.getEnabled() && isUninitialized) {

                    controlPanel.init();
                }
            }

            currentSubsystem = "Climb";
            try {

                if (startButton) {
                    climb.setWinchSpeed(0);
                    pneumatics.setRatchet(true);
                }

                // add safety to make sure that you don't have them go in opposite directions?

                // Left winch engage
                if (lStickY > Constants.kINPUT_DEADBAND) {
                    climb.setLeftWinchSpeed(-lStickY);
                }

                if (lStickY < -Constants.kINPUT_DEADBAND) {
                    pneumatics.setRatchet(true);
                    climb.setLeftWinchSpeed(0.05);
                } else {
                    pneumatics.setRatchet(false);
                }

                // Right winch engage
                if (rStickY > Constants.kINPUT_DEADBAND) {
                    climb.setRightWinchSpeed(rStickY);
                }

                if (rStickY < -Constants.kINPUT_DEADBAND) {
                    pneumatics.setRatchet(true);
                    climb.setRightWinchSpeed(-0.05);
                } else {
                    pneumatics.setRatchet(false);
                }

            } catch (SubsystemException e) {
                Console.error(currentSubsystem + " Problem: " + problemName(e) + ". Stack Trace:");
                e.printStackTrace();

                boolean isUninitialized =
                        e.getClass().isInstance(SubsystemUninitializedException.class);
                if (Climb.getEnabled() && isUninitialized) {

                    climb.init();
                }
            }

            shooterSetPoint = SmartDashboard.getNumber("Set RPM", 0);
            shooter.drumPID.setReference(shooterSetPoint, ControlType.kVelocity);
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
