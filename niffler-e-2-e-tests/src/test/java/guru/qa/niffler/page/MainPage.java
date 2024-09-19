package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement statisticsTitle = $(withText("Statistics"));
  private final SelenideElement historyTitle = $(withText("History of Spendings"));
  private static final Config CFG = Config.getInstance();

  public MainPage waitMainPageLoaded() {
    statisticsTitle.should(appear);
    historyTitle.should(appear);
    return this;
  }

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }

  public ProfilePage openProfileViaUrl() {
    return Selenide.open(CFG.frontUrl() + "profile", ProfilePage.class).waitProfilePageLoaded();
  }

  public FriendsPage openFriendsViaUrl() {
    return Selenide.open(CFG.frontUrl() + "people/friends", FriendsPage.class);
  }

  public AllPeoplePage openAllPeopleViaUrl() {
    return Selenide.open(CFG.frontUrl() + "people/all", AllPeoplePage.class);
  }
}
