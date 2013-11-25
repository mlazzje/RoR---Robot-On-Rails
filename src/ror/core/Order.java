package ror.core;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private Integer idOrder;
    private static Integer lastIdOrder = 0;
    private Integer status;
    private Integer time;
    private List<String> productsName;
    private List<Product> products;

    public Order() {
	Order.lastIdOrder++;
	this.setIdOrder(Order.lastIdOrder);
	this.status = 0;
	this.time = 0;
	this.productsName = new ArrayList<String>();
	this.products = new ArrayList<Product>();
    }

    public Integer getIdOrder() {
	return idOrder;
    }

    private void setIdOrder(int idOrder) {
	this.idOrder = idOrder;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public Integer getTime() {
	return time;
    }

    public void setTime(int time) {
	this.time = time;
    }

    public List<String> getProductsName() {
	return productsName;
    }

    public void setProductsName(List<String> productsName) {
	this.productsName = productsName;
    }

    public void addProductName(String productName) {
	this.productsName.add(productName);
    }

    public List<Product> getProducts() {
	return products;
    }

    public void setProducts(List<Product> products) {
	this.products = products;
    }

    public void addProduct(Product product) {
	this.products.add(product);
    }
}
