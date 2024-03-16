package AdvanceSelenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class dragAndDropPractice {
	
	public static void main(String[]args) {
		
		//dragAndDrop(from , to )
		//dragAndDroopBy(element you want to drop , , )
		
		WebDriver driver;
		
		WebDriverManager.chromedriver().setup();		
	    driver = new ChromeDriver();
	    driver.get("https://the-internet.herokuapp.com/drag_and_drop");
	    driver.manage().window().maximize();
	    driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	    
	    WebElement columnA= driver.findElement(By.xpath("//div[@id='column-a']"));
	    
	    WebElement columnB= driver.findElement(By.xpath("//div[@id='column-b']"));
	    
	    Actions action = new Actions(driver);
	    
	   action.dragAndDrop(columnA, columnB).build().perform();
	   
	   action.dragAndDropBy(columnB, 100, 200).build().perform();	}
	
	

}
