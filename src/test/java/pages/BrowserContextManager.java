package pages;

import com.microsoft.playwright.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe browser context manager for parallel test execution.
 * Each thread gets its own isolated browser context with independent cookies, cache, and session data.
 */
public class BrowserContextManager {
    
    private static final ThreadLocal<BrowserContext> contextThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageThreadLocal = new ThreadLocal<>();
    private static final ConcurrentHashMap<String, Playwright> playwrightInstances = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Browser> browserInstances = new ConcurrentHashMap<>();
    private static final AtomicInteger contextCounter = new AtomicInteger(0);
    
    private static final String DEFAULT_BROWSER = System.getProperty("browser", "Chromium");
    private static final String DEFAULT_HEADLESS = System.getProperty("headless", "true");
    private static final String DEFAULT_VIEWPORT_WIDTH = System.getProperty("viewport.width", "1920");
    private static final String DEFAULT_VIEWPORT_HEIGHT = System.getProperty("viewport.height", "1080");
    
    /**
     * Creates a new browser context for the current thread with isolated session data
     */
    public static Page createNewContext() {
        String threadName = Thread.currentThread().getName();
        int contextId = contextCounter.incrementAndGet();
        
        try {
            // Get or create Playwright instance for this thread
            Playwright playwright = playwrightInstances.computeIfAbsent(threadName, 
                k -> Playwright.create());
            
            // Get browser type based on configuration
            BrowserType browserType = getBrowserType(playwright, DEFAULT_BROWSER);
            
            // Create browser if not exists for this thread
            Browser browser = browserInstances.computeIfAbsent(threadName, k -> {
                BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                    .setHeadless(Boolean.parseBoolean(DEFAULT_HEADLESS))
                    .setSlowMo(0); // No slow motion for faster execution
                return browserType.launch(launchOptions);
            });
            
            // Create new context with isolated session
            Browser.NewContextOptions contextOptions = new Browser.NewContextOptions()
                .setViewportSize(Integer.parseInt(DEFAULT_VIEWPORT_WIDTH), 
                               Integer.parseInt(DEFAULT_VIEWPORT_HEIGHT))
                .setAcceptDownloads(true)
                .setIgnoreHTTPSErrors(true)
                .setLocale("en-US")
                .setTimezoneId("America/New_York");
            
            BrowserContext context = browser.newContext(contextOptions);
            
            // Enable tracing for debugging (optional)
            context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSources(false)
                .setSnapshots(true));
            
            // Create new page in the context
            Page page = context.newPage();
            
            // Set page timeout
            page.setDefaultTimeout(30000); // 30 seconds
            
            // Store in ThreadLocal for this thread
            contextThreadLocal.set(context);
            pageThreadLocal.set(page);
            
            System.out.println("Created new browser context for thread: " + threadName + 
                             " (Context ID: " + contextId + ")");
            
            return page;
            
        } catch (Exception e) {
            System.err.println("Failed to create browser context for thread " + threadName + ": " + e.getMessage());
            throw new RuntimeException("Browser context creation failed", e);
        }
    }
    
    /**
     * Gets the current page for this thread
     */
    public static Page getCurrentPage() {
        Page page = pageThreadLocal.get();
        if (page == null) {
            throw new IllegalStateException("No browser context found for thread: " + 
                                          Thread.currentThread().getName() + 
                                          ". Call createNewContext() first.");
        }
        return page;
    }
    
    /**
     * Gets the current browser context for this thread
     */
    public static BrowserContext getCurrentContext() {
        BrowserContext context = contextThreadLocal.get();
        if (context == null) {
            throw new IllegalStateException("No browser context found for thread: " + 
                                          Thread.currentThread().getName());
        }
        return context;
    }
    
    /**
     * Cleans up browser context for the current thread
     */
    public static void closeContext() {
        String threadName = Thread.currentThread().getName();
        
        try {
            // Close page
            Page page = pageThreadLocal.get();
            if (page != null && !page.isClosed()) {
                page.close();
                pageThreadLocal.remove();
            }
            
            // Save and close tracing
            BrowserContext context = contextThreadLocal.get();
            if (context != null) {
                try {
                    context.tracing().stop(new Tracing.StopOptions()
                        .setPath(java.nio.file.Paths.get("target/traces/trace_" + threadName + "_" + 
                                System.currentTimeMillis() + ".zip")));
                } catch (Exception e) {
                    // Ignore tracing errors
                }
                
                context.close();
                contextThreadLocal.remove();
            }
            
            System.out.println("Closed browser context for thread: " + threadName);
            
        } catch (Exception e) {
            System.err.println("Error closing browser context for thread " + threadName + ": " + e.getMessage());
        }
    }
    
    /**
     * Cleans up all resources (call at end of test suite)
     */
    public static void closeAllResources() {
        try {
            // Close all browsers
            browserInstances.values().forEach(browser -> {
                try {
                    if (browser.isConnected()) {
                        browser.close();
                    }
                } catch (Exception e) {
                    System.err.println("Error closing browser: " + e.getMessage());
                }
            });
            browserInstances.clear();
            
            // Close all Playwright instances
            playwrightInstances.values().forEach(playwright -> {
                try {
                    playwright.close();
                } catch (Exception e) {
                    System.err.println("Error closing Playwright: " + e.getMessage());
                }
            });
            playwrightInstances.clear();
            
            System.out.println("Closed all browser resources");
            
        } catch (Exception e) {
            System.err.println("Error during resource cleanup: " + e.getMessage());
        }
    }
    
    /**
     * Gets browser type based on configuration
     */
    private static BrowserType getBrowserType(Playwright playwright, String browserName) {
        return switch (browserName.toLowerCase()) {
            case "firefox" -> playwright.firefox();
            case "webkit", "safari" -> playwright.webkit();
            case "chromium", "chrome" -> playwright.chromium();
            default -> {
                System.out.println("Unknown browser: " + browserName + ". Defaulting to Chromium.");
                yield playwright.chromium();
            }
        };
    }
    
    /**
     * Gets thread-safe screenshot for the current context
     */
    public static byte[] takeScreenshot() {
        try {
            Page page = getCurrentPage();
            if (page != null && !page.isClosed()) {
                return page.screenshot();
            }
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
        return null;
    }
}