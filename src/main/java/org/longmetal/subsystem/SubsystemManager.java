package org.longmetal.subsystem;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.longmetal.Constants;

public class SubsystemManager {
    private SendableChooser<Runnable> shooterEnable;
    private SendableChooser<Runnable> intakeEnable;
    private SendableChooser<Runnable> climbEnable;

    public SubsystemManager() {
        Preferences preferences = Preferences.getInstance();

        int i = 0;

        for (Subsystem subsystem : Subsystem.values()) {
            boolean subsystemEnableValue = preferences.getBoolean(subsystem.name(), false);

            Runnable enableSubsystem =
                    new Runnable() {
                        @Override
                        public void run() {
                            SubsystemManager.setSubsystem(subsystem, true);
                        }
                    };
            Runnable disableSubsystem =
                    new Runnable() {

                        @Override
                        public void run() {
                            SubsystemManager.setSubsystem(subsystem, false);
                        }
                    };

            // subsystemEnable;
        }

        // Shooter
        boolean shooterEnableValue =
                preferences.getBoolean(Subsystem.SHOOTER.name(), false) /* Shooter enabled */;

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
            shooterEnable.setDefaultOption(Constants.ENABLED, enableShooter);
            shooterEnable.addOption(Constants.DISABLED, disableShooter);
        } else {
            shooterEnable.addOption(Constants.ENABLED, enableShooter);
            shooterEnable.setDefaultOption(Constants.DISABLED, disableShooter);
        }
        SmartDashboard.putData(Constants.SHOOTER_KEY, shooterEnable);
        SmartDashboard.putBoolean(Constants.kSHOOTER_KEY, shooterEnableValue);

        setSubsystem(Subsystem.SHOOTER, shooterEnableValue);

        // Intake
        boolean intakeEnableValue =
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
            intakeEnable.setDefaultOption(Constants.ENABLED, enableIntake);
            intakeEnable.addOption(Constants.DISABLED, disableIntake);
        } else {
            intakeEnable.addOption(Constants.ENABLED, enableIntake);
            intakeEnable.setDefaultOption(Constants.DISABLED, disableIntake);
        }
        SmartDashboard.putData(Constants.INTAKE_KEY, intakeEnable);
        SmartDashboard.putBoolean(Constants.kINTAKE_KEY, intakeEnableValue);

        setSubsystem(Subsystem.INTAKE, intakeEnableValue);

        // Climb
        boolean climbEnableValue =
                preferences.getBoolean(Constants.kCLIMB_KEY, false) /* Climb enabled */;

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
        if (climbEnableValue) {
            climbEnable.setDefaultOption(Constants.ENABLED, enableClimb);
            climbEnable.addOption(Constants.DISABLED, disableClimb);
        } else {
            climbEnable.addOption(Constants.ENABLED, enableClimb);
            climbEnable.setDefaultOption(Constants.DISABLED, disableClimb);
        }
        SmartDashboard.putData(Constants.CLIMB_KEY, climbEnable);
        SmartDashboard.putBoolean(Constants.kCLIMB_KEY, climbEnableValue);

        setSubsystem(Subsystem.CLIMB, climbEnableValue);
    }

    public void checkSendables() {
        shooterEnable.getSelected().run();
        intakeEnable.getSelected().run();
        climbEnable.getSelected().run();
    }

    public static void setSubsystem(Subsystem subsystem, boolean enabled) {
        Preferences preferences = Preferences.getInstance();
        preferences.putBoolean(subsystem.name(), enabled);
        SmartDashboard.putBoolean(subsystem.name(), enabled);
        switch (subsystem) {
            case SHOOTER:
                Shooter.staticSetEnabled(enabled);
                break;

            case INTAKE:
                Intake.staticSetEnabled(enabled);
                break;

            case CLIMB:
                Climb.staticSetEnabled(enabled);
                break;
        }
    }

    public enum Subsystem {
        SHOOTER,
        INTAKE,
        CLIMB
    }
}
