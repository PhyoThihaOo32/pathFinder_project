package steps;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.en.Given;
import io.github.bonigarcia.wdm.WebDriverManager;

public class regression {

	WebDriver driver;
	
	
	@Given("I am at the Target HomePage")
	public void i_am_at_the_target_home_page() {
	  
		
		WebDriverManager.chromedriver().setup();		
	    driver = new ChromeDriver();
	    driver.get("https://www.target.com/");
	    driver.manage().window().maximize();
	    driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	    
	    // hi, how are you, i am fine.
	    // how is the weather today?
	    
	    //zzzzz
	    
	    //gfffgg
		
	}

}
