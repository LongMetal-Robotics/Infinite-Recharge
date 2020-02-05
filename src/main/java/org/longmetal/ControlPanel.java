package org.longmetal;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorSensorV3;

public class ControlPanel {
    private TalonSRX tSRX;
    private DoubleSolenoid dSolenoid;
    private ColorSensorV3 csensorV3;

    private static boolean enabled = true;
    private boolean initialized = false;

    public ControlPanel(boolean setEnabled)
    {
        enabled = setEnabled;
        if (enabled) 
        {
            init();
        } 
        else 
        {
            System.out.println("[WARN]\tShooter wasn't enabled on startup. You must call init() on it later to use it.");
        }
    }

    public void init() 
    {
        //csensorV3 = new ColorSensorV3
        //tSRX = new TalonSRX(Constants.kP_CONTROLPANEL);
        //Note colorsensor has its own port but idk how to access it.
        initialized = true;
    }

}


