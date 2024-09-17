package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement createNewAccBtn = $("[href='/register']");
  private final SelenideElement badCredentialsErrorMsg = $(withText("Неверные учетные данные пользователя"));

  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  public RegisterPage goToRegisterPage() {
    createNewAccBtn.click();
    return new RegisterPage();
  }

  public LoginPage setUsername(String username) {
    usernameInput.setValue(username);
    return this;
  }

  public LoginPage setPassword(String password) {
    passwordInput.setValue(password);
    return this;
  }

  public LoginPage clickSubmitButton() {
    submitButton.click();
    return this;
  }

  public LoginPage badCredentialsErrorShouldAppear() {
    badCredentialsErrorMsg.should(appear);
    return this;
  }
}
