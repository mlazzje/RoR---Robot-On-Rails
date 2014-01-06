package ror.core.actions;

import ror.core.Drawer;
import ror.core.Product;
import ror.core.Robot;

/**
 * DestockingAction class : Class that represents a Destocking action done by a robot to destock a product from a drawer.
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public class DestockingAction extends Action {

    private Drawer drawer;
    private Product product;

    /**
     * Constructor of the DestockingAction class
     * 
     * @param duration
     *            The duration of the action
     * @param robot
     *            The robot which does the action
     * @param product
     *            The destocking product
     */
    public DestockingAction(Integer duration, Robot robot, Product product) {
	super(duration, robot);
	this.setDrawer(product.getDrawer());
	this.setProduct(product);
    }

    /**
     * Return the drawer where is the product
     * 
     * @return The drawer where is the product
     */
    public Drawer getDrawer() {
	return drawer;
    }

    /**
     * Define the drawer where is the product
     * 
     * @param drawer
     *            The drawer where is the product
     */
    public void setDrawer(Drawer drawer) {
	this.drawer = drawer;
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
     * Define the product to go to fetch
     * 
     * @param product
     *            The product to go to fetch
     */
    public void setProduct(Product product) {
	this.product = product;
    }

}
