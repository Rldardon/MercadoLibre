package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;


public class SearchResultsPage extends BasePage {

    @FindBy(xpath ="//form[@class=\"nav-search\"]")
    private WebElement resultsTitle;

    @FindBy(css = "ol.ui-search-layout li.ui-search-layout__item")
    private List<WebElement> productItems;

    @FindBy(xpath = "//span[contains(text(),'Más relevantes')]")
    private WebElement sortDropdown;

    @FindBy(xpath = "//span[contains(text(),'Menor precio')]")
    private WebElement selectSortDropdown;

    @FindBy(xpath = "//span[contains(text(),'Nuevo')]")
    private WebElement newConditionFilter;

    @FindBy(css = ".andes-money-amount__fraction")
    private List<WebElement> pricesFractions;

    @FindBy(css = ".ui-search-result__wrapper, .ui-search-layout__item, .andes-card")
    private List<WebElement> productCards;



    private WebDriverWait shortWait;

    public SearchResultsPage(WebDriver driver) {
        super(driver);
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public boolean areResultsDisplayed() {
        try {
            wait.until(d -> !productItems.isEmpty());
            return !productItems.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public void showByLowestPrice() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(sortDropdown)).click();
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException("Error al ordenar por menor precio: " + e.getMessage());
        }
    }

    public void sortByLowestPrice() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(selectSortDropdown)).click();
            Thread.sleep(2000);
        } catch (Exception e) {
            throw new RuntimeException("Error al ordenar por menor precio: " + e.getMessage());
        }
    }

    public void filterByNewCondition() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(newConditionFilter)).click();
            Thread.sleep(3000);
        } catch (Exception e) {
            throw new RuntimeException("Error al filtrar por condición 'Nuevo': " + e.getMessage());
        }
    }

    public List<String> getNProductTitles(int count) {
        return productCards.stream()
                .limit(count)
                .map(card -> {
                    try {
                        return card.findElement(By.cssSelector(".poly-component__title")).getText();
                    } catch (Exception e) {
                        return "Title not found";
                    }
                })
                .collect(Collectors.toList());
    }

    public List<String> getNProductPrices(int count) {
        return productCards.stream()
                .limit(count)
                .map(card -> {
                    WebElement priceContainer = card.findElement(By.cssSelector(".poly-price__current"));
                    WebElement priceFraction = priceContainer.findElement(By.cssSelector(".andes-money-amount__fraction"));
                    String priceText = priceFraction.getText().trim();
                    priceText = priceText.replace(",", "")
                            .replace("$", "")
                            .replace(" ", "")
                            .trim();
                    if (priceText.isEmpty()) {
                        return "0";
                    }
                    return priceText;
                })
                .collect(Collectors.toList());
    }
}
