package ror.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Input class : Core class that represents the input
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public class Input extends RoRElement {

    // Properties
    /**
     * List of products in input
     */
    private List<Product> productList;
    /**
     * Rail access of input
     */
    private Rail access;

    // Setters and getters
    /**
     * @return the rail access
     */
    public Rail getAccess() {
	return access;
    }

    /**
     * Set the rail access
     * 
     * @param rail access
     */
    public void setAccess(Rail access) {
	this.access = access;
    }

    // Constructor
    /**
     * Constructor of Input
     * 
     * @param int x
     * @param int y
     * @param rail access
     */
    public Input(Integer x, Integer y, Rail access) {
	super(RoRElementTypes.Input, x, y);
	this.productList = new ArrayList<Product>();
	this.access = access;
    }

    // Methods
    /**
     * Add product in input
     * 
     * @param product
     * @return true if everything ok, else false
     */
    public boolean addProduct(Product p) {
	synchronized (this.productList) {
	    boolean returned = this.productList.add(p);
	    this.setChanged();
	    this.notifyObservers();
	    return returned;
	}
    }

    /**
     * Remove a product in input
     * 
     * @param product
     * @return true if everything ok, else false
     */
    public boolean removeProduct(Product p) {
	synchronized (this.productList) {
	    boolean returned = this.productList.remove(p);
	    this.setChanged();
	    this.notifyObservers();
	    return returned;
	}

    }

    /**
     * @return the number of products in input
     */
    public Integer inputProductsCount() {
	return this.productList.size();
    }

    /**
     * @return the product list
     */
    public List<Product> getProductList() {
	return this.productList;
    }

    /**
     * clear the product list with other threads
     */
    public void clearProducts() {
	synchronized (this.productList) {
	    this.productList.clear();
	    this.setChanged();
	    this.notifyObservers();
	}	
    }

}
