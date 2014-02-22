package POS;

import java.util.Vector;

//a sales LineItem class
public class SalesLine {
	public double sumPrice;
	public Vector<PurchaseItem> itemRecord;	
	public SalesLine() {
		sumPrice = 0;
		itemRecord = new Vector<PurchaseItem>();
	}
}

class PurchaseItem {
	public Item item;
	public int number;
	public PurchaseItem(Item _item, int _number){
		item = _item;
		number = _number;
	}
}
