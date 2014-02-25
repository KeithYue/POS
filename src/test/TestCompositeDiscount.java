/**
 * 
 */
package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import POS.CompositeDiscount;
import POS.EventDiscount;
import POS.ProductDiscount;

/**
 * @author Keith
 *
 */
public class TestCompositeDiscount {
	private CompositeDiscount compDiscount;
	private ProductDiscount discount1;
	private EventDiscount discount2;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		compDiscount = new CompositeDiscount();
		discount1 =  new ProductDiscount(0.2f);
		discount2 = new EventDiscount(0.05f);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		compDiscount = null;
		discount1 = null;
		discount2 = null;
	}

	// @Test
	// public void test() {
	// 	fail("Not yet implemented");
	// }
	
	@Test
	public void testAddDiscount(){
		Assert.assertTrue(compDiscount.discount() == 1.0f);
		
		// add first discount, the total discount will be 0.8
		compDiscount.add(discount1);
		Assert.assertTrue(compDiscount.discount() == 0.8f);
		
		// add second discount, the total discount will be 0.8 * 0.95
		compDiscount.add(discount2);
		Assert.assertTrue(compDiscount.discount() == 0.8f * 0.95f);
	}
	
	@Test
	public void testRemoveDiscount(){
		//after removing two discounts, there will be no discount
		compDiscount.remove(discount1);
		compDiscount.remove(discount2);
		Assert.assertTrue("no savings after removing all discounts", compDiscount.discount() == 1.0f);
	}

}










