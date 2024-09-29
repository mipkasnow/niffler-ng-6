package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJDBC;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.Optional;
import java.util.UUID;

public class UserdataDbClient {

    private final UserdataUserDao userdataUserDao = new UserdataUserDaoJDBC();

    public void deleteUser(UserJson user) {
        userdataUserDao.delete(UserEntity.fromJson(user));
    }

    public UserJson createUser(UserJson user) {
        return UserJson.fromEntity(userdataUserDao.createUser(UserEntity.fromJson(user)));
    }

    public Optional<UserJson> findUserById(UUID id) {
        return userdataUserDao.findById(id).map(UserJson::fromEntity);
    }

    public Optional<UserJson> findByUsername(String username) {
        return userdataUserDao.findByUsername(username).map(UserJson::fromEntity);
    }
}
