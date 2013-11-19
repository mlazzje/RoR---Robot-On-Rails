package ror.core.actions;

import ror.core.Robot;

public class PauseAction extends Action {

	private Robot waitingRobot;
	
	public PauseAction(Integer duration, Robot robot, Robot waitingRobot) {
		super(duration, robot);
		this.setWaitingRobot(waitingRobot);
	}

	public Robot getWaitingRobot() {
		return waitingRobot;
	}

	public void setWaitingRobot(Robot waitingRobot) {
		this.waitingRobot = waitingRobot;
	}

}
