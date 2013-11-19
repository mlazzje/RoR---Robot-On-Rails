package ror.core.actions;

import ror.core.Robot;

public abstract class Action {
	private Integer duration;
	private Robot robot;
	
	public Action(Integer duration, Robot robot) {
		this.setDuration(duration);
		this.setRobot(robot);
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		if(duration != null && duration > 0)
			this.duration = duration;
		else
			this.duration = 0;
	}

	public Robot getRobot() {
		return robot;
	}

	public void setRobot(Robot robot) {
		this.robot = robot;
	}

}
