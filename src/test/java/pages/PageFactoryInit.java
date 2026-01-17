package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class PageFactoryInit {
    protected WebDriver driver;

    public PageFactoryInit(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
}
