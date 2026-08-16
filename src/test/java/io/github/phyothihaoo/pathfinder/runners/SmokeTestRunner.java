package io.github.phyothihaoo.pathfinder.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Fast confidence check: the critical path only. Intended to gate every push.
 */
@CucumberOptions(
        features = "classpath:features",
        glue = {
                "io.github.phyothihaoo.pathfinder.steps",
                "io.github.phyothihaoo.pathfinder.hooks"
        },
        tags = "@smoke",
        monochrome = true,
        plugin = {
                "pretty",
                "html:target/cucumber-reports/smoke.html",
                "json:target/cucumber-reports/smoke.json"
        }
)
public class SmokeTestRunner extends AbstractTestNGCucumberTests {
}
