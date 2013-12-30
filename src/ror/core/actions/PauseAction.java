package ror.core.actions;

import ror.core.Robot;

public class PauseAction extends Action {

	/**
	 * waiting Robot
	 */
	private Robot waitingRobot;
	
	/**
	 * Method to put robot in pause
	 * 
	 * @param duration
	 * @param robot
	 * @param waitingRobot
	 */
	public PauseAction(Integer duration, Robot robot, Robot waitingRobot) {
		super(duration, robot);
		this.setWaitingRobot(waitingRobot);
	}

	/**
	 * @return robot
	 */
	public Robot getWaitingRobot() {
		return waitingRobot;
	}

	/**
	 * @param waitingRobot
	 */
	public void setWaitingRobot(Robot waitingRobot) {
		this.waitingRobot = waitingRobot;
	}

}
