package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GameCollectionsPage {

	public WebDriver driver;
	public GameCollectionsPage(WebDriver driver){
		this.driver = driver;
	}
	
	private By selectGame = By.xpath("//div[@id='results_objectname1']/a");
	public By languagePoll = By.xpath("//div[@class='feature-description']/span/span");
	
	
	public WebElement getSelectGame(){
		return driver.findElement(selectGame);
	}
	public WebElement getLaunguageDepPoll()
	{
		return driver.findElement(languagePoll);
	}
}
