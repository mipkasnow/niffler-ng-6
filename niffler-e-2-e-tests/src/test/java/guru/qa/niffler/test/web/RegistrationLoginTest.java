package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

public class RegistrationLoginTest extends BaseWebTest{

    private static final Config CFG = Config.getInstance();
    private static final String defPassword = "12345";

    @Test
    void shouldRegisterNewUser() {
        String username = RandomDataUtils.randomUsername();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goToRegisterPage()
                .setUsername(username)
                .setPassword(defPassword)
                .setPasswordSubmit(defPassword)
                .submitRegistration();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String existingUsername = "duck";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .goToRegisterPage()
                .setUsername(existingUsername)
                .setPassword(defPassword)
                .setPasswordSubmit(defPassword)
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
                .setPasswordSubmit(RandomDataUtils.randomName())
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
                .setUsername(username)
                .setPassword(RandomDataUtils.randomName())
                .clickSubmitButton()
                .badCredentialsErrorShouldAppear();
    }

}
