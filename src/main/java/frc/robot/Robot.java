/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
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
import org.longmetal.subsystem.Shooter;
import org.longmetal.subsystem.SubsystemManager;
import org.longmetal.util.Console;
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
    Listener panelListener;
    ShootFormula formula;
    Compressor compressor;
    

    SendableChooser<Boolean> chooserQuinnDrive;

    Listener quinnDriveListener;
    Listener reverseListener;

    boolean endgameMode = false;

    NetworkTable limelightTable =
            NetworkTableInstance.getDefault()
                    .getTable("limelight"); // takes in values from limelight
    NetworkTableEntry tx = limelightTable.getEntry("tx"); // distances
    NetworkTableEntry ty = limelightTable.getEntry("ty"); // height or something

    double tX, tY;

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
        compressor = new Compressor(0);
        intakeListener =
                new Listener(
                        null,
                        new Runnable() {
                            public void run() {
                                try {
                                    intake.runHopper(Constants.kTRANSPORT_SPEED);
                                } catch (SubsystemException e) {
                                    Console.log(e.getMessage());
                                }
                            }
                        },
                        true);
        panelListener =
                new Listener(
                        new Runnable() {
                            public void run() {
                                try {
                                    controlPanel.flipArmUp();
                                } catch (SubsystemException e) {
                                    Console.log(e.getMessage());
                                }
                            }
                        },
                        new Runnable() {
                            public void run() {
                                try {
                                    controlPanel.flipArmDown();
                                } catch (SubsystemException e) {
                                    Console.log(e.getMessage());
                                }
                            }
                        },
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

        tX = tx.getDouble(0.0);
        tY = ty.getDouble(0.0);

        SmartDashboard.putNumber("LimelightX", tX);
        SmartDashboard.putNumber("LimelightY", tY);

        manager.checkSendables();
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
    public void autonomousInit() {}

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {}

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {

        // Limelight line-up while 1 button is held
        if (input.forwardStick.getRawButton(1)) {
            limelightTable.getEntry("ledMode").setDouble(3.0);
            limelightTable.getEntry("camMode").setDouble(0.0);
            driveTrain.curveRaw(0, (tX / 30) / 2, true);
        } else {
            limelightTable.getEntry("ledMode").setDouble(0.0);
            limelightTable.getEntry("camMode").setDouble(3.0);
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
                if (bButton) {
                    // shooter.runShooter(
                    //        formula.shooterSpeed(
                    //              5)); // Will set shooter based on limelight distance
                    // Add automatic limelight alignment
                    shooter.testShooter(0.2);

                    // Singulator directly controlled by left trigger
                    // Hopper is either on or off
                    if (bButton && lTrigger > Constants.kINPUT_DEADBAND) {
                        shooter.setSingulatorSpeed(lTrigger);
                        // intake.setHopperSpeed(0.8);
                    } else {
                        shooter.setSingulatorSpeed(0);
                        // intake.setHopperSpeed(0);
                    }
                }

                /*else {
                    shooter.runShooter(Constants.kSHOOTER_MIN);
                }*/

                // Stops shooter
                if (lButton) {
                    shooter.stop();
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
                    intake.setIntakeSpeed(-0.2);
                } else { // Stop intake
                    intake.setIntakeSpeed(0);
                }

                // Temporary button mapping... will be automatic
                if (aButton) {
                    intake.setHopperSpeed(0.8);
                } else {
                    intake.setHopperSpeed(0);
                }

                if (bButton && lTrigger > Constants.kINPUT_DEADBAND) {
                    intake.setHopperSpeed(0.8);
                } else {
                    intake.setHopperSpeed(0);
                }

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
                panelListener.update(xButton);

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
                }

                // Left winch engage
                if (lStickY > Constants.kINPUT_DEADBAND) {
                    climb.setLeftWinchSpeed(lStickY);
                }

                // Right winch engage
                if (rStickY > Constants.kINPUT_DEADBAND) {
                    climb.setRightWinchSpeed(rStickY);
                }
                // Need to add solenoids engaging and disengaging
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

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {}
}
