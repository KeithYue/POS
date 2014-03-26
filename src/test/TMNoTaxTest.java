package test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import POS.TMNoTax;

public class TMNoTaxTest {
	private TMNoTax tmNoTax;

	@Before
	public void setUp() throws Exception {
		tmNoTax = new TMNoTax();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testAfterTaxPrice() {
		Assert.assertThat(0.2f, is(tmNoTax.afterTaxPrice(0.2f)));
	}

}
