package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class ProfileTest extends BaseWebTest{

    private static final Config CFG = Config.getInstance();
    private static final String defPassword = "12345";

    @User(
        username = "duck",
        categories = @Category(
                title = "слатенькое",
                archived = true
        )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(category.username(), defPassword)
                .waitMainPageLoaded()
                .openProfileViaUrl()
                .showArchivedCategories()
                .archivedCategoryShouldBeVisible(category.name());

    }

    @User(
        username = "misha",
        categories = @Category()
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(category.username(), defPassword)
                .waitMainPageLoaded()
                .openProfileViaUrl()
                .activeCategoryShouldBeVisible(category.name());
    }
}
