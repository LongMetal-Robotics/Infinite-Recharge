package org.longmetal.util;

import edu.wpi.first.wpilibj.Joystick;

public class DriveStick extends Joystick
{
    private Joystick aStick;

    public DriveStick(int port)
    {
        super(port);
        aStick = new Joystick(port);
    }
}
