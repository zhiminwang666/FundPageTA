package test.ui.WealthFormula;

/**
 * @className: ${OnceAtTheBeginningInvestmentAndTotal}
 * @description: Validate the data in column Investment and Investment Total of the table Detail - Invest Once At The Beginning
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

public class OnceAtTheBeginningInvestmentAndTotal {
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
			System.out.println("Wealth Formula Once At The Beginning Investment and Investment Total Test");
			System.out.println(stock_info);
			Reporter.log("Wealth Formula Once At The Beginning Investment and Investment Total Test ");
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

					// Count the number of rows in the table Detail - Invest Yearly
					List<WebElement> rowsInYearly = driver.findElements(By.xpath(
							"//div[text()='Detail - Invest Yearly']/parent::div/following-sibling::div/table/tbody/tr"));
					int countInYearly = rowsInYearly.size();

					// read the Investment Total value from the last line of the table Detail -
					// Invest Yearly
					String yearlyInvestmentTotalStr = driver.findElement(By.xpath(
							"//div[text()='Detail - Invest Yearly']/parent::div/following-sibling::div/table/tbody/tr["
									+ countInYearly + "]/td[8]"))
							.getText();
					int yearlyInvestmentTotal = Integer.parseInt(yearlyInvestmentTotalStr);

					// read the first line data in the table Detail - Invest Once at the Beginning

					String date1 = driver.findElement(By.xpath(
							"//div[text()='Detail - Invest Once at the Beginning']/parent::div/following-sibling::div/table/tbody/tr/td"))
							.getText();
					String investment1Str = driver.findElement(By.xpath(
							"//div[text()='Detail - Invest Once at the Beginning']/parent::div/following-sibling::div/table/tbody/tr/td[7]"))
							.getText();
					String investmentTotal1Str = driver.findElement(By.xpath(
							"//div[text()='Detail - Invest Once at the Beginning']/parent::div/following-sibling::div/table/tbody/tr/td[8]"))
							.getText();
					int investmentTotal1 = Integer.parseInt(investmentTotal1Str);
					int investment1 = Integer.parseInt(investment1Str);

					// Compare the calculated Investment with the shown Investment
					softassert.assertEquals(investment1, yearlyInvestmentTotal,
							stock[0] + " " + date1 + " Investment is incorrect");

					// Compare the calculated Investment Total with the shown Investment Total
					softassert.assertEquals(investmentTotal1, investment1,
							stock[0] + " " + date1 + " Investment Total is incorrect");

					// Count the number of rows in the table Detail - Invest Once At The Beginning
					List<WebElement> rows = driver.findElements(By.xpath(
							"//div[text()='Detail - Invest Once at the Beginning']/parent::div/following-sibling::div/table/tbody/tr"));
					int count = rows.size();

					for (int i = 2; i <= count; i++) {
						// read the data of following lines in the table Detail - Invest Once At the
						// Beginning
						String date = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Once at the Beginning']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td"))
								.getText();
						String investmentStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Once at the Beginning']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td[7]"))
								.getText();
						String investmentTotalStr = driver.findElement(By.xpath(
								"//div[text()='Detail - Invest Once at the Beginning']/parent::div/following-sibling::div/table/tbody/tr["
										+ i + "]/td[8]"))
								.getText();

						// Convert the read strings into Float or Integer

						int investmentTotal = Integer.parseInt(investmentTotalStr);
						int investment = Integer.parseInt(investmentStr);

						// Compare the calculated Investment with the shown Investment
						softassert.assertEquals(investment, 0, stock[0] + " " + date + " Investment is incorrect");

						// Compare the calculated Investment Total with the shown Investment Total
						softassert.assertEquals(investmentTotal, investmentTotal1,
								stock[0] + " " + date + " Investment Total is incorrect");
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
