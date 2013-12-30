package ror.core.actions;

import ror.core.Robot;

/**
 * PauseAction class
 * Represent a Pause Action
 */
public class PauseAction extends Action {

	private Robot waitingRobot;
	
	/**
	 * Constructor the the PauseAction class
	 * @param duration The duration of the action
	 * @param robot The robot which does the action
	 * @param waitingRobot The waited robot
	 */
	public PauseAction(Integer duration, Robot robot, Robot waitingRobot) {
		super(duration, robot);
		this.setWaitingRobot(waitingRobot);
	}

	/**
	 * Return the waited robot
	 * @return The waited robot
	 */
	public Robot getWaitingRobot() {
		return waitingRobot;
	}

	/**
	 * Set the waited robot
	 * @param waitingRobot The waited robot
	 */
	public void setWaitingRobot(Robot waitingRobot) {
		this.waitingRobot = waitingRobot;
	}

}
