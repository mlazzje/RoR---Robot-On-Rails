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

public class ProductTest {

	public Product product;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		product = new Product("T1");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testProduct() {
		product = new Product("toto");
		assertTrue(product!=null);
	}

	@Test
	public void testGetName() {
		product.setName("toto");
		assertTrue(product.getName() == "toto");
	}

	@Test
	public void testSetName() {
		product.setName("toto");
		assertTrue(product.getName() == "toto");
	}

	@Test
	public void testgetStatus() {
		product = new Product("s");
		assertTrue(product.getStatus() == 0);

	}

	@Test
	public void testsetStatus() {

		product.setStatus(0);
		assertTrue(product.getStatus() == 0);
		product.setStatus(1);
		assertTrue(product.getStatus() == 1);
		product.setStatus(2);
		assertTrue(product.getStatus() == 2);

		// 0 free
		// 1 waiting
		// 2 booked
	}

	@Test
	public void testGetOrder() {
	}

	@Test
	public void testSetOrder() {
		product.setOrder(null);
		assertTrue(product.getOrder() == null);
		product.setOrder(new Order());
		assertTrue(product.getOrder() != null);
		assertTrue(product.getOrder() instanceof Order);
	}

}
