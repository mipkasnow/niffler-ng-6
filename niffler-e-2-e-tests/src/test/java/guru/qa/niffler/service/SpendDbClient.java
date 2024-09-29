package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                                .findCategoryByUsernameAndCategoryName(spendEntity.getUsername(), spendEntity.getCategory().getName())
                                .orElseGet(() -> new CategoryDaoJdbc(connection).create(spendEntity.getCategory()));
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl()
        );
    }

    public void deleteCategory(CategoryJson category) {
        transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    new CategoryDaoJdbc(connection).deleteCategory(categoryEntity);
                },
                CFG.spendJdbcUrl()
        );
    }

    public List<CategoryJson> findAllCategoriesByUserName(String username) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findAllByUserName(username)
                            .stream()
                            .map(CategoryJson::fromEntity)
                            .toList();
                },
                CFG.spendJdbcUrl()
        );

    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, categoryName)
                            .map(CategoryJson::fromEntity);
                }, CFG.spendJdbcUrl()
        );
    }

    public Optional<SpendJson> findSpendById(UUID id) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findSpendById(id).map(SpendJson::fromEntity);
                }, CFG.spendJdbcUrl()
        );
    }

    public void deleteSpend(SpendJson spend) {
        transaction(connection -> {
                    new SpendDaoJdbc(connection).deleteSpend(SpendEntity.fromJson(spend));
                }, CFG.spendJdbcUrl()
        );
    }

    public List<SpendJson> findAllByUsername(String username) {
        return transaction(connection -> {
                    return new SpendDaoJdbc(connection).findAllByUsername(username)
                            .stream()
                            .map(SpendJson::fromEntity)
                            .toList();
                }, CFG.spendJdbcUrl()
        );

    }

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(category.username(), category.name())
                            .map(CategoryJson::fromEntity)
                            .orElseGet(() -> CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(CategoryEntity.fromJson(category))));
                }, CFG.spendJdbcUrl()
        );
    }
}
