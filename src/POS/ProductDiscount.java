package POS;

public class ProductDiscount implements Discount {
	private float discount;
	public ProductDiscount(float discount) {
		this.discount = discount;
	}
	public float discount() {
		// TODO Auto-generated method stub
		return discount;
	}
	public String discountMessage() {
		String message = "\tProduct Discount: ";
		return message;
	}

}
