package ror.core.actions;

import ror.core.Input;
import ror.core.Product;
import ror.core.Robot;

public class InputAction extends Action {

	private Product product;
	private Input input;
	
	public InputAction(Integer duration, Robot robot, Input input) {
		super(duration, robot);
		this.input = input;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Input getInput() {
	    return this.input;
	}

	public void setInput(Input input) {
	    this.input = input;
	}

}
