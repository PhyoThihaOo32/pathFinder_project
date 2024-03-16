package steps;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import common.googleBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import pages.googleHomePage;

//webdriver is interface which inside the selenium library
//webdriver manager itself is a library which will give us all the browers

public class searchProducts extends googleBase {
	
googleHomePage gp;

	@Given("i am on the google homepage")
	public void i_am_on_the_google_homepage() {
		
		launchBrowser();
		
		
		
	}

	@When("i enter the {string} in the search bar")
	public void i_enter_the_in_the_search_bar(String string) {
		
		gp = new googleHomePage(driver);
		gp.enterProductNameInSearchBar(string);
		
//		WebElement searchBar = driver.findElement(By.className("gLFyf"));
//		searchBar.sendKeys(string);
//		//sendkeys - enter text/upload a file
//	  
		
	}

	@When("i click on the search button")
	public void i_click_on_the_search_button() {
		
		gp.clickonSearchButton();
		
//		WebElement searchButton = driver.findElement(By.name("btnK"));
//	    
//		searchButton.click();
//		
	}

	@Then("i can see the search result successfully")
	public void i_can_see_the_search_result_successfully() {
		
//		WebElement searchResult = driver.findElement(By.id("result-stats"));
//		
//		Assert.assertTrue(searchResult.isDisplayed());
		
		Assert.assertTrue(gp.verifySearchResult());
		
		closeAll();
	    
		
//		Assert.assertEquals("Expected", searchResult.getText());
//		
//		Assert.assertNotEquals("Expected", searchResult.getText());
//		
//		Assert.assertFalse(searchResult.isDisplayed());
//		
//		Assert.assertNotNull(searchResult);
		
	}

	//Assertation
	//Expected Result
	//Actual Result
	//1.Assert.assertEqual
	//2.Assert.assertTrue
	//3.Assert.assertFalse


}
