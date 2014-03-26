package test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import POS.CurrencyFactory;
import POS.HKCurrency;
import POS.USCurrency;

public class CurrencyFactoryTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void createCurrencyTest(){
		Assert.assertThat(CurrencyFactory.createCurrency(CurrencyFactory.Country.HK), instanceOf(HKCurrency.class));

		Assert.assertThat(CurrencyFactory.createCurrency(CurrencyFactory.Country.US), instanceOf(USCurrency.class));

		Assert.assertThat(CurrencyFactory.createCurrency(null), is(nullValue()));
	}

}
