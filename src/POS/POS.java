package POS;

import java.util.Scanner;
import java.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.*;
/**
 * 
 */

public class POS {

	/**
	 * @param args
	 */
	private static POS singleton = null;	
	private HashMap<String, String> userPasswdMap = new HashMap<String, String>();
	private String userPasswordFile = "userPasswordFile.txt";
	
	private HashMap<String, Item> itemList = new HashMap<String, Item>();
	private String itemListFile = "productListFile.txt";
	
	private String salesRecordFile = "logAndSales.txt";
	BufferedWriter fWriter;
	
	public String userName;
	public String password;
	
	public static int salesID = 0;
	
	private TaxModel taxModel;
	private CurrencyFactory.Country country;
	private Currency currency;
	private Discount customerDiscount, eventDiscount;
	
	public static boolean batchMode = false;
	public static String batchFile;
	BufferedReader baReader;
	
	int test(){
		return 1;
	}
	
	public static POS getInst(){
		if (singleton == null){
			singleton = new POS();
			return singleton;
		}
		else
			return singleton;
	}
	
	private void getTaxModel(String s) {
		if (s.equals("NOTAX"))
			taxModel = new TMNoTax();
		else if (s.equals("VAT"))
			taxModel = new TMVAT();
	}
		
	private boolean setCurrency(CurrencyFactory.Country country) {
		this.currency = CurrencyFactory.createCurrency(country);
		if (this.currency != null) {
			return true;
		} else {
			return false;
		}
	}
	
	private void checkCurrency(String s) {
		if (s.equals("HK"))
			setCurrency(CurrencyFactory.Country.HK);
		else if (s.equals("US"))
			setCurrency(CurrencyFactory.Country.US);
	}
	
	 /** 
     * 对double数据进行取精度. 
     * <p> 
     * For example: <br> 
     * double value = 100.345678; <br> 
     * double ret = round(value,4,BigDecimal.ROUND_HALF_UP); <br> 
     * ret为100.3457 <br> 
     *  
     * @param value 
     *            double数据. 
     * @param scale 
     *            精度位数(保留的小数位数). 
     * @param roundingMode 
     *            精度取值方式. 
     * @return 精度计算后的数据. 
     */  
    public static double round(double value, int scale, int roundingMode) {  
        BigDecimal bd = new BigDecimal(value);  
        bd = bd.setScale(scale, roundingMode);  
        double d = bd.doubleValue();  
        bd = null;  
        return d;  
    }  
    
    // int 0 to String 00
    public static String int2Str(int i){
    	String []ss = {"00","0",""};
    	String xs = String.valueOf(i);
    	xs = ss[xs.length()]+xs;
    	return xs;
    }
    
    // read the batch file
    public void loadBatchFile(){
    	try {
    		baReader = new BufferedReader(new FileReader(batchFile));    		
       	}catch(IOException e) {
    		System.out.println(e);
    		System.exit(1);
    	}
    }
	
