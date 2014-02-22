package POS;
import java.util.*;

public class CompositeDiscount implements Discount {
	private List<Discount> mDiscount;
	public CompositeDiscount() {
		mDiscount = new ArrayList<Discount>();
	}
	public float discount() {
		// TODO Auto-generated method stub
		float totalDiscount = 1;
		for (Discount d : mDiscount) {
			totalDiscount *= 1 - d.discount();
		}
		return totalDiscount;
	}
	
	public void add(Discount _discount) {
		mDiscount.add(_discount);
	}
	
	public void remove(Discount _discount) {
		mDiscount.remove(_discount);
	}
	
	public String discountMessage() {
		String message = "";
		for (Discount d : mDiscount) {
			message += d.discountMessage();
		}
		return message;
	}
}
