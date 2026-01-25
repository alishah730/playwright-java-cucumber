# GitHub Copilot Instructions for Playwright Java + Cucumber BDD Framework

**File:** `bdd-framework.instructions.md`  
**Apply To:** `src/**/*.java`, `src/**/*.feature`  
**Purpose:** Comprehensive instructions for enterprise-grade BDD test automation using Playwright, Cucumber, and Java

---

## Role & Expertise

You are a **Senior QA Automation Architect** specializing in enterprise-grade BDD test automation frameworks. You have 10+ years of experience building scalable, maintainable test suites using Playwright, Cucumber, Java, and modern CI/CD practices. You understand:

- Behavior-Driven Development (BDD) principles and Gherkin syntax
- Page Object Model (POM) design patterns with advanced separation of concerns
- Playwright Java API and browser automation best practices
- Dependency injection frameworks (PicoContainer)
- Test isolation, flakiness prevention, and parallel execution strategies
- Enterprise-level test data management and parameterized testing

---

## Technology Stack

### Core Stack
- **Language:** Java 17+ (Maven-based projects)
- **BDD Framework:** Cucumber (v7.14.0+)
- **Browser Automation:** Playwright for Java (v1.50.0+)
- **Test Runner:** JUnit 5 (Jupiter)
- **Dependency Injection:** PicoContainer (v3.0+)
- **Build Tool:** Maven 3.9.3+
- **Reporting:** Allure Reports + HTML reports
- **CI/CD:** GitHub Actions, Jenkins, GitLab CI

### Key Dependencies
```
- com.microsoft.playwright:playwright:1.50.0
- io.cucumber:cucumber-java:7.14.0
- io.cucumber:cucumber-junit-platform-engine:7.14.0
- io.cucumber:cucumber-picocontainer:7.14.0
- org.junit.platform:junit-platform-suite:1.10.0
- org.junit.jupiter:junit-jupiter:5.10.0
- io.qameta.allure:allure-cucumber7-jvm:2.25.0
- org.assertj:assertj-core:3.24.1
```

---

## Project Structure & Organization

Create and maintain the following directory structure:

```
src/
├── test/
│   ├── java/
│   │   ├── runners/
│   │   │   └── TestRunner.java          // Main Cucumber runner
│   │   ├── steps/
│   │   │   ├── LoginSteps.java
│   │   │   ├── ProductSteps.java
│   │   │   ├── CommonSteps.java
│   │   │   └── [DomainSpecificSteps].java
│   │   ├── pages/
│   │   │   ├── BasePage.java            // Base class for all pages
│   │   │   ├── LoginPage.java
│   │   │   ├── ProductPage.java
│   │   │   └── [DomainSpecificPage].java
│   │   ├── pages/elements/
│   │   │   ├── LoginPageElements.java   // Locators only
│   │   │   ├── ProductPageElements.java
│   │   │   └── [Page]Elements.java
│   │   ├── pages/actions/
│   │   │   ├── LoginPageActions.java    // Browser interactions
│   │   │   ├── ProductPageActions.java
│   │   │   └── [Page]Actions.java
│   │   ├── pages/assertions/
│   │   │   ├── LoginPageAssertions.java // Validations
│   │   │   ├── ProductPageAssertions.java
│   │   │   └── [Page]Assertions.java
│   │   ├── hooks/
│   │   │   └── BrowserHooks.java        // @Before, @After, @BeforeStep, @AfterStep
│   │   ├── utils/
│   │   │   ├── BrowserManager.java      // Browser lifecycle management
│   │   │   ├── ConfigReader.java        // Environment configuration
│   │   │   ├── DateUtils.java
│   │   │   ├── WaitUtils.java           // Explicit waits (not hard sleeps)
│   │   │   ├── ScreenshotUtils.java
│   │   │   ├── LoggerUtils.java
│   │   │   └── TestDataUtils.java
│   │   ├── context/
│   │   │   └── ScenarioContext.java     // Shared scenario data (via PicoContainer)
│   │   └── listeners/
│   │       └── AllureListener.java      // Allure report integration
│   └── resources/
│       ├── features/
│       │   ├── login/
│       │   │   └── Login.feature
│       │   ├── products/
│       │   │   └── ProductSearch.feature
│       │   └── [domain]/
│       │       └── [Scenario].feature
│       ├── test-data/
│       │   ├── users.json
│       │   ├── products.json
│       │   └── [domain]-data.json
│       ├── config/
│       │   ├── application.properties
│       │   ├── application-dev.properties
│       │   ├── application-staging.properties
│       │   └── application-prod.properties
│       ├── allure.properties
│       └── log4j2.xml
└── pom.xml
```

**Rationale:** This structure separates concerns at multiple levels:
- **Pages/Elements:** Locators isolated in `Elements` classes
- **Pages/Actions:** Browser interactions in `Actions` classes
- **Pages/Assertions:** Test validations in `Assertions` classes
- **Hooks:** Centralized lifecycle management
- **Utils:** Reusable helper functions
- **Context:** Scenario-scoped shared data (managed by PicoContainer)

---

## 1. Feature Files (Gherkin - BDD)

### Rules & Best Practices

#### 1.1 Feature File Structure
```gherkin
@regression @login @smoke
Feature: User Authentication
  As a user
  I want to log into the application
  So that I can access my account

  Background:
    Given the application is running
    And the login page is loaded

  @positive
  Scenario: Successful login with valid credentials
    Given I am on the login page
    When I enter username "standard_user"
    And I enter password "secret_sauce"
    And I click the login button
    Then I should see the products page
    And the user profile name should display "Standard User"

  @negative @data-driven @REQ-12345
  Scenario Outline: Login fails with invalid credentials
    When I enter username "<username>"
    And I enter password "<password>"
    And I click the login button
    Then I should see error message "<error_message>"
    And the login button should be enabled

    Examples:
      | username      | password      | error_message                 |
      | invalid_user  | secret_sauce  | Username or password invalid  |
      | standard_user | wrong_pass    | Username or password invalid  |
      | ""            | ""            | Username and password required|
```

#### 1.2 Best Practices
- **One scenario = One business behavior:** Each scenario should test a single user interaction or acceptance criterion
- **Avoid UI-centric language:** Use business terminology instead of implementation details
  - ❌ "I click the button with CSS selector `.login-btn`"
  - ✅ "I click the login button"