	// load the user-password map file and the item list file, and prepare to write the sales record file
	public void init() {
		try{
			//load the user-password map file
			BufferedReader fReader = new BufferedReader(new FileReader(userPasswordFile));
			String line = fReader.readLine();
			checkCurrency(line);
			line = fReader.readLine();
			getTaxModel(line);
			line = fReader.readLine();
			while(line != null) {
				String[] temp = line.split(" ");
				// a line should be formatted like "userName password"
				if (temp.length == 2){
					String uName = temp[0];
					String passwd = temp[1];
					userPasswdMap.put(uName, passwd);
				}else {
					System.out.println("The user-password map file is wrong formatted!");
					System.exit(1);
				}
				line = fReader.readLine();
			}
			fReader.close();
			//robin
			BufferedReader bReader = new BufferedReader(new FileReader(itemListFile));
			eventDiscount = new EventDiscount(new Float(bReader.readLine()).floatValue());
			customerDiscount = new CustomerDiscount(new Float(bReader.readLine()).floatValue());

			//load the item list file
			String item = bReader.readLine();
			while(item != null) {
				String[] temp = item.split(" ");
				// a line should be formatted like "itemID itemName price number"
				if ((temp.length == 4) || (temp.length == 5)){
					String itemID = temp[0];
					String itemName = temp[1];
					Float price = new Float(temp[2]);
					Integer number = new Integer(temp[3]);
					Item _item = new Item(itemName, number, price);
					//robin
					if (temp.length == 5) {
						Discount productDiscount = new ProductDiscount(new Float(temp[4]).floatValue());
						_item.setDiscount(productDiscount);
					}
					
					itemList.put(itemID, _item);
					}else {
					System.out.println("The item list file is wrongly formatted!");
					System.exit(1);
				}
				item = bReader.readLine();
			}
			bReader.close();
			
			// prepare to write the sales record file
			fWriter = new BufferedWriter(new FileWriter(salesRecordFile));
		}catch(IOException e){
			System.out.println(e);
			System.exit(1); // 1 means abnormal termination, 0 means normal termination
		}
	}
	// read a line from the batch file
	public String getLine(){
		String line = null;
		try {
			line = baReader.readLine();
			if(line != null)
				System.out.println(line);
		}catch(IOException e) {
			System.out.println(e);
			System.exit(1);
		}
		return line;
	}
	// a manager should register to activate the sales counter
	public void register(){
		System.out.println("Welcome to the Point-Of-Sale Registration System");
		System.out.print("Please enter your user name: ");
		Scanner sc = new Scanner(System.in);
		
		if(batchMode)
			userName = getLine();
		else
			userName = sc.next();
		
		while (!userPasswdMap.containsKey(userName)) {		
			String log = "Non-existent user " + userName + ", please enter again!";
			System.out.println(log);
			logInfo(log);
			if (batchMode)
				System.exit(1);
			System.out.print("Please enter your user name:" );
			if (batchMode)
				userName = getLine();
			else
				userName = sc.next();
		}
		
		System.out.print("Please enter your password: ");		
		if (batchMode)
			password = getLine();
		else
			password = sc.next();
		
		while (!userPasswdMap.get(userName).equals(password)) {
			String log = "Wrong password " + password + " for user " + userName +", please enter again!";
			System.out.println(log);
			logInfo(log);
			if (batchMode)
				System.exit(1);
			System.out.print("Please enter your password: ");			
			if (batchMode)
				password = getLine();
			else
				password = sc.next();
		}
		
		String log = "Electronic-Sales Counter is started successfully by user " + userName + "!";
		System.out.println(log);
		logInfo(log);
		System.out.println("--------------------------------------------------------");
		saleRegister();	
	}
	
	// get a valid int from Console
	public int getNum(String sMode){		
		int mode = 0;
		boolean flag = true;
		while(flag) {
			try {
				mode = Integer.parseInt(sMode);
				if(mode < 0)
				{
					if (batchMode){
						String log = "Incorrect number!";
						logInfo(log);
						System.exit(1);
					}
					System.out.print("You should enter a valid number:");
					Scanner sc = new Scanner(System.in);
					sMode = sc.next();
				}else
					flag = false;
			}catch(NumberFormatException e) {
				if (batchMode){
					String log = "Incorrect number!";
					logInfo(log);
					System.exit(1);
				}
				System.out.print("You should enter a valid number:");
				Scanner sc = new Scanner(System.in);
				sMode = sc.next();
			}
		}
		return mode;
	}
	

	// get a valid float from Console
	public float getFloat(String sMode){		
		float mode = 0;
		boolean flag = true;
		while(flag) {
			try {
				mode = Float.parseFloat(sMode);
				if (mode < 0){
					if (batchMode){
						String log = "Incorrect number!";
						logInfo(log);
						System.exit(1);
					}
					System.out.print("You should enter a valid number:");
					Scanner sc = new Scanner(System.in);
					sMode = sc.next();
				}else
					flag = false;
			}catch(NumberFormatException e) {
				if (batchMode){
					String log = "Incorrect number!";
					logInfo(log);
					System.exit(1);
				}
				System.out.print("You should enter a valid number:");
				Scanner sc = new Scanner(System.in);
				sMode = sc.next();
			}
		}
		return mode;
	}
	
