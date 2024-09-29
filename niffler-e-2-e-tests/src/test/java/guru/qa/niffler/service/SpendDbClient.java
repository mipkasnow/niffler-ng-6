package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {

  private final SpendDao spendDao = new SpendDaoJdbc();
  private final CategoryDao categoryDao = new CategoryDaoJdbc();

  public SpendJson createSpend(SpendJson spend) {
    SpendEntity spendEntity = SpendEntity.fromJson(spend);
    if (spendEntity.getCategory().getId() == null) {
      CategoryEntity categoryEntity = categoryDao.findCategoryByUsernameAndCategoryName(spendEntity.getUsername(), spendEntity.getCategory().getName())
              .orElseGet(() -> categoryDao.create(spendEntity.getCategory()));
      spendEntity.setCategory(categoryEntity);
    }
    return SpendJson.fromEntity(
        spendDao.create(spendEntity)
    );
  }

  public void deleteCategory(CategoryJson category) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
    categoryDao.deleteCategory(categoryEntity);
  }

  public List<CategoryJson> findAllCategoriesByUserName(String username) {
    return categoryDao.findAllByUserName(username)
            .stream()
            .map(CategoryJson::fromEntity)
            .toList();
  }

  public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
    return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName)
            .map(CategoryJson::fromEntity);
  }

  public Optional<SpendJson> findSpendById(UUID id) {
    return spendDao.findSpendById(id).map(SpendJson::fromEntity);
  }

  public void deleteSpend(SpendJson spend) {
    spendDao.deleteSpend(SpendEntity.fromJson(spend));
  }

  public List<SpendJson> findAllByUsername(String username) {
     return spendDao.findAllByUsername(username)
             .stream()
             .map(SpendJson::fromEntity)
             .toList();
  }

  public CategoryJson createCategory(CategoryJson category) {
    return categoryDao.findCategoryByUsernameAndCategoryName(category.username(), category.name())
            .map(CategoryJson::fromEntity)
            .orElseGet(() -> CategoryJson.fromEntity(categoryDao.create(CategoryEntity.fromJson(category))));
  }
}
