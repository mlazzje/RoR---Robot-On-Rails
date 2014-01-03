package ror.core.algo;

import java.util.ArrayList;

import ror.core.Order;
import ror.core.Product;
import ror.core.actions.DestockingAction;
import ror.core.algo.IAlgDestocking;

public class AlgDestockingFifo implements IAlgDestocking {

    @Override
    public ArrayList<DestockingAction> getActions(ArrayList<Order> orders, ArrayList<Product> stockProducts) {

	ArrayList<DestockingAction> actions = new ArrayList<DestockingAction>();
	ArrayList<DestockingAction> actionsToSend = new ArrayList<DestockingAction>();

	ArrayList<String> stockProductsName = new ArrayList<String>();
	ArrayList<Product> storedProducts = new ArrayList<Product>();
	//System.out.println("##### nombre de produits stocks "+stockProducts.size()+"#####");

	// On liste les produits stockés, et non réservés pour une commande
	for (Product product : stockProducts) {
	    if (product.getStatus() == Product.STORED || (product.getStatus() == Product.BOOKED && product.getDrawer()!=null)) {
		stockProductsName.add(product.getName());
		storedProducts.add(product);
	    }
	}

	// On parcourt chacune des commandes en cours
	for (Order currentOrder : orders) {
	    // On réinitialise les actions
	    actions.clear();
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
		    //System.out.println("\n##################\nCommande prête "+currentOrder+"\n##################");
		    currentOrder.setStatus(Order.READY_FOR_DESTOCKING);
		    actionsToSend.addAll(actions);
		    break;
		} else {
		    return actionsToSend;
		}
	    }
	}
	return actionsToSend;
    }

    public static Boolean containsAllWithDoublon(ArrayList<String> container, ArrayList<String> testList) {
	ArrayList<String> copy = new ArrayList<String>(container);
	//System.out.println("##### nombre de produits  "+container.size()+"#####");
	
	for (String test : testList) {
	    if (copy.contains(test))
		copy.remove(test);
	    else
		return false;
	}
	return true;
    }
}
