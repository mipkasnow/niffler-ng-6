package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
            .ifPresent(user -> {
                Category[] categories = user.categories();

                if (categories.length > 0) {
                    Category categoryUserAnno = categories[0];

                    spendDbClient.findAllCategoriesByUserName(user.username())
                        .stream()
                        .filter(c -> c.name().equals(categoryUserAnno.title()))
                        .findFirst()
                        .ifPresentOrElse(
                            c ->
                                context.getStore(NAMESPACE).put(
                                        context.getUniqueId(),
                                        c
                                ),
                            () -> {
                                CategoryJson category = new CategoryJson(
                                        null,
                                        categoryUserAnno.title().isEmpty() ? RandomDataUtils.randomCategoryName() : categoryUserAnno.title(),
                                        user.username(),
                                        categoryUserAnno.archived()
                                );

                                CategoryJson createdCategory = spendDbClient.createCategory(category);

                                context.getStore(NAMESPACE).put(
                                        context.getUniqueId(),
                                        createdCategory
                                );
                            }
                        );
                }
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

        if (categoryFromStore != null) {
            spendDbClient.deleteCategory(categoryFromStore);
        }
    }
}
