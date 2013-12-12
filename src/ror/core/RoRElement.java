package ror.core;

import java.util.Observable;

public abstract class RoRElement extends Observable {

    protected Integer x;
    protected Integer y;
    protected RoRElementTypes type;

    public RoRElement(RoRElementTypes type, Integer x, Integer y) {
	this.type = type;
	this.x = x;
	this.y = y;
    }

    public Integer getX() {
	return x;
    }

    protected void setX(Integer x) {
	this.x = x;
    }

    public Integer getY() {
	return y;
    }

    protected void setY(Integer y) {
	this.y = y;
    }

    protected RoRElementTypes getType() {
	return type;
    }

    protected void setType(RoRElementTypes type) {
	this.type = type;
    }
}
