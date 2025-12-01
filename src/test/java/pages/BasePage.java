package pages;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public abstract class BasePage {

	/**
	 * Page instance for this thread - managed by BrowserContextManager
	 */
	
	protected Browser browser;
	protected Page page;

	/**
	 * Creates a new Playwright page instance with isolated browser context for parallel execution
	 * @param browserTypeAsString Browser type (Chromium, Firefox, Webkit)
	 * @return Thread-safe Page instance
	 */
	public Page createPlaywrightPageInstance(String browserTypeAsString) {
		// Use the new context manager for thread-safe parallel execution
		return BrowserContextManager.createNewContext();
	}

	/**
	 * Legacy method - kept for backward compatibility but now uses context manager
	 */
	@Deprecated
	public Page createLegacyPageInstance(String browserTypeAsString) {
		BrowserType browserType = null;
		switch (browserTypeAsString) {
		case "Firefox":
			browserType = Playwright.create().firefox();
			break;
		case "Chromium":
			browserType = Playwright.create().chromium();
			break;
		case "Webkit":
			browserType = Playwright.create().webkit();
			break;
		}
		if (browserType == null) {
			throw new IllegalArgumentException("Could not launch a browser for type " + browserTypeAsString);
		}
		browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(true));
		page = browser.newPage();
		return page;
	}

	/**
	 * Gets the current page for this thread
	 */
	public Page getCurrentPage() {
		return BrowserContextManager.getCurrentPage();
	}

	/**
	 * Gets the current browser context for this thread
	 */
	public BrowserContext getCurrentContext() {
		return BrowserContextManager.getCurrentContext();
	}

}