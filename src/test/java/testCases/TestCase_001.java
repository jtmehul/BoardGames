package testCases;

import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import base.Base;

public class TestCase_001 extends Base{

	public WebDriver driver;
	public ExtentReports extent;
	public ExtentSparkReporter reporter;
	
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
	public void getGameCollection() throws InterruptedException{
		extent.createTest("Game Collection Demo");
		driver.findElement(By.cssSelector("#results_objectname1")).click();
	}
	@Test(priority=1, dataProvider="getData")
	public void getAPI(String city, String api){	
		extent.createTest("API Testing");
		RestAssured.baseURI ="http://api.openweathermap.org/";	
		String invalidAPIkey = given().log().all().queryParam("q", city).
		when().get("data/3.0/stations").
		then().assertThat().log().all().statusCode(401).extract().response().asString();
		
		JsonPath jp = new JsonPath(invalidAPIkey);
		String respCode = jp.getString("cod");
		String respMessage = jp.getString("message");
		System.out.println( "Response Code is "+respCode + " and " + respMessage);		
	}
	@AfterTest
	public void closeBrowser(){
		extent.flush();
		driver.quit();
	}
	@DataProvider 
	public Object[][] getData(){
		return new Object [][]{
				{ "pune", "e686306e2c24a6ab1821bf94ed870e29" }};
	}
	
}
