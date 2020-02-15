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
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.File;
import java.util.Scanner;
import org.longmetal.Climb;
import org.longmetal.Constants;
import org.longmetal.ControlPanel;
import org.longmetal.DriveTrain;
import org.longmetal.Intake;
import org.longmetal.Shooter;
import org.longmetal.exception.SubsystemDisabledException;
import org.longmetal.exception.SubsystemException;
import org.longmetal.exception.SubsystemUninitializedException;
import org.longmetal.input.Input;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private static final String kDefaultAuto = "Default";
    private static final String kCustomAuto = "My Auto";
    private String m_autoSelected;
    private final SendableChooser<String> m_chooser = new SendableChooser<>();

    Input input;
    DriveTrain driveTrain;
    Intake intake;
    Shooter shooter;
    Climb climb;
    ControlPanel controlPanel;

    SendableChooser<Boolean> chooserQuinnDrive;

    boolean lastQuinnDrive = false;
    boolean lastForwardDrive = false;
    boolean lastReverseDrive = false;
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
        m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
        m_chooser.addOption("My Auto", kCustomAuto);
        SmartDashboard.putData("Auto choices", m_chooser);

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
            System.out.println(
                    "Could not determine commit or branch. ("
                            + e.getLocalizedMessage()
                            + ") Trace:");
            e.printStackTrace();
        }

        input = new Input();
        driveTrain = new DriveTrain();
        intake = new Intake(true);
        shooter = new Shooter(true);
        climb = new Climb(true);
        controlPanel = new ControlPanel(true);

        chooserQuinnDrive = new SendableChooser<>();
        chooserQuinnDrive.setDefaultOption("Disabled", false);
        chooserQuinnDrive.addOption("Enabled", true);
        SmartDashboard.putData("Quinn Drive Chooser", chooserQuinnDrive);
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
        boolean quinnDrive = (Boolean) chooserQuinnDrive.getSelected();
        if (quinnDrive != lastQuinnDrive) {
            input.setQuinnDrive(quinnDrive);
        }
        lastQuinnDrive = quinnDrive;

        // Reverse Drive mode

        boolean forwardDrive = input.forwardStick.getRawButtonPressed(Constants.kFORWARD_BUTTON);
        boolean reverseDrive = input.forwardStick.getRawButton(Constants.kREVERSE_BUTTON);

        if (forwardDrive
                && forwardDrive != lastForwardDrive
                && !reverseDrive) { // If it is pressed and it changed and both aren't pressed
            // Set forward drive
            driveTrain.setReverseDrive(false);
        }
        lastForwardDrive = forwardDrive;

        if (reverseDrive
                && reverseDrive != lastReverseDrive
                && !forwardDrive) { // If it is pressed and it changed and both aren't pressed
            // Set reverse drive
            driveTrain.setReverseDrive(true);
        }
        lastReverseDrive = reverseDrive;

        SmartDashboard.putBoolean("Reverse Drive", driveTrain.getReverseDrive());

        tX = tx.getDouble(0.0);
        tY = ty.getDouble(0.0);

        SmartDashboard.putNumber("LimelightX", tX);
        SmartDashboard.putNumber("LimelightY", tY);
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
        m_autoSelected = m_chooser.getSelected();
        // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
        System.out.println("Auto selected: " + m_autoSelected);
    }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic() {
        switch (m_autoSelected) {
            case kCustomAuto:
                // Put custom auto code here
                break;
            case kDefaultAuto:
            default:
                // Put default auto code here
                break;
        }
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic() {

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
        double lTrigger = input.gamepad.getRawAxis(Constants.kA_LEFT_TRIGGER);

        // Right Gamepad trigger, currently used for intake
        double rTrigger = input.gamepad.getRawAxis(Constants.kA_RIGHT_TRIGGER);

        // Left stick Y axis, left climb up/down
        double lStickY = input.gamepad.getRawAxis(Constants.kA_LS_Y);

        // Right stick Y axis, right climb up/down
        double rStickY = input.gamepad.getRawAxis(Constants.kA_RS_Y);

        // LB button, used to stop shooter
        boolean lButton = input.gamepad.getRawButton(Constants.kB_LB);

        // RB button, used to run reverse intake and release climb [only in climb mode]
        boolean rButton = input.gamepad.getRawButton(Constants.kB_RB);

        // X button, not currently used
        boolean xButton = input.gamepad.getRawButton(Constants.kB_X);

        // Y button, enables Control Panel Mode
        boolean yButton = input.gamepad.getRawButton(Constants.kB_Y);

        // A button, not currently used
        boolean aButton = input.gamepad.getRawButton(Constants.kB_A);

        // B button, currently prompts shooter to aim and set speed
        boolean bButton = input.gamepad.getRawButton(Constants.kB_B);

        // Start button, engages Endgame Mode
        boolean startButton = input.gamepad.getRawButton(Constants.kB_START);

        String currentSubsystem = "Subsystem";
        try {
            if (!endgameMode) {
                // Aims and sets shooter to limelight speed
                if (bButton) {
                    // Do stuff
                }

                // Sets shooter to a speed
                if (lTrigger > Constants.kINPUT_DEADBAND) { // Left trigger has passed deadband
                    shooter.testShooter(lTrigger);
                }

                // Stops shooter
                if (lButton) {
                    shooter.testShooter(0);
                }

                // Sets intake to a speed
                if (lTrigger > Constants.kINPUT_DEADBAND) {
                    intake.setIntakeSpeed(rTrigger);
                } else if (rButton) { // Reverse intake
                    intake.setIntakeSpeed(-0.2);
                } else { // Stop intake
                    intake.setIntakeSpeed(0);
                }

                // Temporary button mapping... will be automatic
                if (aButton) {
                    intake.setHopperSpeed(0.5);
                } else {
                    intake.setHopperSpeed(0);
                }

                // Temporary button mapping... will be automatically
                if (xButton) {
                    shooter.setSingulatorSpeed(1);
                } else {
                    shooter.setSingulatorSpeed(0);
                }

                // Flip up control panel and engage based on FMS values
                if (yButton) {
                    // For now, this button will just spin the motor for testing purposes
                    controlPanel.setSpinnerSpeed();
                } else {
                    controlPanel.spinnerStop();
                }

                // Puts the robot into endgame mode, disabling all manipulator subsystems
                if (startButton) {
                    endgameMode = true;
                }
            } else {
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
            }

        } catch (SubsystemException e) {
            // status.sendStatus(Status.PROBLEM);
            System.out.println(currentSubsystem + " Problem: " + problemName(e) + ". Stack Trace:");
            e.printStackTrace();

            boolean isUninitialized =
                    e.getClass().isInstance(SubsystemUninitializedException.class);
            if (currentSubsystem.equals("Shooter") && Shooter.getEnabled() && isUninitialized) {

                shooter.init();
            } else if (currentSubsystem.equals("Intake")
                    && Intake.getEnabled()
                    && isUninitialized) {

                intake.init();
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