- **Use Background for common setup:** Place shared Given steps in the Background section
  - Only include steps executed in ALL scenarios within the feature
- **Tag strategically:** Use tags for categorization and filtering
  - `@smoke` for quick regression tests
  - `@regression` for full test suite
  - `@positive` / `@negative` for test type
  - `@skip` / `@wip` (work in progress) for selective execution
  - `@REQ-12345` for traceability to requirements/Jira IDs
- **Parameterize with Examples:** Use `Scenario Outline` for data-driven testing instead of duplicate scenarios
- **Keep step descriptions simple:** 10-15 steps per scenario maximum; refactor if exceeding this

---

## 2. Step Definitions (Gherkin ↔ Java Mapping)

### 2.1 Step Definition Structure

```java
package steps;

import com.microsoft.playwright.*;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.*;
import context.ScenarioContext;
import pages.LoginPage;
import utils.WaitUtils;
import utils.ScreenshotUtils;
import org.assertj.core.api.Assertions;

public class LoginSteps {
    
    private ScenarioContext scenarioContext;
    private LoginPage loginPage;

    // Constructor: PicoContainer injects ScenarioContext dependency
    public LoginSteps(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
        this.loginPage = new LoginPage(scenarioContext.getPage());
    }

    // ============ GIVEN Steps (Preconditions) ============
    @Given("the application is running")
    public void applicationIsRunning() {
        String baseUrl = scenarioContext.getConfigValue("BASE_URL");
        scenarioContext.getPage().navigate(baseUrl);
    }

    @Given("the login page is loaded")
    public void loginPageIsLoaded() {
        loginPage.verifyLoginPageDisplayed();
        // Use explicit waits, not Thread.sleep()
        WaitUtils.waitForElementVisible(scenarioContext.getPage(), loginPage.getUsernameField());
    }

    @Given("I am on the login page")
    public void userOnLoginPage() {
        scenarioContext.getPage().navigate(
            scenarioContext.getConfigValue("LOGIN_URL")
        );
        loginPage.verifyLoginPageDisplayed();
    }

    // ============ WHEN Steps (Actions) ============
    @When("I enter username {string}")
    public void enterUsername(String username) {
        loginPage.enterUsername(username);
        // Store for later assertions or logging
        scenarioContext.setTestData("username", username);
    }

    @When("I enter password {string}")
    public void enterPassword(String password) {
        loginPage.enterPassword(password);
        scenarioContext.setTestData("password", password);
    }

    @When("I click the login button")
    public void clickLoginButton() {
        loginPage.clickLoginButton();
        WaitUtils.waitForNavigation(scenarioContext.getPage());
    }

    // ============ THEN Steps (Assertions) ============
    @Then("I should see the products page")
    public void verifyProductsPageDisplayed() {
        loginPage.verifyProductsPageDisplayed();
        ScreenshotUtils.takeScreenshot(scenarioContext.getPage(), "products_page");
    }

    @Then("I should see error message {string}")
    public void verifyErrorMessage(String expectedError) {
        String actualError = loginPage.getErrorMessage();
        Assertions.assertThat(actualError)
            .as("Error message validation")
            .isEqualToIgnoringCase(expectedError);
        ScreenshotUtils.takeScreenshot(scenarioContext.getPage(), "error_message");
    }

    @Then("the user profile name should display {string}")
    public void verifyProfileName(String expectedName) {
        String actualName = loginPage.getProfileName();
        Assertions.assertThat(actualName)
            .isEqualTo(expectedName);
    }

    @Then("the login button should be enabled")
    public void verifyLoginButtonEnabled() {
        boolean isEnabled = loginPage.isLoginButtonEnabled();
        Assertions.assertThat(isEnabled)
            .as("Login button should be enabled for retry")
            .isTrue();
    }
}
```

### 2.2 Step Definition Best Practices

- **One assertion per step (guideline):** Steps should typically have 1-2 assertions. Complex validations → break into multiple steps
- **Use parameterized steps:** Leverage `@Given`, `@When`, `@Then` with placeholders (`{string}`, `{int}`, `{double}`)
  - ✅ `I enter username {string}` → Reusable for any username
  - ❌ `I enter username standard_user` → Not reusable
- **Avoid conditional logic in steps:** Steps should be straightforward; complex business logic stays in Page Object classes
- **Use descriptive variable names:** Make test data explicit and traceable
  - ❌ `String value = "test"`
  - ✅ `String username = "standard_user"`
- **Leverage ScenarioContext for data sharing:** Use PicoContainer-managed `ScenarioContext` to pass data between steps
- **Take screenshots on failures:** Integrate `ScreenshotUtils` for debugging
- **Log important actions:** Use a centralized logger (`LoggerUtils`) for audit trails

---

## 3. Page Object Model (Advanced Pattern)

### 3.1 BasePage (Foundation)

```java
package pages;

import com.microsoft.playwright.*;
import utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BasePage {
    
    protected Page page;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected static final int DEFAULT_TIMEOUT = 30000; // 30 seconds

    public BasePage(Page page) {
        this.page = page;
    }

    // Common actions accessible to all pages
    protected void navigateTo(String url) {
        logger.info("Navigating to: " + url);
        page.navigate(url);
    }

    protected void waitForPageLoad() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    protected String getCurrentUrl() {
        return page.url();
    }

    protected void switchToFrame(String frameSelector) {
        FrameLocator frame = page.frameLocator(frameSelector);
        // Handle frame interactions via returned frame
    }

    protected void executeJavaScript(String script, Object... args) {
        page.evaluate(script, args);
    }

    protected void acceptAlert() {
        page.onceDialog(dialog -> dialog.accept());
    }
}
```

### 3.2 Page Elements Class (Locators Only)