	// register a sales record
	public void saleRegister(){
		System.out.println("Welcome to the Electronic-Sales Counter!");
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter '1' to record sales or '2' to exit: ");
		String sMode;
		if(batchMode)
			sMode = getLine();
		else
			sMode = sc.next();
		int mode = getNum(sMode);
		
		
		while (mode != 2) {
			if (mode != 1) {
				String log = "Invalid command";
				System.out.println(log);
				logInfo(log);
				if (batchMode)
					System.exit(1);				
			}
			else
			{
				//robin
				Payment payment = new Payment();
				payment.addDiscount(eventDiscount);
				System.out.print("Does the customer have a membership card? 'y' or 'n': ");
				String membership = sc.next();
				while (!membership.equals("y") && !membership.equals("n")) {
					System.out.println("Invalid command");
					membership = sc.next();
				}
				if (membership.equals("y")) {
					payment.addDiscount(customerDiscount);
				} else {
					System.out.println("No member purchase discount");
				}
				
				System.out.println("Please enter a list of purchasing-product ID and number");
				System.out.println("--------------------------------------------------------");
				System.out.print("Please enter product ID or press 'c' to end the list: ");			
				
				String itemID;
				if (batchMode)
					itemID = getLine();
				else
					itemID = sc.next();
				SalesLine ss = new SalesLine();
				
				while(!itemID.equals("c")){
					if(!itemList.containsKey(itemID)){
						if (batchMode){
							String log = "Incorrect product ID!";
							logInfo(log);
							System.exit(1);
						}
						System.out.println("Product  not exists!");
						System.out.print("Please enter product ID or press 'c' to end the list: ");
						if (batchMode)
							itemID = getLine();
						else
							itemID = sc.next();
					} else {
						Item _item = (Item)itemList.get(itemID);
						System.out.print("Product name is " + _item.itemName + ", purchased number: ");
						String sNumber;
						if (batchMode) sNumber = getLine();
						else sNumber = sc.next();
						int number = getNum(sNumber);
						
						ss.itemRecord.add(new PurchaseItem(_item, number));
						//robin
						ss.sumPrice += (number*_item.price.doubleValue());
						if (_item.discount != null) {
							ss.sumPrice -= number*_item.price.doubleValue()*_item.discount.discount();
						}
						
						System.out.print("Please enter product ID or press 'c' to end the list: ");
						if (batchMode)
							itemID = getLine();
						else
							itemID = sc.next();
					}
				}
				System.out.println("--------------------------------------------------------");
				
				System.out.println("Purchasing-product list:");
				for (int i = 0; i < ss.itemRecord.size(); i++)
				{
					System.out.println(ss.itemRecord.get(i).item.itemName + " * " + ss.itemRecord.get(i).number
							+ "\t= " + currency.show() + round((ss.itemRecord.get(i).item.price)*(ss.itemRecord.get(i).number),1,BigDecimal.ROUND_HALF_UP));
					if (ss.itemRecord.get(i).item.discount != null) {
						System.out.println(ss.itemRecord.get(i).item.discount.discountMessage() + "-" + ss.itemRecord.get(i).number * ss.itemRecord.get(i).item.price * ss.itemRecord.get(i).item.discount.discount());
					}
				}
				ss.sumPrice = round(ss.sumPrice, 1, BigDecimal.ROUND_HALF_UP);
				System.out.println("The total price is " + currency.show() + ss.sumPrice);
				ss.sumPrice = round(payment.afterDiscount(ss.sumPrice), 1, BigDecimal.ROUND_HALF_UP);
				System.out.println(payment.showDiscount() + "After discount: " + currency.show() + ss.sumPrice);

				System.out.print("Please pay with cash, received cash (or cancel by entering '0'): ");
				
				String sMoney;
				if (batchMode) sMoney = getLine();
				else sMoney = sc.next();
				double receivedMoney = getFloat(sMoney);
				
				while(Math.abs(receivedMoney - 0)>1e-5 && receivedMoney < ss.sumPrice ) {
					if (batchMode){
						String log = "Cash not enough!";
						logInfo(log);
						System.exit(1);
					}
					System.out.print("Sorry, cash not enough, please enter cash amount again: ");
					if (batchMode) sMoney = getLine();
					else sMoney = sc.next();
					receivedMoney = getFloat(sMoney);
				}
				
				if (Math.abs(receivedMoney - 0)>1e-5 && receivedMoney >= ss.sumPrice){
					System.out.println("Change : " + currency.show() + round(receivedMoney - ss.sumPrice,1,BigDecimal.ROUND_HALF_UP));
					recordSalesLine(ss);
				} else
				{
					String log = "SalesID" + int2Str(salesID++) + " cancelled!";
					System.out.println(log);
					logInfo(log);
				}
			}
			
			System.out.print("Please enter '1' to continue or '2' to exit: ");
			if(batchMode)
				sMode = getLine();
			else
				sMode = sc.next();
			mode = getNum(sMode);
		}
		
		
		try{			
			String log = "User " + userName + " has successfully logged off!";
			System.out.println(log);
			logInfo(log);
			fWriter.close();
		}catch (IOException e){
			System.out.println(e);
			System.exit(1);
		}
	}
	
	// record the sales record into the sales record file
	public void recordSalesLine(SalesLine ss) {
		String str = "SalesID" + int2Str(salesID++) + "\ttotal " + currency.show() + ss.sumPrice;
		for (int i = 0; i < ss.itemRecord.size(); i++)
		{
			str += "; " + ss.itemRecord.get(i).item.itemName + " " + ss.itemRecord.get(i).number;
		}
		logInfo(str);
	}
	
	// write a log info
	public void logInfo(String str) {
		try {
			fWriter.write(str);
			fWriter.newLine();
			fWriter.flush();
		} catch (IOException e){
			System.out.println(e);
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length > 0){
			batchMode = true;
			batchFile = args[0];
			POS.getInst().loadBatchFile();
		}
		POS.getInst().init();
		POS.getInst().register();
		
	}

}
