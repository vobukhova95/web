import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

public class AppOrderTest {

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldSendForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79135432321");
        driver.findElement(By.cssSelector("label[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='order-success']"));
        assertTrue(result.isDisplayed());
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", result.getText().trim());
    }

    @Test
    void shouldReturnErrorForNameIfEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+78972345443");
        driver.findElement(By.cssSelector("label[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Поле обязательно для заполнения", result.getText().trim());
    }

    @Test
    void shouldReturnErrorForPhoneIfEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("label[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Поле обязательно для заполнения", result.getText().trim());
    }

    @Test
    void shouldReturnErrorForLabelIfEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иван Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79135432321");
        driver.findElement(By.cssSelector("button")).click();
        WebElement result = driver.findElement(By.cssSelector(".checkbox__text"));
        assertEquals("rgba(255, 92, 92, 1)", result.getCssValue("color"));
    }

    @Test
    void shouldReturnErrorForName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Ivan Markov");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+78972345443");
        driver.findElement(By.cssSelector("label[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", result.getText().trim());
    }

    @Test
    void shouldReturnErrorForPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Вера Климова");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+789723454435");
        driver.findElement(By.cssSelector("label[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button")).click();
        WebElement result = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"));
        assertTrue(result.isDisplayed());
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());
    }


}