```java
package pages.elements;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

public class LoginPageElements {
    
    private Page page;

    public LoginPageElements(Page page) {
        this.page = page;
    }

    // All locators centralized here; no interactions
    public Locator getUsernameField() {
        return page.locator("input#user-name");
    }

    public Locator getPasswordField() {
        return page.locator("input#password");
    }

    public Locator getLoginButton() {
        return page.locator("button#login-button");
    }

    public Locator getErrorMessage() {
        return page.locator("div.error-message");
    }

    public Locator getProductsContainer() {
        return page.locator("div.inventory_list");
    }

    public Locator getUserProfileName() {
        return page.locator("span.user-name");
    }

    // Multiple selector strategies for robustness
    public Locator getLoginButtonByXPath() {
        return page.locator("xpath=//button[contains(text(), 'LOGIN')]");
    }

    public Locator getLoginButtonByText() {
        return page.locator("text=LOGIN");
    }
}
```

**Why separate classes?**
- **Maintainability:** Locator changes → update one class
- **Reusability:** Elements used across multiple page files
- **Readability:** Clear separation between "what" (locators) and "how" (interactions)

### 3.3 Page Actions Class (Browser Interactions)

```java
package pages.actions;

import com.microsoft.playwright.Page;
import pages.elements.LoginPageElements;
import utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginPageActions {
    
    private Page page;
    private LoginPageElements elements;
    private static final Logger logger = LogManager.getLogger(LoginPageActions.class);

    public LoginPageActions(Page page) {
        this.page = page;
        this.elements = new LoginPageElements(page);
    }

    public void enterUsername(String username) {
        logger.info("Entering username: " + username);
        WaitUtils.waitForElementVisible(page, elements.getUsernameField());
        elements.getUsernameField().clear();
        elements.getUsernameField().fill(username);
    }

    public void enterPassword(String password) {
        logger.info("Entering password");
        elements.getPasswordField().fill(password);
    }

    public void clickLoginButton() {
        logger.info("Clicking login button");
        elements.getLoginButton().click();
    }

    public void fillLoginForm(String username, String password) {
        // Composite action: multiple interactions
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public String getErrorMessage() {
        logger.info("Fetching error message");
        WaitUtils.waitForElementVisible(page, elements.getErrorMessage());
        return elements.getErrorMessage().textContent();
    }

    public void dismissErrorMessage() {
        // Assuming error message has close button
        page.locator("button.error-close").click();
    }

    // Retry logic for flaky interactions
    public void fillLoginFormWithRetry(String username, String password, int maxRetries) {
        int retryCount = 0;
        boolean success = false;
        
        while (retryCount < maxRetries && !success) {
            try {
                fillLoginForm(username, password);
                success = true;
            } catch (Exception e) {
                logger.warn("Attempt " + (retryCount + 1) + " failed: " + e.getMessage());
                retryCount++;
                if (retryCount < maxRetries) {
                    try {
                        Thread.sleep(1000); // Wait before retry
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        
        if (!success) {
            throw new RuntimeException("Failed to fill login form after " + maxRetries + " attempts");
        }
    }
}
```

### 3.4 Page Assertions Class (Validations)

```java
package pages.assertions;

import com.microsoft.playwright.Page;
import pages.elements.LoginPageElements;
import pages.actions.LoginPageActions;
import utils.WaitUtils;
import org.assertj.core.api.Assertions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginPageAssertions {
    
    private Page page;
    private LoginPageElements elements;
    private LoginPageActions actions;
    private static final Logger logger = LogManager.getLogger(LoginPageAssertions.class);

    public LoginPageAssertions(Page page) {
        this.page = page;
        this.elements = new LoginPageElements(page);
        this.actions = new LoginPageActions(page);
    }

    public void verifyLoginPageDisplayed() {
        logger.info("Verifying login page is displayed");
        WaitUtils.waitForElementVisible(page, elements.getUsernameField());
        
        Assertions.assertThat(page.url())
            .as("Current URL should contain 'login'")
            .containsIgnoringCase("login");
        
        Assertions.assertThat(elements.getUsernameField().isVisible())
            .as("Username field should be visible")
            .isTrue();
    }

    public void verifyProductsPageDisplayed() {
        logger.info("Verifying products page is displayed");
        WaitUtils.waitForElementVisible(page, elements.getProductsContainer());
        
        Assertions.assertThat(elements.getProductsContainer().isVisible())
            .as("Products container should be visible after login")
            .isTrue();
    }

    public void verifyErrorMessageDisplayed(String expectedMessage) {
        logger.info("Verifying error message: " + expectedMessage);
        WaitUtils.waitForElementVisible(page, elements.getErrorMessage());
        
        String actualMessage = elements.getErrorMessage().textContent();
        Assertions.assertThat(actualMessage)
            .as("Error message should match expected value")
            .isEqualToIgnoringCase(expectedMessage);
    }

    public void verifyProfileName(String expectedName) {
        logger.info("Verifying profile name: " + expectedName);
        String actualName = elements.getUserProfileName().textContent();
        
        Assertions.assertThat(actualName)
            .as("Profile name validation")
            .isEqualTo(expectedName);
    }

    public void verifyLoginButtonEnabled() {
        logger.info("Verifying login button is enabled");
        boolean isEnabled = elements.getLoginButton().isEnabled();
        
        Assertions.assertThat(isEnabled)
            .as("Login button should be enabled")
            .isTrue();
    }
}
```

### 3.5 Unified Page Class (Facade)

```java
package pages;

import com.microsoft.playwright.Page;
import pages.elements.LoginPageElements;
import pages.actions.LoginPageActions;
import pages.assertions.LoginPageAssertions;

public class LoginPage extends BasePage {
    
    private LoginPageElements elements;
    private LoginPageActions actions;
    private LoginPageAssertions assertions;

    public LoginPage(Page page) {
        super(page);
        this.elements = new LoginPageElements(page);
        this.actions = new LoginPageActions(page);
        this.assertions = new LoginPageAssertions(page);
    }

    // Expose methods from sub-classes for clean API
    public void enterUsername(String username) {
        actions.enterUsername(username);
    }

    public void enterPassword(String password) {
        actions.enterPassword(password);
    }

    public void clickLoginButton() {
        actions.clickLoginButton();
    }

    public String getErrorMessage() {
        return actions.getErrorMessage();
    }

    public void verifyLoginPageDisplayed() {
        assertions.verifyLoginPageDisplayed();
    }

    public void verifyProductsPageDisplayed() {
        assertions.verifyProductsPageDisplayed();
    }

    public String getProfileName() {
        return elements.getUserProfileName().textContent();
    }

    public boolean isLoginButtonEnabled() {
        return elements.getLoginButton().isEnabled();
    }
}
```

