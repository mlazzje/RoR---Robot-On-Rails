package ror.core.actions;

import ror.core.Rail;
import ror.core.Robot;

/**
 * MoveAction class
 * Represent a move of a robot
 */
public class MoveAction extends Action {
    private Rail previous;
    private Rail next;

    /**
     * Constructor of the InputAction class
     * 
     * @param duration
     *            The duration of the action
     * @param robot
     *            The robot which does the action
     * @param previous
     *            The previous rail
     * @param next
     *            The next rail
     */
    public MoveAction(Integer duration, Robot robot, Rail previous, Rail next) {
	super(duration, robot);
	this.setNext(next);
	this.setPrevious(previous);
    }

    @Override
    public String toString() {
	return "MoveAction : " + previous + " -> " + next;
    }

    /**
     * Return the previous rail
     * @return The previous rail
     */
    public Rail getPrevious() {
	return previous;
    }

    /**
     * Set the previous rail
     * @param previous The previous rail
     */
    public void setPrevious(Rail previous) {
	this.previous = previous;
    }

    /**
     * Get the next rail
     * @return The next rail
     */
    public Rail getNext() {
	return next;
    }

    /**
     * Set the next rail
     * @param next The next rail
     */
    public void setNext(Rail next) {
	this.next = next;
    }

}
