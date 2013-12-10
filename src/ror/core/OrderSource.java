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
	if (orders.isEmpty())
	    return new ArrayList<Product>();
	ArrayList<Product> newProducts = new ArrayList<Product>();
	ArrayList<String> productNamesInStock = new ArrayList<String>();
	ArrayList<String> productNamesInOrder = new ArrayList<String>();
	Integer cpt = 0, cptProduct = 0;
	// Add all product name to productNamesInStock
	for (Product product : stock) {
	    productNamesInStock.add(product.getName());
	}

	for (Order order : orders) {
	    for (String product : order.getProductsName()) { // Parcours des produits des commandes en attente
		if (productNamesInStock.contains(product)) { // Si le produit est en stock, on le destock fictivement
		    productNamesInStock.remove(product);
		} else // Si le produit n'est pas disponible en stock
		{
		    // Plus une commande est ancienne, plus elle a une chance de généré ses produits
		    if (random(1, orders.size()) == 1) {
			if (random(0, orders.size()*2) >= orders.indexOf(order) && cptProduct < 1) {
			    productNamesInOrder.add(product);
			    cptProduct++;
			}
		    }
		}
	    }
	    cpt++;
	}

	for (String productName : productNamesInOrder) {
	    newProducts.add(new Product(productName));
	}

	return newProducts;
    }

    public ArrayList<Order> getRandomOrders() {
	ArrayList<Order> newOrders = new ArrayList<Order>();
	Integer nbOrdersToCreate = this.random(1, 5); // 1 a 5 commande(s) crée(s)
	// 1 chance sur 20 de créer les commandes
	if (random(1, 15) == 1) {
	    // Création des commandes
	    for (int i = 0; i < nbOrdersToCreate; i++) {
		Order order = new Order();
		Integer nbProducts = this.random(1, 7);
		// Ajout des produits à la commande
		for (int j = 0; j < nbProducts; j++) {
		    order.addProductName(catalog.get(this.random(0, catalog.size() - 1)));
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

    public ArrayList<Product> getScenarioProducts(Integer uptime) {
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
