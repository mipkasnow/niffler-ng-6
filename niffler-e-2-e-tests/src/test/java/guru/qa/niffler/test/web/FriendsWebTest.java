package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

@ExtendWith(UsersQueueExtension.class)
public class FriendsWebTest extends BaseWebTest{

    private static final Config CFG = Config.getInstance();

    @Test
    void friendShouldBePresentInFriendsTable (@UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .waitMainPageLoaded()
                .openFriendsViaUrl()
                .assertThatFriendIsPresent(user.friend());

    }

    @Test
    void friendsTableShouldBeEmptyForNewUser (@UserType(EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .waitMainPageLoaded()
                .openFriendsViaUrl()
                .emptyFriendsMsgShouldAppear();
    }

    @Test
    void incomeInvitationBePresentInFriendsTable (@UserType (WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .waitMainPageLoaded()
                .openFriendsViaUrl()
                .assertThatIncomingRequestIsPresent(user.income());
    }

    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.password())
                .waitMainPageLoaded()
                .openAllPeopleViaUrl()
                .assertThatOutcomeRequestIsPresent(user.outcome());
    }
}
