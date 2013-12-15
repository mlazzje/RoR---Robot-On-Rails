package ror.core.actions;

import ror.core.Drawer;
import ror.core.Product;
import ror.core.Robot;

public class StoreAction extends Action {

    private Drawer drawer;
    private Product product;

    public StoreAction(Integer duration, Robot robot, Drawer drawer, Product product) {
	super(duration, robot);
	this.setDrawer(drawer);
	this.setProduct(product);
    }

    public Drawer getDrawer() {
	return drawer;
    }

    public void setDrawer(Drawer drawer) {
	this.drawer = drawer;
    }

    public Product getProduct() {
	return product;
    }

    public void setProduct(Product product) {
	this.product = product;
    }

}
