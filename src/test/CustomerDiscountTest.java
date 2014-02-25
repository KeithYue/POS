package test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import POS.CustomerDiscount;
import POS.CustomerDiscountProvider;

public class CustomerDiscountTest {
	private CustomerDiscount customerDiscount;

	@Before
	public void setUp() throws Exception {
		customerDiscount = CustomerDiscountProvider.getCustomerDiscount(0.2f);
	}

	@After
	public void tearDown() throws Exception {
		customerDiscount = null;
	}

	@Test
	public void testDiscount() {
		Assert.assertTrue(customerDiscount.discount() == 0.2f);
	}

	@Test
	public void testDiscountMessage() {
		Assert.assertTrue(customerDiscount.discountMessage().equals("\tMembership Discount: "+customerDiscount.discount()*100+"%\n"));
	}

}
