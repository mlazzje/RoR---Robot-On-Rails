package ror.core.actions;

import ror.core.Output;
import ror.core.Product;
import ror.core.Robot;

public class OutputAction extends Action {

	/**
	 * product
	 */
	private Product product;
	/**
	 * output spot
	 */
	private Output output;

	/**
	 * Output action
	 * 
	 * @param duration
	 * @param robot
	 * @param output
	 */
	public OutputAction(Integer duration, Robot robot, Output output) {
		super(duration, robot);
		this.output = output;
	}

	/**
	 * @return product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * Set product
	 * 
	 * @param product
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * @return output
	 */
	public Output getOutput() {
	    return this.output;
	}

	/**
	 * Set output
	 * 
	 * @param output
	 */
	public void setOutput(Output output) {
	    this.output = output;
	}

}
