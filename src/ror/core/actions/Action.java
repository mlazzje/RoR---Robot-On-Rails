package ror.core.actions;

import ror.core.Robot;

/**
 * Abstract Class 
 * Represent a robot action
 */
public abstract class Action {
    private Integer duration;
    private Robot robot;

    /**
     * Constructor of the Action class called by child classess
     * 
     * @param duration
     *            Duration of the action
     * @param robot
     *            Robot which does the action
     */
    public Action(Integer duration, Robot robot) {
	this.setDuration(duration);
	this.setRobot(robot);
    }

    /**
     * Return the duration of the action
     * 
     * @return The duration of the action
     */
    public Integer getDuration() {
	return duration;
    }

    /**
     * Set the duration of the action
     * 
     * @param duration
     *            The duration of the action
     */
    public void setDuration(Integer duration) {
	if (duration != null && duration > 0)
	    this.duration = duration;
	else
	    this.duration = 0;
    }

    /**
     * Return the robot wich does the action
     * 
     * @return The robot wich does the action
     */
    public Robot getRobot() {
	return robot;
    }

    /**
     * Set the robot wich does the action
     * 
     * @param robot
     *            The robot wich does the action
     */
    public void setRobot(Robot robot) {
	this.robot = robot;
    }
}
