package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJDBC;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class UserdataDbClient {

    private static final Config CFG = Config.getInstance();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;

    public void deleteUser(UserJson user) {
        transaction(connection -> {
                    new UserdataUserDaoJDBC(connection).delete(UserEntity.fromJson(user));
                }, CFG.userdataJdbcUrl(), TRANSACTION_ISOLATION_LEVEL
        );
    }

    public UserJson createUser(UserJson user) {
        return transaction(connection -> {
                    return UserJson.fromEntity(new UserdataUserDaoJDBC(connection).createUser(UserEntity.fromJson(user)));
                }, CFG.userdataJdbcUrl(), TRANSACTION_ISOLATION_LEVEL
        );

    }

    public Optional<UserJson> findUserById(UUID id) {
        return transaction(connection -> {
                    return new UserdataUserDaoJDBC(connection).findById(id).map(UserJson::fromEntity);
                }, CFG.userdataJdbcUrl(), TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<UserJson> findByUsername(String username) {
        return transaction(connection -> {
                    return new UserdataUserDaoJDBC(connection).findByUsername(username).map(UserJson::fromEntity);
                }, CFG.userdataJdbcUrl(), TRANSACTION_ISOLATION_LEVEL
        );
    }

}
