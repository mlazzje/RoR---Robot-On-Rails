package ror.core.actions;

import ror.core.Rail;
import ror.core.Robot;

/**
 * Classe MoveAction représente un déplacement d'un robot
 * 
 */
public class MoveAction extends Action {
    private Rail previous;
    private Rail next;

    /**
     * Constructeur de la classe InputAction
     * 
     * @param duration
     *            La durée de l'action
     * @param robot
     *            Le robot associé à l'action
     * @param previous
     *            La rail sur lequel doit se trouver le robot pour effectuer l'action
     * @param next
     *            La rail vers lequel se déplace le robot
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
