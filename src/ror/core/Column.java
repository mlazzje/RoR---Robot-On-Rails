package ror.core;

import java.util.ArrayList;
import java.util.List;

public class Column extends RoRElement {

	// Constants
	private static final int drawerNumber = 10;

	// Properties
	private Integer positionInCabinet;
	private List<Drawer> drawerList;
	private Rail access;

	// Setters and getters
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

	// Constructor
	public Column(Integer x, Integer y, Integer positionInCabinet, Rail access) {
		super(RoRElementTypes.Column, x, y);
		this.positionInCabinet = positionInCabinet;
		this.drawerList = new ArrayList<Drawer>(drawerNumber);
		this.access = access;
		this.initDrawers();
	}

	// Methods
	private void initDrawers() {
		for (int i = 0; i < drawerNumber; i++) {
			drawerList.add(new Drawer(i));
		}
	}
}
