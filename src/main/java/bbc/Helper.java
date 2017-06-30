package bbc;

import java.awt.geom.Point2D;

public class Helper {
	
    public static double absoluteAngle(double baseAngle, double relativeAngle) {
        double angle = (baseAngle + relativeAngle) % 360;

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    public static double absoluteAngle(double xOrigin, double yOrigin, double xDestination, double yDestination) {
        double offsetX = xDestination - xOrigin;
        double offsetY = yDestination - yOrigin;

        return Math.toDegrees(Math.atan2(offsetX, offsetY));
    }

    public static double relativeAngle(double baseAngle, double destAngle) {
        double angle = (destAngle - baseAngle) % 360;
        if (angle > 180) {
            angle -= 360;
        } else if (angle < -180) {
            angle += 360;
        }

        return angle;
    }

    public static double calculateX(double xBase, double absoluteAngle, double distance) {
        double offsetX = (Math.sin(Math.toRadians(absoluteAngle)) * distance);
        return xBase + offsetX;
    }

    public static double calculateY(double yBase, double absoluteAngle, double distance) {
        double offsetY = (Math.cos(Math.toRadians(absoluteAngle)) * distance);
        return yBase + offsetY;
    }

    public static double distance(double xOrigin, double yOrigin, double xDestination, double yDestination) {
        double offsetX = xDestination - xOrigin;
        double offsetY = yDestination - yOrigin;

        return Math.sqrt(offsetX*offsetX + offsetY*offsetY);
    }
    

    public static double normalizeBearing(double a) {
    	while (a >  180) a -= 360;
    	while (a < -180) a += 360;
    	return a;
    }

    public static double absoluteBearing(double x1, double y1, double x2, double y2) {
    	double dx = x2-x1;
    	double dy = y2-y1;
    	double dist = Point2D.distance(x1, y1, x2, y2);
    	double aSin = Math.toDegrees(Math.asin(dx / dist));

    	if (dx > 0 && dy > 0) {
    		return aSin;
    	} else if (dx < 0 && dy > 0) {
    		return 360 + aSin;
    	} else if (dx > 0 && dy < 0) {
    		return 180 - aSin;
    	} else if (dx < 0 && dy < 0) {
    		return 180 - aSin;
    	}
    	return 0;
    }

    public static double firePower(double dist) {
    	return (Math.min(500 / dist, 3));
    }

    public static double bulletSpeed(double fp){
    	return (20 - fp * 3);
    }

    public static long time(double dist, double vel) {
    	return (long)(dist / vel);
    }

    public static double enemyX(double robotX, double robotHeading, double enemyBearing, double dist){
    	double absBearingDeg = robotHeading + enemyBearing;
    	if (absBearingDeg < 0) absBearingDeg += 360;
    	return (robotX + Math.sin(Math.toRadians(absBearingDeg)) * dist);
    }

    public static double enemyY(double robotY, double robotHeading, double enemyBearing, double dist){
    	double absBearingDeg = robotHeading + enemyBearing;
    	if (absBearingDeg < 0) absBearingDeg += 360;
    	return (robotY + Math.cos(Math.toRadians(absBearingDeg)) * dist);
    }

    public static double enemyFutureX(double enemyX, double enemyHeading, double velocity, long _time){
    	return (enemyX + Math.sin(Math.toRadians(enemyHeading)) * velocity * _time);
    }

    public static double enemyFutureY(double enemyY, double enemyHeading, double velocity, long _time){
    	return (enemyY + Math.cos(Math.toRadians(enemyHeading)) * velocity * _time);
    }
    
    //Função que calcula o ângulo para pontos gravitacionais
    public static double RobotAngleForGravitationalPoints (double heading, double angle) {
    	double ang;
    	ang = normalizeBearing(heading - angle);
    	return ang;    	
    }
    //Normaliza o ângulo para pontos gravitacionais
    public static double RobotAngleModifiedForGravitationalPoints (double ang) {
    	if (ang > 90) ang -= 180;
        else if (ang < -90) ang += 180;
    	return ang;
    }
    //Seta a direção que o robô deve ir
    public static int DirectionforTheRobot (double ang) {
    	int dir = 1;
    	if ((ang > 90) || (ang < -90)) dir = -1;
    	return dir;
    }

}
