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

import java.time.Duration;
import java.util.List;

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
    public void testSearchAndFilterPlayStation5() {
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
            Assert.assertTrue(searchResultsPage.areResultsDisplayed(),
                    "Deberían mostrarse resultados de búsqueda");

            // 4. Filtrar por condición "Nuevo"
            searchResultsPage.filterByNewCondition();
            wordReport.addStep("3", "Filtrar por condición 'Nuevo'", "Filtro aplicado correctamente",
                    wordReport.takeScreenshot(driver, "filtro_nuevo"));

            // 5. Catalogo de precios
            searchResultsPage.showByLowestPrice();
            wordReport.addStep("4", "Opciones de precio", "Vista del catalogo",
                    wordReport.takeScreenshot(driver, "ordenado_menor_precio"));

            // 6. Seleecion Menor Precio
            searchResultsPage.sortByLowestPrice();
            wordReport.addStep("4", "Ordenar por 'Menor precio'", "Resultados ordenados",
                    wordReport.takeScreenshot(driver, "ordenado_menor_precio"));

            // 7. Finalización exitosa
            wordReport.addStep("6", "Prueba completada", "ÉXITO - Todos los pasos completados", "");


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
