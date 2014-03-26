package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import POS.CustomerDiscount;
import POS.CustomerDiscountProvider;
import POS.EventDiscount;
import POS.Payment;

public class PaymentTest {
	
	Payment payment;

	@Before
	public void setUp() throws Exception {
		payment = new Payment();
	}

	@After
	public void tearDown() throws Exception {
		
		payment = null;
	}

	@Test
	public void testAddDiscount() {
		payment.addDiscount(new EventDiscount(0.8f));
		String eventDiscountMsgString  = "\tEvent Discount: " + 0.8f*100 + "%" + "\n";
		assertTrue(payment.showDiscount().contentEquals(eventDiscountMsgString));
		
		payment.addDiscount(CustomerDiscountProvider.getCustomerDiscount(0.6f));
		String DiscountMsgString  = "\tEvent Discount: " + 0.8f*100 + "%" + "\n";
		DiscountMsgString += "\tMembership Discount: " + 0.6f*100 + "%" + "\n";
		assertTrue(payment.showDiscount().contentEquals(DiscountMsgString));
	}


	@Test
	public void testAfterDiscount() {
		
		payment.addDiscount(new EventDiscount(0.8f));
		payment.addDiscount(CustomerDiscountProvider.getCustomerDiscount(0.6f));

		double expectSumAfterDiscount = 200 *(1-0.8f)*(1-0.6f);
		
		assertEquals(expectSumAfterDiscount, payment.afterDiscount(200),0.0001);
	}

}