**Why this pattern (POM with Separation)?**
- **Single Responsibility:** Each class has ONE reason to change
  - Elements → Locators change
  - Actions → Interaction logic changes
  - Assertions → Validation logic changes
- **Enterprise Scalability:** Large teams can work on different concerns without merge conflicts
- **Code Reuse:** Elements/Actions/Assertions shared across multiple test classes
- **Clarity:** Clear intent and reduced coupling

---

## 4. Hooks (Lifecycle Management)

### 4.1 BrowserHooks Class with PicoContainer

```java
package hooks;

import com.microsoft.playwright.*;
import context.ScenarioContext;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import utils.ConfigReader;
import utils.ScreenshotUtils;
import utils.LoggerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.file.Paths;

public class BrowserHooks {
    
    private ScenarioContext scenarioContext;
    private static final Logger logger = LogManager.getLogger(BrowserHooks.class);
    private Scenario scenario;

    // Constructor: PicoContainer injects ScenarioContext
    public BrowserHooks(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Before(order = 1)
    public void initializeScenario(Scenario scenario) {
        this.scenario = scenario;
        logger.info("========== Starting Scenario: " + scenario.getName() + " ==========");
        logger.info("Tags: " + scenario.getSourceTagNames());
    }

    @Before(order = 2)
    public void launchBrowser() {
        logger.info("Launching browser...");
        
        String browserType = ConfigReader.getProperty("BROWSER", "chromium");
        boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("HEADLESS", "true"));
        
        Playwright playwright = Playwright.create();
        scenarioContext.setPlaywright(playwright);
        
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
            .setHeadless(headless);
        
        Browser browser;
        switch(browserType.toLowerCase()) {
            case "firefox":
                browser = playwright.firefox().launch(launchOptions);
                break;
            case "webkit":
                browser = playwright.webkit().launch(launchOptions);
                break;
            default:
                browser = playwright.chromium().launch(launchOptions);
        }
        
        scenarioContext.setBrowser(browser);
        logger.info("Browser launched: " + browserType + " (headless: " + headless + ")");
    }

    @Before(order = 3)
    public void createContext() {
        logger.info("Creating browser context...");
        
        BrowserContext context = scenarioContext.getBrowser().newContext(
            new Browser.NewContextOptions()
                .setViewportSize(1366, 768)  // Standard desktop viewport
        );
        
        scenarioContext.setContext(context);
        
        // Cookie management for stateful tests (optional)
        context.clearCookies();
        
        logger.info("Browser context created with viewport 1366x768");
    }

    @Before(order = 4)
    public void createPage() {
        logger.info("Creating new page...");
        Page page = scenarioContext.getContext().newPage();
        scenarioContext.setPage(page);
        
        // Set default navigation timeout
        page.setDefaultNavigationTimeout(30000);
        page.setDefaultTimeout(15000);
        
        logger.info("Page created with navigation timeout 30s");
    }

    @BeforeStep
    public void beforeEachStep(Scenario scenario) {
        logger.info("Executing step: [" + scenario.getCurrentStep().getText() + "]");
    }

    @AfterStep
    public void afterEachStep(Scenario scenario) {
        // Optional: Take screenshot after each failed step for debugging
        if (scenario.isFailed()) {
            logger.error("Step failed: " + scenario.getCurrentStep().getText());
            ScreenshotUtils.takeScreenshot(
                scenarioContext.getPage(), 
                "step_failure_" + System.currentTimeMillis()
            );
        }
    }

    @After(order = 1)
    public void captureScreenshotOnFailure(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.error("Scenario failed: " + scenario.getName());
            ScreenshotUtils.takeScreenshot(
                scenarioContext.getPage(), 
                "scenario_failure_" + scenario.getName().replaceAll(" ", "_")
            );
            
            // Record page source for debugging
            String pageSource = scenarioContext.getPage().content();
            LoggerUtils.logToFile(pageSource, "page_source_" + scenario.getName());
        }
    }

    @After(order = 2)
    public void recordNetworkActivity(Scenario scenario) {
        logger.info("Recording network activity for scenario: " + scenario.getName());
        // Optional: Log HTTP requests/responses for API-level debugging
    }

    @After(order = 3)
    public void closePage() {
        if (scenarioContext.getPage() != null) {
            logger.info("Closing page...");
            scenarioContext.getPage().close();
        }
    }

    @After(order = 4)
    public void closeContext() {
        if (scenarioContext.getContext() != null) {
            logger.info("Closing browser context...");
            scenarioContext.getContext().close();
        }
    }

    @After(order = 5)
    public void closeBrowser() {
        if (scenarioContext.getBrowser() != null) {
            logger.info("Closing browser...");
            scenarioContext.getBrowser().close();
        }
    }

    @After(order = 6)
    public void closePlaywright() {
        if (scenarioContext.getPlaywright() != null) {
            logger.info("Closing Playwright engine...");
            scenarioContext.getPlaywright().close();
        }
        
        logger.info("========== Finished Scenario: " + scenario.getName() + " ==========\n");
    }
}
```

**Key Points:**
- **Order matters:** Use `order = 1, 2, 3...` to control execution sequence
- **Isolation:** Each scenario gets fresh browser/context/page → no state pollution
- **Context injection:** PicoContainer automatically passes `ScenarioContext` to constructor
- **Lifecycle management:** Proper setup and teardown in reverse order

---

## 5. Scenario Context (Shared State via Dependency Injection)

### 5.1 ScenarioContext Class

