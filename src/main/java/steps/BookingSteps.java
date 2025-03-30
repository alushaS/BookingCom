package steps;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

public class BookingSteps {

    private String city;
    private String place;
    private String destination;

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

    @Given("User is looking for a taxi in {string} place")
    public void userIsLookingForATaxi(String place) {
        this.place = place;
    }

    @When("User sets {string} as a destination")
    public void userSetsDestination(String destination) {
        this.destination = destination;
    }

    @Then("User searches for a taxi")
    public void userSearchesForATaxi() {
        open("https://www.booking.com/taxi/");
        $x("//*[@name='pickup']").sendKeys(place);
        $x("//*[@name='dropoff']").sendKeys(destination);
    }

    @And("Sets the date of the ride")
    public void setsTheDateOfTheRide() {
        $x("//*[@data-testid='taxi-date-time-picker-form-element-oneway']").click();
        $x("//*[@data-date='2025-04-06']").click();
    }

    @And("Sets the time of the ride")
    public void setsTheTimeOfTheRide() {
        $x("//*[@name='hours-oneway']").selectOption(15);
    }

    @And("Sets the quantity of passengers")
    public void setsTheQuantityOfPassengers() {
        $x("//*[@data-testid='taxi-input-select-input-passengers']").selectOption(3);
        $x("//*[@type='submit']").click();
    }

    @Then("Taxi category {string} should be on the search results page")
    public void taxiCategoryStandardShouldBeOnTheSearchResultsPage(String taxiCategory) {
        String category = $x(String.format("//h4[text() = '%s']", taxiCategory)).getText();
        Assert.assertEquals(category, "Standard");
    }
}