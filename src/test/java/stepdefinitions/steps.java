package stepdefinitions;


import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import pages.*;

public class steps extends BasePage{
	

	LoginPage loginPage;
	ItemsPage itemsPage;
	CheckoutPage checkoutPage;
	
	Page page;
	
	@Given("^User launched SwagLabs application$")
	public void user_launched_swaglabs_application() {
		try {
			// Create new isolated browser context for this thread/scenario
			page = createPlaywrightPageInstance(System.getProperty("browser"));
			page.navigate(System.getProperty("applicationUrl"));
			
			// Initialize page objects with the thread-safe page instance
			loginPage = new LoginPage(page);
			itemsPage = new ItemsPage(page);
			checkoutPage = new CheckoutPage(page);
			
			System.out.println("Thread: " + Thread.currentThread().getName() + 
							 " - Launched application: " + System.getProperty("applicationUrl"));
		    
		}
		catch (Exception e) {
			System.err.println("Failed to launch application in thread: " + 
							 Thread.currentThread().getName() + " - " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	@When("User logged in the app using username {string} and password {string}")
	public void user_logged_in_the_app_using_username_and_password(String username, String password) {
		loginPage.login(username, password);
	}

	@Then("^user should be able to log in$")
	public void logInSuccessful() {
		itemsPage.loginSuccessful();
	}

	@Then("^User should not get logged in$")
	public void logInFailed() {
		loginPage.loginFailed();
	}

	@When("User adds {string} product to the cart")
	public void user_adds_product_to_the_cart(String product) {
        itemsPage.orderProduct(product);
	}

	@When("User enters Checkout details with {string}, {string}, {string}")
	public void user_enters_Checkout_details_with(String FirstName, String LastName, String Zipcode) {
		checkoutPage.fillCheckoutDetails(FirstName, LastName, Zipcode);
	}
	
	@When("User completes Checkout process")
	public void user_completes_checkout_process() {
         checkoutPage.completeCheckout();
	}

	@Then("User should get the Confirmation of Order")
	public void user_should_get_the_Confirmation_of_Order() {
         checkoutPage.checkoutSuccessful();
	}
	

	
	@After
	public void tearDown(Scenario scenario) {
		String threadName = Thread.currentThread().getName();
		
		try {
			// Take screenshot if scenario failed using thread-safe method
			if (scenario.isFailed()) {
				// Generate timestamp for unique screenshot name
				String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
				String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
				String screenshotName = "failed_" + scenarioName + "_" + threadName + "_" + timestamp + ".png";
				
				// Create screenshots directory if it doesn't exist
				java.nio.file.Path screenshotDir = Paths.get("target/screenshots");
				if (!java.nio.file.Files.exists(screenshotDir)) {
					java.nio.file.Files.createDirectories(screenshotDir);
				}
				
				// Take thread-safe screenshot
				byte[] screenshot = BrowserContextManager.takeScreenshot();
				if (screenshot != null) {
					// Save screenshot to file
					java.nio.file.Path screenshotPath = screenshotDir.resolve(screenshotName);
					java.nio.file.Files.write(screenshotPath, screenshot);
					
					// Attach screenshot to Cucumber report
					scenario.attach(screenshot, "image/png", "Screenshot of failed step - Thread: " + threadName);
					
					System.out.println("Thread: " + threadName + " - Screenshot saved: " + screenshotPath.toString());
				}
			}
		} catch (Exception e) {
			System.err.println("Thread: " + threadName + " - Failed to capture screenshot: " + e.getMessage());
		} finally {
			// Clean up thread-specific browser context
			try {
				BrowserContextManager.closeContext();
				System.out.println("Thread: " + threadName + " - Browser context closed");
			} catch (Exception e) {
				System.err.println("Thread: " + threadName + " - Error closing browser context: " + e.getMessage());
			}
		}
	}
}
