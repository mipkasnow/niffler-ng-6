package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {
    private static final Config CFG = Config.getInstance();
    private final SelenideElement profileTitle = $(withText("Profile"));
    private final SelenideElement showArchivedBtn = $(".MuiSwitch-switchBase");

    public ProfilePage waitProfilePageLoaded() {
        profileTitle.should(appear);
        return this;
    }

    public ProfilePage showArchivedCategories() {
        showArchivedBtn.shouldNotHave(cssClass("Mui-checked"));
        showArchivedBtn.click();
        showArchivedBtn.shouldHave(cssClass("Mui-checked"));
        return this;
    }

    public ProfilePage hideArchivedCategories() {
        showArchivedBtn.shouldHave(cssClass("Mui-checked"));
        showArchivedBtn.click();
        showArchivedBtn.shouldNotHave(cssClass("Mui-checked"));
        return this;
    }

    public ProfilePage archivedCategoryShouldBeVisible(String name) {
        $(withText(name)).shouldBe(visible).parent().sibling(0)
                .shouldHave(attribute("aria-label", "Unarchive category"));
        return this;
    }

    public ProfilePage activeCategoryShouldBeVisible(String name) {
        $(withText(name)).shouldBe(visible).parent().sibling(0).$$("button").get(1)
                .shouldHave(attribute("aria-label", "Archive category"));
        return this;
    }
}
