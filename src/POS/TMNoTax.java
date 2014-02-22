package POS;

public class TMNoTax implements TaxModel {
	public float afterTaxPrice(float price) {
		return price;
	}
}