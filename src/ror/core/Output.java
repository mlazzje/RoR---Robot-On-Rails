package ror.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
     * @param access
     *            rail
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
    public boolean addProduct(final Product p) {
	synchronized (this.productList) {
	    boolean returned = this.productList.add(p);
	    p.setStatus(Product.DONE);
	    if(p.getOrder().getStatus() == Order.DONE)
		    new Timer().schedule(new TimerTask() {          
		        @Override
		        public void run() {
		            Output.this.removeProduct(p);    
		        }
		    }, 5000);
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
