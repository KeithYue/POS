package test;

import static org.junit.Assert.*;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.*;
import static org.hamcrest.CoreMatchers.*;
import java.lang.reflect.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import POS.*;

public class POSConsoleModeTest {
	private String logfile = "logAndSales.txt";
	private String userName = "zhulin";
	private String passWord = "zhulin";
	private String userNameInput = "zhulin\n" + "zhulin\n";

	private String saleRecordInput = "1\n" + "y\n" + "ID001\n" + "2\n" + "ID003\n" + "2\n" + "c\n"
			+ "100\n" + "2\n";

	private String saleWithNoMembership = "1\n" + "n\n" + "ID001\n" + "2\n"
			+ "c\n" + "2\n" + "2\n";
	private String saleWithNotEnoughCash = "1\n" + "n\n" + "ID001\n" + "2\n"
			+ "c\n" + "0.5\n" + "3\n" + "2\n";

	private String saleWithInvalidCash = "1\n" + "y\n" + "ID001\n" + "2\n"
			+ "c\n" + "-2\n" + "-1\n" + "2\n" + "2\n";
	private String saleWithNoExistItem = "1\n" + "n\n" + "ID1000\n" + "ID001\n"
			+ "2\n" + "c\n" + "2\n" + "2\n";

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();
	
