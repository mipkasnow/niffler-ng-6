package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.sjdbc.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.sjdbc.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;

import java.sql.Connection;

public class AuthDbClient {

    private static final Config CFG = Config.getInstance();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.authJdbcUrl()
    );


    public AuthUserEntity createUserInAuthWithAuthorities(AuthUserEntity user) {
        return jdbcTxTemplate.execute(() -> {
                   AuthUserEntity created =  authUserDao.createUserInAuth(user);
                   authAuthorityDao.setReadAndWriteAuthorityToUser(created.getId());
                   return created;
                }
                ,TRANSACTION_ISOLATION_LEVEL
        );
    }
}
