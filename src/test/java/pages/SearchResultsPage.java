package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import java.util.List;
import java.util.stream.Collectors;


public class SearchResultsPage extends BasePage {

    @FindBy(xpath ="//form[@class=\"nav-search\"]")
    private WebElement resultsTitle;

    @FindBy(css = "ol.ui-search-layout li.ui-search-layout__item")
    private List<WebElement> productItems;

    @FindBy(xpath = "//*[@id=\":R1b55ie:\"]")
    private WebElement sortDropdown;

    @FindBy(xpath = "//span[contains(text(),'Menor precio')]")
    private WebElement selectSortDropdown;

    @FindBy(xpath = "(//span[contains(text(),'Nuevo')])[2]")
    private WebElement newConditionFilter;

    @FindBy(css = ".ui-search-price__part--medium .andes-money-amount__fraction")
    private List<WebElement> pricesFractions;

    @FindBy(css = ".ui-search-result__wrapper")
    private List<WebElement> productCards;

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public String getResultsTitle() {
        return wait.until(ExpectedConditions.visibilityOf(resultsTitle)).getText();
    }

    public int getNumberOfResults() {
        return productItems.size();
    }

    public boolean areResultsDisplayed() {
        return !productItems.isEmpty();
    }

    public void showByLowestPrice() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(sortDropdown)).click();

        } catch (Exception e) {
            throw new RuntimeException("Error al ordenar por menor precio: " + e.getMessage());
        }
    }

    public void sortByLowestPrice() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(selectSortDropdown)).click();

        } catch (Exception e) {
            throw new RuntimeException("Error al ordenar por menor precio: " + e.getMessage());
        }
    }



    public void filterByNewCondition() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(newConditionFilter)).click();
            wait.until(ExpectedConditions.stalenessOf(productCards.get(0)));
        } catch (Exception e) {
            throw new RuntimeException("Error al filtrar por condición 'Nuevo': " + e.getMessage());
        }
    }





    public List<String> getFirstNProductTitles(int count) {
        return productCards.stream()
                .limit(count)
                .map(card -> {
                    try {
                        return card.findElement(By.cssSelector(".ui-search-item__title")).getText();
                    } catch (Exception e) {
                        return "Title not found";
                    }
                })
                .collect(Collectors.toList());
    }

    public List<String> getFirstNProductPrices(int count) {
        return productCards.stream()
                .limit(count)
                .map(card -> {
                    WebElement priceFraction = card.findElement(By.cssSelector(".andes-money-amount__fraction"));
                    WebElement priceDecimals = card.findElement(By.cssSelector(".andes-money-amount__cents"));
                    return priceFraction.getText() + "," + priceDecimals.getText();
                })
                .collect(Collectors.toList());
    }
}
