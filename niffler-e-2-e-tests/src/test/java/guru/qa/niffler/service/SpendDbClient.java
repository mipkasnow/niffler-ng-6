package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.jdbc.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;

    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();
    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDao
                                .findCategoryByUsernameAndCategoryName(spendEntity.getUsername(), spendEntity.getCategory().getName())
                                .orElseGet(() -> categoryDao.create(spendEntity.getCategory()));
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            spendDao.create(spendEntity)
                    );
                },
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public void deleteCategory(CategoryJson category) {
        jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    categoryDao.deleteCategory(categoryEntity);
                    return null;
                },
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public List<CategoryJson> findAllCategoriesByUserName(String username) {
        return jdbcTxTemplate.execute(() -> categoryDao.findAllByUserName(username)
                .stream()
                .map(CategoryJson::fromEntity)
                .toList(),
                TRANSACTION_ISOLATION_LEVEL
        );

    }

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return jdbcTxTemplate.execute(() -> categoryDao
                .findCategoryByUsernameAndCategoryName(username, categoryName)
                .map(CategoryJson::fromEntity),
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<SpendJson> findSpendById(UUID id) {
        return jdbcTxTemplate.execute(() -> spendDao.findSpendById(id).map(SpendJson::fromEntity), TRANSACTION_ISOLATION_LEVEL
        );
    }

    public void deleteSpend(SpendJson spend) {
        jdbcTxTemplate.execute(() -> {
                    spendDao.deleteSpend(SpendEntity.fromJson(spend));
                    return null;
                }, TRANSACTION_ISOLATION_LEVEL
        );
    }

    public List<SpendJson> findAllByUsername(String username) {
        return jdbcTxTemplate.execute(() -> spendDao.findAllByUsername(username)
                .stream()
                .map(SpendJson::fromEntity)
                .toList(), TRANSACTION_ISOLATION_LEVEL
        );

    }

    public CategoryJson createCategory(CategoryJson category) {
        return jdbcTxTemplate.execute(() -> categoryDao.findCategoryByUsernameAndCategoryName(category.username(), category.name())
                .map(CategoryJson::fromEntity)
                .orElseGet(() -> CategoryJson.fromEntity(categoryDao.create(CategoryEntity.fromJson(category)))), TRANSACTION_ISOLATION_LEVEL
        );
    }
}
