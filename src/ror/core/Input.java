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
		return this.productList.add(p);
	}

	public boolean removeProduct(Product p) {
		return this.productList.remove(p);
	}

	public Integer inputProductsCount() {
		return this.productList.size();
	}

	public List<Product> getProductList() {
		return this.productList;
	}

}
