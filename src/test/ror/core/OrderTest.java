package test.ror.core;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ror.core.Order;
import ror.core.Product;

public class OrderTest {

	public Order orderTest;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		orderTest = new Order();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testOrder() {
		orderTest = new Order();
	}

	@Test
	public void testGetIdOrder() {
		assertTrue(orderTest.getIdOrder()>0);
		assertTrue(orderTest.getIdOrder()!=null);
	}

	@Test
	public void testGetStatus() {
		
		assertTrue(orderTest.getStatus()>=0);
		assertTrue(orderTest.getStatus()<=3);
		
		//init = 0
		//waiting = 1
 		//inProgress = 2
		//Done = 3
	}

	@Test
	public void testSetStatus() {
		orderTest.setStatus(0);
		assertTrue(orderTest.getStatus()==0);
		orderTest.setStatus(1);
		assertTrue(orderTest.getStatus()==1);
		orderTest.setStatus(2);
		assertTrue(orderTest.getStatus()==2);
		orderTest.setStatus(3);
		assertTrue(orderTest.getStatus()==3);
		
	}

	@Test
	public void testGetTime() {
		//assertTrue(orderTest.getTime()>=0);
	}

	@Test
	public void testSetTime() {
		//orderTest.setTime(0);
		//assertTrue(orderTest.getTime()>=0);
	}

	@Test
	public void testGetProductsName() {
		assertTrue(orderTest.getProductsName()!=null);
		assertTrue(orderTest.getProductsName().size()>=0);
	}

	@Test
	public void testSetProductsName() {
		orderTest.setProductsName(null);
		
		orderTest.setProductsName(new ArrayList<String>());
		assertTrue(orderTest.getProductsName()!=null);
		assertTrue(orderTest.getProductsName().size()>=0);
	}

	@Test
	public void testAddProductName() {
		orderTest.setProductsName(new ArrayList<String>());
		orderTest.addProductName("toto");
		assertTrue(orderTest.getProductsName().get(0)=="toto");
	}

	@Test
	public void testGetProducts() {
		orderTest=new Order();
		assertTrue(orderTest.getProducts()!=null);
		orderTest.setProducts(new ArrayList<Product>());
		assertTrue(orderTest.getProducts().size()==0);
		orderTest.addProduct(new Product("toto"));
		assertTrue(orderTest.getProducts().size()==1);
	}

	@Test
	public void testSetProducts() {
		orderTest=new Order();
		assertTrue(orderTest.getProducts()!=null);
		orderTest.setProducts(new ArrayList<Product>());
	}

	@Test
	public void testAddProduct() {
		orderTest.setProducts(new ArrayList<Product>());
		assertTrue(orderTest.getProducts().size()==0);
		orderTest.addProduct(new Product("toto"));
		assertTrue(orderTest.getProducts().size()==1);
	}

}
