package ror.core.actions;

import ror.core.Drawer;
import ror.core.Product;
import ror.core.Robot;

/**
 * Classe DestockingAction Représente l'action de destocker un produit d'une armoire
 */
public class DestockingAction extends Action {

    private Drawer drawer;
    private Product product;

    /**
     * Constructeur de la classe DestockingAction
     * 
     * @param duration
     *            La durée de l'action
     * @param robot
     *            Le robot associé à l'action
     * @param product
     *            Le produit à destocker
     */
    public DestockingAction(Integer duration, Robot robot, Product product) {
	super(duration, robot);
	this.setDrawer(product.getDrawer());
	this.setProduct(product);
    }

    /**
     * Retourne l'emplacement où se situe le produit
     * 
     * @return Le drawer où se situe le produit
     */
    public Drawer getDrawer() {
	return drawer;
    }

    /**
     * Défini l'emplacement où se situe le produit
     * 
     * @param drawer
     *            Le drawer où se situe le produit
     */
    public void setDrawer(Drawer drawer) {
	this.drawer = drawer;
    }

    /**
     * Retourne le produit à aller chercher
     * 
     * @return Le produit à aller chercher
     */
    public Product getProduct() {
	return product;
    }

    /**
     * Défini le produit à aller chercher
     * 
     * @param product
     *            Le prodit à aller chercher
     */
    public void setProduct(Product product) {
	this.product = product;
    }

}
