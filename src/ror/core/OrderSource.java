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

    /**
     * Scenario file for orders
     */
    private File scenarioFile;
    /**
     * Cataog file for products
     */
    private File catalogFile;

    /**
     * ArrayList string for catalog
     */
    private ArrayList<String> catalog;
    /**
     * Hashmaps of products
     */
    private HashMap<Integer, ArrayList<Product>> products;
    /**
     * Hashmap of orders
     */
    private HashMap<Integer, ArrayList<Order>> orders;

    /**
     * Constructor of OrderSource
     */
    public OrderSource() {
	super();
	this.products = new HashMap<Integer, ArrayList<Product>>();
	this.orders = new HashMap<Integer, ArrayList<Order>>();
	this.catalog = new ArrayList<String>();

	File catalogFile = new File("xml/catalog.xml");
	this.setCatalogFile(catalogFile);
    }

    /**
     * @return the Hashmap of products
     */
    public HashMap<Integer, ArrayList<Product>> getProducts() {
	return products;
    }

    /**
     * @return the Hashmap of orders
     */
    public HashMap<Integer, ArrayList<Order>> getOrders() {
	return orders;
    }

    /**
     * @param orders
     * @param stock
     * @return a random products
     */
    public ArrayList<Product> getRandomProducts(ArrayList<Order> orders, ArrayList<Product> stock) {
	if (orders.isEmpty())
	    return new ArrayList<Product>();
	ArrayList<Product> newProducts = new ArrayList<Product>();
	ArrayList<String> productNamesInStock = new ArrayList<String>();
	ArrayList<String> productNamesInOrder = new ArrayList<String>();
	Integer cpt = 0, cptProduct = 0, nbProductToCreate = 8;
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
		    if (cptProduct < nbProductToCreate) {
			productNamesInOrder.add(product);
			cptProduct++;
		    } else {
			break;
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

    /**
     * @return random orders
     */
    public ArrayList<Order> getRandomOrders() {
	ArrayList<Order> newOrders = new ArrayList<Order>();
	Integer nbOrdersToCreate = 1; // 1 a 5 commande(s) crée(s)
	// 1 chance sur 3 de créer les commandes
	if (random(1, 2) == 1) {
	    // Création des commandes
	    for (int i = 0; i < nbOrdersToCreate; i++) {
		Order order = new Order();
		Integer nbProducts = this.random(1, 20);
		// Ajout des produits à la commande
		for (int j = 0; j < nbProducts; j++) {
		    order.addProductName(catalog.get(this.random(0, catalog.size() - 1)));
		}
		newOrders.add(order);
	    }
	}
	return newOrders;
    }

    /**
     * @param uptime
     * @return scenario of orders
     */
    public ArrayList<Order> getScenarioOrders(Long uptime) {
	ArrayList<Order> orders = new ArrayList<Order>();
	ArrayList<Integer> keyToRemove = new ArrayList<Integer>();

	for (Integer key : this.orders.keySet()) {
	    if (key <= uptime) {
		orders.addAll(this.orders.get(key));
		System.out.println("Nouvelle commande : " + this.orders.get(key).toString());
		keyToRemove.add(key);
	    }
	}

	for (Integer key : keyToRemove) {
	    this.orders.remove(key);
	}

	return orders;
    }

    /**
     * @param uptime
     * @return scenario of products created
     */
    public ArrayList<Product> getScenarioProducts(Long uptime) {
	ArrayList<Product> returnedProducts = new ArrayList<Product>();
	ArrayList<Integer> keyToRemove = new ArrayList<Integer>();

	for (Integer key : this.products.keySet()) {
	    if (key <= uptime) {
		System.out.println("Production de : " + this.products.get(key).toString());
		returnedProducts.addAll(this.products.get(key));
		keyToRemove.add(key);
	    }
	}

	for (Integer key : keyToRemove) {
	    this.products.remove(key);
	}
	return returnedProducts;
    }

    /**
     * @param min
     *            integer
     * @param max
     *            integer
     * @return random integer between a minimum and a maximum
     */
    private Integer random(Integer min, Integer max) {
	return min + (int) (Math.random() * ((max - min) + 1));
    }

    /**
     * Set a catalog file
     * 
     * @param catalogFile
     */
    public void setCatalogFile(File catalogFile) {
	this.catalogFile = catalogFile;

	// XML SAX parsing
	Document document = null;
	Element racine;
	SAXBuilder sxb = new SAXBuilder();
	try {
	    document = sxb.build(catalogFile);
	} catch (Exception e) {
	}
	racine = document.getRootElement();

	// Récupération du catalogue de produits
	Element catalogElement = racine.getChild("Catalog");
	List<Element> catalogProductList = catalogElement.getChildren("Product");
	Iterator<Element> itpc = catalogProductList.iterator();
	while (itpc.hasNext()) {
	    Element currentProduct = itpc.next();
	    this.catalog.add(currentProduct.getValue());
	}
    }

    /**
     * Set a scenario File
     * 
     * @param scenarioFile
     */
    public void setScenarioFile(File scenarioFile) {
	System.out.println("Chargement fichier scenario");

	this.scenarioFile = scenarioFile;

	// XML SAX parsing
	Document document = null;
	Element racine;
	SAXBuilder sxb = new SAXBuilder();
	try {
	    document = sxb.build(scenarioFile);
	} catch (Exception e) {
	}
	racine = document.getRootElement();

	// Récupération des commandes
	Element scenarioElement = racine.getChild("Scenario");
	List<Element> orderList = scenarioElement.getChildren("Order");
	Iterator<Element> i = orderList.iterator();
	// Boucle sur chaque commande
	while (i.hasNext()) {
	    Element currentOrder = i.next();
	    Order newOrder = new Order();
	    // Date de la commande en secondes
	    int orderDate = Integer.parseInt(currentOrder.getAttribute("orderDate").getValue()) * 1000;
	    if (this.orders.get(orderDate) == null)
		this.orders.put(orderDate, new ArrayList<Order>());
	    this.orders.get(orderDate).add(newOrder);
	    // Liste de produits
	    List<Element> productList = currentOrder.getChildren("Product");
	    Iterator<Element> j = productList.iterator();
	    // Boucle sur chaque produit de la commande
	    while (j.hasNext()) {
		// Création du produit
		Element currentProduct = j.next();
		Product newProduct = new Product(currentProduct.getValue());
		//newProduct.setOrder(newOrder);
		// Date de production
		int productionDate = orderDate + Integer.parseInt(currentProduct.getAttributeValue("productionDate")) * 1000;
		if (this.products.get(productionDate) == null)
		    this.products.put(productionDate, new ArrayList<Product>());
		this.products.get(productionDate).add(newProduct);

		newOrder.addProductName(newProduct.getName());
	    }
	}
    }

    /**
     * @return the catalog of products
     */
    public ArrayList<String> getCatalog() {
	return catalog;
    }

    /**
     * Set the catalog of products
     * 
     * @param catalog
     */
    public void setCatalog(ArrayList<String> catalog) {
	this.catalog = catalog;
    }

    /**
     * @return the scenario file of orders
     */
    public File getScenarioFile() {
	return this.scenarioFile;
    }

    /**
     * @return the catalog file of products
     */
    public File getCatalogFile1() {
	return this.catalogFile;
    }

}
