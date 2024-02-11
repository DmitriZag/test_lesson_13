package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

public class RegistrationRemoteTests {

    @BeforeAll
    static void beforeAll() {
        Configuration.baseUrl = "https://demoqa.com";
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadStrategy = "eager";
       Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();

    }

    @Test
    @Tag("demoqa")
    void fillPracticeFormTest() {
        step("Open form", () -> {
            open("/automation-practice-form");
            $(".practice-form-wrapper").shouldHave(text("Student Registration Form"));
            executeJavaScript("$('#fixedban').remove()");
            executeJavaScript("$('footer').remove()");
        });
        step("Fill form", () -> {
            $("#firstName").setValue("Ivan");
            $("#lastName").setValue("Ivanov");
            $("#userEmail").setValue("ivanov88@mail.ru");
            $("#genterWrapper").$(byText("Male")).click();
            $("#userNumber").setValue("9119991919");
            $("#dateOfBirthInput").click();
            $(".react-datepicker__month-select").selectOption(7);
            $(".react-datepicker__year-select").selectOption(88);
            $(".react-datepicker__day--023").click();
            $("#subjectsInput").setValue("Arts").pressEnter();
            $("#hobbiesWrapper").$(byText("Music")).click();
            $("#uploadPicture").uploadFromClasspath("screen.jpg");
            $("#currentAddress").setValue("Test");
            $("#state").click();
            $("#stateCity-wrapper").$(byText("Haryana")).click();
            $("#city").click();
            $("#stateCity-wrapper").$(byText("Karnal")).click();
            $("#submit").click();
        });
        step("Verify results", () -> {
            $(".modal-header").shouldHave(text("Thanks for submitting the form"));
            $(".table-responsive").shouldHave(text("Ivan Ivanov"));
            $(".table-responsive").shouldHave(text("ivanov88@mail.ru"));
            $(".table-responsive").shouldHave(text("Male"));
            $(".table-responsive").shouldHave(text("9119991919"));
            $(".table-responsive").shouldHave(text("23 August,1988"));
            $(".table-responsive").shouldHave(text("Test"));
            $(".table-responsive").shouldHave(text("screen.jpg"));
            $(".table-responsive").shouldHave(text("Test"));
            $(".table-responsive").shouldHave(text("Haryana Karnal"));
        });
    }
}
