package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.jdbc.UdUserDaoJDBC;
import guru.qa.niffler.data.dao.impl.sjdbc.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.sjdbc.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.sjdbc.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public void deleteUser(UserJson user) {
        transaction(connection -> {
                    new UdUserDaoJDBC(connection).delete(UserEntity.fromJson(user));
                }, CFG.userdataJdbcUrl(), TRANSACTION_ISOLATION_LEVEL
        );
    }

    public UserJson createUser(UserJson user) {
        return transaction(connection -> {
                    return UserJson.fromEntity(new UdUserDaoJDBC(connection).createUser(UserEntity.fromJson(user)));
                }, CFG.userdataJdbcUrl(), TRANSACTION_ISOLATION_LEVEL
        );

    }

    public Optional<UserJson> findUserById(UUID id) {
        return transaction(connection -> {
                    return new UdUserDaoJDBC(connection).findById(id).map(UserJson::fromEntity);
                }, CFG.userdataJdbcUrl(), TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<UserJson> findByUsername(String username) {
        return transaction(connection -> {
                    return new UdUserDaoJDBC(connection).findByUsername(username).map(UserJson::fromEntity);
                }, CFG.userdataJdbcUrl(), TRANSACTION_ISOLATION_LEVEL
        );
    }

    public UserJson createUserSpringJdbc(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        AuthUserEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .createUserInAuth(authUser);
        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);
        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .create(authorityEntities);
        return UserJson.fromEntity(
                new UdUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
                        .createUser(
                                UserEntity.fromJson(user)
                        )
        );
    }


}
