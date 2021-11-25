package Search;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.testng.Reporter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
import org.openqa.selenium.WebDriver;

public class SecuritiesSearch {
	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	private int[] RandomRowNumber = new int[5];
	private List<String> TickersList = new ArrayList<String>();

	@BeforeClass
	@Parameters("browser")
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
			/*} else if (browser.equalsIgnoreCase("IE")) {
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

	@Test(description = "Prepare randam securites list from NASDAQ100")
	public void login() throws Exception { // Get user name and password from Keyboard input
		driver.get("https://en.wikipedia.org/wiki/Nasdaq-100");
		TimeUnit.MICROSECONDS.sleep(500);
		List<Integer> myList = new ArrayList<Integer>();
		Random rd = new Random();
		while (myList.size() < 3) {
			int num = rd.nextInt(101);
			if (!myList.contains(num)) {
				myList.add(num);
			}
		}
		for (int i = 0; i < myList.size(); i++) {
			RandomRowNumber[i] = (Integer) (myList.get(i));
		}

		String tmpTicker;
		for (int i = 0; i <= 2; i++) {
			tmpTicker = driver.findElement(By.xpath(
					"//table[@id='constituents']/tbody[1]/tr[" + Integer.toString(RandomRowNumber[i]) + "]/td[2]"))
					.getText();
			TickersList.add(tmpTicker);

		}
		Reporter.log(TickersList + " to be searched");
		driver.get("https://www.fundpage.com/");
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("UserName")).sendKeys("gregyang");
		driver.findElement(By.id("inputPassword")).sendKeys("Abcde@123" + Keys.ENTER);

	}

	@Test(dependsOnMethods = "login", description = "Search Securites")
	public void search() throws Exception {

		int countFound = 0;
		List<String> noFound = new ArrayList<String>();
		driver.get("https://www.fundpage.com/Home/SecuritySearch");
		TimeUnit.MICROSECONDS.sleep(500);
		WebDriverWait wait1 = new WebDriverWait(driver, 5);
		WebDriverWait wait2 = new WebDriverWait(driver, 3);
		int i1 = 0;
		for (String s : TickersList) {

			try {
				Reporter.log("Trying to search " + s);
				wait1.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Search Securities']")))
						.click();
			} catch (WebDriverException e) {
			}

			try {
				wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("(//i[@class='entypo-down-open'])[4]")))
						.click();
			} catch (WebDriverException e) {
			}

			try {
				driver.findElement(By.xpath("//input[@placeholder='Enter security ticker']")).sendKeys(Keys.CONTROL,
						"a");
				driver.findElement(By.xpath("//input[@placeholder='Enter security ticker']")).sendKeys(s + Keys.ENTER);
				i1++;
			} catch (WebDriverException e) {
			}
			try {
				wait2.until(ExpectedConditions.elementToBeClickable(By.linkText("View all detail"))).click();
				countFound++;
				TimeUnit.SECONDS.sleep(2);
				String stckName = driver.findElement(By.xpath("//h3[@class='fp-header-margin-bottom']")).getText();
				Reporter.log(stckName + " is found");
				driver.navigate().back();
			} catch (WebDriverException e) {
				Reporter.log(s + " is not found!");
				noFound.add(s);
			}
			System.gc();
			Reporter.log(countFound + "/" + i1 + " found");
			driver.get("https://www.fundpage.com/Home/SecuritySearch");
		}
		// assertTrue(noFound.equals(null));
		if (noFound.size() == 0) {
			Reporter.log("All securites have been found.");
		} else if (noFound.size() == 1) {
			for (String nf : noFound) {
				Reporter.log(nf + "  ");
			}
			Reporter.log(" is not found");
		} else {
			for (String nf : noFound) {
				Reporter.log(nf + "  ");
			}
			Reporter.log(" are not found");
		}
	}

	@Test(dependsOnMethods = "search", alwaysRun = true)
	public void logout() throws Exception {
		driver.get("https://www.fundpage.com/Account/LogOff");
		driver.quit();
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {

		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			AssertJUnit.fail(verificationErrorString);
		}
	}
}