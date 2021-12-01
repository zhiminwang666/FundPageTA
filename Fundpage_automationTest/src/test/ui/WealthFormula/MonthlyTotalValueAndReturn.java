package test.ui.WealthFormula;
/**
 * @className: ${MonthlyTotalValueAndReturn}
 * @description: Validate the data in column Total Value and Return of the table Detail - Invest Monthly
 * @author: Sandy
 * @date: ${October 25, 2021}
 * @Modified by: Greg
 * @Modified Date:${October 28, 2021}
 * @Modified reason: 1. Canceling CSV file for parameter; 2. Combination to Test Suite by TestNG.xml
 * @Modified by: Sandy
 * @Modified Date:${November 10, 2021}
 * @Modified reason: Format Standardization: 1. parameter name, 2. TestNG assert, 3. TestNG report
 * @Version: V1.1 
 **/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.Reporter;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MonthlyTotalValueAndReturn {
	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeClass(alwaysRun = true)
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Parameters("stock_info") // added by Greg on October 28, 2021
	@Test
	public void wealthFormula(String stock_info) throws Exception { // "stock_info" added by Greg on October 28, 2021

		{
			SoftAssert softassert = new SoftAssert();
			System.out.println("Wealth Formula Monthly Total Value and Return Test");
			System.out.println(stock_info);
			Reporter.log("Wealth Formula Monthly Total Value and Return Test ");
			Reporter.log(stock_info);
			String line = "";
			String splitBy = ",";
			try {
				// parsing a CSV file into BufferedReader class constructor

				// BufferedReader br = new BufferedReader(new FileReader("C:\\Stocks.csv"));
				// //canceled by Greg on October 28, 2021
				BufferedReader br = new BufferedReader(new StringReader(stock_info)); // added by Greg on October 28,
																						// 2021
				// parsing a set of String Parameters from TestNG.xml into BufferedReader class
				// constructor
				while ((line = br.readLine()) != null) // returns a Boolean value
				{
					String[] stock = line.split(splitBy); // use comma as separator

					// show fundpage.com web page and input security, starting date, dollar saved
					// per day and click Refresh button
					driver.get("https://fundpage.com/Home/WealthFormula");

					driver.findElement(By.id("search-sec-id")).click();
					driver.findElement(By.id("search-sec-id")).sendKeys(Keys.CONTROL, "a");
					driver.findElement(By.id("search-sec-id")).sendKeys(stock[0]);

					driver.findElement(By.id("search-from-datre")).click();
					driver.findElement(By.xpath("//form[@id='wealth-calc-form']/div[2]")).click();
					driver.findElement(By.id("search-from-datre")).clear();
					driver.findElement(By.id("search-from-datre")).sendKeys(stock[1]);
					driver.findElement(By.id("search-amount")).click();
					driver.findElement(By.id("search-amount")).clear();
					driver.findElement(By.id("search-amount")).sendKeys(stock[2]);

					driver.findElement(By.id("wealth-calc")).click();
					TimeUnit.SECONDS.sleep(3);

					// Count the number of rows in the table
					List<WebElement> rows = driver.findElements(By.xpath(
							"//div[text()='Detail - Invest Monthly']/parent::div/following-sibling::div/table/tbody/tr"));
					int count = rows.size();

					for (int i = 1; i <= count; i++) {
						// read the data of every line in the table Detail - Invest Monthly
						String date = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Monthly']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td"))
								.getText();
						String priceStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Monthly']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td[2]"))
								.getText();
						String investmentTotalStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Monthly']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td[8]"))
								.getText();
						String shareStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Monthly']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td[9]"))
								.getText();
						String cashStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Monthly']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td[10]"))
								.getText();
						String totalValueStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Monthly']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td[11]"))
								.getText();
						String returnStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Monthly']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td[12]"))
								.getText();
						// Convert the read strings into Float or Integer
						float price = Float.parseFloat(priceStr);
						int investmentTotal = Integer.parseInt(investmentTotalStr);
						float shareF = Float.parseFloat(shareStr);
						int share = (int) Math.floor(shareF);
						float cash = Float.parseFloat(cashStr);
						float totalValue = Float.parseFloat(totalValueStr);
						float returnShown = Float.parseFloat(returnStr);

						// Calculate Total Value, Return and convert them into float
						float expectedTotalValue = (float) (price * share + cash);
						float expectedReturnMid = (float) ((totalValue - investmentTotal) / investmentTotal);
						expectedReturnMid = expectedReturnMid * 100;
						// keep 2 decimal places for the result
						float expectedReturn = (float) (Math.round(expectedReturnMid * 100)) / 100;

						// Compare the calculated Total Value with the shown Total Value

						softassert.assertTrue(Math.abs(expectedTotalValue - totalValue) <= 10,
								stock[0] + " " + date + " Total Value is incorrect");

						// Compare the calculated Return with the shown Return
						softassert.assertEquals(returnShown, expectedReturn,
								stock[0] + " " + date + " Return is incorrect");

					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			softassert.assertAll();
		}
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}
