package Automation;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TestAccuWheather {

	static WebDriver driver; 
	@BeforeMethod 
	public void setUp() {
		try {
			System.setProperty("webdriver.chrome.driver", ".\\src\\main\\resources\\Driver\\chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			driver.get("https://www.accuweather.com/");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static double b_getCityWheather(String cityName) {

		try {
			String inputCityName = cityName;
			driver.findElement(By.xpath("//*[@class=\"search-form\"]/input")).click();
			driver.findElement(By.xpath("//*[@class=\"search-form\"]/input")).sendKeys(inputCityName);
			driver.findElement(By.xpath("//*[@class=\"search-results\"]/div[2]/div[1]")).click();
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			// System.out.println(driver.getTitle());
			String todayWheatherUI = driver
					.findElement(
							By.xpath("//div[@class=\"cur-con-weather-card__panel\"]//div[@class=\"temp-container\"]/div[1]"))
					.getText().trim();
			todayWheatherUI = todayWheatherUI.substring(0, 2) + ".00";
			System.out.println(
					"Today's Wheather forecast for " + inputCityName + " from UI is " + todayWheatherUI + " Celcius");
			Double tempUI = new Double(todayWheatherUI);
			return tempUI;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	public double getTempAPI(String cityName, DecimalFormat df) {

		try {
			String inputCityName = cityName;

			Response response = RestAssured.get("http://api.openweathermap.org/data/2.5/weather?q=" + cityName
					+ "&appid=7fe67bf08c80ded756e598d6f8fedaea");
			String jsonArrayStr = response.getBody().asString();
			JSONObject jsonObject = new JSONObject(jsonArrayStr);
			Float tempKelvin = jsonObject.getJSONObject("main").getFloat("temp");
			double tempCelcius = tempKelvin - 273.15;
			tempCelcius = Double.parseDouble(df.format(tempCelcius));
			System.out.println("API Response temperature for " + cityName + " is " + tempCelcius + " Celcius");
			return tempCelcius;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	public void compareTempUIandAPI(String cityName, DecimalFormat df) {
		String inputCityName = cityName;
		try {
			double tempCelciusAPI = getTempAPI(inputCityName, df);
			double tempCelciusUI = b_getCityWheather(inputCityName);

			try {
				Assert.assertEquals(tempCelciusAPI, tempCelciusUI);
			} catch (AssertionError e) {
				// TODO Auto-generated catch block
				System.out.println("The current wheather temprature in Celcius for " + inputCityName
						+ " is not matching between UI :  " + tempCelciusUI + " and API : " + tempCelciusAPI);
				double diff = (tempCelciusAPI > tempCelciusUI) ? tempCelciusAPI - tempCelciusUI
						: tempCelciusUI - tempCelciusAPI;
				System.out.println("difference : " + Double.parseDouble(df.format(diff)));
				if (diff > 1) {
					System.out.println("The Variation in degree celcius is not within accepted limit ");
					throw e;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void compareTempUIandAPIVariance(String cityName, DecimalFormat df) {
		String inputCityName = cityName;
		try {
			double tempCelciusAPI = getTempAPI(inputCityName, df);
			double tempCelciusUI = b_getCityWheather(inputCityName);

			try {
				Assert.assertEquals(tempCelciusAPI, tempCelciusUI);
			} catch (AssertionError e) {
				// TODO Auto-generated catch block
				System.out.println("The current wheather temprature in Celcius for " + inputCityName
						+ " is not matching between UI :  " + tempCelciusUI + " and API : " + tempCelciusAPI);
				double diff = (tempCelciusAPI > tempCelciusUI) ? tempCelciusAPI - tempCelciusUI
						: tempCelciusUI - tempCelciusAPI;
				System.out.println("difference : " + Double.parseDouble(df.format(diff)));
				if (diff > 3) {
					System.out.println("The Variation is more than 1 degree celcius ");
					throw e;
				} else {
					System.out.println("The Variation is within accepted limit, Considering the case as passed ");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
}
