package stepdefinitions;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import pages.BrowserContextManager;

/**
 * Test suite level hooks for parallel execution setup and cleanup
 */
public class TestSuiteHooks {
    
    @BeforeAll
    public static void globalSetup() {
        String threadCount = System.getProperty("parallel.thread.count", "5");
        System.out.println("=== Playwright-Cucumber Parallel Execution Started ===");
        System.out.println("Configured parallel thread count: " + threadCount);
        System.out.println("Browser: " + System.getProperty("browser", "Chromium"));
        System.out.println("Application URL: " + System.getProperty("applicationUrl", "https://www.saucedemo.com/"));
        System.out.println("Headless mode: " + System.getProperty("headless", "true"));
        System.out.println("========================================================");
    }
    
    @AfterAll
    public static void globalTeardown() {
        System.out.println("=== Cleaning up all browser resources ===");
        BrowserContextManager.closeAllResources();
        System.out.println("=== Playwright-Cucumber Parallel Execution Completed ===");
    }
}