package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CountrySelectionPage extends BasePage {
    @FindBy(css = "a.andes-list__item--center[href*='mercadolibre.com.mx']")
    private WebElement mexicoOption;

    public CountrySelectionPage(WebDriver driver) {
        super(driver);
    }

    public void selectMexico() {
        mexicoOption.click();
    }

    public boolean isCountrySelectionDisplayed() {
        return mexicoOption.isDisplayed();
    }
}
