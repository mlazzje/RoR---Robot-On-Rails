package ror.core.actions;

public abstract class Action {
	private Integer duration;

	public Action(Integer duration) {
		this.duration = duration;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

}
