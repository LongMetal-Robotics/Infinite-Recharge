/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.File;
import java.util.Scanner;
import org.longmetal.Constants;
import org.longmetal.subsystem.DriveTrain;
import org.longmetal.Input;

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
    private SendableChooser<String> m_chooser = new SendableChooser<>();

    Input input;
    DriveTrain driveTrain;

    SendableChooser<Boolean> chooserQuinnDrive;

    boolean lastQuinnDrive = false;
    boolean lastForwardDrive = false;
    boolean lastReverseDrive = false;

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
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select between different
     * autonomous modes using the dashboard. The sendable chooser code works with the Java
     * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
     * uncomment the getString line to get the auto name from the text box below the Gyro
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

        driveTrain.curve(
                input.forwardStick.getY(),
                input.forwardStick.getThrottle(),
                input.turnStick.getTwist(),
                input.turnStick.getThrottle());
    }

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic() {}
}
