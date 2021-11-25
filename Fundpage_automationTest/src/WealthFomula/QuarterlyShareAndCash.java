package WealthFomula;
/**
 * @className: ${QuarterlyShareAndCash}
 * @description: Validate the data in column Share and Cash of the table Detail - Invest Quarterly
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

public class QuarterlyShareAndCash {
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
			System.out.println("Wealth Formula Quarterly Share and Cash Test");
			System.out.println(stock_info);
			Reporter.log("Wealth Formula Quarterly Share and Cash Test ");
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

					// show fundpage.com webpage and input security, starting date, dollar saved per
					// day and click Refresh button
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

					// read the data of first line in the table Detail - Invest quarterly
					String firstDate = driver.findElement(By.xpath(
							"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr/td"))
							.getText();
					String firstPriceStr = driver.findElement(By.xpath(
							"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr/td[2]"))
							.getText();
					String firstInvestmentStr = driver.findElement(By.xpath(
							"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr/td[7]"))
							.getText();
					String firstShareStr = driver.findElement(By.xpath(
							"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr/td[9]"))
							.getText();
					String firstCashStr = driver.findElement(By.xpath(
							"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr/td[10]"))
							.getText();
					// Convert the read strings into Float or Integer
					float firstPrice = Float.parseFloat(firstPriceStr);
					int firstInvestment = Integer.parseInt(firstInvestmentStr);
					int firstShare = Integer.parseInt(firstShareStr);
					float firstCash = Float.parseFloat(firstCashStr);
					// Calculate expected share and convert it into Integer
					float expectedShareF = (float) (firstInvestment / firstPrice);
					int expectedShare1 = (int) Math.floor(expectedShareF);
					// Calculate expected cash and keep it to two decimal places
					float expectedCashMid = (float) (firstInvestment - firstPrice * firstShare);
					float expectedCash1 = (float) (Math.round(expectedCashMid * 100)) / 100;
					// Compare the calculated share with the shown share

					softassert.assertEquals(firstShare, expectedShare1,
							stock[0] + " " + firstDate + " Share is incorrect");

					// Compare the calculated cash with the shown cash
					softassert.assertTrue(Math.abs(expectedCash1 - firstCash) <= 0.1,
							stock[0] + " " + firstDate + " Cash is incorrect");

					// validate the data of following lines

					// Count the number of rows in the table
					List<WebElement> rows = driver.findElements(By.xpath(
							"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr"));
					int count = rows.size();

					for (int i = 2; i <= count; i++) {
						// read the data of following lines in the table Detail - Invest Quarterly
						String date = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td"))
								.getText();
						String priceStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td[2]"))
								.getText();
						String dividendInLastRowStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr["
										+ (i - 1) + "]/td[5]"))
								.getText();
						String splitStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td[6]"))
								.getText();
						String investmentStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td[7]"))
								.getText();
						String shareStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td[9]"))
								.getText();
						String shareInLastRowStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr["
										+ (i - 1) + "]/td[9]"))
								.getText();
						String cashStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td[10]"))
								.getText();
						String cashInLastRowStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Quarterly']/parent::div/following-sibling::div/table/tbody/tr["
										+ (i - 1) + "]/td[10]"))
								.getText();

						// Convert the read strings into Float or Integer
						float price = Float.parseFloat(priceStr);
						int investment = Integer.parseInt(investmentStr);
						float share2 = Float.parseFloat(shareStr);
						int share = (int) Math.floor(share2);
						float shareInLastRow2 = Float.parseFloat(shareInLastRowStr);
						int shareInLastRow = (int) Math.floor(shareInLastRow2);
						float cash = Float.parseFloat(cashStr);
						float cashInLastRow = Float.parseFloat(cashInLastRowStr);

						if (splitStr.length() <= 0) {

							if (dividendInLastRowStr.length() <= 0) {
								// Calculate expected share and convert it into Integer
								float newShareF = (float) ((investment + cashInLastRow) / price);
								int newShare = (int) Math.floor(newShareF);
								int expectedShare = (int) (newShare + shareInLastRow);
								// Calculate expected cash and keep it to two decimal places
								float expectedCash2 = (float) (investment + cashInLastRow - price * newShare);
								float expectedCash = (float) (Math.round(expectedCash2 * 100)) / 100;
								// Compare the calculated share with the shown share
								softassert.assertEquals(share, expectedShare,
										stock[0] + " " + date + " Share is incorrect");

								// Compare the calculated cash with the shown cash
								softassert.assertTrue(Math.abs(expectedCash - cash) <= 0.1,
										stock[0] + " " + date + " Cash is incorrect");

							} else {
								float dividendInLastRow = Float.parseFloat(dividendInLastRowStr);

								// Calculate expected share and convert it into Integer
								float newShareF = (float) ((investment + cashInLastRow
										+ dividendInLastRow * shareInLastRow) / price);
								int newShare = (int) Math.floor(newShareF);
								int expectedShare = (int) (newShare + shareInLastRow);

								// Calculate expected cash and keep it to two decimal places
								float expectedCash2 = (float) (investment + cashInLastRow
										+ dividendInLastRow * shareInLastRow - price * newShare);
								float expectedCash = (float) (Math.round(expectedCash2 * 100)) / 100;

								// Compare the calculated share with the shown share
								softassert.assertEquals(share, expectedShare,
										stock[0] + " " + date + " Share is incorrect");

								// Compare the calculated cash with the shown cash
								softassert.assertTrue(Math.abs(expectedCash - cash) <= 0.1,
										stock[0] + " " + date + " Cash is incorrect");

							}
						} else {
							// Convert the read string Split into Float
							float split = Float.parseFloat(splitStr);

							if (dividendInLastRowStr.length() <= 0) {

								// Calculate expected share and convert it into Integer
								float newShareF = (float) ((investment + cashInLastRow) / price);
								int newShare = (int) Math.floor(newShareF);
								int expectedShare = (int) (newShare + shareInLastRow * split);

								// Calculate expected cash and keep it to two decimal places
								float expectedCash2 = (float) (investment + cashInLastRow - price * newShare);
								float expectedCash = (float) (Math.round(expectedCash2 * 100)) / 100;

								// Compare the calculated share with the shown share
								softassert.assertEquals(share, expectedShare,
										stock[0] + " " + date + " Share is incorrect");

								// Compare the calculated cash with the shown cash
								softassert.assertTrue(Math.abs(expectedCash - cash) <= 0.1,
										stock[0] + " " + date + " Cash is incorrect");

							} else {
								float dividendInLastRow = Float.parseFloat(dividendInLastRowStr);

								// Calculate expected share and convert it into Integer
								float newShareF = (float) ((investment + cashInLastRow
										+ dividendInLastRow * shareInLastRow) / price);
								int newShare = (int) Math.floor(newShareF);
								int expectedShare = (int) (newShare + shareInLastRow * split);

								// Calculate expected cash and keep it to two decimal places
								float expectedCash2 = (float) (investment + cashInLastRow
										+ dividendInLastRow * shareInLastRow - price * newShare);
								float expectedCash = (float) (Math.round(expectedCash2 * 100)) / 100;

								// Compare the calculated share with the shown share
								softassert.assertEquals(share, expectedShare,
										stock[0] + " " + date + " Share is incorrect");

								// Compare the calculated cash with the shown cash
								softassert.assertTrue(Math.abs(expectedCash - cash) <= 0.1,
										stock[0] + " " + date + " Cash is incorrect");

							}
						}
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
