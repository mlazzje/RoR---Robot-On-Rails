package ror.core.actions;

import ror.core.Output;
import ror.core.Product;
import ror.core.Robot;

/**
 * OutputAction class : Class that represents an output action done by to put a product in the output point.
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public class OutputAction extends Action {

    private Product product;
    private Output output;

    /**
     * Constructor the the OutputAction class
     * 
     * @param duration
     *            The duration of the action
     * @param robot
     *            The robot which does the action
     * @param output
     *            The output where the product have to be put
     */
    public OutputAction(Integer duration, Robot robot, Output output) {
	super(duration, robot);
	this.output = output;
    }

    /**
     * Return the product which have to be put in the Output
     * 
     * @return The product which have to be put in the Output
     */
    public Product getProduct() {
	return product;
    }

    /**
     * Set the product which have to be put in the Output
     * 
     * @param product
     *            The product which have to be put in the Output
     */
    public void setProduct(Product product) {
	this.product = product;
    }

    /**
     * Return The output where the product have to be put
     * 
     * @return The output where the product have to be put
     */
    public Output getOutput() {
	return this.output;
    }

    /**
     * Set the output where the product have to be put
     * 
     * @param output
     *            The output where the product have to be put
     */
    public void setOutput(Output output) {
	this.output = output;
    }

}
