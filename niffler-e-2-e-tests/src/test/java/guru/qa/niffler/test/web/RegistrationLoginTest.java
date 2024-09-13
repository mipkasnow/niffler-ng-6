package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class RegistrationLoginTest {

    private static final Config CFG = Config.getInstance();
    private final Faker faker = new Faker();
    private static final String defPassword = "12345";

    @Test
    void shouldRegisterNewUser() {
        String username = faker.name().firstName();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goToRegisterPage()
                .setUsername(username).setPassword(defPassword).setPasswordSubmit(defPassword)
                .submitRegistration();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String existingUsername = "duck";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goToRegisterPage()
                .setUsername(existingUsername).setPassword(defPassword).setPasswordSubmit(defPassword)
                .clickSubmitButton()
                .userAlreadyExistErrorShouldAppear(existingUsername);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String username = "duck";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goToRegisterPage()
                .setUsername(username)
                .setPassword(defPassword)
                .setPasswordSubmit(faker.funnyName().name())
                .clickSubmitButton()
                .passwordsNotEqualErrorMsgShouldAppear();
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        String username = "duck";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, defPassword)
                .waitMainPageLoaded();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        String username = "duck";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(username).setPassword(faker.funnyName().name()).clickSubmitButton()
                .badCredentialsErrorShouldAppear();
    }

}
