package ror.core;

import java.util.Observable;

/**
 * Drawer class : Core class that represents a drawer
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public class Drawer extends Observable {

    /**
     * Free status of Drawer
     */
    public static final Integer FREE = 0;
    /**
     * Booked status of Drawer
     */
    public static final Integer BOOKED = 1;
    /**
     * Booked for order status of Drawer (for Algo Stocking Order)
     */
    public static final Integer BOOKED_FOR_ORDER = 2;

    /**
     * Position of Drawer in column
     */
    private Integer positionInColumn;
    /**
     * Drawer status
     */
    private Integer status;
    /**
     * Column of Drawer
     */
    private Column column;
    /**
     * Product in Drawer
     */
    private Product product;

    /**
     * Constructor of Drawer
     * 
     * @param column
     * @param positionInColumn
     */
    public Drawer(Column column, Integer positionInColumn) {
	this.column = column;
	this.positionInColumn = positionInColumn;
	this.status = FREE;
	this.product = null;
	this.addObserver(column);
    }

    /**
     * @return Drawer position in column
     */
    protected Integer getPositionInColumn() {
	return positionInColumn;
    }

    /**
     * Set the position of Drawer in column
     * 
     * @param positionInColumn
     */
    protected void setPositionInColumn(Integer positionInColumn) {
	this.positionInColumn = positionInColumn;
    }

    /**
     * FREE/BOOKED/BOOKED_FOR_ORDER
     * 
     * @return Status of Drawer
     */
    public Integer getStatus() {
	return status;
    }

    /**
     * Set the status of Drawer FREE/BOOKED/BOOKED_FOR_ORDER
     * 
     * @param status
     */
    public void setStatus(Integer status) {
	this.status = status;
    }

    /**
     * @return the column that owns the drawer
     */
    public Column getColumn() {
	return column;
    }

    /**
     * Set the column that owns the drawer
     * 
     * @param column
     */
    public void setColumn(Column column) {
	this.column = column;
    }

    /**
     * @return the product in the drawer (null if nothing)
     */
    public Product getProduct() {
	return product;
    }

    /**
     * Set the product in drawer
     * 
     * @param product
     */
    public void setProduct(Product product) {
	synchronized (this.getColumn().getDrawerList()) {
	    if (this.product != null && product != null)
		System.out.println("Erreur " + this + " plein ");
	    this.product = product;
	    if (product == null)
		this.status = FREE;
	    else
		this.product.setStatus(Product.STORED);

	    this.setChanged();
	    this.notifyObservers();
	}
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Drawer #" + positionInColumn + " in " + this.getColumn();
    }
}
