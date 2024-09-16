package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage {
    private final ElementsCollection allPeopleRows = $$("#all tr");

    public AllPeoplePage assertThatOutcomeRequestIsPresent(String name) {
        allPeopleRows.filterBy(text(name)).first().$(byText("Waiting...")).should(appear);
        return this;
    }
}
