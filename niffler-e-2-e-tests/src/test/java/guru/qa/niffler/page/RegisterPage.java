package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static java.lang.String.format;

public class RegisterPage {
    private static final Config CFG = Config.getInstance();

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement signUpButton = $("button[type='submit']");
    private final SelenideElement successRegistrationMsg = $(withText("Congratulations! You've registered!"));
    private final SelenideElement signInAfterSuccessRegistrationBtn = $(format("[href*='%s']", CFG.frontUrl()));
    private final SelenideElement passwordsNotEqualErrorMsg = $(withText("Passwords should be equal"));

    private static final String userAlreadyExistErrorMsgStr = "Username `%s` already exists";

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }

    public LoginPage submitRegistration() {
        signUpButton.click();
        successRegistrationMsg.should(appear);
        signInAfterSuccessRegistrationBtn.click();
        return new LoginPage();
    }

    public RegisterPage clickSubmitButton() {
        signUpButton.click();
        return this;
    }

    public RegisterPage userAlreadyExistErrorShouldAppear(String username) {
        $(withText(format(userAlreadyExistErrorMsgStr, username))).should(appear);
        return this;
    }

    public RegisterPage passwordsNotEqualErrorMsgShouldAppear() {
        passwordsNotEqualErrorMsg.should(appear);
        return this;
    }


}
