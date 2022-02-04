package Automation;
import java.text.DecimalFormat;

import org.testng.annotations.Test;

public class TestCase1 extends TestAccuWheather  {

	String CityName = "Singapore"; // Input City Name needed
	@Test
	public void testcase1 () {
		try {
		final DecimalFormat df = new DecimalFormat("0.00");
		compareTempUIandAPI(CityName, df);
		} catch(Exception e) {
			System.out.println("Some Error occured please check.!!!");
		}
	}
}
