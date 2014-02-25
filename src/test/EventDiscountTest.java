package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import POS.EventDiscount;

public class EventDiscountTest {
	private EventDiscount eventDiscount;

	@Before
	public void setUp() throws Exception {
		eventDiscount = new EventDiscount(0.3f);
	}

	@After
	public void tearDown() throws Exception {
		eventDiscount = null;
	}

	@Test
	public void testDiscount() {
		Assert.assertTrue(eventDiscount.discount() == 0.3f);
	}

	@Test
	public void testDiscountMessage() {
		Assert.assertTrue(eventDiscount.discountMessage().equals("\tEvent Discount: "+eventDiscount.discount()*100+"%\n"));
	}

}
