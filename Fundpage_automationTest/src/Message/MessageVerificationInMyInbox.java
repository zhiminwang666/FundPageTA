package Message;
/*
 * className: ${MessageVerificationInMyInbox}
 * description: To extract the securities ticker, prices and shift from Alert Message for subscription service users
 * author: Mingzhe Zhang
 * date: ${20211025}
 */

import static org.testng.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test; 

public class MessageVerificationInMyInbox {
	private WebDriver driver;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	// private int[] RandomRowNumber = new int[25];
	private List<String> tickersList = new ArrayList<>();
	private List<String> stockPricesList = new ArrayList<>();
	private List<String> priceShiftList = new ArrayList<>();

	@Parameters("browser")
	@BeforeClass(alwaysRun = true)
	public void setUp(String browser) throws Exception {
		// public void setUp() throws Exception {

		if (browser.equalsIgnoreCase("Firefox")) { //
			System.setProperty("webdriver.firefox.driver", "C:\\Windows\\geckodriver.exe");
			driver = new FirefoxDriver();
			Reporter.log(browser + "started");
		} else if (browser.equalsIgnoreCase("Chrome")) {
			// System.setProperty("webdriver.chrome.driver",
			// "C:\\Windows\\chromedriver.exe");
			driver = new ChromeDriver();
			Reporter.log(browser + "started");
		} else if (browser.equalsIgnoreCase("Edge")) {
			System.setProperty("webdriver.edge.driver", "C:\\Windows\\msedgedriver.exe");
			driver = new EdgeDriver();
			Reporter.log(browser + "started");
		} else if (browser.equalsIgnoreCase("Opera")) { //
			// System.setProperty("webdriver.opera.driver", "C:\\Windows\\operadriver.exe");
			driver = new OperaDriver();
			Reporter.log(browser + "started");
/*		} else if (browser.equalsIgnoreCase("IE")) {
			File file = new File("C:\\Windows\\IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			DesiredCapabilities ieCapabilities = new DesiredCapabilities();
			ieCapabilities.setCapability("nativeEvents", false);
			ieCapabilities.setCapability("unexpectedAlertBehaviour", "accept");
			ieCapabilities.setCapability("ignoreProtectedModeSettings", true);
			ieCapabilities.setCapability("disable-popup-blocking", true);
			ieCapabilities.setCapability("enablePersistentHover", true);
			ieCapabilities.setCapability("ignoreZoomSetting", true);
			driver = new InternetExplorerDriver();
			Reporter.log(browser + "started");*/
		} else {
			throw new Exception("Browser is not correct");
		}
		// driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}

	@Test
	public void login() throws Exception {
		// Get user name and password from keyboard
		driver.get("https://www.fundpage.com/");
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("UserName")).sendKeys("Mingzhe Zhang");
		driver.findElement(By.id("inputPassword")).sendKeys("19810606.sbsb" + Keys.ENTER);
		// Jump to MyInbox
		driver.get("https://www.fundpage.com/Social/ListReceivedMessage");

		String tickerName, stockPriceInUSD, stockPriceValue, stockShifts;

		for (int i = 1; i <= 25; i++) {
			String j = Integer.toString(i);
			String tmpTicker = (String) driver
					.findElement(
							By.xpath("//table[contains(@class,'table mail-table')]/tbody/tr[" + j + "]/td[3]/a[1]"))
					.getText();
			// extract alert MSG title from MyInbox

			tickerName = tmpTicker.substring(0, tmpTicker.indexOf("\s")); // Get ticker name from alert MSG title

			stockPriceInUSD = tmpTicker.substring(tmpTicker.indexOf("$"), tmpTicker.indexOf(")"));
			stockPriceValue = stockPriceInUSD.substring(1);

			stockShifts = tmpTicker.substring(tmpTicker.indexOf("%") - 6, tmpTicker.indexOf("%"));

			// System.out.println(tickerName);
			// System.out.println(stockPriceInUSD);
			// System.out.println(stockPriceValue);
			tickersList.add(tickerName);
			stockPricesList.add(stockPriceInUSD);
			priceShiftList.add(stockShifts);

		}
		System.out.println(tickersList);
		System.out.println(stockPricesList);
		System.out.println(priceShiftList);
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
