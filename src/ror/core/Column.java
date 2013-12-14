package ror.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Column extends RoRElement implements Observer {

	private static final int drawerNumber = 10;
	private Cabinet cabinet;
	private Integer positionInCabinet;
	private List<Drawer> drawerList;
	private Rail access;
	@SuppressWarnings("unused")
	private Drawer availableDrawer;

	public Column(Cabinet cabinet, Integer x, Integer y, Integer positionInCabinet, Rail access) {
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

	public Drawer getAvailableDrawer() {
		Iterator<Drawer> itDrawer = this.drawerList.iterator();
		while (itDrawer.hasNext()) {
			Drawer drawer = itDrawer.next();
			if (drawer.getStatus() == Drawer.FREE) {
				return drawer;
			}
		}
		return null;
	}

	public int getNbAvailableDrawers() {
		int nb = 0;
		Iterator<Drawer> itDrawer = this.drawerList.iterator();
		while (itDrawer.hasNext()) {
			Drawer drawer = itDrawer.next();
			if (drawer.getStatus() == Drawer.FREE) {
				nb++;
			}
		}
		return nb;
	}

	public void setAvailableDrawer(Drawer availableDrawer) {
		this.availableDrawer = availableDrawer;
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

	public List<Drawer> getDrawerList() {
		return this.drawerList;
	}
	
	public boolean bookNDrawersOrder(Order order, int nb) {
		Drawer drawer;
		for (; nb>0; nb--) {
			drawer = this.getAvailableDrawer();
			if(drawer==null) { 
				System.out.println("Problem with booking Drawer");
				return false; 
			}
			drawer.setStatus(Drawer.BOOKED_FOR_ORDER);
			order.addDrawer(drawer);
		}
		return true;
	}

	@Override
	public void update(Observable o, Object arg) {
	    System.out.println("Drawer maj");
	    this.setChanged();
	    this.notifyObservers();
	}
}
