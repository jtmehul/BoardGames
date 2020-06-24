package testCases;

import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pageObjects.GameCollectionsPage;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.gson.JsonArray;

import base.Base;

public class TestCase_001 extends Base{

	public WebDriver driver;
	public ExtentReports extent;
	public ExtentSparkReporter reporter;
	public String sample="";
	String check="";
	int vote, size;
	public WebElement element;
	
	@BeforeTest
	public void launchBrowser() throws IOException{
		driver = initializeDriver();
		String path = System.getProperty("user.dir")+"\\reports\\index.html";
		reporter = new ExtentSparkReporter(path);
		reporter.config().setReportName("Web Automation Reports");
		reporter.config().setDocumentTitle("Web Automation Testing");
		extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("QA","Mehul Thakar");	
		
	}
	@Test(priority=0)
	public void webActivity(){
        extent.createTest("Web Activity Demo");
        WebDriverWait wait = new WebDriverWait(driver, 10);
        
        GameCollectionsPage gcp = new GameCollectionsPage(driver);
        gcp.getSelectGame().click();
		element = gcp.getLaunguageDepPoll();
		wait.until(ExpectedConditions.visibilityOfElementLocated(gcp.languagePoll));
		sample = element.getText();
		
		
	}
	@Test(priority=1,dataProvider="getData")
	public void getGameCollection(String result,String idPoll) throws InterruptedException{
		extent.createTest("Game Collection Demo");
	
		RestAssured.baseURI ="https://boardgamegeek.com/";	
		String gameData = given().log().all().queryParam("action", result).queryParam("pollid", idPoll).
		header("Accept", "application/json").
		when().get("geekpoll.php").
		then().assertThat().log().all().statusCode(200).extract().response().asString();
     
		JsonPath jp = new JsonPath(gameData);
		size = jp.getInt("pollquestions[0].results.results.size()");
	
		for(int i=0;i<size;i++){
			vote = jp.getInt("pollquestions[0].results.results["+i+"].votes");
			System.out.println(vote);
			if(vote>0){
				check = jp.getString("pollquestions[0].results.results["+i+"].columnbody");
			}
		}
		Assert.assertEquals(check, sample);	
	}
	@AfterTest
	public void closeBrowser(){
		extent.flush();
		driver.quit();
	}
	@DataProvider 
	public Object[][] getData(){
		return new Object [][]{
				{ "results", "425103"},
				{"results","423976"}
				};
	}
}
