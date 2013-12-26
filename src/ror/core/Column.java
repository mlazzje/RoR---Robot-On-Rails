package ror.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Column extends RoRElement implements Observer {

    /**
     * 
     * Number of drawers //TODO Pas inutile ? On ne le génère pas ?
     * 
     */
    private static final int drawerNumber = 10;
    /**
     * Cabinet
     */
    private Cabinet cabinet;
    /**
     * Position in Cabinet
     */
    private Integer positionInCabinet;
    /**
     * List of drawers
     */
    private List<Drawer> drawerList;
    /**
     * Rail access for the drawer
     */
    private Rail access;
    /**
     * 
     */
    @SuppressWarnings("unused")
    private Drawer availableDrawer;

    /**
     * Constructor of Column
     * 
     * @param cabinet
     * @param x
     * @param y
     * @param positionInCabinet
     * @param access
     */
    public Column(Cabinet cabinet, Integer x, Integer y, Integer positionInCabinet, Rail access) {
	super(RoRElementTypes.Column, x, y);
	this.cabinet = cabinet;
	this.positionInCabinet = positionInCabinet;
	this.drawerList = new ArrayList<Drawer>(drawerNumber);
	this.access = access;
	this.initDrawers();
    }

    /**
     * @return the Position of Column in Cabinet
     */
    public Integer getPositionInCabinet() {
	return positionInCabinet;
    }

    /**
     * Update the position of column in cabinet
     * 
     * @param positionInCabinet
     */
    public void setPositionInCabinet(Integer positionInCabinet) {
	this.positionInCabinet = positionInCabinet;
    }

    /**
     * @return An available Drawer
     */
    public Drawer getAvailableDrawer() {
	synchronized (this.drawerList) {
	    Iterator<Drawer> itDrawer = this.drawerList.iterator();
	    while (itDrawer.hasNext()) {
		Drawer drawer = itDrawer.next();
		if (drawer.getStatus() == Drawer.FREE) {
		    return drawer;
		}
	    }
	    return null;
	}
    }

    /**
     * @return the number of available drawers
     */
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

    /**
     * Set the available drawer (UNUSED)
     * 
     * @param availableDrawer
     */
    public void setAvailableDrawer(Drawer availableDrawer) {
	this.availableDrawer = availableDrawer;
    }

    /**
     * @return The rail access
     */
    public Rail getAccess() {
	return access;
    }

    /**
     * Set the acess rail
     * 
     * @param One rail
     */
    public void setAccess(Rail access) {
	this.access = access;
    }

    /**
     * 
     */
    private void initDrawers() {
	for (int i = 0; i < drawerNumber; i++) {
	    drawerList.add(new Drawer(this, i));
	}
    }

    /**
     * @return The cabinet of Column
     */
    public Cabinet getCabinet() {
	return cabinet;
    }

    /**
     * Set the cabinet of Column
     * 
     * @param cabinet
     */
    public void setCabinet(Cabinet cabinet) {
	this.cabinet = cabinet;
    }

    /**
     * @return The list of drawers of Column
     */
    public List<Drawer> getDrawerList() {
	return this.drawerList;
    }

    /**
     * Book N Drawers for and Order
     * 
     * @param order
     * @param nb
     * @return True if everything is ok, else false
     */
    public boolean bookNDrawersOrder(Order order, int nb) {
	Drawer drawer;
	for (; nb > 0; nb--) {
	    drawer = this.getAvailableDrawer();
	    if (drawer == null) {
		System.out.println("Erreur : Column class with booking Drawer");
		return false;
	    }
	    drawer.setStatus(Drawer.BOOKED_FOR_ORDER);
	    order.addDrawer(drawer);
	}
	return true;
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
	this.setChanged();
	this.notifyObservers();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Column : " + this.getX() + "," + this.getY();
    }

}
