package ror.core.actions;

import ror.core.Input;
import ror.core.Product;
import ror.core.Robot;

/**
 * Classe InputAction Représente l'action de récuperer un produit de l'Input
 */
public class InputAction extends Action {

    private Product product;
    private Input input;

    /**
     * Constructeur de la classe InputAction
     * 
     * @param duration
     *            La durée de l'action
     * @param robot
     *            Le robot associé à l'action
     * @param input
     *            L'input associé à l'action
     */
    public InputAction(Integer duration, Robot robot, Input input) {
	super(duration, robot);
	this.input = input;
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

    /**
     * Retourne l'Input associé à l'action
     * 
     * @return L'Input associé à l'action
     */
    public Input getInput() {
	return this.input;
    }

    /**
     * Défini l'Input associé à l'action
     * 
     * @param input
     *            L'input associé à l'action
     */
    public void setInput(Input input) {
	this.input = input;
    }

}
