package ror.core;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Rail class : Core class that represents a rail
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public class Rail extends RoRElement {

    // Properties

    public ReentrantLock lock;

    /**
     * Left rail
     */
    private Rail leftRail;
    /**
     * Right rail
     */
    private Rail rightRail;
    /**
     * ArrayList of previous rail
     */
    private ArrayList<Rail> previous;
    /**
     * Next rail
     */
    private Rail next;
    /**
     * Robot on Rail !!!
     */
    private Robot robot;

    // Setters and getters
    /**
     * @return left rail
     */
    public Rail getLeftRail() {
	return leftRail;
    }

    /**
     * Set left rail
     * 
     * @param leftRail
     */
    public void setLeftRail(Rail leftRail) {
	this.leftRail = leftRail;
    }

    /**
     * @return right rail
     */
    public Rail getRightRail() {
	return rightRail;
    }

    /**
     * Set right rail
     * 
     * @param rightRail
     */
    public void setRightRail(Rail rightRail) {
	this.rightRail = rightRail;
    }

    /**
     * @return ArrayList of previous rails
     */
    public ArrayList<Rail> getPreviousRail() {
	return previous;
    }

    /**
     * Set ArrayList of previousRail
     * 
     * @param previoustRail
     */
    public void setPreviousRail(ArrayList<Rail> previoustRail) {
	this.previous = previoustRail;
    }

    /**
     * Add previous rail
     * 
     * @param rail
     */
    public void addPreviousRail(Rail rail) {
	this.previous.add(rail);
    }

    /**
     * @return next rail
     */
    public Rail getNextRail() {
	return next;
    }

    /**
     * Set next rail
     * 
     * @param nextRail
     */
    public void setNextRail(Rail nextRail) {
	this.next = nextRail;
    }

    // Constructor
    /**
     * @param int x
     * @param int y
     * @param leftRail
     * @param rightRail
     * @param previousRail
     * @param nextRail
     */
    public Rail(Integer x, Integer y, Rail leftRail, Rail rightRail, ArrayList<Rail> previousRail, Rail nextRail) {
	super(RoRElementTypes.Rail, x, y);
	this.leftRail = leftRail;
	this.rightRail = rightRail;
	this.previous = previousRail;
	this.next = nextRail;
	this.robot = null;
	this.lock = new ReentrantLock();
    }

    /**
     * @return Robot on Rail !!!
     */
    public Robot getRobot() {
	return robot;
    }

    /**
     * Set Robot on Rail !!!
     * 
     * @param robot
     */
    public void setRobot(Robot robot) {
	this.robot = robot;
	this.setChanged();
	this.notifyObservers();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Rail (" + this.getX() + "," + this.getY() + ")";
    }

    // Methods

    // TODO MoveAction

}
