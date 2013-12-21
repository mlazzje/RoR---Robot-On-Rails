package ror.core;

import java.util.ArrayList;
import java.util.List;

public class Input extends RoRElement {

    // Properties
    private List<Product> productList;
    private Rail access;

    // Setters and getters
    public Rail getAccess() {
	return access;
    }

    public void setAccess(Rail access) {
	this.access = access;
    }

    // Constructor
    public Input(Integer x, Integer y, Rail access) {
	super(RoRElementTypes.Input, x, y);
	this.productList = new ArrayList<Product>();
	this.access = access;
    }

    // Methods
    public boolean addProduct(Product p) {
	boolean returned = this.productList.add(p);
	this.setChanged();
	this.notifyObservers();
	return returned;
    }

    public boolean removeProduct(Product p) {
	boolean returned = this.productList.remove(p);
	this.setChanged();
	this.notifyObservers();
	return returned;
    }

    public Integer inputProductsCount() {
	return this.productList.size();
    }

    public List<Product> getProductList() {
	return this.productList;
    }
    
    public void clearProducts() {
	this.productList.clear();
	this.setChanged();
	this.notifyObservers();
    }

}
