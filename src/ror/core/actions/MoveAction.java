package ror.core.actions;

import ror.core.Rail;

public class MoveAction extends Action {
	private Rail previous;
	private Rail next;

	public MoveAction(Integer duration) {
		super(duration);
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
