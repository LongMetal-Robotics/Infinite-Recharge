package org.longmetal.subsystem;

public class Vision {
    public static final double feetToMeters = 1 / 78.74; // Conversion factor
    public static final double limelightHeight = (1 + (11 / 12.0)) * feetToMeters; // Limelight's height above the ground (m)
    public static final double limelightAngle = 11; // Angle above horizontal (deg)
    public static final double powerPortHeight = 230 * 100; // Center of target (m)
    public static final double loadingBayHeight = 42 * 100; // Center of target (m)

    public static double getLimelightDistance(double angleY, Target target) {
        double targetHeight = -1;
        switch (target) {
            case POWER_PORT:
                targetHeight = powerPortHeight;
                break;
            case LOADING_BAY:
                targetHeight = loadingBayHeight;
                break;
            default:
                throw new EnumConstantNotPresentException(Target.class, target.name());
        }

        double distance = (targetHeight - limelightHeight) / Math.tan(limelightAngle + angleY);

        return distance;
    }

    public enum Target {
        POWER_PORT,
        LOADING_BAY
    }
}