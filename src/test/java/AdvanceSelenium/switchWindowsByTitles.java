package AdvanceSelenium;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class switchWindowsByTitles {
	
	public static void main(String[]args) {
		
		
		WebDriver driver;
		
		WebDriverManager.chromedriver().setup();		
	    driver = new ChromeDriver();
	    driver.get("https://the-internet.herokuapp.com/windows");
	    driver.manage().window().maximize();
	    driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	    
	    WebElement clickHereButton = driver.findElement(By.linkText("Click Here"));
	    
	    clickHereButton.click();
	    
	    windowByTitles(driver,"New Window");
	    
	    System.out.println(driver.getTitle());
	    
	    
		
	}
	
	public static void windowByTitles(WebDriver driver, String titles) {
		
		String mainWindow = driver.getWindowHandle();
		
		
		Set<String> windows =driver.getWindowHandles();
		
		for(String window : windows) {
			
			if(!driver.switchTo().window(window).getTitle().equals(titles)) {
				
				
				driver.switchTo().window(mainWindow);
				
			}
			
			
		}
		
		
	}

}
