package ror.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class OrderSource {

    private File file;
    private File catalogFile;
    private ArrayList<String> catalog;
    private HashMap<Integer, ArrayList<Product>> products;
    private HashMap<Integer, ArrayList<Order>> orders;

    public OrderSource() {
	super();
	this.products = new HashMap<Integer, ArrayList<Product>>();
	this.orders = new HashMap<Integer, ArrayList<Order>>();
	this.catalog = new ArrayList<String>();
	File file = new File("xml/scenario-test.xml");
	this.setFile(file);
    }

    public HashMap<Integer, ArrayList<Product>> getProducts() {
	return products;
    }

    public HashMap<Integer, ArrayList<Order>> getOrders() {
	return orders;
    }

    public ArrayList<Product> getRandomProducts(ArrayList<Order> orders, ArrayList<Product> stock) {
	ArrayList<Product> newProducts = new ArrayList<Product>();
	ArrayList<String> productNamesInStock = new ArrayList<String>();
	// add all product name to productNamesInStock
	for (Product product : stock) {
	    productNamesInStock.add(product.getName());
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
	Integer nbOrdersToCreate = this.random(1, 5); // 1 a 5 commande(s) crée(s)
	// 1 chance sur 20 de créer les commandes
	if (random(1, 20) == 1) {
	    // Création des commandes
	    for (int i = 0; i < nbOrdersToCreate; i++) {
		Order order = new Order();
		Integer nbProducts = this.random(1, 7);
		// Ajout des produits à la commande
		for(int j = 0 ; j < nbProducts ; j++) {
		    order.addProductName(catalog.get(this.random(0, catalog.size()-1)));
		}
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
	    document = sxb.build(file);
	} catch (Exception e) {
	}

	racine = document.getRootElement();

	// recuperation du catalogue des produits
	Element catalogElement = racine.getChild("Catalog");
	List catalogProductList = catalogElement.getChildren("Product");
	Iterator itpc = catalogProductList.iterator();
	// on boucle sur chaque produit
	while (itpc.hasNext()) {
	    Element currentProduct = (Element) itpc.next();
	    this.catalog.add(currentProduct.getValue());
	}

	// recuperation du scenario de la simulation

	Element scenarioElement = racine.getChild("Scenario");
	List orderList = scenarioElement.getChildren("Order");

	Iterator i = orderList.iterator();
	// on boucle sur chaque commande
	while (i.hasNext()) {
	    Order newOrder = new Order();
	    ArrayList<String> orderProductsName = new ArrayList<String>();

	    Element currentOrder = (Element) i.next();
	    List productList = currentOrder.getChildren("Product");
	    Iterator j = productList.iterator();
	    while (j.hasNext()) {
		Element currentProduct = (Element) j.next();
		Product newProduct = new Product(currentProduct.getValue());
		int productionDate = Integer.parseInt(currentProduct.getAttributeValue("productionDate"));

		if (this.products.get(productionDate) == null)
		    this.products.put(productionDate, new ArrayList<Product>());
		this.products.get(productionDate).add(newProduct);
	    }
	    newOrder.setProductsName(orderProductsName);
	    int orderDate = Integer.parseInt(currentOrder.getAttribute("orderDate").getValue());

	    if (this.orders.get(orderDate) == null)
		this.orders.put(orderDate, new ArrayList<Order>());

	    this.orders.get(orderDate).add(newOrder);
	}

	this.file = file;
    }

    public ArrayList<String> getCatalog() {
	return catalog;
    }

    public void setCatalog(ArrayList<String> catalog) {
	this.catalog = catalog;
    }

    public File getCatalogFile() {
	return catalogFile;
    }

    public void setCatalogFile(File catalogFile) {
	this.catalogFile = catalogFile;
    }

}
