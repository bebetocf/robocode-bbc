package bbcTEAM;

import java.io.IOException;

import robocode.TeamRobot;

public class Action {
    private int        type;
    private double     parameter;
    private int        priority;
    private long       time;
    private EnemyRobot enemy;

    private TeamRobot robot;   // Reference to the bot who will execute the action

    public static final int AHEAD=1;
    public static final int BACK=2;
    public static final int STOP=3;
    public static final int SHOOT=4;
    public static final int TANK_RIGHT=5;
    public static final int TANK_LEFT=6;
    public static final int RADAR_RIGHT=7;
    public static final int RADAR_LEFT=8;
    public static final int GUN_RIGHT=9;
    public static final int GUN_LEFT=10;
    public static final int BROADCAST=11;

    public Action() {
    }

    public Action(int _type, double _parameter, int _priority, long _time) {
        this.type = _type;
        this.time = _time;
        this.parameter = _parameter;
        this.priority = _priority;
        this.enemy = null;
    }

    public Action(int _type, EnemyRobot e, int _priority, long _time) {
        this.type = _type;
        this.time = _time;
        this.enemy = e;
        this.priority = _priority;
    }

    public double getParameter() {
        return parameter;
    }

    public void setParameter(double parameter) {
        this.parameter = parameter; 
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void initExecution() {
        if (this.robot != null) {
            switch (this.type) {
                case Action.SHOOT: robot.setFire(parameter); break;
                case Action.AHEAD: robot.setAhead(parameter); break;
                case Action.BACK: robot.setBack(parameter); break;
                case Action.STOP: robot.setStop(); break;
                case Action.GUN_RIGHT: robot.setTurnGunRight(parameter); break;
                case Action.GUN_LEFT: robot.setTurnGunLeft(parameter); break;
                case Action.RADAR_RIGHT: robot.setTurnRadarRight(parameter); break;
                case Action.RADAR_LEFT: robot.setTurnRadarLeft(parameter); break;
                case Action.TANK_RIGHT: robot.setTurnRight(parameter); break;
                case Action.TANK_LEFT: robot.setTurnLeft(parameter); break;
                case Action.BROADCAST: try {
					robot.broadcastMessage(this.enemy);
				} catch (IOException e) {
					
				} break;
            }
        }
    }

    void setRobot(TeamRobot robot) {
        this.robot = robot;
    }

    public String toString(){
        String strType="";
            switch (this.type) {
                case Action.SHOOT:strType="Shoot"; break;
                case Action.AHEAD: strType="Going Ahead"; break;
                case Action.BACK: strType="Going Back"; break;
                case Action.STOP: strType="Stop"; break;
                case Action.GUN_RIGHT: strType="Gun Right"; break;
                case Action.GUN_LEFT: strType="Gun Left"; break;
                case Action.RADAR_RIGHT: strType="Radar Right"; break;
                case Action.RADAR_LEFT: strType="Radar Left"; break;
                case Action.TANK_RIGHT: strType="Tank Right"; break;
                case Action.TANK_LEFT: strType="Tank Left"; break;
                case Action.BROADCAST: strType="Broadcast Message"; break;
            }
        if(this.enemy == null)
        	return "Action[type:"+strType+", parameter:"+parameter+", priority:"+priority+", Time:"+time+"]";
        else
        	return "Action[type:"+strType+", Enemy:"+enemy.getName()+", priority:"+priority+", Time:"+time+"]";
    }

}
