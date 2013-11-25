package ror.core;

public abstract class RoRElement {

    protected Integer x;
    protected Integer y;
    protected RoRElementTypes type;

    public RoRElement(RoRElementTypes type, Integer x, Integer y) {
	this.type = type;
	this.x = x;
	this.y = y;
    }

    protected Integer getX() {
	return x;
    }

    protected void setX(Integer x) {
	this.x = x;
    }

    protected Integer getY() {
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
