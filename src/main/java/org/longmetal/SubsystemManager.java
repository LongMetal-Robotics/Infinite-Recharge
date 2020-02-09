package org.longmetal;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.longmetal.exception.SubsystemDisabledException;
import org.longmetal.exception.SubsystemException;
import org.longmetal.exception.SubsystemUninitializedException;

public class SubsystemManager {
    private SendableChooser<Runnable> shooterEnable;
    private SendableChooser<Runnable> intakeEnable;
    private SendableChooser<Runnable> climbEnable;
    private SendableChooser<Runnable> controlPanelEnable;

    public SubsystemManager() {
        Preferences preferences = Preferences.getInstance();
        boolean shooterEnableValue = false;
        boolean intakeEnableValue = false;
        boolean controlPanelEnableValue = false;
        boolean climbEnableValue = false;

        shooterEnableValue =
                preferences.getBoolean(Constants.kSHOOTER_KEY, false) /* Shooter enabled */;

        Runnable enableShooter =
                new Runnable() {

                    @Override
                    public void run() {
                        SubsystemManager.setSubsystem(Subsystem.SHOOTER, true);
                    }
                };
        Runnable disableShooter =
                new Runnable() {

                    @Override
                    public void run() {
                        SubsystemManager.setSubsystem(Subsystem.SHOOTER, false);
                    }
                };

        shooterEnable = new SendableChooser<>();
        if (shooterEnableValue) { // (hopefully) set the order of the options in the menu so enabled
            // is always first but the
            // initially selected option indicates whether the subsystem is actually enabled or not
            // based on previously-saved preferences
            shooterEnable.setDefaultOption(Constants.kENABLED, enableShooter);
            shooterEnable.addOption(Constants.kDISABLED, disableShooter);
        } else {
            shooterEnable.addOption(Constants.kENABLED, enableShooter);
            shooterEnable.setDefaultOption(Constants.kDISABLED, disableShooter);
        }
        SmartDashboard.putData(Constants.kSHOOTER_ENABLER_KEY, shooterEnable);
        SmartDashboard.putBoolean(Constants.kSHOOTER_STATE_KEY, shooterEnableValue);

        setSubsystem(Subsystem.SHOOTER, shooterEnableValue);

        intakeEnableValue =
                preferences.getBoolean(Constants.kINTAKE_KEY, false) /* Intake enabled */;

        Runnable enableIntake =
                new Runnable() {

                    @Override
                    public void run() {
                        SubsystemManager.setSubsystem(Subsystem.INTAKE, true);
                    }
                };

        Runnable disableIntake =
                new Runnable() {

                    @Override
                    public void run() {
                        SubsystemManager.setSubsystem(Subsystem.INTAKE, false);
                    }
                };

        intakeEnable = new SendableChooser<>();
        if (intakeEnableValue) {
            intakeEnable.setDefaultOption(Constants.kENABLED, enableIntake);
            intakeEnable.addOption(Constants.kDISABLED, disableIntake);
        } else {
            intakeEnable.addOption(Constants.kENABLED, enableIntake);
            intakeEnable.setDefaultOption(Constants.kDISABLED, disableIntake);
        }
        SmartDashboard.putData(Constants.kINTAKE_ENABLER_KEY, intakeEnable);
        SmartDashboard.putBoolean(Constants.kINTAKE_STATE_KEY, intakeEnableValue);

        setSubsystem(Subsystem.INTAKE, intakeEnableValue);

        climbEnableValue =
                preferences.getBoolean(Constants.kINTAKE_KEY, false) /* Climb enabled */;

        Runnable enableClimb =
                new Runnable() {

                    @Override
                    public void run() {
                        SubsystemManager.setSubsystem(Subsystem.CLIMB, true);
                    }
                };

        Runnable disableClimb =
                new Runnable() {

                    @Override
                    public void run() {
                        SubsystemManager.setSubsystem(Subsystem.CLIMB, false);
                    }
                };

        climbEnable = new SendableChooser<>();
        if (intakeEnableValue) {
            climbEnable.setDefaultOption(Constants.kENABLED, enableClimb);
            climbEnable.addOption(Constants.kDISABLED, disableClimb);
        } else {
            climbEnable.addOption(Constants.kENABLED, enableClimb);
            climbEnable.setDefaultOption(Constants.kDISABLED, disableClimb);
        }
        SmartDashboard.putData(Constants.kINTAKE_ENABLER_KEY, climbEnable);
        SmartDashboard.putBoolean(Constants.kINTAKE_STATE_KEY, climbEnableValue);

        setSubsystem(Subsystem.CLIMB, climbEnableValue);
    }

    public void checkSendables() {
        shooterEnable.getSelected().run();
        intakeEnable.getSelected().run();
        climbEnable.getSelected().run();
        controlPanelEnable.getSelected().run();
    }

    public static void setSubsystem(Subsystem subsystem, boolean enabled) {
        Preferences preferences = Preferences.getInstance();
        switch (subsystem) {
            case SHOOTER:
                Shooter.staticSetEnabled(enabled);
                preferences.putBoolean(Constants.kSHOOTER_KEY, enabled);
                SmartDashboard.putBoolean(Constants.kSHOOTER_STATE_KEY, enabled);
                break;

            case INTAKE:
                Intake.staticSetEnabled(enabled);
                preferences.putBoolean(Constants.kINTAKE_KEY, enabled);
                SmartDashboard.putBoolean(Constants.kINTAKE_STATE_KEY, enabled);
                break;
        }
    }

    public static void check(boolean enabled, boolean initialized) throws SubsystemException {
        if (!initialized) {
            throw new SubsystemUninitializedException();
        }
        if (!enabled) {
            throw new SubsystemDisabledException();
        }
    }

    public enum Subsystem {
        SHOOTER,
        INTAKE,
        CONTROL_PANEL,
        CLIMB
    }
}