	// @Rule
	// public final StandardOutputStreamLog log = new StandardOutputStreamLog(); // This log instance  is to help check the out stream of System.out

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		POS.getInst().batchMode = false;
	}

	@After
	public void tearDown() throws Exception {
		// output the log file
		System.err.println(getLogFileContent());
	}

	@Test
	public void initTest() {
		POS.getInst().init();
	}

	@Test
	public void saleRegisterTest() {
		setUpWithUser();
		// provide the sales record input
		systemInMock.provideText(saleRecordInput);
		POS.getInst().saleRegister();
		
		// check whether the user zhulin has logged out
		String [] logStr = getLogFileContent().split("\n");
		Assert.assertThat("User zhulin has successfully logged off!", is(logStr[logStr.length - 1]));
	}

	@Test
	public void saleRegisterNoMemberCard() {
		setUpWithUser();
		// provide user not a membership
		systemInMock.provideText(saleWithNoMembership);
		POS.getInst().saleRegister();
	}

	@Test
	public void saleRegisterWithoutEnoughCash() {
		setUpWithUser();
		// provider a user which has not enough money for the pencil
		systemInMock.provideText(saleWithNotEnoughCash);
		POS.getInst().saleRegister();
	}

	@Test
	public void saleRegisterWithInvalidCash() {
		setUpWithUser();
		// provide a user with invalid cash number, the cash number is negative
		systemInMock.provideText("1\n" + "x\n" + "y\n" + "ID001\n" + "2\n" + "c\n"
			+ "2\n" + "2\n");
		POS.getInst().saleRegister();
	}
	
	@Test
	public void saleRegisterWithLessCash(){
		setUpWithUser();
		systemInMock.provideText("1\n"+"y\n" +"ID001\n" + "2\n" + "c\n" +  "0.0000007\n" + "2\n" + "2\n");
		POS.getInst().saleRegister();
	}
	
	
	@Test
	public void saleRegisterWithInvalidMemshipCommand(){
		setUpWithUser();
		// insert the invalid membership command
		systemInMock.provideText(saleRecordInput);
		POS.getInst().saleRegister();
	}

	@Test
	public void saleRegisterWithNoExistItem() {
		setUpWithUser();
		systemInMock.provideText(saleWithNoExistItem);
		POS.getInst().saleRegister();
	}
	
	@Test 
	public void saleRegisterWithInvalidCommand(){
		setUpWithUser();
		systemInMock.provideText("3\n"+"1\n" + "y\n" + "ID001\n" + "2\n" + "c\n"
			+ "2\n" + "2\n");
		POS.getInst().saleRegister();
	}
	

	private void setUpWithUser() {
		POS.getInst().init();
		// set the user name and password
		POS.getInst().userName = userName;
		POS.getInst().password = passWord;
	}

	// we test the register method, but the register method would invoke the saleRegister method, so we combine them by providing different input stream
	@Test
	public void registerWithNormalUser() throws IOException {
		SubPOS subPos = new SubPOS();
		System.out.println("We are using sub-class to test register method");
		subPos.init();
		// provide user input for register
		systemInMock.provideText(userNameInput);
		subPos.register();
		
		// read the logfile
		System.out.println("I am reading the log file!!!!");
		System.out.println(FileUtils.readFile(logfile));
	}
	
	@Test
	public void registerWithWrongUser(){
		// wrong password
		SubPOS subPos = new SubPOS();
		subPos.init();
		systemInMock.provideText("zhulin\n" +
				"wrongpass\n" +
				"zhulin\n");
		subPos.register();
		
		// wrong user name
		subPos.init();
		systemInMock.provideText("wronguser\n" +
				"zhulin\n" +
				"zhulin\n");
		subPos.register();
	}
	
	@Test
	public void setCurrencyTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Method setCurrencyMethod = POS.class.getDeclaredMethod("setCurrency", CurrencyFactory.Country.class);
		setCurrencyMethod.setAccessible(true);
		
		Assert.assertThat((Boolean)setCurrencyMethod.invoke(POS.getInst(), CurrencyFactory.Country.US), is(true));

		Assert.assertThat((Boolean)setCurrencyMethod.invoke(POS.getInst(), CurrencyFactory.Country.HK), is(true));

		Assert.assertThat((Boolean)setCurrencyMethod.invoke(POS.getInst(), (Object)null), is(false));
	}
	
	@Test
	public void checkCurrencyTest() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException{
		Method checkCurrencyMethod = POS.class.getDeclaredMethod("checkCurrency", String.class);
		checkCurrencyMethod.setAccessible(true);
		Field currencyField = POS.class.getDeclaredField("currency");
		currencyField.setAccessible(true);
		
		checkCurrencyMethod.invoke(POS.getInst(), "HK");
		Assert.assertThat(currencyField.get(POS.getInst()), instanceOf(HKCurrency.class));

		checkCurrencyMethod.invoke(POS.getInst(), "US");
		Assert.assertThat(currencyField.get(POS.getInst()), instanceOf(USCurrency.class));

		checkCurrencyMethod.invoke(POS.getInst(), "WrongInput");
	}
	
	@Test
	public void testTest() throws IllegalArgumentException, IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException{
		// use reflection to test the private function
		// test the test function of POS
		// Assert.assertThat(POS.getInst().test(), is(1));
		Method m = POS.class.getDeclaredMethod("test");// get the method of test 
		m.setAccessible(true);
		// m.invoke(POS.getInst());
		Assert.assertThat((Integer)m.invoke(POS.getInst()), is(1));
	}

	@Test
	public void getTaxModelTest() throws NoSuchMethodException, SecurityException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		// set this method to public
		Method getTaxModelMethod = POS.class.getDeclaredMethod("getTaxModel", String.class);
		getTaxModelMethod.setAccessible(true);
		// set taxmodel to true
		Field tax = POS.class.getDeclaredField("taxModel");
		tax.setAccessible(true);
		
		getTaxModelMethod.invoke(POS.getInst(), "NOTAX");
		Assert.assertThat(tax.get(POS.getInst()), instanceOf(TMNoTax.class));
		
		getTaxModelMethod.invoke(POS.getInst(), "VAT");
		Assert.assertThat(tax.get(POS.getInst()), instanceOf(TMVAT.class));

		getTaxModelMethod.invoke(POS.getInst(), "Wrong input");
		Assert.assertThat(tax.get(POS.getInst()), instanceOf(TMVAT.class));
	}
	
	
	// we override the saleRegister method to assure that the register method
	// can be tested normally
	private class SubPOS extends POS {
		private String saleRecordInput = "1\n" + "y\n" + "ID001\n" + "2\n"
				+ "c\n" + "2\n" + "2\n";
		@Override
		public void saleRegister() {
			// record the old system input
			System.out.println("This is sub-class sale register");
			InputStream oldStdIn = System.in;
			ByteArrayInputStream in = new ByteArrayInputStream(
					saleRecordInput.getBytes());
			System.setIn(in);

			// invoke the super class' method saleRegister
			super.saleRegister();
			// restore the old system input
			System.setIn(oldStdIn);
		}
		
		// Initializing Instance Members 
		{
			batchMode = false;
		}
	}
	
	@Test
	public void getNumTest(){
		Assert.assertThat(1, is(POS.getInst().getNum("1")));
		
		// input negative number
		systemInMock.provideText("1");
		Assert.assertThat(1, is(POS.getInst().getNum("-1")));
		
		// input a string instead of a number
		systemInMock.provideText("1");
		Assert.assertThat(1, is(POS.getInst().getNum("wrong input string")));
	}
	
	@Test
	public void getFloat(){
		Assert.assertThat(2.2f, is(POS.getInst().getFloat("2.2")));
		
		// input a negative value
		systemInMock.provideText("2.2");
		Assert.assertThat(2.2f, is(POS.getInst().getFloat("-2.2")));
		
		// intpua invalid string
		systemInMock.provideText("2.2");
		Assert.assertThat(2.2f, is(POS.getInst().getFloat("wrong input string")));
	}
	
	@Test
	public void testMain(){
		SubPOS subPOS = new SubPOS();
		// systemInMock.provideText(userNameInput);// provide text for register input, not saleRegister
		subPOS.main(null);
	}
	
	private String getLogFileContent(){
		try {
			return FileUtils.readFile(logfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	
}
