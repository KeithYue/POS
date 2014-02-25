package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestCompositeDiscount.class,
	ProductDiscountTest.class,
	EventDiscountTest.class,
	CustomerDiscountTest.class
	})
public class CSIT5100_TestMain {

}