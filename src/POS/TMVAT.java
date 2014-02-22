package POS;

public class TMVAT implements TaxModel {
	public float afterTaxPrice(float price) {
		return price;
	}
}