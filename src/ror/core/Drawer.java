package ror.core;

import java.util.Observable;

public class Drawer extends Observable {

    public static final Integer FREE = 0;
    public static final Integer BOOKED = 1;
    public static final Integer BOOKED_FOR_ORDER = 2; // Algo Stocking Order

    private Integer positionInColumn;
    private Integer status;
    private Column column;
    private Product product;

    public Drawer(Column column, Integer positionInColumn) {
	this.column = column;
	this.positionInColumn = positionInColumn;
	this.status = FREE;
	this.product = null;
	this.addObserver(column);
    }

    protected Integer getPositionInColumn() {
	return positionInColumn;
    }

    protected void setPositionInColumn(Integer positionInColumn) {
	this.positionInColumn = positionInColumn;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public Column getColumn() {
	return column;
    }

    public void setColumn(Column column) {
	this.column = column;
    }

    public Product getProduct() {
	return product;
    }

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

    @Override
    public String toString() {
	return "Drawer #" + positionInColumn + " in " + this.getColumn();
    }
}
