package ror.core;

import java.util.ArrayList;
import java.util.List;

public class Output extends RoRElement {
    // Properties
    /**
     * List of product in output
     */
    private List<Product> productList;
    /**
     * Rail access for output
     */
    private Rail access;

    // Setters and getters
    /**
     * @return the access rail
     */
    public Rail getAccess() {
	return access;
    }

    /**
     * Set an access rail
     * 
     * @param access
     */
    public void setAccess(Rail access) {
	this.access = access;
    }

    // Constructor
    /**
     * @param int x
     * @param int y
     * @param access rail
     */
    public Output(Integer x, Integer y, Rail access) {
	super(RoRElementTypes.Output, x, y);
	this.productList = new ArrayList<Product>();
	this.access = access;
    }

    // Methods
    /**
     * Add product in output
     * 
     * @param p
     * @return true if everything if ok, else false
     */
    public boolean addProduct(Product p) {
	synchronized (this.productList) {
	    boolean returned = this.productList.add(p);
	    p.setStatus(Product.DONE);
	    this.setChanged();
	    this.notifyObservers();
	    return returned;
	}
    }

    /**
     * Remove a product from output
     * 
     * @param product
     * @return true if everything if ok, else false
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
     * @return Product's number in output
     */
    public Integer outputProductsCount() {
	return this.productList.size();
    }

    /**
     * @return Product list in output
     */
    public List<Product> getProductList() {
	return this.productList;
    }

    /**
     * Clear product list between threads
     */
    public void clearProducts() {
	synchronized (this.productList) {

	    this.productList.clear();
	    this.setChanged();
	    this.notifyObservers();
	}
    }

}
