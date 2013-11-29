package ror.core;

import java.util.ArrayList;

public class Rail extends RoRElement {

    // Properties
    private Rail leftRail;
    private Rail rightRail;
    private ArrayList<Rail> previous;
    private Rail next;

    // Setters and getters
    public Rail getLeftRail() {
	return leftRail;
    }

    public void setLeftRail(Rail leftRail) {
	this.leftRail = leftRail;
    }

    public Rail getRightRail() {
	return rightRail;
    }

    public void setRightRail(Rail rightRail) {
	this.rightRail = rightRail;
    }

    public ArrayList<Rail> getPreviousRail() {
	return previous;
    }

    public void setPreviousRail(ArrayList<Rail> previoustRail) {
	this.previous = previoustRail;
    }
    
    public void addPreviousRail(Rail rail)
    {
	this.previous.add(rail);
    }

    public Rail getNextRail() {
	return next;
    }

    public void setNextRail(Rail nextRail) {
	this.next = nextRail;
    }

    // Constructor
    public Rail(Integer x, Integer y, Rail leftRail, Rail rightRail, ArrayList<Rail> previousRail, Rail nextRail) {
	super(RoRElementTypes.Rail, x, y);
	this.leftRail = leftRail;
	this.rightRail = rightRail;
	this.previous = previousRail;
	this.next = nextRail;
    }

    // Methods

    // TODO MoveAction
}
