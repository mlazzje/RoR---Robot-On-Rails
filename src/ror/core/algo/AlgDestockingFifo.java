package ror.core.algo;

import java.util.ArrayList;
import ror.core.Order;
import ror.core.Product;
import ror.core.actions.Action;
import ror.core.actions.DestockingAction;
import ror.core.algo.IAlgDestocking;

public class AlgDestockingFifo implements IAlgDestocking {

	@Override
	public ArrayList<Action> getActions(ArrayList<Order> orders, ArrayList<Product> stockProducts) {

		ArrayList<Action> actions = new ArrayList<Action>();
		ArrayList<Action> actionsToSend = new ArrayList<Action>();

		ArrayList<String> stockProductsName = new ArrayList<String>();
		ArrayList<Product> storedProducts = new ArrayList<Product>();

		// On liste les produits stockés, et non réservés pour une commande
		for (Product product : stockProducts) {
			if (product.getStatus() == Product.STORED) {
				stockProductsName.add(product.getName());
				storedProducts.add(product);
			}
		}

		// On parcourt chacune des commandes en cours
		for (Order currentOrder : orders) {
			// On réinitialise les actions
			actions.clear();
			// On ne prend en compte que les commandes initialisées ou encore en
			// attente de produits
			if (currentOrder.getStatus() == Order.INIT || currentOrder.getStatus() == Order.WAITING) {
				// On ne fait de traitement que si le stock contient tous les
				// produits de la commande
				if (stockProductsName.retainAll(currentOrder.getProductsName())) {
					// Pour chaque produit du stock
					for (Product stockedProduct : storedProducts) {
						// Pour chaque produit de la commande
						for (String orderProductName : currentOrder.getProductsName()) {
							// Si leur nom est identique et que le produit est
							// libre en stock, on le réserve et on ajoute une
							// action
							if (orderProductName.equals(stockedProduct.getName()) && stockedProduct.getStatus() == Product.STORED) {
								stockedProduct.setStatus(Product.BOOKED);
								currentOrder.addProduct(stockedProduct);
								DestockingAction currentAction = new DestockingAction(0, null, stockedProduct);
								actions.add(currentAction);
								break;
							}
						}
					}
					// On vérifie que tous les produits sont ok
					if (currentOrder.getProductsName().size() == currentOrder.getProducts().size()) {
						actionsToSend.addAll(actions);
					}
				} else {
					return actionsToSend;
				}
			}
		}
		return actionsToSend;
	}
}