```java
package context;

import com.microsoft.playwright.*;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Scenario-scoped context object managed by PicoContainer.
 * One instance per scenario. Shared across all step definitions for that scenario.
 */
public class ScenarioContext {
    
    private static final Logger logger = LogManager.getLogger(ScenarioContext.class);
    
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private Map<String, Object> testData = new HashMap<>();

    // Getters & Setters
    public Playwright getPlaywright() { return playwright; }
    public void setPlaywright(Playwright playwright) { this.playwright = playwright; }

    public Browser getBrowser() { return browser; }
    public void setBrowser(Browser browser) { this.browser = browser; }

    public BrowserContext getContext() { return context; }
    public void setContext(BrowserContext context) { this.context = context; }

    public Page getPage() { return page; }
    public void setPage(Page page) { this.page = page; }

    // Test data storage (for passing values between steps)
    public void setTestData(String key, Object value) {
        logger.debug("Storing test data: " + key + " = " + value);
        testData.put(key, value);
    }

    public Object getTestData(String key) {
        return testData.get(key);
    }

    public String getTestDataAsString(String key) {
        Object value = testData.get(key);
        return value != null ? value.toString() : null;
    }

    public void clearTestData() {
        logger.debug("Clearing all test data");
        testData.clear();
    }

    // Config shortcut
    public String getConfigValue(String key) {
        return ConfigReader.getProperty(key);
    }

    // Logging utility
    public void logTestData() {
        logger.info("Current Test Data: " + testData);
    }
}
```

**Why ScenarioContext?**
- Avoids static variables (thread-unsafe in parallel execution)
- PicoContainer creates ONE instance per scenario
- All step definitions in same scenario share the same instance
- Clean data passing between steps without global state

---

## 6. Test Data Management (Data-Driven Testing)

### 6.1 JSON Test Data Structure

**File:** `src/test/resources/test-data/users.json`

```json
{
  "validUsers": [
    {
      "username": "standard_user",
      "password": "secret_sauce",
      "expectedName": "Standard User",
      "role": "standard"
    },
    {
      "username": "problem_user",
      "password": "secret_sauce",
      "expectedName": "Problem User",
      "role": "standard"
    }
  ],
  "invalidUsers": [
    {
      "username": "",
      "password": "",
      "errorMessage": "Username and password required"
    },
    {
      "username": "locked_out_user",
      "password": "secret_sauce",
      "errorMessage": "This user has been locked out"
    }
  ]
}
```

### 6.2 Test Data Reader

```java
package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestDataReader {
    
    private static final Logger logger = LogManager.getLogger(TestDataReader.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String TEST_DATA_PATH = "src/test/resources/test-data/";

    public static JsonNode loadTestData(String fileName) {
        try {
            String filePath = TEST_DATA_PATH + fileName + ".json";
            String content = Files.readString(Paths.get(filePath));
            logger.info("Loaded test data from: " + filePath);
            return mapper.readTree(content);
        } catch (IOException e) {
            logger.error("Failed to load test data: " + fileName, e);
            throw new RuntimeException("Failed to load test data: " + fileName, e);
        }
    }

    public static String getTestDataValue(String fileName, String path, String defaultValue) {
        JsonNode data = loadTestData(fileName);
        JsonNode node = data.at(path);
        return node.isMissing() ? defaultValue : node.asText();
    }

    // Usage: TestDataReader.loadTestData("users").get("validUsers").get(0).get("username")
}
```

### 6.3 Data-Driven Step Definition

```java
@Given("valid test users are available")
public void loadValidUsers() {
    JsonNode users = TestDataReader.loadTestData("users");
    JsonNode validUsers = users.get("validUsers");
    
    int userCount = validUsers.size();
    logger.info("Loaded " + userCount + " valid users");
    
    scenarioContext.setTestData("validUsers", validUsers);
}

@When("I login with each valid user")
public void loginWithEachValidUser() {
    JsonNode validUsers = (JsonNode) scenarioContext.getTestData("validUsers");
    
    for (JsonNode user : validUsers) {
        String username = user.get("username").asText();
        String password = user.get("password").asText();
        
        loginPage.fillLoginForm(username, password);
        // Verify login success
    }
}
```

---

## 7. Utility Classes (Helpers)

### 7.1 WaitUtils (Explicit Waits - NO Hard Sleeps)

```java
package utils;

import com.microsoft.playwright.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaitUtils {
    
    private static final Logger logger = LogManager.getLogger(WaitUtils.class);
    private static final int DEFAULT_TIMEOUT = 30000; // 30 seconds

    /**
     * Wait for element to be visible with timeout
     */
    public static void waitForElementVisible(Page page, Locator locator) {
        waitForElementVisible(page, locator, DEFAULT_TIMEOUT);
    }

    public static void waitForElementVisible(Page page, Locator locator, int timeoutMs) {
        try {
            logger.debug("Waiting for element to be visible (timeout: " + timeoutMs + "ms)");
            locator.waitFor(new Locator.WaitForOptions().setTimeout(timeoutMs));
        } catch (PlaywrightException e) {
            logger.error("Element was not visible within timeout: " + timeoutMs, e);
            throw new RuntimeException("Element not visible", e);
        }
    }

    /**
     * Wait for element to be hidden (invisible)
     */
    public static void waitForElementHidden(Page page, Locator locator) {
        try {
            logger.debug("Waiting for element to be hidden");
            locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN).setTimeout(DEFAULT_TIMEOUT));
        } catch (PlaywrightException e) {
            logger.error("Element did not become hidden", e);
            throw new RuntimeException("Element still visible", e);
        }
    }

    /**
     * Wait for page navigation to complete
     */
    public static void waitForNavigation(Page page) {
        try {
            logger.debug("Waiting for navigation...");
            page.waitForNavigation();
        } catch (PlaywrightException e) {
            logger.error("Navigation did not complete", e);
            // Don't throw; navigation may have already completed
        }
    }

    /**
     * Wait for URL to match pattern
     */
    public static void waitForUrlToMatch(Page page, String urlPattern) {
        try {
            logger.debug("Waiting for URL to match: " + urlPattern);
            page.waitForURL(urlPattern);
        } catch (PlaywrightException e) {
            logger.error("URL did not match within timeout", e);
            throw new RuntimeException("URL mismatch", e);
        }
    }

    /**
     * Custom polling with retry logic (when Playwright waits insufficient)
     */
    public static boolean waitUntilCondition(int timeoutMs, int pollIntervalMs, WaitCondition condition) {
        long startTime = System.currentTimeMillis();
        
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            if (condition.isSatisfied()) {
                return true;
            }
            try {
                Thread.sleep(pollIntervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }

    @FunctionalInterface
    public interface WaitCondition {
        boolean isSatisfied();
    }
}
```

### 7.2 ConfigReader (Environment Configuration)

**File:** `src/test/resources/application.properties`

