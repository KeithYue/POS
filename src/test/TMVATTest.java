package test;

import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import POS.TMVAT;

public class TMVATTest {
	private TMVAT t;

	@Before
	public void setUp() throws Exception {
		t =  new TMVAT();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testAfterTaxPrice() {
		Assert.assertThat(0.2f, is(t.afterTaxPrice(0.2f)));
	}

}
