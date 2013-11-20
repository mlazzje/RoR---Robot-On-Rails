package ror.core;

import java.io.File;
import java.util.ArrayList;

public class OrderSource {

	private File file;

	public ArrayList<Product> getRandomProducts(ArrayList<Order> orders,
			ArrayList<Product> stock) {
		ArrayList<Product> newProducts = new ArrayList<Product>();
		ArrayList<String> productNamesInStock = new ArrayList<String>();
		for (Product product : stock) {
			// TODO add all product name to productNamesInStock
		}

		for (Order order : orders) {
			// TODO substract productNames of order to productNamesInStock;
		}

		for (String string : productNamesInStock) {
			// one chance on productNamesInStock.size()*2 to add the product to
			// newProducts
			if (random(1, productNamesInStock.size() * 2) == 1) {
				newProducts.add(new Product()); // TODO specify the product name
												// in the constructor
			}
		}

		return newProducts;
	}

	public ArrayList<Order> getRandomOrders() {
		ArrayList<Order> newOrders = new ArrayList<Order>();
		Integer nbOrdersToCreate = this.random(1, 5); // between 1 and 5 orders
														// will be create
		if (random(1, 20) == 1) // on chance on 20 that the orders will be
								// create
		{
			for (int i = 0; i < nbOrdersToCreate; i++) {
				Order order = new Order();
				// TODO addRandomProductsToOrder
				newOrders.add(order);
			}
		}
		return newOrders;
	}

	public ArrayList<Order> getScenarioOrders(Integer uptime) {
		// TODO implement
		return new ArrayList<Order>();
	}

	public ArrayList<Product> getScenartioProducts(Integer uptime) {
		// TODO implement
		return new ArrayList<Product>();
	}

	private Integer random(Integer min, Integer max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
