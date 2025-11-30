package pages;

import static org.assertj.core.api.Assertions.assertThat;
import com.microsoft.playwright.Page;

public class ItemsPage extends BasePage {

	Page page;

	public ItemsPage(Page page) {
		this.page = page;
	}

	public void orderProduct(String ProductName) {
		page.click("//div[text()='" + ProductName + "']/following::button[1]");
		page.click("#shopping_cart_container > a");
		assertThat(page.isVisible("text=" + ProductName))
			.as("Product '" + ProductName + "' should be visible in cart")
			.isTrue();
		page.click("[data-test=\"checkout\"]");
	}

	public void loginSuccessful() {
		assertThat(page.isVisible("text=Products"))
			.as("Products page should be visible after successful login")
			.isTrue();
	}
}
