package steps;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

public class BookingSteps {

    private String city;

    @BeforeMethod
    public void initTest(){
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        options.addArguments("--disable-popup-blocking");
        prefs.put("profile.default_content_setting_values.notifications", 2);
        options.setExperimentalOption("prefs", prefs);
        WebDriver driver = new ChromeDriver(options);
        setWebDriver(driver);

        Configuration.browser = "chrome";
        Configuration.timeout = 15000;
        Configuration.headless = false;
        Configuration.browserSize = "1024x768";
    }

    @Given("User is looking for hotels in {string} city")
    public void userIsLookingForHotelsInUnitedStatedCity(String city) {
        this.city = city;
    }

    @When("User does search")
    public void userDoesSearch() {

        open("https://www.booking.com/");
        $(By.name("ss")).setValue(city);
        $(By.cssSelector("[data-testid='searchbox-dates-container']")).click();
        $(By.cssSelector("[data-date='2025-04-10']")).click();
        $(By.cssSelector("[data-date='2025-04-12']")).click();
        $(By.cssSelector("[type='submit']")).click();
    }

    @Then("Hotel {string} should be on the search results page")
    public void hotelNorthBeachResortVillasShouldBeOnTheSearchResultsPage(String hotel) {
        ElementsCollection titleList = $$(By.cssSelector("[data-testid='title']"));
        ArrayList<String> hotelNames = new ArrayList<>();
        for (SelenideElement element: titleList){
            hotelNames.add(element.getText());
        }
        Assert.assertTrue(hotelNames.contains(hotel));
    }

    @Then("Hotel {string} rating is {string}")
    public void hotelNorthBeachResortVillasRatingIs(String hotel, String rate) {
        String hotelRate = $x(String.format("//*[contains(text(),'%s')]/ancestor::div[@data-testid='property-card-container']//*[@data-testid='review-score']/div/div", hotel)).getText();
        Assert.assertEquals(hotelRate.split(" ")[1], rate);
    }


//    @Given("User provide information:")
//    public void userProvideInformation(DataTable dataTable) {
//        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
//        String name = data.get(0).get("name");
//        String email = data.get(0).get("email");
//        String phone = data.get(0).get("phone");
//        System.out.println("Name: " + name);
//    }
}