package ror.core.actions;

import ror.core.Drawer;
import ror.core.Product;
import ror.core.Robot;

public class StoreAction extends Action {

    /**
     * drawer
     */
    private Drawer drawer;
    /**
     * product
     */
    private Product product;

    /**
     * Constructor storeAction
     * 
     * @param duration
     * @param robot
     * @param drawer
     * @param product
     */
    public StoreAction(Integer duration, Robot robot, Drawer drawer, Product product) {
	super(duration, robot);
	this.setDrawer(drawer);
	this.setProduct(product);
    }

    /**
     * @return drawer
     */
    public Drawer getDrawer() {
	return drawer;
    }

    /**
     * @param drawer
     */
    public void setDrawer(Drawer drawer) {
	this.drawer = drawer;
    }

    /**
     * @return product
     */
    public Product getProduct() {
	return product;
    }

    /**
     * @param product
     */
    public void setProduct(Product product) {
	this.product = product;
    }

}