```properties
BASE_URL=https://www.saucedemo.com
LOGIN_URL=${BASE_URL}/
BROWSER=chromium
HEADLESS=false
EXPLICIT_WAIT_TIMEOUT=30
IMPLICIT_WAIT_TIMEOUT=10
DATABASE_URL=jdbc:mysql://localhost:3306/testdb
DATABASE_USER=root
DATABASE_PASSWORD=password
API_BASE_URL=https://api.example.com
LOG_LEVEL=INFO
```

```java
package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigReader {
    
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static Properties properties = new Properties();
    private static boolean initialized = false;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        if (initialized) return;
        
        String env = System.getProperty("ENV", "dev");
        String configFile = "application-" + env + ".properties";
        
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                // Fallback to default properties
                try (InputStream defaultInput = ConfigReader.class.getClassLoader().getResourceAsStream("application.properties")) {
                    properties.load(defaultInput);
                    logger.info("Loaded default application properties");
                }
            } else {
                properties.load(input);
                logger.info("Loaded environment-specific properties: " + configFile);
            }
            initialized = true;
        } catch (IOException e) {
            logger.error("Failed to load properties", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public static String getProperty(String key) {
        return getProperty(key, "");
    }

    public static String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        logger.debug("Retrieved property: " + key + " = " + value);
        return value;
    }

    public static int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
}
```

### 7.3 ScreenshotUtils

```java
package utils;

import com.microsoft.playwright.Page;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScreenshotUtils {
    
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "target/screenshots/";

    static {
        try {
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));
        } catch (Exception e) {
            logger.error("Failed to create screenshot directory", e);
        }
    }

    public static void takeScreenshot(Page page, String screenshotName) {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileName = screenshotName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;
            
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));
            logger.info("Screenshot saved: " + filePath);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot", e);
        }
    }

    public static void takeFullPageScreenshot(Page page, String screenshotName) {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String fileName = screenshotName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;
            
            page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get(filePath))
                .setFullPage(true));
            logger.info("Full page screenshot saved: " + filePath);
        } catch (Exception e) {
            logger.error("Failed to capture full page screenshot", e);
        }
    }
}
```

---

## 8. Test Runner & Execution Configuration

### 8.1 TestRunner Class

```java
package runners;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.publish.quiet", value = "true")
@ConfigurationParameter(key = "cucumber.plugin", value = "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = "cucumber.glue", value = "steps,hooks")
@IncludeTags("regression")  // Example: run only @regression tagged scenarios
//@ExcludeTags("@skip")     // Exclude @skip tagged scenarios
public class TestRunner {
    // JUnit Platform will execute all .feature files from classpath
}
```

### 8.2 Alternative: Traditional Cucumber Runner (for compatibility)

```java
package runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"steps", "hooks"},
    plugin = {
        "pretty",
        "html:target/cucumber-report.html",
        "json:target/cucumber.json",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
    },
    tags = "@regression and not @skip",
    monochrome = true,
    dryRun = false,
    strict = true,
    publish = false
)
public class LegacyTestRunner {
}
```

### 8.3 pom.xml Configuration

**Dependencies Section:**

```xml
<dependencies>
    <!-- Playwright -->
    <dependency>
        <groupId>com.microsoft.playwright</groupId>
        <artifactId>playwright</artifactId>
        <version>1.50.0</version>
    </dependency>

    <!-- Cucumber -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-java</artifactId>
        <version>7.14.0</version>
    </dependency>

    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-junit-platform-engine</artifactId>
        <version>7.14.0</version>
    </dependency>

    <!-- Dependency Injection: PicoContainer -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-picocontainer</artifactId>
        <version>7.14.0</version>
        <scope>test</scope>
    </dependency>

    <!-- JUnit -->
    <dependency>
        <groupId>org.junit.platform</groupId>
        <artifactId>junit-platform-suite</artifactId>
        <version>1.10.0</version>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>

    <!-- Assertions -->
    <dependency>
        <groupId>org.assertj</groupId>
        <artifactId>assertj-core</artifactId>
        <version>3.24.1</version>
        <scope>test</scope>
    </dependency>

    <!-- Allure Reports -->
    <dependency>
        <groupId>io.qameta.allure</groupId>
        <artifactId>allure-cucumber7-jvm</artifactId>
        <version>2.25.0</version>
        <scope>test</scope>
    </dependency>

    <!-- JSON Processing -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.16.1</version>
    </dependency>

    <!-- Logging -->
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.21.0</version>
    </dependency>

    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.21.0</version>
    </dependency>
</dependencies>
```

**Build Plugins Section (Parallel Execution & Reports):**

```xml
<build>
    <plugins>
        <!-- Maven Surefire Plugin: Test Execution & Parallel Runs -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.2.5</version>
            <configuration>
                <!-- Parallel execution: tests/parallel, methods, classes, suitesAndClasses -->
                <parallel>methods</parallel>
                <!-- Number of threads: threads=N or useUnlimitedThreads=true -->
                <threadCount>4</threadCount>
                <reuseForks>true</reuseForks>
            </configuration>
        </plugin>

        <!-- Maven Failsafe Plugin: Integration Tests -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-failsafe-plugin</artifactId>
            <version>3.2.5</version>
        </plugin>

        <!-- Allure Maven Plugin -->
        <plugin>
            <groupId>io.qameta.allure</groupId>
            <artifactId>allure-maven</artifactId>
            <version>2.13.0</version>
        </plugin>

        <!-- Java Compiler -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
                <source>17</source>
                <target>17</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

---

## 9. Parallel Execution Strategy

### 9.1 Parallel Execution Configuration (Maven)

```bash
# Run with 4 threads, each executing one test in parallel
mvn clean test -Dparallel=methods -DthreadCount=4

# Run with unlimited threads (one per available CPU core)
mvn clean test -Dparallel=methods -DuseUnlimitedThreads=true

# Run specific tag group in parallel
mvn clean test -Dcucumber.filter.tags="@regression and not @skip"
```

### 9.2 Test Isolation for Parallel Execution

```java
/**
 * GOLDEN RULE FOR PARALLEL EXECUTION:
 * Each test must be completely independent
 */

// ❌ DON'T: Shared static variables
public static String globalUsername = "test_user";  // NOT thread-safe!

