package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(
		
		tags = "@RegressionTest",
		features = "Features" ,
		glue = "steps" 
		
		)

public class regressionTest extends AbstractTestNGCucumberTests {

}
