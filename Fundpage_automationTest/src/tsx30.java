import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.*;
import org.testng.annotations.Test;


import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class tsx30 {
	private WebDriver driver;

	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeMethod
	@BeforeClass(alwaysRun = true)
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	
	@Test public void GetTSX30() throws Exception {
		List<WebElement> TSX_List;
		driver.get("https://en.wikipedia.org/wiki/S%26P/TSX_60#TSX_60-based_funds");
		TimeUnit.SECONDS.sleep(3);
			 
		
		TSX_List = driver.findElements(By.xpath(
				"//tbody/tr['1-last()']//a[@class='external text']"));
		int ListLength = TSX_List.size();
		String[] TSX60 = new String[ListLength];
		for (int i=1;i<ListLength;i++) {
			TSX60[i]=driver.findElement(By.xpath(
					"//tbody/tr["+(Integer.toString(i))+"]//a[@class='external text']")).getText();
			System.out.println(TSX60[i]);
		}						
	}

	@AfterMethod
	@AfterClass(alwaysRun = true)
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}
}
