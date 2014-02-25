package POS;

// this class is to help test the CustomerDiscount class, which has a package field constructor
public class CustomerDiscountProvider {
	public static CustomerDiscount getCustomerDiscount(float f){
		return (new CustomerDiscount(f));
	}

}
