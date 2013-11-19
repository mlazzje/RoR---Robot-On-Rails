package ror.core.actions;

import ror.core.Rail;

public class MoveAction extends Action {
	private Rail previous;
	private Rail next;

	public MoveAction(Integer duration, Rail previous, Rail next) {
		super(duration);
		this.setNext(next);
		this.setPrevious(previous);
	}

	public Rail getPrevious() {
		return previous;
	}

	public void setPrevious(Rail previous) {
		this.previous = previous;
	}

	public Rail getNext() {
		return next;
	}

	public void setNext(Rail next) {
		this.next = next;
	}

}