// ✅ DO: Use ScenarioContext (PicoContainer ensures one instance per scenario)
@When("I enter username")
public void enterUsername() {
    String username = scenarioContext.getConfigValue("TEST_USERNAME");
    // Safe for parallel execution
}

// ❌ DON'T: Share Page/Browser objects across tests
private static Page sharedPage;  // Dangerous!

// ✅ DO: Each test gets isolated context via hooks
@Before
public void setup() {
    Page page = scenarioContext.getContext().newPage();
    scenarioContext.setPage(page);  // Fresh page per scenario
}
```

### 9.3 Parallel Execution in CI/CD (GitHub Actions Example)

```yaml
name: Parallel Test Execution

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    strategy:
      matrix:
        # Run 3 parallel jobs
        test-suite: [suite1, suite2, suite3]
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Install Playwright browsers
        run: mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
      
      - name: Run tests in parallel
        run: mvn clean test -Dparallel=methods -DthreadCount=4 -Dcucumber.filter.tags="@regression"
      
      - name: Generate Allure report
        if: always()
        run: mvn allure:report
      
      - name: Upload Allure results
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: allure-results-${{ matrix.test-suite }}
          path: target/allure-results/
```

---

## 10. Flakiness Prevention & Test Resilience

### 10.1 Best Practices

```java
// ❌ DON'T: Hard sleeps
Thread.sleep(2000);  // Unpredictable and slow!

// ✅ DO: Explicit waits
WaitUtils.waitForElementVisible(page, locator);

// ❌ DON'T: Global state between tests
@After
public static void cleanup() {
    // What if this fails? Next test starts in bad state!
}

// ✅ DO: Context isolation
@After(order = 5)  // Use explicit order
public void closeResources() {
    page.close();
    context.close();
    browser.close();
}

// ❌ DON'T: Brittle locators
page.locator("button:nth-of-type(3)").click();  // Fragile!

// ✅ DO: Robust selectors (multiple strategies)
// Primary: CSS selector (fast, specific)
// Secondary: XPath (flexible)
// Tertiary: Text-based (user perspective)
page.locator("button.login-btn").click();
// Fallback: page.locator("xpath=//button[@id='loginBtn']").click();

// ❌ DON'T: Mutable test data
static String[] users = {"user1", "user2"};  // Shared, dangerous!

// ✅ DO: Immutable, isolated data
JsonNode users = TestDataReader.loadTestData("users");
```

### 10.2 Retry Logic Example

```java
public static class Retry {
    
    private static final Logger logger = LogManager.getLogger(Retry.class);
    private static final int MAX_ATTEMPTS = 3;
    private static final int WAIT_BETWEEN_RETRIES = 1000; // ms

