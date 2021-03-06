package ror.core.algo;

import java.util.ArrayList;

import ror.core.Drawer;
import ror.core.Order;
import ror.core.Product;
import ror.core.actions.DestockingAction;
import ror.core.algo.IAlgDestocking;

/**
 * AlgDestockingOrder class : Destocking Algorithm that implements the IAlgDestocking interface.
 * 
 * @author GLC - CPE LYON
 * @version 1.0
 * @since 2013-11-18
 */
public class AlgDestockingOrder implements IAlgDestocking {

    @Override
    public ArrayList<DestockingAction> getActions(ArrayList<Order> orders, ArrayList<Product> stockProducts, IAlgStore algo) {

	ArrayList<DestockingAction> actions = new ArrayList<DestockingAction>();
	ArrayList<DestockingAction> actionsToSend = new ArrayList<DestockingAction>();

	ArrayList<String> stockProductsName = new ArrayList<String>();
	ArrayList<Product> storedProducts = new ArrayList<Product>();

	// On liste les produits stockés, et non réservés pour une commande
	for (Product product : stockProducts) {
	    if (product.getStatus() == Product.STORED && product.getDrawer() != null) {
		stockProductsName.add(product.getName());
		storedProducts.add(product);
	    }
	}

	// On parcourt chacune des commandes en cours
	for (Order currentOrder : orders) {
	    // On réinitialise les actions
	    actions.clear();
	    if (algo.getClass().getSimpleName().equals("AlgStoreFifo")) {
		// On ne prend en compte que les commandes initialisées ou encore en attente de produits
		if (currentOrder.getStatus() == Order.INIT || currentOrder.getStatus() == Order.WAITING) {
		    // On ne fait de traitement que si le stock contient tous les produits de la commande
		    if (containsAllWithDoublon(stockProductsName, (ArrayList<String>) currentOrder.getProductsName())) {
			// Pour chaque produit de la commande
			for (String orderProductName : currentOrder.getProductsName()) {
			    Product productAdded = null;
			    // Pour chaque produit du stock
			    for (Product stockedProduct : storedProducts) {
				// Si leur nom est identique et que le produit est libre en stock, on le réserve et on ajoute une action
				if (orderProductName.equals(stockedProduct.getName())) {
				    productAdded = stockedProduct;
				    currentOrder.addProduct(stockedProduct);
				    DestockingAction currentAction = new DestockingAction(1000, null, stockedProduct);
				    actions.add(currentAction);
				    break;
				}
			    }
			    // on supprime le produit du stock
			    if (productAdded != null) {
				storedProducts.remove(productAdded);
				synchronized (stockProducts) {
				    stockProducts.remove(productAdded);
				}
			    }
			}
			currentOrder.setStatus(Order.READY_FOR_DESTOCKING);
			actionsToSend.addAll(actions);
			break;
		    }
		}
	    } else {
		if (currentOrder.isReadyForDestocking() && currentOrder.getStatus() != Order.READY_FOR_DESTOCKING && currentOrder.getStatus() != Order.DONE) {
		    for (Drawer d : currentOrder.getDrawers()) {
			Product currentProduct = d.getProduct();
			if (currentProduct == null) {
			    System.out.println("Problem with product in AlgDestockingOrder");
			}
			DestockingAction currentAction = new DestockingAction(1000, null, currentProduct);
			actions.add(currentAction);
			currentOrder.addProduct(currentProduct);
		    }
		    currentOrder.setStatus(Order.READY_FOR_DESTOCKING);
		    actionsToSend.addAll(actions);
		}
	    }
	}
	return actionsToSend;
    }

    /**
     * 
     * @param container
     *            example : {"a","a","b","c"}
     * @param testList
     *            example : {"c","a","a"}
     * @return true if the container contains the testlist else false
     */
    public static Boolean containsAllWithDoublon(ArrayList<String> container, ArrayList<String> testList) {
	ArrayList<String> copy = new ArrayList<String>(container);

	for (String test : testList) {
	    if (copy.contains(test))
		copy.remove(test);
	    else
		return false;
	}
	return true;
    }
}
