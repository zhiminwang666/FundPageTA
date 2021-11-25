package Search;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

// test update conflict
public class SecuritesCompare {
	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	private List<String> TickersList = new ArrayList<String>();
	// private String browser;

	
	@Parameters("browser")
	@BeforeClass(alwaysRun = true)
	public void setUp(String browser) throws Exception {
		// public void setUp() throws Exception {

		if (browser.equalsIgnoreCase("Firefox")) { //
			//System.setProperty("webdriver.firefox.driver", "C:\\Windows\\geckodriver.exe");
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
			Reporter.log(browser + "started"); */
		} else {
			throw new Exception("Browser is not correct");
		}
		// driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}

	public List<String> ToCompareList(int ListLength) {

		if (ListLength != 2) {
			Reporter.log("Number of securities to be compared is invalid!");
		} else {
			TickersList.add("AAPL");
			TickersList.add("TSLA");
		}

		return TickersList;

	}

	@Test
	public void login() throws Exception { // Get user name and password from Keyboard input

		driver.get("https://www.fundpage.com/");
		ToCompareList(2);
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("UserName")).sendKeys("gregyang");
		driver.findElement(By.id("inputPassword")).sendKeys("Abcde@123");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Test(dependsOnMethods = { "login" })
	public void search() throws Exception {

		WebDriverWait wait1 = new WebDriverWait(driver, 15);
		for (String s : TickersList) {

			driver.get("https://www.fundpage.com/Home/SecuritySearch");
			TimeUnit.SECONDS.sleep(2);
			driver.findElement(By.xpath("(//i[@class='entypo-down-open'])[4]")).click();
			driver.findElement(By.xpath("//input[@placeholder='Enter security ticker']")).sendKeys(s + Keys.ENTER);

			try {
				wait1.until(ExpectedConditions.elementToBeClickable(By.linkText("View all detail"))).click();
			} catch (WebDriverException e) {
				Reporter.log(s + " is not found");
			}

			try {
				wait1.until(ExpectedConditions.elementToBeClickable(By.xpath("//h3[@title='Add to your watch list']")))
						.click();
			} catch (WebDriverException e) {
				Reporter.log(s+ "Failed to add to watch list.");
			}
		}
	}

	@Test(dependsOnMethods = { "search" })
	public void compare() throws Exception { // Multiple selection of Securities, and Compare.

		WebDriverWait wait1 = new WebDriverWait(driver, 10);
		System.out.println(TickersList);
		driver.get("https://www.fundpage.com/Funds/MyFavourites");
		TimeUnit.SECONDS.sleep(2);
		for (String s : TickersList) {
			try {
				Reporter.log(s);
				wait1.until(ExpectedConditions.elementToBeClickable(By.xpath(
						"//a[contains(text(), \"(" + s + ")\" )]/ancestor::tr/td[1]//input[@type=\"checkbox\"]")))
						.click();
			} catch (WebDriverException e) {
				Reporter.log("Failed to click the checkbox");
			}
		}
		try {
			wait1.until(ExpectedConditions.elementToBeClickable(By.id("btnCompare"))).click();
		} catch (WebDriverException e) {
			Reporter.log("Can not compare");
		}

		String curUrl = driver.getCurrentUrl();
		while (!curUrl.equals("https://www.fundpage.com/Funds/CompareFunds")) {
			curUrl = driver.getCurrentUrl();
			TimeUnit.SECONDS.sleep(1);
		}
		TimeUnit.SECONDS.sleep(1);
	}

	@Test(dependsOnMethods = { "compare" })
	public void screenShot() throws Exception { // Screen Shot of the Compare Result
		Reporter.log(driver.findElement(By.xpath("//h4[@class='fp-header-margin-bottom']")).getText());		
		Reporter.log(driver.findElement(By.xpath("(//blockquote[@class='blockquote-blue']//span)[1]")).getText());
		Reporter.log(driver.findElement(By.xpath("(//blockquote[@class='blockquote-blue']//strong)[1]")).getText());
		Reporter.log(driver.findElement(By.xpath("(//blockquote[@class='blockquote-blue']//strong)[2]")).getText());
		Reporter.log(driver.findElement(By.xpath("(//blockquote[@class='blockquote-blue']//span)[2]")).getText());
		Reporter.log(driver.findElement(By.xpath("(//blockquote[@class='blockquote-blue']//strong)[3]")).getText());
		Reporter.log(driver.findElement(By.xpath("(//blockquote[@class='blockquote-blue']//strong)[4]")).getText());
		/*try {
			driver.manage().window().maximize();
			String titleofResult = driver.findElement(By.xpath("//h4[@class='fp-header-margin-bottom']")).getText();
			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String fileName = "Compare-" + titleofResult + new Date().getTime() + ".jpg";
			FileHandler.copy(srcFile, new File("C:\\screenShot\\" + fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	@Test(dependsOnMethods = { "screenShot" })
	public void remove() throws Exception {
		//driver.get("https://www.fundpage.com/Funds/MyFavourites");
		//TimeUnit.SECONDS.sleep(2);
		WebDriverWait wait1 = new WebDriverWait(driver, 10);
		WebElement del;
		for (String s : TickersList) {
			driver.get("https://www.fundpage.com/Funds/MyFavourites");
			TimeUnit.SECONDS.sleep(2);
			del = driver.findElement(By.xpath("//tbody//a[contains(text(),\"(" + s
						+ ")\")]/ancestor::tr/td[6]//img[@title=\"Remove from favorite list\"]"));
			try {
				wait1.until(ExpectedConditions.elementToBeClickable(del)).click();
				TimeUnit.MICROSECONDS.sleep(500);
			} catch (WebDriverException e) {
				Reporter.log("Failed to delete");
			}
		}
		
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		driver.get("https://www.fundpage.com/Account/LogOff");
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}
}
