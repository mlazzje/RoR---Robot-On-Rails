/**
 * StoreAction class : Class that represent a store action done by to put a product in a drawer.
 * 
 * @author      GLC - CPE LYON
 * @version     1.0
 * @since       2013-01-06
 */

package ror.core.actions;

import ror.core.Drawer;
import ror.core.Product;
import ror.core.Robot;

public class StoreAction extends Action {

    private Drawer drawer;
    private Product product;
    
    /**
     * Constructor of the StoreAction class
     * 
     * @param duration
     *            The duration of the action
     * @param robot
     *            The robot which does the action
     * @param product
     *            The store product
     */
    public StoreAction(Integer duration, Robot robot, Drawer drawer, Product product) {
	super(duration, robot);
	this.setDrawer(drawer);
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
