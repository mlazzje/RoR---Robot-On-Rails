package ror.core.actions;

import ror.core.Product;
import ror.core.Robot;

public class InputAction extends Action {

	private Product product;

	public InputAction(Integer duration, Robot robot) {
		super(duration, robot);
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
