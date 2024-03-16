package AdvanceSelenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class checkBox {

	
	public static void main(String[]args) {
		
		
		WebDriver driver;
		
		WebDriverManager.chromedriver().setup();		
	    driver = new ChromeDriver();
	    driver.get("https://the-internet.herokuapp.com/checkboxes");
	    driver.manage().window().maximize();
	    driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
	    
	    
	    WebElement cbox1=driver.findElement(By.xpath("(//input[@type='checkbox'])[1]"));
	    
	    WebElement cbox2=driver.findElement(By.xpath("(//input[@type='checkbox'])[2]"));
	    
	    if(!cbox1.isSelected()) {
	    	
	    	cbox1.click();
	    	
	    }
	    
	    else {
	    	
	    	System.out.println("check box 1 is already selected");
	    }
	    
	    if(cbox2.isSelected()) {
	    	
	    	cbox2.click();
	    }
	    
	    
	    
	   
		
		
	}
			
}
