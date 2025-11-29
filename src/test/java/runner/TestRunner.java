package runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.FEATURES_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "classpath:features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "stepdefinitions")
@ConfigurationParameter(key = "cucumber.plugin", value = "html:target/cucumber-html-reports/index.html,json:target/cucumber-json-reports/cucumber.json,junit:target/cucumber-junit-reports/cucumber.xml")
@ConfigurationParameter(key = "cucumber.publish.quiet", value = "true")
@IncludeTags("test")
public class TestRunner {

}

