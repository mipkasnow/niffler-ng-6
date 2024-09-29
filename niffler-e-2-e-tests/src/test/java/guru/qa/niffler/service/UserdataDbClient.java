package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJDBC;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class UserdataDbClient {

    private static final Config CFG = Config.getInstance();

    public void deleteUser(UserJson user) {
        transaction(connection -> {
                    new UserdataUserDaoJDBC(connection).delete(UserEntity.fromJson(user));
                }, CFG.userdataJdbcUrl()
        );
    }

    public UserJson createUser(UserJson user) {
        return transaction(connection -> {
                    return UserJson.fromEntity(new UserdataUserDaoJDBC(connection).createUser(UserEntity.fromJson(user)));
                }, CFG.userdataJdbcUrl()
        );

    }

    public Optional<UserJson> findUserById(UUID id) {
        return transaction(connection -> {
                    return new UserdataUserDaoJDBC(connection).findById(id).map(UserJson::fromEntity);
                }, CFG.userdataJdbcUrl()
        );
    }

    public Optional<UserJson> findByUsername(String username) {
        return transaction(connection -> {
                    return new UserdataUserDaoJDBC(connection).findByUsername(username).map(UserJson::fromEntity);
                }, CFG.userdataJdbcUrl()
        );
    }
}
