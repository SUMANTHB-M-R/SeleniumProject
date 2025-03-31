import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestCases {
    WebDriver driver;

    @BeforeTest
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void openGoogle() {
        driver.get("https://www.google.com");
        System.out.println("Google homepage opened successfully");
        Assert.assertEquals(driver.getTitle(),"Google123");
    }
    
    @Test(priority = 2)
    public void loginTest() {
        System.out.println("Executing Login Test");
    }

    @Test(priority = 3)
    public void searchTest() {
        System.out.println("Executing Search Test");
    }

    @Test(priority = 4)
    public void checkoutTest() {
        System.out.println("Executing Checkout Test");
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