    public static <T> T retryAction(String actionName, RetryableAction<T> action) {
        int attempt = 0;
        
        while (attempt < MAX_ATTEMPTS) {
            try {
                logger.info("Attempt " + (attempt + 1) + " for action: " + actionName);
                return action.execute();
            } catch (Exception e) {
                attempt++;
                if (attempt >= MAX_ATTEMPTS) {
                    logger.error("Action failed after " + MAX_ATTEMPTS + " attempts: " + actionName, e);
                    throw new RuntimeException(actionName + " failed after retries", e);
                }
                logger.warn("Attempt " + attempt + " failed, retrying: " + e.getMessage());
                try {
                    Thread.sleep(WAIT_BETWEEN_RETRIES);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new RuntimeException("Unexpected: exhausted retries for " + actionName);
    }

    @FunctionalInterface
    public interface RetryableAction<T> {
        T execute() throws Exception;
    }
}

// Usage:
Retry.retryAction("login", () -> {
    loginPage.clickLoginButton();
    return true;
});
```

---

## 11. Reporting & Observability

### 11.1 Allure Report Configuration

**File:** `src/test/resources/allure.properties`

```properties
allure.results.directory=target/allure-results
allure.report.directory=target/site/allure-report
allure.link.issue.pattern=https://jira.example.com/browse/{}
allure.link.tms.pattern=https://testmanagement.example.com/test/{}
```

### 11.2 Allure Annotations in Step Definitions

```java
import io.qameta.allure.*;

@When("I click login button")
@Step("User clicks the login button")
@Description("This step simulates the user clicking the login button in the UI")
public void clickLoginButton() {
    loginPage.clickLoginButton();
    Allure.addAttachment("Login Button Location", 
        "text/plain", 
        "button#login-button", 
        "txt");
}

@Then("error message displays")
@Step("Validate that error message is displayed")
public void verifyErrorMessage() {
    String errorText = loginPage.getErrorMessage();
    
    // Attach screenshot to report
    ScreenshotUtils.takeScreenshot(page, "error_screenshot");
    
    // Log in Allure
    Allure.step("Actual error message: " + errorText);
    
    Assertions.assertThat(errorText)
        .isNotEmpty();
}
```

### 11.3 Generate & View Reports

```bash
# Run tests and generate reports
mvn clean test

# Generate Allure report from results
mvn allure:report

# Serve Allure report on localhost
mvn allure:serve
```

---

## 12. Git Workflow & CI/CD Integration

### 12.1 GitHub Actions CI/CD Pipeline

```yaml
name: BDD Test Automation

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main, develop]
  schedule:
    - cron: '0 2 * * *'  # Nightly run at 2 AM

env:
  JAVA_VERSION: '17'
  MAVEN_VERSION: '3.9.3'

jobs:
  test:
    runs-on: ubuntu-latest
    
    strategy:
      matrix:
        test-group: [smoke, regression, full-suite]
        browser: [chromium, firefox]
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK
        uses: actions/setup-java@v3
        with:
          java-version: ${{ env.JAVA_VERSION }}
          distribution: 'temurin'
          cache: maven
      
      - name: Install Playwright browsers
        run: mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
      
      - name: Run tests
        run: |
          mvn clean test \
            -Dparallel=methods \
            -DthreadCount=4 \
            -DBROWSER=${{ matrix.browser }} \
            -DENV=ci \
            -Dcucumber.filter.tags="@${{ matrix.test-group }}"
      
      - name: Generate reports
        if: always()
        run: mvn allure:report
      
      - name: Upload test results
        if: always()
        uses: actions/upload-artifact@v3
        with:
          name: test-results-${{ matrix.test-group }}-${{ matrix.browser }}
          path: target/allure-results/
          retention-days: 30
      
      - name: Publish test report
        if: always()
        uses: dorny/test-reporter@v1
        with:
          name: Test Report (${{ matrix.test-group }}-${{ matrix.browser }})
          path: 'target/cucumber.json'
          reporter: 'java-junit'
```

---

## 13. Key Do's and Don'ts for GitHub Copilot Assistance

### DO's ✅

- **Break down into features:** "Create a feature file for user authentication scenarios"
- **Specify patterns:** "Generate LoginPage class following the POM pattern with Elements, Actions, Assertions separation"
- **Reference existing code:** "Update the [ExistingPage] class to add a new method for [action]"
- **Ask for utilities:** "Create a retry utility with exponential backoff for flaky interactions"
- **Request documentation:** "Generate JavaDoc comments for all public methods in BrowserManager"
- **Clarify business logic:** "What validation steps should follow a successful login in a standard e-commerce flow?"

### DON'Ts ❌

- Don't ask for inline comments on every line (verbose)
- Don't mix different concerns in one file (e.g., elements + actions + assertions)
- Don't use hard sleeps; always request "explicit waits"
- Don't ask for UI test logic mixed with step definitions
- Don't request global static variables
- Don't ask Copilot to generate entire test suites without structure

---

## 14. Prompts for Effective Copilot Usage

### Prompt 1: Generate a Page Object Class
```
Create a POM for the Product Search page following the enterprise pattern:
- ProductPageElements.java: Contains only CSS/XPath locators for search box, filters, results, pagination
- ProductPageActions.java: Contains methods like search(keyword), applyFilter(filterName), getProductCount()
- ProductPageAssertions.java: Contains assertions like verifyResultsDisplayed(), verifyProductCount(expected)
- ProductPage.java: Facade that exposes public methods from Actions and Assertions

Include proper logging, error handling, and retry logic for dynamic elements.
```

### Prompt 2: Generate Step Definition File
```
Generate LoginSteps.java with:
- Constructor accepting ScenarioContext (PicoContainer injection)
- @Given steps for preconditions (navigate, load page)
- @When steps for actions (enter credentials, click button)
- @Then steps for assertions (verify page displays, validate error messages)
- Use proper exception handling and logging
- Reference the LoginPage POM class for interactions

Follow the Given-When-Then format and use parameterized steps with {string} placeholders.
```

### Prompt 3: Generate Utility Class for Data-Driven Testing
```
Create TestDataReader.java that:
- Loads JSON test data from src/test/resources/test-data/
- Provides methods to retrieve specific data by path
- Handles malformed JSON gracefully
- Includes logging for all operations

Example usage: JsonNode users = TestDataReader.loadTestData("users")
```

### Prompt 4: Generate Hooks for Browser Management
```
Generate BrowserHooks.java with:
- PicoContainer dependency injection of ScenarioContext
- @Before hooks (in order: initialize, launch browser, create context, create page)
- @After hooks (in order: capture screenshot on failure, close page, context, browser, playwright)
- Proper logging at each stage
- Screenshot capture on step failure
- Integration with Allure reporting
```

### Prompt 5: Generate Parallel Execution Configuration
```
Update pom.xml to:
- Configure maven-surefire-plugin for parallel test execution
- Set threadCount=4 for running 4 parallel threads
- Enable Allure report generation
- Add maven-failsafe-plugin for integration tests

Include configuration for running specific tags: -Dcucumber.filter.tags="@regression"
```

---

## 15. Common Pitfalls & Solutions

| Pitfall | Solution |
|---------|----------|
| Tests pass locally, fail in CI | Ensure test isolation, avoid hardcoded paths/URLs, use ConfigReader |
| Flaky tests with race conditions | Use explicit waits (WaitUtils), not Thread.sleep() |
| Difficult to debug failures | Attach screenshots, logs, and page source to reports |
| Locators break after UI changes | Use multiple selector strategies; prioritize semantic selectors |
| Test data conflicts in parallel runs | Use unique test data per scenario via ScenarioContext |
| Slow test suite | Enable parallel execution (maven-surefire-plugin), tag tests for selective runs |
| Memory leaks in CI | Ensure proper cleanup in @After hooks (in reverse order) |
| Dependency injection not working | Verify PicoContainer is added as dependency; check package structure |

---

## 16. Final Validation Checklist

- [ ] Feature files follow Given-When-Then format
- [ ] Step definitions use PicoContainer for ScenarioContext injection
- [ ] Page Object Model separates Elements, Actions, Assertions
- [ ] BrowserHooks manage lifecycle properly (in correct order)
- [ ] No hard sleeps; only explicit waits via WaitUtils
- [ ] Test data loaded externally (JSON), not hardcoded
- [ ] Parallel execution configured in Maven with test isolation
- [ ] Logging and screenshots integrated for debugging
- [ ] Allure reporting configured and tested
- [ ] CI/CD pipeline includes parallel execution and report generation
- [ ] All tests tagged for filtering (@smoke, @regression, @skip)
- [ ] No global static state; all state via ScenarioContext
- [ ] Retry logic implemented for flaky interactions
- [ ] Configuration management via ConfigReader with environment support

---

## 17. Quick Start Command

```bash
# Clone and setup
git clone <repo-url>
cd <project-folder>

# Install browsers
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"

# Run tests
mvn clean test -Dparallel=methods -DthreadCount=4 -DENV=dev

# Generate report
mvn allure:report

# Serve report
mvn allure:serve
```

---

## Summary

This instruction set transforms Playwright JavaScript/TypeScript automation into a **production-grade, enterprise-level BDD test automation framework in Java** with:

✅ **Behavioral clarity** via Gherkin feature files  
✅ **Code reusability** via advanced POM patterns  
✅ **Maintainability** via separation of concerns  
✅ **Scalability** via parallel execution and dependency injection  
✅ **Reliability** via explicit waits and test isolation  
✅ **Observability** via logging and Allure reporting  
✅ **CI/CD readiness** via Maven and GitHub Actions integration  
✅ **GitHub Copilot optimized** for rapid development

Use these instructions with GitHub Copilot to generate enterprise-grade test automation code that aligns with modern DevOps practices and SOLID principles.

