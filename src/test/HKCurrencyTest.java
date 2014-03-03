package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import POS.HKCurrency;

public class HKCurrencyTest {
	private HKCurrency hkCurrency;

	@Before
	public void setUp() throws Exception {
		hkCurrency = new HKCurrency();
	}

	@After
	public void tearDown() throws Exception {
		hkCurrency = null;
	}

	@Test
	public void testShow() {
		Assert.assertTrue(hkCurrency.show() == "HK$");
	}

}
