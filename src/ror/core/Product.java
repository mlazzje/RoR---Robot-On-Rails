package ror.core;

public class Product {
    private String name;
    private Integer statut;
    private Order order;

    public Product(String name) {
	this.name = name;
	this.statut = 0;
	this.order = null;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Integer getStatut() {
	return statut;
    }

    public void setStatut(Integer statut) {
	this.statut = statut;
    }

    public Order getOrder() {
	return order;
    }

    public void setOrder(Order order) {
	this.order = order;
    }
}
