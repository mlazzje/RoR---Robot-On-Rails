package ror.core.actions;

import ror.core.Output;
import ror.core.Product;
import ror.core.Robot;

public class OutputAction extends Action {

	private Product product;
	private Output output;

	public OutputAction(Integer duration, Robot robot, Output output) {
		super(duration, robot);
		this.output = output;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Output getOutput() {
	    return this.output;
	}

	public void setOutput(Output output) {
	    this.output = output;
	}

}
