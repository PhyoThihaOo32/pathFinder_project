package io.github.phyothihaoo.pathfinder.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Full coverage, run in parallel.
 *
 * <p>Overriding the data provider with {@code parallel = true} is what actually enables
 * concurrent scenarios; the thread count is set by {@code data-provider-thread-count} in
 * {@code testng-regression.xml}. This is only safe because the driver lives in a ThreadLocal.
 */
@CucumberOptions(
        features = "classpath:features",
        glue = {
                "io.github.phyothihaoo.pathfinder.steps",
                "io.github.phyothihaoo.pathfinder.hooks"
        },
        tags = "@regression",
        monochrome = true,
        plugin = {
                "pretty",
                "html:target/cucumber-reports/regression.html",
                "json:target/cucumber-reports/regression.json"
        }
)
public class RegressionTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
