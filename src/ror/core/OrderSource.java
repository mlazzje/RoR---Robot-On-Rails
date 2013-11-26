package ror.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.SAXException;

public class OrderSource {

	private File file;
	private HashMap<Integer, ArrayList<Product>> products;
	private HashMap<Integer, ArrayList<Order>> orders;
	
	
	public OrderSource() {
		super();
		this.products= new HashMap<Integer, ArrayList<Product>>();
		this.orders = new HashMap<Integer, ArrayList<Order>>();
	}

	public HashMap<Integer, ArrayList<Product>> getProducts() {
		return products;
	}

	public HashMap<Integer, ArrayList<Order>> getOrders() {
		return orders;
	}

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
				newProducts.add(new Product("Nom")); // TODO specify the product
														// name
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

		Document document = null;
		Element racine;

		SAXBuilder sxb = new SAXBuilder();
		try {
			// On crée un nouveau document JDOM avec en argument le fichier XML
			// Le parsing est terminé ;)
			document = sxb.build(file);
		} catch (Exception e) {
		}

		racine = document.getRootElement();

		List orderList = racine.getChildren("order");

		Iterator i = orderList.iterator();
		//on boucle sur chaque commande
		while (i.hasNext()) {
			Order newOrder = new Order();
			ArrayList<String> orderProductsName= new ArrayList<String>();
			
			Element currentOrder = (Element) i.next();
			List productList = currentOrder.getChildren("product");
			Iterator j = productList.iterator();
			while (j.hasNext()) {
				Element currentProduct = (Element) j.next();
				Product newProduct = new Product(currentProduct.getValue());
				int productionDate = Integer.parseInt(currentProduct.getAttributeValue("productionDate"));
				
				if(this.products.get(productionDate)==null)
					this.products.put(productionDate, new ArrayList<Product>());
				this.products.get(productionDate).add(newProduct);
			}
			newOrder.setProductsName(orderProductsName);
			int orderDate = Integer.parseInt(currentOrder.getAttribute("orderDate").getValue());
			
			if(this.orders.get(orderDate)==null)
				this.orders.put(orderDate, new ArrayList<Order>());
			
			this.orders.get(orderDate).add(newOrder);
		}

		this.file = file;
	}

}
