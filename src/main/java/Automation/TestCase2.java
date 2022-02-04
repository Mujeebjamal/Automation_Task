package Automation;
import java.text.DecimalFormat;

import org.testng.annotations.Test;

public class TestCase2 extends TestAccuWheather  {

	String CityName = "Singapore"; // Input City Name needed
	@Test
	public void testcase2 () {
		try {
		final DecimalFormat df = new DecimalFormat("0.00");
		compareTempUIandAPIVariance(CityName, df);
		} catch(Exception e) {
			System.out.println("Some Error occured please check.!!!");
		}
	}
}
