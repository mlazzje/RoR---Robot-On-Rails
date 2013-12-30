package ror.core.actions;

import ror.core.Input;
import ror.core.Product;
import ror.core.Robot;

/**
 * InputAction class
 * Represent a Input Action
 */
public class InputAction extends Action {

    private Product product;
    private Input input;

    /**
     * Constructor of the InputAction class
     * 
     * @param duration
     *            The duration of the action
     * @param robot
     *            The robot which does the action
     * @param input
     *            The input where is the product
     */
    public InputAction(Integer duration, Robot robot, Input input) {
	super(duration, robot);
	this.input = input;
    }

    /**
     * Return the product to go to fetch
     * 
     * @return The product to go to fetch
     */
    public Product getProduct() {
	return product;
    }

    /**
     * Set the product to go to fetch
     * 
     * @param product
     *            The product to go to fetch
     */
    public void setProduct(Product product) {
	this.product = product;
    }

    /**
     * Return the input where is the product
     * 
     * @return The input where is the product
     */
    public Input getInput() {
	return this.input;
    }

    /**
     * Set the input where is the product
     * 
     * @param input
     *            The input where is the product
     */
    public void setInput(Input input) {
	this.input = input;
    }

}
