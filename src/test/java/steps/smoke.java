package steps;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.en.Given;
import io.github.bonigarcia.wdm.WebDriverManager;

public class smoke {
	
	WebDriver driver;
	
	@Given("I am at the Amazon HomePage")
	public void i_am_at_the_amazon_home_page() {
		
		
		WebDriverManager.chromedriver().setup();		
	    driver = new ChromeDriver();
	    driver.get("https://www.amazon.com/");
	    driver.manage().window().maximize();
	    driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	 
		
	}

}
