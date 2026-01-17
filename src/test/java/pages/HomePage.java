package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage{

    @FindBy(css = "input.nav-search-input")
    private WebElement searchInput;

    @FindBy(css = "button.nav-search-btn")
    private WebElement searchButton;

    @FindBy(xpath ="//*[@id=\"MX\"]")
    private WebElement mexicoCountryLink;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void selectMexicoCountry() {
        wait.until(ExpectedConditions.elementToBeClickable(mexicoCountryLink)).click();
    }

    public void searchFor(String item) {
        wait.until(ExpectedConditions.visibilityOf(searchInput)).sendKeys(item);
        searchButton.click();
    }

    public boolean isSearchInputDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(searchInput)).isDisplayed();
    }
}
