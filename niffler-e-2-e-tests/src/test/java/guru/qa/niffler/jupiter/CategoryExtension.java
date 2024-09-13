package guru.qa.niffler.jupiter;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();
    private final Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                    CategoryJson category = new CategoryJson(
                            null,
                            faker.food().dish(),
                            anno.username(),
                            false
                    );

                    CategoryJson createdCategory = spendApiClient.addCategory(category);

                    if (anno.archived()) {
                        createdCategory = spendApiClient.updateCategory(new CategoryJson(
                                createdCategory.id(),
                                createdCategory.name(),
                                createdCategory.username(),
                                true
                        ));
                    }

                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            createdCategory
                    );
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson categoryFromStore = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        //потому что в сторе информация о категории может быть неактуальная из-за действий в тесте.
        CategoryJson categoryFromApi= spendApiClient.getCategories(categoryFromStore.username())
                .stream()
                .filter(c -> c.id().equals(categoryFromStore.id()))
                .findFirst()
                .get();

        if (!categoryFromApi.archived()) {
            spendApiClient.updateCategory(new CategoryJson(
                    categoryFromStore.id(),
                    categoryFromStore.name(),
                    categoryFromStore.username(),
                    true
            ));
        }
    }
}
