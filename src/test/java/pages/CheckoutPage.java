package pages;

import static org.assertj.core.api.Assertions.assertThat;
import com.microsoft.playwright.Page;

public class CheckoutPage extends BasePage{
	
	Page page;
	
	  public CheckoutPage(Page page) { 
		  this.page = page;
      }
	 
	 public void fillCheckoutDetails(String firstname, String lastname, String postcode) {	
	      page.fill("[data-test=\"firstName\"]", firstname);
	      page.fill("[data-test=\"lastName\"]", lastname);
	      page.fill("[data-test=\"postalCode\"]", postcode);
	}
	 public void completeCheckout() {
	      page.click("[data-test=\"continue\"]");
	      page.click("[data-test=\"finish\"]");
	    }
	 
	 public void checkoutSuccessful() {
	      assertThat(page.isVisible("text=THANK YOU FOR YOUR ORDER"))
	      	.as("Order confirmation should be visible after successful checkout")
	      	.isTrue();
	}
}
