package ror.core.actions;

import ror.core.Product;

public class OutputAction extends Action {

	private Product product;

	public OutputAction(Integer duration) {
		super(duration);
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
