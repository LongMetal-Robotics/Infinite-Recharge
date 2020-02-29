package org.longmetal.subsystem;

public class Vision {
    public static final double feetToMeters = 1 / 3.281; // Conversion factor
    public static final double limelightHeight = 0.58417; // Limelight's height above the ground (m)
    public static final double limelightAngle = 0; // Angle above horizontal (deg)
    public static final double powerPortHeight = 2.30; // Center of target (m)
    public static final double loadingBayHeight = 0.42; // Center of target (m)

    public static double getLimelightDistance(double angleY/*, Target target*/) {
        double targetHeight = powerPortHeight;
        // switch (target) {
        //     case POWER_PORT:
        //         targetHeight = powerPortHeight;
        //         break;
        //     case LOADING_BAY:
        //         targetHeight = loadingBayHeight;
        //         break;
        //     default:
        //         throw new EnumConstantNotPresentException(Target.class, target.name());
        // }

        double distance =
                (targetHeight - limelightHeight)
                        / Math.tan(Math.toRadians(limelightAngle + angleY));

        return distance;
    }

    // public enum Target {
    //     POWER_PORT,
    //     LOADING_BAY
    // }
}
