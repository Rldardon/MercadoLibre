package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CountrySelectionPage;
import pages.HomePage;
import pages.SearchResultsPage;
import utils.WordReport;
import utils.WebDriverFactory;


import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;


public class MercadoLibreSearchTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private HomePage homePage;
    private CountrySelectionPage countrySelectionPage;
    private SearchResultsPage searchResultsPage;
    private WordReport wordReport;

    @BeforeMethod
    public void setUp() {

        try {
            Path srcPath = Paths.get("src", "test", "resources", "banner.txt");
            String banner = Files.readString(srcPath, StandardCharsets.UTF_8);
            System.out.println("\n");
            System.out.println(banner);
            Thread.sleep(100);
            driver = WebDriverFactory.createDriver("chrome");
            wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            homePage = new HomePage(driver);
            countrySelectionPage = new CountrySelectionPage(driver);
            searchResultsPage = new SearchResultsPage(driver);
            wordReport = new WordReport();

            // Navegar a MercadoLibre
            homePage.navigateTo("https://www.mercadolibre.com/");
            // Paso 0: Inicio de prueba
            wordReport.addStep("0", "Inicio de prueba", "Navegando a MercadoLibre",
                    wordReport.takeScreenshot(driver, "inicio"));

        }catch (Exception e){
            throw new RuntimeException("Error en la configuración inicial: " + e.getMessage(), e);
        }
    }

    @Test
    public void testSearchAndFilterPlayStation5() throws InterruptedException {
        try {
            // 1. Seleccionar país México
            wordReport.addStep("1", "Seleccionar país México", "Clic en opción México",
                    wordReport.takeScreenshot(driver, "seleccion_pais"));
            homePage.selectMexicoCountry();

            // 2. Buscar PlayStation 5
            homePage.searchFor("PlayStation 5");
            wordReport.addStep("2", "Buscar 'PlayStation 5'", "Resultados de búsqueda mostrados",
                    wordReport.takeScreenshot(driver, "busqueda_realizada"));

            // 3. Verificar resultados iniciales
            Assert.assertTrue(searchResultsPage.areResultsDisplayed());
            wordReport.addStep("3", "Deberían mostrarse resultados de búsqueda", "Resultados de búsqueda",
                    wordReport.takeScreenshot(driver, "Resultados_busqueda"));

            // 4. Filtrar por condición "Nuevo"
            searchResultsPage.filterByNewCondition();
            wordReport.addStep("4", "Filtrar por condición 'Nuevo'", "Filtro aplicado correctamente",
                    wordReport.takeScreenshot(driver, "filtro_nuevo"));

            // 5. Catalogo de precios
            searchResultsPage.showByLowestPrice();
            wordReport.addStep("5", "Opciones de precio", "Vista del catalogo",
                    wordReport.takeScreenshot(driver, "ordenado_menor_precio"));

            // 6. Seleecion Menor Precio
            searchResultsPage.sortByLowestPrice();
            wordReport.addStep("6", "Ordenar por 'Menor precio'", "Resultados ordenados",
                    wordReport.takeScreenshot(driver, "ordenado_menor_precio"));

            //7. Tomna de nombre y precios de los resultados --pasar el numero de productos
            Thread.sleep(3000);
            System.out.println(searchResultsPage.getNProductTitles(3));
            System.out.println(searchResultsPage.getNProductPrices(3));

            // 8. Finalización exitosa
            wordReport.addStep("8", "Prueba completada", "ÉXITO - Todos los pasos completados", "");

        } catch (Exception e) {
            // En caso de error
            wordReport.addStep("Error", "Fallo en la prueba", "FALLO: " + e.getMessage(),
                    wordReport.takeScreenshot(driver, "error"));
            throw e;
        } finally {
            wordReport.saveReport();
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
