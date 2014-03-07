package test;

import static org.junit.Assert.*;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import POS.POS;

public class POSConsoleModeTest {
	private String userName = "zhulin";
	private String passWord = "zhulin";
	private String userNameInput = "zhulin\n" + "zhulin\n";

	private String saleRecordInput = "1\n" + "y\n" + "ID001\n" + "2\n" + "c\n"
			+ "2\n" + "2\n";

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
		systemInMock.provideText(saleWithInvalidCash);
		POS.getInst().saleRegister();
	}

	@Test
	public void saleRegisterWithNoExistItem() {
		setUpWithUser();
		systemInMock.provideText(saleWithNoExistItem);
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
	public void registerWithNormalUser() {
		SubPOS subPos = new SubPOS();
		System.out.println("We are using sub-class to test register method");
		subPos.init();
		// provide user input for register
		systemInMock.provideText(userNameInput);
		subPos.register();
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
}
