package test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.junit.rules.ExpectedException;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.*;

import POS.POS;

public class POSBatchModeTest {
	private String batchFilePath = "input_example1.txt";
	private String nonExitBatchPath = "non_exit_batch.txt";

	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Rule
	public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

	@Before
	public void setUp() throws Exception {
		POS.getInst().batchMode = true; 
		POS.getInst().batchFile = batchFilePath;
		POS.getInst().loadBatchFile();
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("Test tear down");
	}

	@Test
	public void getNumTest(){
		// normal condition test
		Assert.assertEquals(1, POS.getInst().getNum("1"));
		Assert.assertEquals(2, POS.getInst().getNum("2"));
	}
	
	@Test
	public void getFloatTest(){
		// normal input test
		Assert.assertTrue("getFloat works", 0.01 == POS.getInst().getFloat("0.01"));
	}
	
	@Test
	public void getLineTest(){
		// the first line of the input file
		Assert.assertEquals("zhulin", POS.getInst().getLine());
	}
	
	@Test
	public void wrongBatchTest(){
		POS.batchFile = nonExitBatchPath;
		exit.expectSystemExit();
		POS.getInst().loadBatchFile();
	}

	@Test
	public void int2StrTest(){
		Assert.assertEquals("00", POS.int2Str(0));
		Assert.assertEquals("01", POS.int2Str(1));
		Assert.assertEquals("12", POS.int2Str(12));
	}
	
	@Test
	public void initTest(){
		POS.getInst().init();
	}
	
	@Test
	public void registerTest(){
		POS.getInst().register();
	}
	
	@Test
	public void testMain(){
		String []s = {"input_example1.txt"};
		// due to the membership card bug, we provide this information mannualy
		systemInMock.provideText("y\n");
		POS.main(s);
	}
	
}
