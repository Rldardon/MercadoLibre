package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class WebDriverFactory {

    public static WebDriver createDriver(String browser) {
        WebDriver driver = null;

        switch (browser.toLowerCase()) {
            case "chrome":
                String driverPath = Paths.get("src/test/resources/drivers/chromedriver.exe").toAbsolutePath().toString();
                System.setProperty("webdriver.chrome.driver", driverPath);

                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(options);
                break;

            case "firefox":
                // Configuración para Firefox
                // ...
                break;

            default:
                throw new IllegalArgumentException("Navegador no soportado: " + browser);
        }

        driver.manage().window().maximize();
        return driver;
    }

}
