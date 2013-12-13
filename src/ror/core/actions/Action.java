package ror.core.actions;

import ror.core.Robot;

/**
 * Classe abstraite Action Représente une action d'un robot
 */
public abstract class Action {
    private Integer duration;
    private Robot robot;

    /**
     * Constructeur de la classe Action appelée par les classes filles
     * 
     * @param duration
     *            Durée de l'action
     * @param robot
     *            Robot associée à l'action
     */
    public Action(Integer duration, Robot robot) {
	this.setDuration(duration);
	this.setRobot(robot);
    }

    /**
     * Retourne la durée de l'action
     * 
     * @return La durée de l'action
     */
    public Integer getDuration() {
	return duration;
    }

    /**
     * Défini la durée de l'action
     * 
     * @param duration
     *            La durée de l'action
     */
    public void setDuration(Integer duration) {
	if (duration != null && duration > 0)
	    this.duration = duration;
	else
	    this.duration = 0;
    }

    /**
     * Retourne le robot associé à l'action
     * 
     * @return Le robot associé à l'action
     */
    public Robot getRobot() {
	return robot;
    }

    /**
     * Défini le robot associé à l'action
     * 
     * @param robot
     *            Le robot associé à l'action
     */
    public void setRobot(Robot robot) {
	this.robot = robot;
    }

}
