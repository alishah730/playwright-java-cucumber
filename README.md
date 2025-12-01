# Playwright Java Cucumber Automation Framework 🎭

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![Playwright](https://img.shields.io/badge/Playwright-1.56.0-green.svg)](https://playwright.dev/java/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.32.0-brightgreen.svg)](https://cucumber.io/)
[![JUnit](https://img.shields.io/badge/JUnit-5-red.svg)](https://junit.org/junit5/)

A modern, production-ready test automation framework combining **Microsoft Playwright** for browser automation with **Cucumber BDD** for readable test scenarios. Built with Java 17, Maven, and JUnit Platform Suite Engine.

## 🚀 Features

- ⚡ **Fast & Reliable**: Playwright's auto-wait and resilient selectors
- 📝 **BDD Approach**: Cucumber for business-readable test scenarios  
- 🎯 **Cross-Browser**: Support for Chromium, Firefox, and WebKit
- 🚀 **Parallel Execution**: Advanced configurable parallel test execution with isolated browser contexts
- 📊 **Rich Reporting**: HTML, JSON, and XML test reports with automatic screenshots
- 🏗️ **Page Object Model**: Maintainable test architecture
- 🔧 **Modern Stack**: Java 17, JUnit 5 Platform Suite, Maven 3.9+
- 🏷️ **Test Tagging**: Run specific test suites with Cucumber tags
- 🐳 **CI/CD Ready**: Headless execution for continuous integration
- 📸 **Auto Screenshots**: Automatic screenshot capture on test failures
- 🔄 **Thread Safety**: Complete isolation between parallel tests (no shared state, cookies, or cache)

## 📋 Prerequisites

- **Java 17+** (Oracle JDK or OpenJDK)
- **Maven 3.6+**
- **Git** (for cloning the repository)

## 🛠️ Quick Setup

### 1. Clone the Repository

```bash
git clone https://github.com/alishah730/playwright-java-cucumber.git
cd playwright-java-cucumber
```

### 2. Install Dependencies

```bash
mvn clean compile
```

### 3. Install Playwright Browsers

```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

### 4. Run Tests

```bash
# Run all tests (all scenarios tagged with @test)
mvn clean test

# Run specific tagged tests
mvn clean test -Dgroups="SmokeTest"
mvn clean test -Dgroups="End2End"
```

## ⚡ Parallel Execution

This framework supports **advanced parallel execution** with **isolated browser contexts** for faster test execution without interference.

### 🔧 Configuration Options

```bash
# Run tests with custom parallel thread count (default: 5)
mvn test -Dparallel.thread.count=3
mvn test -Dparallel.thread.count=10

# Each thread gets its own isolated browser context
# No shared cookies, cache, or session data between tests
```

### 🛡️ Isolation Features

- **Thread-Safe Browser Contexts**: Each test thread gets its own isolated Playwright browser context
- **No State Interference**: Tests run completely independently with no shared data
- **Thread-Safe Screenshots**: Automatic screenshot capture with thread-specific naming
- **Resource Management**: Proper cleanup of browser resources after parallel execution
- **Configurable Parallelism**: Easily adjust thread count based on your system capabilities

### 📊 Parallel Execution Benefits

- **Faster Test Execution**: Run multiple tests simultaneously
- **Better Resource Utilization**: Leverage multi-core systems effectively
- **Scalable**: Configure thread count based on your CI/CD environment
- **Reliable**: Complete isolation prevents test interdependencies and flaky tests

### 🏗️ Technical Architecture

**BrowserContextManager.java**
- Thread-safe browser context management using `ThreadLocal<BrowserContext>`
- Isolated browser instances per thread with `ConcurrentHashMap` for tracking
- Automatic resource cleanup and context disposal
- Thread-specific screenshot capture with unique naming

**Maven Configuration**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <parallel>all</parallel>
        <threadCount>${parallel.thread.count}</threadCount>
        <useUnlimitedThreads>false</useUnlimitedThreads>
        <forkCount>1</forkCount>
        <reuseForks>true</reuseForks>
    </configuration>
</plugin>
```

**Cucumber Configuration**
```properties
cucumber.execution.parallel.enabled=true
cucumber.execution.parallel.mode.default=concurrent
cucumber.execution.parallel.config.strategy=dynamic
```

## 📁 Project Structure

```text
src/
├── test/
│   ├── java/
│   │   ├── pages/                    # Page Object Model classes
│   │   │   ├── BasePage.java         # Base page with common functionality
│   │   │   ├── LoginPage.java        # Login page objects and actions
│   │   │   ├── ItemsPage.java        # Products page objects and actions
│   │   │   └── CheckoutPage.java     # Checkout flow objects and actions
│   │   ├── runner/
│   │   │   └── TestRunner.java       # JUnit Platform Suite test runner
│   │   └── stepdefinitions/
│   │       └── steps.java            # Cucumber step definitions
│   └── resources/
│       ├── features/                 # Cucumber feature files
│       │   ├── Login.feature         # Login functionality scenarios
│       │   └── BuyProduct.feature    # E2E purchase scenarios
│       └── cucumber.properties       # Cucumber configuration
├── target/                           # Generated reports and artifacts
│   ├── cucumber-html-reports/        # Interactive HTML reports
│   ├── cucumber-json-reports/        # JSON format reports
│   └── cucumber-junit-reports/       # JUnit XML reports
└── pom.xml                          # Maven configuration and dependencies
```

## 🔧 Configuration

### Maven Dependencies (Latest Versions)

```xml
<dependencies>
    <!-- Playwright for browser automation -->
    <dependency>
        <groupId>com.microsoft.playwright</groupId>
        <artifactId>playwright</artifactId>
        <version>1.56.0</version>
    </dependency>
    
    <!-- Cucumber BDD framework -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-java</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- JUnit 5 Platform Suite for test execution -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-junit-platform-engine</artifactId>
        <scope>test</scope>
    </dependency>
    
    <dependency>
        <groupId>org.junit.platform</groupId>
        <artifactId>junit-platform-suite</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### System Properties Configuration

Configure browser and application settings in `pom.xml`:

```xml
<systemPropertyVariables>
    <browser>Chromium</browser>  <!-- Chromium, Firefox, or Webkit -->
    <applicationUrl>https://www.saucedemo.com/</applicationUrl>
</systemPropertyVariables>
```

## 🎯 Test Execution

### Command Line Options

| Command | Description |
|---------|-------------|
| `mvn clean test` | Run all test scenarios (tagged with @test) |
| `mvn test -Dgroups="SmokeTest"` | Run smoke tests only |
| `mvn test -Dgroups="End2End"` | Run end-to-end tests |
| `mvn test -Dbrowser=Firefox` | Run tests in Firefox browser |
| `mvn test -DapplicationUrl=https://staging.example.com` | Test against staging environment |

### Browser Configuration

Set browser type via system property:

- `Chromium` (default)
- `Firefox`
- `Webkit` (Safari engine)

### Headless Execution

For CI/CD environments, tests run in headless mode by default. To run with visible browser:

```java
// In BasePage.java, modify LaunchOptions
browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false));
```

## 📊 Test Reports & Screenshots

After test execution, reports are generated in multiple formats, with automatic screenshot capture for failed scenarios:

### 📈 HTML Reports

- **Location**: `target/cucumber-html-reports/index.html`
- **Features**: Interactive, filterable, with screenshots and step details
- **Best for**: Manual review and stakeholder sharing

### 📋 JSON Reports

- **Location**: `target/cucumber-json-reports/cucumber.json`
- **Features**: Structured data format
- **Best for**: CI/CD integration and custom reporting tools

### 📄 JUnit XML Reports

- **Location**: `target/cucumber-junit-reports/cucumber.xml`
- **Features**: Standard JUnit format
- **Best for**: CI/CD systems (Jenkins, GitHub Actions, etc.)

### 📸 Screenshot Capture

- **Location**: `target/screenshots/`
- **Features**: Automatic screenshot capture on test failure with timestamp
- **Integration**: Screenshots are automatically attached to HTML and JSON reports
- **Naming**: `failed_<ScenarioName>_<Timestamp>.png`

## 🏷️ Test Tags & Organization

Use Cucumber tags to organize and execute specific test suites:

```gherkin
@SmokeTest @test
Scenario: Login with valid credentials
  Given User launched SwagLabs application
  When User logged in the app using username "standard_user" and password "secret_sauce"
  Then user should be able to log in

@End2End @test  
Scenario: Complete purchase flow
  Given User launched SwagLabs application
  When User logged in and adds product to cart
  Then User should complete the purchase successfully
```

## 🎭 Playwright Code Generation

Generate test code automatically using Playwright's codegen tool:

```bash
# Generate code for a specific workflow
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="codegen https://www.saucedemo.com/"

# Generate code with specific browser
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="codegen --browser firefox https://example.com"
```

This opens a browser where you can interact with the application. Playwright records your actions and generates the corresponding Java code.

## 🏗️ Writing Tests

### 1. Create Feature File

```gherkin
# src/test/resources/features/NewFeature.feature
Feature: New Feature Testing

  @test @NewFeature
  Scenario: Test new functionality
    Given User is on the application homepage
    When User performs some action
    Then Expected result should be displayed
```

### 2. Implement Step Definitions

```java
// In src/test/java/stepdefinitions/steps.java
@Given("User is on the application homepage")
public void user_is_on_homepage() {
    // Implementation using page objects
}

@When("User performs some action") 
public void user_performs_action() {
    // Implementation using page objects
}

@Then("Expected result should be displayed")
public void expected_result_displayed() {
    // Assertions using page objects
}
```

### 3. Create Page Objects

```java
// src/test/java/pages/NewPage.java
public class NewPage extends BasePage {
    private Page page;
    
    public NewPage(Page page) {
        this.page = page;
    }
    
    public void performAction() {
        page.click("selector");
    }
    
    public boolean isResultDisplayed() {
        return page.isVisible("result-selector");
    }
}
```

## 🔍 Best Practices

### Test Design

- ✅ Use descriptive scenario names
- ✅ Keep scenarios focused and independent  
- ✅ Use Page Object Model for maintainability
- ✅ Implement proper wait strategies
- ✅ Add meaningful assertions

### Code Organization

- ✅ Separate page objects by functionality
- ✅ Use inheritance for common page elements
- ✅ Implement proper exception handling
- ✅ Use meaningful variable names
- ✅ Add comments for complex logic

### Performance

- ✅ Use Playwright's auto-waiting features
- ✅ Avoid unnecessary `Thread.sleep()`
- ✅ Implement parallel execution when possible
- ✅ Use headless mode for CI/CD
- ✅ Clean up resources in @After hooks

## 🚀 CI/CD Integration

### GitHub Actions Example

```yaml
name: Playwright Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    - uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
    - name: Install dependencies
      run: mvn clean compile
    - name: Install Playwright browsers
      run: mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --with-deps"
    - name: Run tests
      run: mvn test
    - name: Upload test results
      uses: actions/upload-artifact@v4
      if: always()
      with:
        name: test-results
        path: target/cucumber-*-reports/
```

### Jenkins Pipeline

```groovy
pipeline {
    agent any
    stages {
        stage('Setup') {
            steps {
                sh 'mvn clean compile'
                sh 'mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --with-deps"'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/cucumber-html-reports',
                        reportFiles: 'index.html',
                        reportName: 'Cucumber HTML Report'
                    ])
                }
            }
        }
    }
}
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📚 Additional Resources

- 📖 [Playwright Java Documentation](https://playwright.dev/java/)
- 🥒 [Cucumber Documentation](https://cucumber.io/docs/)
- ☕ [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- 🔧 [Maven Documentation](https://maven.apache.org/guides/)

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙋‍♂️ Support

If you encounter any issues or have questions:

1. Check existing [GitHub Issues](https://github.com/alishah730/playwright-java-cucumber/issues)
2. Create a new issue with detailed description
3. Join the discussions in [Playwright Community](https://github.com/microsoft/playwright/discussions)

---

## ✅ Complete Implementation Checklist

This template includes all the essential components for a production-ready automation framework:

### ✅ Core Framework Setup
- [x] Java 17 LTS configuration with proper compiler settings
- [x] Maven 3.9+ with latest dependency management using BOM pattern
- [x] Playwright 1.56.0 for cross-browser automation
- [x] Cucumber 7.32.0 for BDD test scenarios
- [x] JUnit Platform Suite API for modern test execution

### ✅ Project Structure & Architecture  
- [x] Page Object Model implementation with inheritance
- [x] Proper separation of concerns (Pages, Steps, Runner)
- [x] Feature files with comprehensive test scenarios
- [x] Step definitions with parameter binding
- [x] Base page class with common functionality

### ✅ Test Configuration & Execution
- [x] JUnit Platform Suite configuration with @Suite annotations
- [x] Cucumber properties for test execution settings
- [x] Test tagging system (@SmokeTest, @End2End, @test)
- [x] System properties for browser and URL configuration
- [x] Maven Surefire Plugin 3.5.4 with proper test discovery

### ✅ Reporting & Documentation
- [x] HTML report generation (target/cucumber-html-reports/index.html)
- [x] JSON report output for CI/CD integration
- [x] JUnit XML reports for test result analysis
- [x] Comprehensive README with setup and usage instructions
- [x] Contributing guidelines for open-source collaboration

### ✅ Quality Assurance & Best Practices
- [x] Proper .gitignore configuration for Maven/Java projects
- [x] MIT License for open-source distribution
- [x] GitHub Actions CI/CD workflow for automated testing
- [x] Cross-browser testing support (Chromium, Firefox, WebKit)
- [x] Headless execution capability for CI environments

### ✅ Test Scenarios Implemented
- [x] Login functionality with valid/invalid credentials
- [x] End-to-end product purchase workflow
- [x] Error handling and validation scenarios
- [x] Comprehensive test data management

### ✅ Development Tools & Integration
- [x] Playwright code generation support
- [x] IDE-friendly project structure
- [x] Maven wrapper configuration
- [x] VS Code and IntelliJ IDEA compatibility

**Status: Production Ready** 🚀

**Happy Testing!** 🎭✨
