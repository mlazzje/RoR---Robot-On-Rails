package ror.core.actions;

import ror.core.Robot;

public abstract class Action {
	private Integer duration;
	private Robot robot;
	
	public Action(Integer duration) {
		this.duration = duration;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Robot getRobot() {
		return robot;
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}

}
