package POS;

//a Item class storing the itemname, price, and number
public class Item {
	public String itemName;
	public Integer number;
	public Float price;
	public Discount discount;
	public Item(String iName, Integer _number, Float _price){
		itemName = iName;
		number = _number;
		price = _price;
		discount = null;
	}
	public void setDiscount(Discount _discount) {
		discount = _discount;
	}
}
