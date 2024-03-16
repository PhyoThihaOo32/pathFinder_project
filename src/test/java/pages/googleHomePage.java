package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

//Page Object Model - is a concept of centralizing all the web element locators and their actions
//so that we can call the common action between different test classes

public class googleHomePage {
	
	WebDriver driver;

	public googleHomePage(WebDriver dr) {
		
		driver = dr ;
		PageFactory.initElements(driver, this);
		
	}
	
	@FindBy(className= "gLFyf")
	WebElement searchBar;
	
	@FindBy(name="btnK")
	WebElement searchButton;
	
	@FindBy(id="result-stats")
	WebElement searchResult;
	
	public void enterProductNameInSearchBar(String product) {
		
		searchBar.sendKeys(product);
		
	}
	
	public void clickonSearchButton() {
		
		searchButton.submit();
	}
	
	public boolean verifySearchResult() {
		
		return searchResult.isDisplayed();
	}
	
}
