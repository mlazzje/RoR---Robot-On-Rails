package core;

import java.util.ArrayList;
import java.util.List;

public class Output extends RoRElement {
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
	public Output(Integer x, Integer y, Rail access) {
		super(RoRElementTypes.Output, x, y);
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

	public Integer outputProductsCount() {
		return this.productList.size();
	}

}
