package ror.core;

import java.util.ArrayList;
import java.util.List;

public class Column extends RoRElement {

    private static final int drawerNumber = 10;
    private Cabinet cabinet;
    private Integer positionInCabinet;
    private List<Drawer> drawerList;
    private Rail access;

    public Column(Cabinet cabinet, Integer x, Integer y,
	    Integer positionInCabinet, Rail access) {
	super(RoRElementTypes.Column, x, y);
	this.cabinet = cabinet;
	this.positionInCabinet = positionInCabinet;
	this.drawerList = new ArrayList<Drawer>(drawerNumber);
	this.access = access;
	this.initDrawers();
    }

    public Integer getPositionInCabinet() {
	return positionInCabinet;
    }

    public void setPositionInCabinet(Integer positionInCabinet) {
	this.positionInCabinet = positionInCabinet;
    }

    public Rail getAccess() {
	return access;
    }

    public void setAccess(Rail access) {
	this.access = access;
    }

    private void initDrawers() {
	for (int i = 0; i < drawerNumber; i++) {
	    drawerList.add(new Drawer(this, i));
	}
    }

    public Cabinet getCabinet() {
	return cabinet;
    }

    public void setCabinet(Cabinet cabinet) {
	this.cabinet = cabinet;
    }
}
