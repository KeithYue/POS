package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import POS.USCurrency;

public class USCurrencyTest {
	private USCurrency usCurrency;

	@Before
	public void setUp() throws Exception {
		usCurrency = new USCurrency();
	}

	@After
	public void tearDown() throws Exception {
		usCurrency = null;
	}

	@Test
	public void testShow() {
		Assert.assertTrue(usCurrency.show() == "US$");
	}

}
