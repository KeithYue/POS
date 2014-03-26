package test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import POS.ProductDiscount;

public class ProductDiscountTest {
	private ProductDiscount productDisconut;

	@Before
	public void setUp() throws Exception {
		productDisconut = new ProductDiscount(0.2f);
	}

	@After
	public void tearDown() throws Exception {
		productDisconut = null;
	}

	@Test
	public void testDiscount() {
		Assert.assertThat(0.2f, is(productDisconut.discount()));
	}

	@Test
	public void testDiscountMessage() {
		// Assert.assertTrue(productDisconut.discountMessage() == "\tProduct Discount: 20%\n");
		Assert.assertThat("\tProduct Discount: 20%\n", is(productDisconut.discountMessage()));
	}

}
