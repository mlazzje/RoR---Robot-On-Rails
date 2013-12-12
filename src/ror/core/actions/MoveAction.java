package ror.core.actions;

import ror.core.Rail;
import ror.core.Robot;

public class MoveAction extends Action {
    private Rail previous;
    private Rail next;

    public MoveAction(Integer duration, Robot robot, Rail previous, Rail next) {
	super(duration, robot);
	this.setNext(next);
	this.setPrevious(previous);
    }

    @Override
    public String toString() {
	return "MoveAction : " + previous + " -> " + next;
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
