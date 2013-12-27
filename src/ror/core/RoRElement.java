package ror.core;

import java.util.Observable;

public abstract class RoRElement extends Observable {

    /**
     * Integer x, position
     */
    protected Integer x;
    /**
     * Integer y, position
     */
    protected Integer y;
    /**
     * RoRElement type
     */
    protected RoRElementTypes type;

    /**
     * Constructor of RoRElement
     * 
     * @param type
     * @param int x
     * @param int y
     */
    public RoRElement(RoRElementTypes type, Integer x, Integer y) {
	this.type = type;
	this.x = x;
	this.y = y;
    }

    /**
     * @return Position X
     */
    public Integer getX() {
	return x;
    }

    /**
     * Set position X
     * 
     * @param x
     */
    protected void setX(Integer x) {
	this.x = x;
    }

    /**
     * @return Position Y
     */
    public Integer getY() {
	return y;
    }

    /**
     * Set position Y
     * 
     * @param y
     */
    protected void setY(Integer y) {
	this.y = y;
    }

    /**
     * @return RoRElement type
     */
    protected RoRElementTypes getType() {
	return type;
    }

    /**
     * Set RoRElement type
     * 
     * @param type
     */
    protected void setType(RoRElementTypes type) {
	this.type = type;
    }
}
